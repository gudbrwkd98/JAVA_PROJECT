//��� ������ ���������� �������� �Ӽ� �޼������ �����ϱ� ���� �ֻ��� ������ Ŭ����
//���� home,product, Q&A mypage login ���� ����������
//��Ŭ������ ��ӹ��� ��� �ڵ带 �ߺ��ؼ� �ۼ������ʾƵ� �ȴ�!!


package com.shoppingmall.main;

import java.awt.Dimension;

import javax.swing.JPanel;

public class Page extends JPanel{
	ShopMain shopMain;
	public ShopMain getShopMain() {
		return shopMain;
	}
	public void setShopMain(ShopMain shopMain) {
		this.shopMain = shopMain;
	}
	public Page(ShopMain shopMain) {
		this.shopMain = shopMain;
		this.setPreferredSize(new Dimension(this.shopMain.WIDTH,this.shopMain.HEIGHT-100));
	}
}
