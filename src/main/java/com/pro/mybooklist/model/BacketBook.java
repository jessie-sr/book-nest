package com.pro.mybooklist.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "backet_book")
public class BacketBook {

	@EmbeddedId
	private BacketBookKey id;

	@Column(name = "quantity")
	private int quantity;

	@ManyToOne
	@MapsId("backetid")
	@JoinColumn(name = "backetid", nullable = false)
	private Cart cart;

	@ManyToOne
	@MapsId("bookid")
	@JoinColumn(name = "bookid", nullable = false)
	private Book book;

	public BacketBook() {}

	public BacketBook(int quantity, Cart cart, Book book) {
		super();
		BacketBookKey backetBookKey = new BacketBookKey(cart.getBacketid(), book.getId());
		this.id = backetBookKey;
		this.quantity = quantity;
		this.cart = cart;
		this.book = book;
	}

	public BacketBook(Cart cart, Book book) {
		super();
		BacketBookKey backetBookKey = new BacketBookKey(cart.getBacketid(), book.getId());
		this.id = backetBookKey;
		this.quantity = 1;
		this.cart = cart;
		this.book = book;
	}

	public BacketBookKey getId() {
		return id;
	}

	public void setId(BacketBookKey id) {
		this.id = id;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Cart getBacket() {
		return cart;
	}

	public void setBacket(Cart cart) {
		this.cart = cart;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
