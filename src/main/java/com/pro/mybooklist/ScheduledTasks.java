package com.pro.mybooklist;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pro.mybooklist.model.Cart;
import com.pro.mybooklist.model.BacketRepository;

@Component
public class ScheduledTasks {
	@Autowired
	private BacketRepository barepository;

	@Scheduled(fixedRate = 1, timeUnit = TimeUnit.DAYS)
	@Transactional
	public void deleteUnusedCarts() {
		List<Cart> carts = (List<Cart>) barepository.findAll();
		
		if (carts.size() > 0) {
			for (int i = 0; i < carts.size(); i++) {
				if (carts.get(i).getUser() == null && carts.get(i).isCurrent()) {
					if (LocalDate.now().isAfter(LocalDate.parse(carts.get(i).getExpiryDate()))) {
						barepository.delete(carts.get(i));
					}
				}
			}
		}
	}
}
