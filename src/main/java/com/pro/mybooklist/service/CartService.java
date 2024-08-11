package com.pro.mybooklist.service;

import java.util.Optional;

import com.pro.mybooklist.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.pro.mybooklist.httpforms.BacketInfo;
import com.pro.mybooklist.httpforms.BookQuantityInfo;
import com.pro.mybooklist.httpforms.QuantityInfo;
import com.pro.mybooklist.model.Cart;
import com.pro.mybooklist.sqlforms.QuantityOfCart;
import com.pro.mybooklist.sqlforms.TotalOfCart;

@Service
public class CartService {
	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartBookRepository cartBookRepository;

	@Autowired
	private CommonService commonService;

	// Method to get the total price of the backet by cartid and backet password:
	public TotalOfCart getTotalBycartid(BacketInfo backetInfo) {
		Long cartid = backetInfo.getId();
		String password = backetInfo.getPassword();
		commonService.findBacketAndCheckIsPrivateAndCheckPassword(cartid, password);

		TotalOfCart totalOfBacket = cartRepository.findTotalOfCart(cartid);
		return totalOfBacket;
	}

	// Method to get the total of current backet of user by authentication:
	public TotalOfCart getCurrentCartTotal(Authentication authentication) {
		User user = commonService.checkAuthentication(authentication);
		Long userId = user.getId();
		commonService.findCurrentBacketOfUser(user);

		TotalOfCart totalOfCurrentBacket = cartRepository.findTotalOfCurrentCart(userId);
		return totalOfCurrentBacket;
	}

	// Method to get the total amount of books in the current backet of the user
	// (returns interface with cartid and items fields):
	public QuantityOfCart getCurrentCartQuantity(Authentication authentication) {
		User user = commonService.checkAuthentication(authentication);
		Long userId = user.getId();
		commonService.findCurrentBacketOfUser(user);

		QuantityOfCart quantityOfCurrentBacket = cartRepository.findQuantityInCurrent(userId);
		return quantityOfCurrentBacket;
	}

	// Method to create Cart with password and no user. The method returns the
	// backet Id and its password
	public BacketInfo createBacketNoAuthentication() {
		String password = RandomStringUtils.randomAlphanumeric(15);
		String hashedPassword = commonService.encodePassword(password);
		Long cartid = this.createBacket(hashedPassword);

		BacketInfo createdBacketInfo = new BacketInfo(cartid, password);
		return createdBacketInfo;
	}

	private Long createBacket(String hashedPassword) {
		Cart cart = new Cart(hashedPassword);
		cartRepository.save(cart);
		Long cartid = cart.getCartid();

		return cartid;
	}

	// Method to add the certain quantity of the book to the backet by cartid and
	// backet password:
	public ResponseEntity<?> addBookToCartNoAuthentication(Long cartid,
			BookQuantityInfo bookQuantityAndBacketPassword) {
		Long bookId = bookQuantityAndBacketPassword.getBookid();
		int additionalQuantity = bookQuantityAndBacketPassword.getQuantity();
		String password = bookQuantityAndBacketPassword.getPassword();

		Cart cart = commonService.findBacketAndCheckIsPrivateAndCheckPasswordAndCheckIsCurrent(cartid, password);

		return this.addQuantityOfBookToTheBacket(cart, bookId, additionalQuantity);
	}

	// Method to add the certain quantity of the book to the current backet of the
	// user:
	public ResponseEntity<?> addBookToCurrentBacket(Long bookId, QuantityInfo quantityInfo,
			Authentication authentication) {
		int additionalQuantity = quantityInfo.getQuantity();

		User user = commonService.checkAuthentication(authentication);
		Cart currentCart = commonService.findCurrentBacketOfUser(user);

		return this.addQuantityOfBookToTheBacket(currentCart, bookId, additionalQuantity);
	}

	private ResponseEntity<?> addQuantityOfBookToTheBacket(Cart cart, Long bookId, int additionalQuantity) {
		Long cartid = cart.getCartid();
		Book book = commonService.findBook(bookId);
		Optional<CartBook> optionalBacketBook = this.getOptionalBacketBook(cartid, bookId);

		if (optionalBacketBook.isPresent()) {
			CartBook cartBook = optionalBacketBook.get();
			this.addQuantityToBacketBook(cartBook, additionalQuantity);
		} else {
			this.createBacketBook(additionalQuantity, cart, book);
		}

		return new ResponseEntity<>("Book was added to cart successfully", HttpStatus.OK);
	}

	private void addQuantityToBacketBook(CartBook cartBook, int additionalQuantity) {
		int currentQuantity = cartBook.getQuantity();
		int newQuantity = currentQuantity + additionalQuantity;
		this.setBookQuantityInCart(newQuantity, cartBook);
	}

	private void createBacketBook(int quantity, Cart cart, Book book) {
		CartBook cartBook = new CartBook(quantity, cart, book);
		cartBookRepository.save(cartBook);
	}

	// Method to reduce the amount of book by bookid and backetInfo
	public ResponseEntity<?> reduceBookNoAuthentication(Long bookId, BacketInfo backetInfo) {
		Long cartid = backetInfo.getId();
		String password = backetInfo.getPassword();

		Cart cart = commonService.findBacketAndCheckIsPrivateAndCheckPasswordAndCheckIsCurrent(cartid, password);

		return this.reduceQuantityOfBookInBacket(cart, bookId);
	}

	// Method to reduce the quantity of the book in the current backet of the
	// authenticated user:
	public ResponseEntity<?> reduceBookAuthenticated(Long bookId, Authentication authentication) {
		User user = commonService.checkAuthentication(authentication);
		Cart currentCart = commonService.findCurrentBacketOfUser(user);

		return this.reduceQuantityOfBookInBacket(currentCart, bookId);
	}

	private ResponseEntity<?> reduceQuantityOfBookInBacket(Cart cart, Long bookId) {
		Long cartid = cart.getCartid();
		commonService.findBook(bookId);

		CartBook cartBook = this.findBacketBook(bookId, cartid);
		int quantity = cartBook.getQuantity();
		quantity = quantity - 1;

		return this.reduceQuantityOfBookInBacket(quantity, cartBook);
	}

	private ResponseEntity<?> reduceQuantityOfBookInBacket(int quantity, CartBook cartBook) {
		if (quantity > 0) {
			this.setBookQuantityInCart(quantity, cartBook);
			return new ResponseEntity<>("The quantity of the book in the cart was reduced by one", HttpStatus.OK);
		} else {
			return this.deleteBookFromCart(cartBook);
		}
	}

	// Method to delete book from backet By bookid and backetInfo
	public ResponseEntity<?> deleteBookNoAuthentication(Long bookId, BacketInfo backetInfo) {
		Long cartid = backetInfo.getId();
		String password = backetInfo.getPassword();

		Cart cart = commonService.findBacketAndCheckIsPrivateAndCheckPasswordAndCheckIsCurrent(cartid, password);

		return this.deleteBookFromBacket(cart, bookId);
	}

	// Method to delete the book from the current backet of the authenticated user:
	public ResponseEntity<?> deleteBookFromCurrentBacket(Long bookId, Authentication authentication) {
		User user = commonService.checkAuthentication(authentication);
		Cart currentCart = commonService.findCurrentBacketOfUser(user);

		return this.deleteBookFromBacket(currentCart, bookId);
	}

	private ResponseEntity<?> deleteBookFromBacket(Cart cart, Long bookId) {
		Long cartid = cart.getCartid();
		commonService.findBook(bookId);
		CartBook cartBook = this.findBacketBook(bookId, cartid);

		return this.deleteBookFromCart(cartBook);
	}

	// Method to clear current backet of the authenticated user:
	public ResponseEntity<?> clearCurrentBacket(Long userId, Authentication authentication) {
		User user = commonService.checkAuthenticationAndAuthorize(authentication, userId);
		Cart currentCart = commonService.findCurrentBacketOfUser(user);

		long deleted = cartBookRepository.deleteByCart(currentCart);
		return new ResponseEntity<>(deleted + " records were deleted from current cart", HttpStatus.OK);
	}

	// Method to find CartBook:
	private CartBook findBacketBook(Long bookId, Long cartid) {
		Optional<CartBook> optionalBacketBook = this.getOptionalBacketBook(cartid, bookId);
		if (!optionalBacketBook.isPresent())
			throw new ResponseStatusException(HttpStatus.CONFLICT, "The book is not in the backet");

		CartBook cartBook = optionalBacketBook.get();
		return cartBook;
	}

	// Method to find optional backet book by cartid and bookId:
	private Optional<CartBook> getOptionalBacketBook(Long cartid, Long bookId) {
		CartBookKey cartBookKey = new CartBookKey(cartid, bookId);

		Optional<CartBook> optionalBacketBook = cartBookRepository.findById(cartBookKey);
		return optionalBacketBook;
	}

	// Method to set new quantity for the book in the backet
	private void setBookQuantityInCart(int quantity, CartBook cartBook) {
		cartBook.setQuantity(quantity);
		cartBookRepository.save(cartBook);
	}

	// Method to delete the book from the backet by deleting cartBook record:
	private ResponseEntity<?> deleteBookFromCart(CartBook cartBook) {
		cartBookRepository.delete(cartBook);
		return new ResponseEntity<>("The book was deleted from the cart", HttpStatus.OK);
	}
}
