//모든 페이작 공통적으로 가져야할 속성 메서드등을 정의하기 위한 최상위 페이지 클래스
//따라서 home,product, Q&A mypage login 등의 페이지들이
//이클래스를 상속받을 경우 코드를 중복해서 작성하지않아도 된다!!


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
