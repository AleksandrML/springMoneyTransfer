package com.example.springmoneytransfer;

import com.example.springmoneytransfer.models.CardsData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UnitTests {

	private final CardsData card = new CardsData("555000", "333", "12/25", "RUR", 5000);

	@Test
	void CardsDataCheckCvc() {
		//given: card from class field, "card from DB", (cvc: 333), and:
		String cvcToCheck = "222";

		//when
		boolean isCvcCorrect = card.checkCvc(cvcToCheck);

		//then
		Assertions.assertFalse(isCvcCorrect);
	}

	@Test
	void CardsDataCheckValidTill() {
		//given: card from class field, "card from DB", (validTill: 12/25), and:
		String validTillToCheck = "12/25";

		//when
		boolean isValidTillCorrect = card.checkValidTill(validTillToCheck);

		//then
		Assertions.assertTrue(isValidTillCorrect);
	}

	@Test
	void CardsDataIncreaseAmount() {
		//given: card with amount 5000 RUR:
		final CardsData localCard = new CardsData("555000", "333", "12/25", "RUR", 5000);

		//when
		localCard.increaseAmount(50, "RUR");

		//then
		Assertions.assertEquals(5050, localCard.getAmount());
	}

	@Test
	void CardsDataDecreaseAmount() {
		//given: card with amount 5000 RUR:
		final CardsData localCard = new CardsData("555000", "333", "12/25", "RUR", 5000);

		//when
		localCard.decreaseAmount(1000, "RUR");

		//then
		Assertions.assertEquals(4000, localCard.getAmount());
	}

}
