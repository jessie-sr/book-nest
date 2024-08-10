package com.pro.mybooklist.repositorytest;

import static org.assertj.core.api.Assertions.assertThat;

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

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@TestInstance(Lifecycle.PER_CLASS)
@Transactional
public class CartBookRepositoryTest {
	@Autowired
	private CategoryRepository crepository;

	@Autowired
	private UserRepository urepository;

	@Autowired
	private BacketRepository backetRepository;

	@Autowired
	private BookRepository bookRepository;

	@Autowired
	private BacketBookRepository backetBookRepository;

	@BeforeAll
	public void resetUserRepo() {
		crepository.deleteAll();
		urepository.deleteAll();
		backetRepository.deleteAll();
		bookRepository.deleteAll();
		backetBookRepository.deleteAll();
	}

	// CRUD tests for the backetbook repository
	// Create functionality
	@Test
	@Rollback
	public void testCreateBacketBookDefaultQuantity() {
		// Testing backetBook creating with backet with no user;
		CartBook newCartBook1 = this.createBacketBookDefaultQuantityNoUser("Little Women", "Other");
		assertThat(newCartBook1.getId()).isNotNull();

		this.createBacketBookDefaultQuantityNoUser("Little Women 2", "Other");
		List<CartBook> cartBooks = (List<CartBook>) backetBookRepository.findAll();
		assertThat(cartBooks).hasSize(2);

		// Testing backetBook creating with backet with user;
		CartBook newCartBookUser1 = this.createBacketBookDefaultQuantityUser("user1", "Little Women 3", "Other");
		assertThat(newCartBookUser1.getId()).isNotNull();

		this.createBacketBookDefaultQuantityUser("user2", "Little Women 4", "Other");
		cartBooks = (List<CartBook>) backetBookRepository.findAll();
		assertThat(cartBooks).hasSize(4);
	}

	@Test
	@Rollback
	public void testCreateBacketBookCustomQuantity() {
		// Testing backetBook creating with backet with no user;
		CartBook newCartBook1 = this.createBacketBookCustomQuantityNoUser(2, "Little Women", "Other");
		assertThat(newCartBook1.getId()).isNotNull();

		this.createBacketBookCustomQuantityNoUser(3, "Little Women 2", "Other");
		List<CartBook> cartBooks = (List<CartBook>) backetBookRepository.findAll();
		assertThat(cartBooks).hasSize(2);

		// Testing backetBook creating with backet with user;
		CartBook newCartBookUser1 = this.createBacketBookCustomQuantityUser(2, "user1", "Little Women 3", "Other");
		assertThat(newCartBookUser1.getId()).isNotNull();

		this.createBacketBookCustomQuantityUser(4, "user2", "Little Women 4", "Other");
		cartBooks = (List<CartBook>) backetBookRepository.findAll();
		assertThat(cartBooks).hasSize(4);
	}

	@Test
	@Rollback
	public void testFindAllAndFindById() {
		List<CartBook> cartBooks = (List<CartBook>) backetBookRepository.findAll();
		assertThat(cartBooks).isEmpty();

		BacketBookKey wrongKey = new BacketBookKey(Long.valueOf(2), Long.valueOf(2));
		Optional<CartBook> optionalBacketBook = backetBookRepository.findById(wrongKey);
		assertThat(optionalBacketBook).isNotPresent();

		CartBook newCartBook = this.createBacketBookDefaultQuantityNoUser("Little Women", "Other");
		BacketBookKey goodKey = newCartBook.getId();

		optionalBacketBook = backetBookRepository.findById(goodKey);
		assertThat(optionalBacketBook).isPresent();

		this.createBacketBookDefaultQuantityNoUser("Little Women 2", "Other");

		cartBooks = (List<CartBook>) backetBookRepository.findAll();
		assertThat(cartBooks).hasSize(2);
	}
	
	@Test
	@Rollback
	public void testFindByBacket() {
		Cart emptyCart = this.createBacketNoUser();
		List<CartBook> cartBooks = backetBookRepository.findByBacket(emptyCart);
		assertThat(cartBooks).isEmpty();
		
		CartBook newCartBook = this.createBacketBookDefaultQuantityNoUser("Little Women", "Other");
		Cart cart = newCartBook.getBacket();
		
		cartBooks = backetBookRepository.findByBacket(cart);
		assertThat(cartBooks).hasSize(1);
		
		this.createBacketBookDefaultQuantityUser("user1", "Little Women", "Other");
		this.createBacketBookDefaultQuantityUser("user1", "Little Women 2", "Other");
		CartBook cartBook3User1 = this.createBacketBookCustomQuantityUser(2, "user1", "Fight Club", "Thriller");
		Cart cartOfUser1 = cartBook3User1.getBacket();
		
		cartBooks = backetBookRepository.findByBacket(cartOfUser1);
		assertThat(cartBooks).hasSize(3);
	}

	@Test
	@Rollback
	public void testUpdate() {
		CartBook cartBook = this.createBacketBookDefaultQuantityNoUser("Little Women", "Other");
		cartBook.setQuantity(3);
		backetBookRepository.save(cartBook);
		
		assertThat(cartBook.getQuantity()).isEqualTo(3);
	}
	
	@Test
	@Rollback
	public void testDeleteByIdAndDeleteAll() {
		CartBook cartBookToDelete = this.createBacketBookCustomQuantityNoUser(2, "Little Women", "Other");
		BacketBookKey key = cartBookToDelete.getId();
		backetBookRepository.deleteById(key);
		
		Optional<CartBook> optionalBacketBook = backetBookRepository.findById(key);
		assertThat(optionalBacketBook).isNotPresent();
		
		this.createBacketBookCustomQuantityNoUser(2, "Little Women", "Other");
		this.createBacketBookCustomQuantityNoUser(2, "Little Women 2", "Other");
		backetBookRepository.deleteAll();
		
		List<CartBook> cartBooks = (List<CartBook>) backetBookRepository.findAll();
		assertThat(cartBooks).isEmpty();
	}
	
	@Test
	@Rollback
	public void testDeleteByBacket() {
		CartBook newCartBook = this.createBacketBookDefaultQuantityNoUser("Little Women", "Other");
		Cart cart = newCartBook.getBacket();

		long quantityOfDeletedBacketBooks = backetBookRepository.deleteByBacket(cart);
		assertThat(quantityOfDeletedBacketBooks).isEqualTo(1);

		this.createBacketBookDefaultQuantityUser("user1", "Little Women", "Other");
		this.createBacketBookDefaultQuantityUser("user1", "Little Women 2", "Other");
		CartBook cartBook3User1 = this.createBacketBookCustomQuantityUser(2, "user1", "Fight Club", "Thriller");
		Cart cartOfUser1 = cartBook3User1.getBacket();

		quantityOfDeletedBacketBooks = backetBookRepository.deleteByBacket(cartOfUser1);
		assertThat(quantityOfDeletedBacketBooks).isEqualTo(3);
	}

	private CartBook createBacketBookCustomQuantityNoUser(int quantity, String title, String categoryName) {
		Cart cart = this.createBacketNoUser();
		Book book = this.createBook(title, categoryName);

		CartBook newCartBook = new CartBook(quantity, cart, book);
		backetBookRepository.save(newCartBook);

		return newCartBook;
	}

	private CartBook createBacketBookCustomQuantityUser(int quantity, String username, String title,
														String categoryName) {
		User user = this.createUser(username);

		Cart cart = this.createBacketWithUser(user);
		Book book = this.createBook(title, categoryName);

		CartBook newCartBook = new CartBook(quantity, cart, book);
		backetBookRepository.save(newCartBook);

		return newCartBook;
	}

	private CartBook createBacketBookDefaultQuantityNoUser(String title, String categoryName) {
		Cart cart = this.createBacketNoUser();
		Book book = this.createBook(title, categoryName);

		CartBook newCartBook = new CartBook(cart, book);
		backetBookRepository.save(newCartBook);

		return newCartBook;
	}

	private CartBook createBacketBookDefaultQuantityUser(String username, String title, String categoryName) {
		User user = this.createUser(username);

		Cart cart = this.createBacketWithUser(user);
		Book book = this.createBook(title, categoryName);

		CartBook newCartBook = new CartBook(cart, book);
		backetBookRepository.save(newCartBook);

		return newCartBook;
	}

	private User createUser(String username) {
		Optional<User> optionalUser = urepository.findByUsername(username);
		if (optionalUser.isPresent())
			return optionalUser.get();

		User newUser = new User(username, username, username, username, "USER", username, true);
		urepository.save(newUser);

		return newUser;
	}

	private Cart createBacketWithUser(User user) {
		List<Cart> currentCarts = backetRepository.findCurrentByUserid(user.getId());
		if (currentCarts.size() != 0)
			return currentCarts.get(0);

		Cart newCart = new Cart(true, user);
		backetRepository.save(newCart);

		return newCart;
	}

	private Cart createBacketNoUser() {
		Cart newCart = new Cart(true);
		backetRepository.save(newCart);

		return newCart;
	}

	private Book createBook(String title, String categoryName) {
		String isbn = title + "isbn";
		Optional<Book> optionalBook = bookRepository.findByIsbn(isbn);

		if (optionalBook.isPresent())
			return optionalBook.get();

		Category category = this.createCategory(categoryName);

		Book newBook = new Book(title, "Chuck Palahniuk", isbn, 1940, 11.2, category, "some_url");

		return newBook;
	}

	private Category createCategory(String categoryName) {
		Optional<Category> optionalCategory = crepository.findByName(categoryName);

		if (optionalCategory.isPresent())
			return optionalCategory.get();

		Category newCategory = new Category(categoryName);
		crepository.save(newCategory);

		return newCategory;
	}
}
