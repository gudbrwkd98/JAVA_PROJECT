/*
 * ��ǰ��� ���� �����Ѵ�!!
 * */
package com.shoppingmall.admin.product;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.shoppingmall.admin.AdminMain;

public class RegistForm extends JPanel {
	JPanel p_container;// �׸��� ���� ����(7, 2)

	String[] title = { "����ī�װ�", "����ī�װ�", "��ǰ��", "�귣��", "����", "�̹���", "�󼼼���" };
	JLabel[] la_title = new JLabel[title.length];

	Choice ch_top;// �ֻ��� ī�װ�
	Choice ch_sub;// ���� ī�װ�
	JTextField t_product_name;// ��ǰ��
	JTextField t_brand;// �귣��
	JTextField t_price;// ����
	JTextField t_filename;// �̹���
	JTextArea t_detail;// �󼼼���
	JScrollPane s1; // �󼼼��� ������ ��ũ��
	JButton bt_regist, bt_list;
	Product product;

	public RegistForm(Product product) {
		this.product = product;
		p_container = new JPanel();
		for (int i = 0; i < title.length; i++) {
			la_title[i] = new JLabel(title[i], SwingConstants.RIGHT);
		}
		ch_top = new Choice();
		ch_sub = new Choice();
		t_product_name = new JTextField();
		t_brand = new JTextField();
		t_price = new JTextField();
		t_filename = new JTextField();
		t_detail = new JTextArea();
		s1 = new JScrollPane(t_detail);
		bt_regist = new JButton("���");
		bt_list = new JButton("���");

		// �ֻ��� ī�װ� ä���
		for (int i = 0; i < product.topCategory.size(); i++) {
			ch_top.add(product.topCategory.get(i));
		}

		// ��Ÿ�� ����
		Dimension d = new Dimension(320, 25);

		p_container.setBackground(Color.WHITE);
		p_container.setPreferredSize(new Dimension(AdminMain.WIDTH - 500, AdminMain.HEIGHT - 400));
		for (int i = 0; i < title.length; i++) {
			la_title[i].setPreferredSize(d);
		}
		ch_top.setPreferredSize(d);
		ch_sub.setPreferredSize(d);
		t_product_name.setPreferredSize(d);
		t_brand.setPreferredSize(d);
		t_price.setPreferredSize(d);
		t_filename.setPreferredSize(d);
		t_detail.setLineWrap(true);
		t_detail.setWrapStyleWord(true);
		s1.setPreferredSize(new Dimension(320, 300));
		bt_regist.setPreferredSize(new Dimension(300, 40));
		bt_regist.setFont(new Font("���� ���", Font.PLAIN, 12));
		bt_regist.setBackground(new Color(15, 76, 129));
		bt_regist.setForeground(Color.WHITE);
		bt_list.setPreferredSize(new Dimension(300, 40));
		bt_list.setFont(new Font("���� ���", Font.PLAIN, 12));
		bt_list.setBackground(new Color(15, 76, 129));
		bt_list.setForeground(Color.WHITE);
		s1.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		// ����
		p_container.add(la_title[0]);
		p_container.add(ch_top);
		p_container.add(la_title[1]);
		p_container.add(ch_sub);
		p_container.add(la_title[2]);
		p_container.add(t_product_name);
		p_container.add(la_title[3]);
		p_container.add(t_brand);
		p_container.add(la_title[4]);
		p_container.add(t_price);
		p_container.add(la_title[5]);
		p_container.add(t_filename);
		p_container.add(la_title[6]);
		p_container.add(s1);

		this.add(p_container);// ���� �гο� �������̳� ����
		this.add(bt_regist);// ���� �гο� ��ư ���� //��ưũ�������� �ϱ����ؼ� �г��� �ϳ� ����� ��ư2���� �ִ´�
		this.add(bt_list);

		t_price.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				String value = t_price.getText();
				int l = value.length();
				if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
					t_price.setEditable(true);

				} else {
					t_price.setEditable(false);

				}
			}
		});

		bt_regist.addActionListener((e) -> {

			if (t_product_name.getText().isEmpty() || t_brand.getText().isEmpty() || t_price.getText().isEmpty()
					|| t_filename.getText().isEmpty() || t_detail.getText().isEmpty()) {

				JOptionPane.showMessageDialog(RegistForm.this, "��ĭ�� ä���ּ���");
			} else {

				if (!(Pattern.matches("^[0-9]+$", t_price.getText()))) {
					JOptionPane.showMessageDialog(RegistForm.this, "���ڸ� �Է����ּ���");
				} else {
					if (regist() == 0) {
						JOptionPane.showMessageDialog(RegistForm.this, "����");
					} else {
						JOptionPane.showMessageDialog(RegistForm.this, "����");
						product.getProduct(null);// ����� ���Ž�Ű���� �޼��� ȣ�� ���̺��� �ٽ� �����´�..
						this.product.addRemoveContent(this, this.product.p_center);

					}
				}
			}
		});

		bt_list.addActionListener((e) -> {
			this.product.addRemoveContent(this, this.product.p_center);

		});

		ch_top.addItemListener((e) -> {
			int index = ch_top.getSelectedIndex();
			getSubcategory(index);
		});
		getSubcategory(0);
	}

	// ���� ������ ������ �ֻ��� ī�װ��� �Ҽӵ� ����ī�װ��� ��������!
	public void getSubcategory(int index) {
		ArrayList<String> list = (ArrayList<String>) product.subCategory.get(index);
		ch_sub.removeAll();
		for (String item : list) {
			ch_sub.add(item);
		}
	}

	// ������ ������ ���������κ��� subcategory�� pk �� ��������
	public int getSubId(String name) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int subcategory_id = 0;
		String sql = "select * from subcategory where name = ?";

		try {
			pstmt = product.getAdminMain().getCon().prepareStatement(sql);
			pstmt.setString(1, name); // �Ű������� ���޵� �������� ���ε� ������ ����
			rs = pstmt.executeQuery();
			if (rs.next()) {
				subcategory_id = rs.getInt("subcategory_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			product.getAdminMain().getDbManager().close(pstmt, rs);
		}

		return subcategory_id;

	}

	public int regist() {
		PreparedStatement pstmt = null;
		int result = 0;
		String sql = "insert into product(product_id,subcategory_id,product_name,brand,price,filename,detail) values(seq_product.nextval,?,?,?,?,?,?)";
		try {
			pstmt = product.getAdminMain().getCon().prepareStatement(sql);
			pstmt.setInt(1, getSubId(ch_sub.getSelectedItem()));
			pstmt.setString(2, t_product_name.getText());
			pstmt.setString(3, t_brand.getText());
			pstmt.setInt(4, Integer.parseInt(t_price.getText()));
			pstmt.setString(5, t_filename.getText());
			pstmt.setString(6, t_detail.getText());

			result = pstmt.executeUpdate();
			if (result == 0) {
				System.out.println("����");
			} else {
				System.out.println("����");
			}

			ch_top.select(0);
			getSubcategory(0);
			t_product_name.setText("");
			t_brand.setText("");
			t_price.setText("");
			t_filename.setText("");
			t_detail.setText("");

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			product.getAdminMain().getDbManager().close(pstmt);
		}
		return result;

	}

}
