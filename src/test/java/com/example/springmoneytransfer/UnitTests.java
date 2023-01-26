package com.example.springmoneytransfer;

import com.example.springmoneytransfer.models.Amount;
import com.example.springmoneytransfer.models.CardsData;
import com.example.springmoneytransfer.models.TransferRequest;
import com.example.springmoneytransfer.repositories.CardsRepository;
import com.example.springmoneytransfer.services.MoneyTransferService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;

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

	@Test
	void moneyTransferServiceCheckTransaction() {
		//given
		TransferRequest transferRequest = new TransferRequest();
		transferRequest.setCardFromNumber("test");
		transferRequest.setCardFromValidTill("test");
		transferRequest.setCardFromCVV("test");
		Amount amount = new Amount();
		amount.setValue(10000);
		amount.setCurrency("RUR");
		transferRequest.setAmount(amount);
		transferRequest.setCardToNumber("test");
		CardsRepository cardsRepository = Mockito.mock(CardsRepository.class);
		Mockito.when(cardsRepository.checkAcceptorCard(transferRequest.getCardToNumber())).thenReturn(true);
		Mockito.when(cardsRepository.checkDonateCard(transferRequest.getCardFromNumber(), transferRequest.getCardFromCVV(),
				transferRequest.getCardFromValidTill(), transferRequest.getAmount().getCurrency(),
				(double) transferRequest.getAmount().getValue())).thenReturn(true);
		MoneyTransferService moneyTransferService = new MoneyTransferService(cardsRepository);

		int expectedIdLength = 36;

		//when
		String operationId = moneyTransferService.checkTransaction(transferRequest);

		//then
		Assertions.assertEquals(expectedIdLength, operationId.length());
	}

}
