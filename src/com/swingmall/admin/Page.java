//��� ������ ���������� �������� �Ӽ� �޼������ �����ϱ� ���� �ֻ��� ������ Ŭ����
//���� home,product, Q&A mypage login ���� ����������
//��Ŭ������ ��ӹ��� ��� �ڵ带 �ߺ��ؼ� �ۼ������ʾƵ� �ȴ�!!


package com.swingmall.admin;

import java.awt.Dimension;

import javax.swing.JPanel;

public class Page extends JPanel{
	private AdminMain adminMain;
	
	public AdminMain getAdminMain() {
		return adminMain;
	}
	
	public void setAdminMain(AdminMain adminMain) {
		this.adminMain = adminMain;
	}
	public Page(AdminMain adminMain) {
		this.adminMain = adminMain;
		this.setPreferredSize(new Dimension(this.adminMain.WIDTH,this.adminMain.HEIGHT-100));
	}
}
