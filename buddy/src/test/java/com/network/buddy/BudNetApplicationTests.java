package com.network.buddy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BudNetApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void addTest() {
		assertEquals(5, 2 + 3);
	}

	@Test
	void subTest() {
		assertEquals(6, 10 - 4);
	}

}
