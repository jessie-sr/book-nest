package com.pro.mybooklist.web;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pro.mybooklist.httpforms.AddressInfo;
import com.pro.mybooklist.httpforms.OrderPasswordInfo;
import com.pro.mybooklist.httpforms.PasswordInfo;
import com.pro.mybooklist.httpforms.QuantityInfo;
import com.pro.mybooklist.model.Order;
import com.pro.mybooklist.model.User;
import com.pro.mybooklist.service.CartService;
import com.pro.mybooklist.service.BookService;
import com.pro.mybooklist.service.OrderService;
import com.pro.mybooklist.service.UserService;
import com.pro.mybooklist.sqlforms.BookInCurrentCart;
import com.pro.mybooklist.sqlforms.QuantityOfCart;
import com.pro.mybooklist.sqlforms.TotalOfCart;

import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;

@CrossOrigin(origins = "*")
@RestController
@PreAuthorize("isAuthenticated()")
public class RestAuthenticatedController {
	@Autowired
	private OrderService orderService;

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	@Autowired
	private CartService cartService;

	@PutMapping("/updateuser/{userid}")
	public ResponseEntity<?> updateUser(@PathVariable("userid") Long userId, @RequestBody User user,
			Authentication authentication) {
		
		return userService.updateUser(userId, user, authentication);
		
	}

	@PutMapping("/changepassword")
	public ResponseEntity<?> changePassword(@RequestBody PasswordInfo passwordInfo, Authentication authentication) {
		
		return userService.changePassword(passwordInfo, authentication);
		
	}

	@GetMapping("/users/{userid}/orders")
	public @ResponseBody List<Order> getOrdersByUserId(@PathVariable("userid") Long userId,
			Authentication authentication) {

		return orderService.getOrdersByUserId(userId, authentication);

	}

	@GetMapping(value = "/users/{id}")
	public @ResponseBody User getUserById(@PathVariable("id") Long userId, Authentication authentication) {

		return userService.getUserById(userId, authentication);

	}

	@GetMapping("/booksids")
	public @ResponseBody List<Long> getIdsOfBooksInCurrentCart(Authentication authentication) {

		return bookService.getIdsOfBooksInCurrentCart(authentication);

	}

	@GetMapping("/showcart/{userid}")
	public @ResponseBody List<BookInCurrentCart> getCurrentCartByUserId(@PathVariable("userid") Long userId,
			Authentication authentication) {

		return bookService.getCurrentCartByUserId(userId, authentication);

	}

	@GetMapping("/getcurrenttotal")
	public @ResponseBody TotalOfCart getCurrentCartTotal(Authentication authentication) {

		return cartService.getCurrentCartTotal(authentication);

	}

	@GetMapping("/currentcartquantity")
	public @ResponseBody QuantityOfCart getCurrentCartQuantity(Authentication authentication) {

		return cartService.getCurrentCartQuantity(authentication);

	}

	@PostMapping("/additem/{bookid}")
	public ResponseEntity<?> addBookToCurrentCart(@PathVariable("bookid") Long bookId,
			@RequestBody QuantityInfo quantityInfo, Authentication authentication) {

		return cartService.addBookToCurrentCart(bookId, quantityInfo, authentication);

	}

	@PostMapping("/makesale/{userid}")
	public @ResponseBody OrderPasswordInfo makeSaleByUserId(@PathVariable("userid") Long userId,
			@RequestBody AddressInfo addressInfo, Authentication authentication)
			throws MessagingException, UnsupportedEncodingException {

		return orderService.makeSaleByUserId(userId, addressInfo, authentication);

	}

	@PutMapping("/reduceitem/{bookid}")
	@Transactional
	public ResponseEntity<?> reduceBookAuthenticated(@PathVariable("bookid") Long bookId,
			Authentication authentication) {

		return cartService.reduceBookAuthenticated(bookId, authentication);

	}

	@DeleteMapping("/deleteitem/{bookid}")
	@Transactional
	public ResponseEntity<?> deleteBookFromCurrentCart(@PathVariable("bookid") Long bookId,
			Authentication authentication) {

		return cartService.deleteBookFromCurrentCart(bookId, authentication);

	}

	@DeleteMapping("/clearcart/{userid}")
	@Transactional
	public ResponseEntity<?> clearCurrentCart(@PathVariable("userid") Long userId, Authentication authentication) {
		
		return cartService.clearCurrentCart(userId, authentication);
		
	}
}
