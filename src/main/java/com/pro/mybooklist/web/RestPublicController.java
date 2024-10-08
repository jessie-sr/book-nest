package com.pro.mybooklist.web;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pro.mybooklist.httpforms.AccountCredentials;
import com.pro.mybooklist.httpforms.AddressInfoNoAuthentication;
import com.pro.mybooklist.httpforms.CartInfo;
import com.pro.mybooklist.httpforms.BookQuantityInfo;
import com.pro.mybooklist.httpforms.EmailInfo;
import com.pro.mybooklist.httpforms.OrderPasswordInfo;
import com.pro.mybooklist.httpforms.SignupCredentials;
import com.pro.mybooklist.httpforms.TokenInfo;
import com.pro.mybooklist.model.Book;
import com.pro.mybooklist.model.Category;
import com.pro.mybooklist.model.Order;
import com.pro.mybooklist.service.CartService;
import com.pro.mybooklist.service.BookService;
import com.pro.mybooklist.service.CategoryService;
import com.pro.mybooklist.service.OrderService;
import com.pro.mybooklist.service.UserService;
import com.pro.mybooklist.sqlforms.BookInCurrentCart;
import com.pro.mybooklist.sqlforms.RawBookInfo;
import com.pro.mybooklist.sqlforms.TotalOfCart;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@CrossOrigin(origins = "*")
@RestController
public class RestPublicController {
	@Autowired
	private BookService bookService;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CartService cartService;

	@Autowired
	private UserService userService;

	@Value("${spring.mail.username}")
	private String springMailUsername;

	@GetMapping("/books")
	public @ResponseBody List<Book> getBooks() {

		return bookService.getBooks();

	}

	@GetMapping("/categories")
	public @ResponseBody List<Category> getCategories() {

		return categoryService.getCategories();

	}

	@GetMapping("/books/{id}")
	public @ResponseBody Optional<Book> getBookById(@PathVariable("id") Long bookId) {

		return bookService.getBookById(bookId);

	}

	@PostMapping("/booksbycategory")
	public @ResponseBody List<Book> getBooksByCategory(@RequestBody Category category) {

		return bookService.getBooksByCategory(category);

	}

	@GetMapping("/topsales")
	public @ResponseBody List<RawBookInfo> getTopSales() {

		return bookService.getTopSales();

	}

	@PostMapping("/orderbypassword")
	public @ResponseBody Order getOrderByIdAndPassword(@RequestBody OrderPasswordInfo orderInfo) {

		return orderService.getOrderByIdAndPassword(orderInfo);

	}

	@GetMapping("/booksids/{cartid}")
	public @ResponseBody List<Long> getIdsOfBooksBycartid(@PathVariable("cartid") Long cartid) {

		return bookService.getIdsOfBooksBycartid(cartid);

	}

	@PostMapping("/showcart")
	public @ResponseBody List<BookInCurrentCart> getBooksInCartByIdAndPassword(@RequestBody CartInfo cartInfo) {

		return bookService.getBooksInCartByIdAndPassword(cartInfo);

	}

	@GetMapping("/booksinorder/{orderid}")
	public @ResponseBody List<BookInCurrentCart> getBooksByOrderId(@PathVariable("orderid") Long orderId) {

		return bookService.getBooksByOrderId(orderId);

	}

	@PostMapping("/totalofcart")
	public @ResponseBody TotalOfCart getTotalBycartid(@RequestBody CartInfo cartInfo) {

		return cartService.getTotalBycartid(cartInfo);

	}

	@GetMapping("/getordertotal/{orderid}")
	public @ResponseBody TotalOfCart getTotalOfOrderByOrderId(@PathVariable("orderid") Long orderId) {

		return orderService.getTotalOfOrderByOrderId(orderId);

	}

	@PostMapping("/checkordernumber")
	public ResponseEntity<?> checkOrderNumber(@RequestBody OrderPasswordInfo orderInfo) {

		return orderService.checkOrderNumber(orderInfo);

	}

	@PostMapping("/createcart")
	public @ResponseBody CartInfo createCartNoAuthentication() {

		return cartService.createCartNoAuthentication();

	}

	@PostMapping("/addbook/{cartid}")
	public ResponseEntity<?> addBookToCartNoAuthentication(@PathVariable("cartid") Long cartid,
			@RequestBody BookQuantityInfo bookQuantity) {

		return cartService.addBookToCartNoAuthentication(cartid, bookQuantity);

	}

	@PostMapping("/makesale")
	public @ResponseBody OrderPasswordInfo makeSaleNoAuthentication(
			@RequestBody AddressInfoNoAuthentication addressInfo)
			throws MessagingException, UnsupportedEncodingException {

		return orderService.makeSaleNoAuthentication(addressInfo);

	}

	@PutMapping("/reduceitemnoauth/{bookid}")
	@Transactional
	public ResponseEntity<?> reduceBookNoAuthentication(@PathVariable("bookid") Long bookId,
			@RequestBody CartInfo cartInfo) {

		return cartService.reduceBookNoAuthentication(bookId, cartInfo);

	}

	@DeleteMapping("/deletebook/{bookid}")
	@Transactional
	public ResponseEntity<?> deleteBookNoAuthentication(@PathVariable("bookid") Long bookId,
			@RequestBody CartInfo cartInfo) {

		return cartService.deleteBookNoAuthentication(bookId, cartInfo);

	}

	@PostMapping("/login")
	public ResponseEntity<?> getToken(@RequestBody AccountCredentials credentials) {

		return userService.getToken(credentials);

	}

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignupCredentials credentials)
			throws UnsupportedEncodingException, MessagingException {

		return userService.signUp(credentials);

	}

	@PutMapping("/verify")
	public ResponseEntity<?> verifyUser(@RequestBody TokenInfo tokenInfo) {
		
		return userService.verifyUser(tokenInfo);
		
	}

	@PutMapping("/resetpassword")
	public ResponseEntity<?> resetPassword(@RequestBody EmailInfo emailInfo)
			throws UnsupportedEncodingException, MessagingException {
		
		return userService.resetPassword(emailInfo);
		
	}

}
