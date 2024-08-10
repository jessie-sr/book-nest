package com.pro.mybooklist.sqlforms;

public interface BookInCurrentCart {
	Long getBookid();
	Long getCartid();
	String getTitle();
	String getAuthor();
	String getIsbn();
	Integer getBook_year();
	Double getPrice();
	String getCategory();
	Integer getQuantity();
	String getUrl();
}
