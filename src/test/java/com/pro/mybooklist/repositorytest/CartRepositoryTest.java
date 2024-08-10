package com.pro.mybooklist.repositorytest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.pro.mybooklist.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.pro.mybooklist.model.Cart;
import com.pro.mybooklist.sqlforms.QuantityOfBacket;
import com.pro.mybooklist.sqlforms.TotalOfBacket;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class CartRepositoryTest {
	@Autowired
	private UserRepository urepository;

	@Autowired
	private BacketBookRepository backetBookRepository;

	@Autowired
	private BookRepository bookrepository;

	@Autowired
	private CategoryRepository crepository;

	@Autowired
	private OrderRepository orepository;

	@Autowired
	private BacketRepository backetrepository;

	@BeforeAll
	public void resetUserRepo() {
		urepository.deleteAll();
		backetBookRepository.deleteAll();
		bookrepository.deleteAll();
		crepository.deleteAll();
		orepository.deleteAll();
		backetrepository.deleteAll();
	}

	// CRUD tests for the backet repository
	// Create functionality
	@Test
	@Rollback
	public void testCreateBacketWithUser() {
		Cart newCart1 = this.createBacketWithUser(true, "user1");
		assertThat(newCart1.getBacketid()).isNotNull();

		this.createBacketWithUser(true, "user2");
		List<Cart> carts = (List<Cart>) backetrepository.findAll();
		assertThat(carts).hasSize(2);
	}

	@Test
	@Rollback
	public void testCreateBacketNoUser() {
		Cart newCart1 = this.createBacketNoUser(true);
		assertThat(newCart1.getBacketid()).isNotNull();

		this.createBacketNoUser(true);
		List<Cart> carts = (List<Cart>) backetrepository.findAll();
		assertThat(carts).hasSize(2);
	}

	@Test
	@Rollback
	public void testFindAllAndFindById() {
		List<Cart> carts = (List<Cart>) backetrepository.findAll();
		assertThat(carts).isEmpty();

		Optional<Cart> optionalBacket = backetrepository.findById(Long.valueOf(2));
		assertThat(optionalBacket).isNotPresent();

		Cart cart = this.createBacketNoUser(true);
		Long backetId = cart.getBacketid();

		optionalBacket = backetrepository.findById(backetId);
		assertThat(optionalBacket).isPresent();

		this.createBacketNoUser(true);

		carts = (List<Cart>) backetrepository.findAll();
		assertThat(carts).hasSize(2);
	}

	@Test
	@Rollback
	public void testFindQuantityInCurrent() {
		QuantityOfBacket quantityOfBacket = backetrepository.findQuantityInCurrent(Long.valueOf(2));
		assertThat(quantityOfBacket).isNull();

		String username = "user1";
		User user = this.createUser(username);
		Long userId = user.getId();

		Cart cart = this.createBacketWithUser(true, username);
		Book book1 = this.createBook("Little Women", "Other", 10.2);
		this.createBacketBookCustomQuantity(2, book1, cart);

		quantityOfBacket = backetrepository.findQuantityInCurrent(userId);
		assertThat(quantityOfBacket).isNotNull();
		assertThat(quantityOfBacket.getItems()).isEqualTo(2);

		Book book2 = this.createBook("Little Women 2", "Other", 10.2);
		this.createBacketBookCustomQuantity(3, book2, cart);
		quantityOfBacket = backetrepository.findQuantityInCurrent(userId);
		assertThat(quantityOfBacket.getItems()).isEqualTo(5);
	}

	@Test
	@Rollback
	public void testFindTotalOfBacket() {
		TotalOfBacket totalOfBacket = backetrepository.findTotalOfBacket(Long.valueOf(2));
		assertThat(totalOfBacket).isNull();

		String username = "user1";
		Cart cart = this.createBacketWithUser(true, username);
		Long backetId = cart.getBacketid();

		double price1 = 10.2;
		Book book1 = this.createBook("Little Women", "Other", price1);
		this.createBacketBookCustomQuantity(2, book1, cart);

		totalOfBacket = backetrepository.findTotalOfBacket(backetId);
		assertThat(totalOfBacket).isNotNull();
		assertThat(totalOfBacket.getTotal()).isEqualTo(price1 * 2);

		double price2 = 8.2;
		Book book2 = this.createBook("Little Women 2", "Other", price2);
		this.createBacketBookCustomQuantity(3, book2, cart);
		totalOfBacket = backetrepository.findTotalOfBacket(backetId);
		assertThat(totalOfBacket).isNotNull();
		assertThat(totalOfBacket.getTotal()).isEqualTo(price1 * 2 + price2 * 3);

		Cart cartNoUser = this.createBacketNoUser(true);
		Long backet2Id = cartNoUser.getBacketid();

		totalOfBacket = backetrepository.findTotalOfBacket(backet2Id);
		assertThat(totalOfBacket).isNull();

		this.createBacketBookCustomQuantity(1, book2, cartNoUser);
		totalOfBacket = backetrepository.findTotalOfBacket(backet2Id);
		assertThat(totalOfBacket).isNotNull();
		assertThat(totalOfBacket.getTotal()).isEqualTo(price2);
	}

	@Test
	@Rollback
	public void testFindTotalOfOrder() {
		TotalOfBacket totalOfBacket = backetrepository.findTotalOfOrder(Long.valueOf(2));
		assertThat(totalOfBacket).isNull();

		int quantity = 2;
		double priceBook1 = 11.2;
		double priceBook2 = 4.6;

		List<Book> booksInOrder1 = new ArrayList<Book>();
		Book book1 = this.createBook("Little Women", "Other", priceBook1);
		Book book2 = this.createBook("Fight Club", "Thriller", priceBook2);
		booksInOrder1.add(book1);
		booksInOrder1.add(book2);

		Order order1 = this.createSale(quantity, booksInOrder1);
		Long order1Id = order1.getOrderid();

		totalOfBacket = backetrepository.findTotalOfOrder(order1Id);
		assertThat(totalOfBacket).isNotNull();
		assertThat(totalOfBacket.getTotal()).isCloseTo(quantity * (priceBook1 + priceBook2), offset(0.01));
	}

	@Test
	@Rollback
	public void testFindTotalOfCurrentCart() {
		TotalOfBacket totalOfCurrent = backetrepository.findTotalOfCurrentCart(Long.valueOf(2));
		assertThat(totalOfCurrent).isNull();

		String username = "user1";
		Cart cart = this.createBacketWithUser(true, username);

		double price1 = 10.2;
		Book book1 = this.createBook("Little Women", "Other", price1);
		this.createBacketBookCustomQuantity(2, book1, cart);

		Long user1Id = urepository.findByUsername(username).get().getId();

		totalOfCurrent = backetrepository.findTotalOfCurrentCart(user1Id);
		assertThat(totalOfCurrent).isNotNull();
		assertThat(totalOfCurrent.getTotal()).isEqualTo(price1 * 2);

		double price2 = 8.2;
		Book book2 = this.createBook("Little Women 2", "Other", price2);
		this.createBacketBookCustomQuantity(3, book2, cart);
		totalOfCurrent = backetrepository.findTotalOfCurrentCart(user1Id);
		assertThat(totalOfCurrent).isNotNull();
		assertThat(totalOfCurrent.getTotal()).isEqualTo(price1 * 2 + price2 * 3);
	}

	@Test
	@Rollback
	public void testFindNotCurrentByUserid() {
		List<Long> idsOfNotCurrentBackets = backetrepository.findNotCurrentByUserid(Long.valueOf(2));
		assertThat(idsOfNotCurrentBackets).isEmpty();

		String username = "user1";
		Cart cart = this.createBacketWithUser(false, username);
		this.createBacketWithUser(true, username);
		Long backetId = cart.getBacketid();

		Long user1Id = urepository.findByUsername(username).get().getId();

		idsOfNotCurrentBackets = backetrepository.findNotCurrentByUserid(user1Id);
		assertThat(idsOfNotCurrentBackets).hasSize(1);
		assertThat(idsOfNotCurrentBackets.get(0)).isEqualTo(backetId);
	}
	
	@Test
	@Rollback
	public void testFindCurrentByUserid() {
		List<Cart> currentCarts = backetrepository.findCurrentByUserid(Long.valueOf(2));
		assertThat(currentCarts).isEmpty();
		
		String username = "user1";
		this.createBacketWithUser(false, username);
		this.createBacketWithUser(true, username);
		
		Long user1Id = urepository.findByUsername(username).get().getId();

		currentCarts = backetrepository.findCurrentByUserid(user1Id);
		assertThat(currentCarts).hasSize(1);
	}
	
	@Test
	@Rollback
	public void testUpdateBacket() {
		boolean current = true;
		Cart newCart = this.createBacketNoUser(current);
		
		String updatedPwd = "newHash";
		
		newCart.setCurrent(!current);
		newCart.setPasswordHash(updatedPwd);
		backetrepository.save(newCart);
		
		assertThat(newCart.isCurrent()).isEqualTo(!current);
		assertThat(newCart.getPasswordHash()).isEqualTo(updatedPwd);
	}
	
	@Test
	@Rollback
	public void testDeleteBacket() {
		Cart cartToDelete = this.createBacketNoUser(false);
		Long backetId = cartToDelete.getBacketid();
		backetrepository.deleteById(backetId);
		
		Optional<Cart> optionalBacket = backetrepository.findById(backetId);
		assertThat(optionalBacket).isNotPresent();
		
		this.createBacketNoUser(false);
		this.createBacketNoUser(false);
		backetrepository.deleteAll();
		
		List<Cart> carts = (List<Cart>) backetrepository.findAll();
		assertThat(carts).isEmpty();
	}

	private Order createSale(int quantity, List<Book> books) {
		Cart cart = this.createBacketNoUser(false);

		for (Book book : books) {
			this.createBacketBookCustomQuantity(quantity, book, cart);
		}

		String stringField = "field";

		Order newOrder = new Order(stringField, stringField, stringField, stringField, stringField, stringField,
				stringField, cart, stringField, stringField);
		orepository.save(newOrder);

		return newOrder;
	}

	private Cart createBacketWithUser(boolean current, String username) {
		User user = this.createUser(username);

		List<Cart> currentCarts = backetrepository.findCurrentByUserid(user.getId());
		if (currentCarts.size() != 0)
			return currentCarts.get(0);

		Cart newCart = new Cart(current, user);
		backetrepository.save(newCart);

		return newCart;
	}

	private Cart createBacketNoUser(boolean current) {
		Cart newCart = new Cart(current);
		backetrepository.save(newCart);

		return newCart;
	}

	private User createUser(String username) {
		Optional<User> optionalUser = urepository.findByUsername(username);

		if (optionalUser.isPresent())
			return optionalUser.get();

		User user = new User("Firstname", "Lastname", username, "hash_pwd", "USER", username + "@mail.com", false);
		urepository.save(user);

		return user;
	}

	private BacketBook createBacketBookCustomQuantity(int quantity, Book book, Cart cart) {
		BacketBook newBacketBook = new BacketBook(quantity, cart, book);
		backetBookRepository.save(newBacketBook);

		return newBacketBook;
	}

	private Book createBook(String title, String categoryName, double price) {
		Category category = this.createCategory(categoryName);
		Book newBook = new Book(title, "Chuck Palahniuk", title + "isbn", 1998, price, category, "someurlToPicture");
		bookrepository.save(newBook);

		return newBook;
	}

	private Category createCategory(String categoryName) {
		Optional<Category> optionalCategory = crepository.findByName(categoryName);
		if (optionalCategory.isPresent())
			return optionalCategory.get();

		Category category = new Category(categoryName);
		crepository.save(category);

		return category;
	}

}
