package org.xpdojo.bank;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.xpdojo.bank.Account.accountWithBalance;
import static org.xpdojo.bank.Account.emptyAccount;
import static org.xpdojo.bank.Money.ZERO;
import static org.xpdojo.bank.Money.amountOf;
import static org.xpdojo.bank.Transaction.Deposit.deposit;

class AccountTest {

	@Test
	void newAccountShouldHaveZeroBalance() {
		Account account = emptyAccount();
		assertThat(account.balance(), is(ZERO));
	}

	@Test
	void depositToEmptyAccountShouldIncreaseTheBalance() {
		Account account = emptyAccount();
		account.deposit(amountOf(10));
		assertThat(account.balance(), is(amountOf(10)));
	}

	@Test
	void depositToNonEmptyAccountShouldIncreaseTheBalance() {
		Account account = accountWithBalance(amountOf(10));
		account.deposit(amountOf(20));
		assertThat(account.balance(), is(amountOf(30)));
	}

	@Test
	void withdrawalShouldDecreaseTheBalance() {
		Account account = accountWithBalance(amountOf(10));
		account.withdraw(amountOf(10));
		assertThat(account.balance(), is(ZERO));
	}

	@Test
	void withdrawalShouldNotBeAppliedWhenItTakesAccountOverdrawn() {
		Account account = emptyAccount();
		account.withdraw(amountOf(1));
		assertThat(account.balance(), is(ZERO));
	}

	@Test
	void transferShouldMoveMoneyFromOneAccountToAnother() {
		Account sender = accountWithBalance(amountOf(10));
		Account receiver = emptyAccount();

		sender.transfer(amountOf(10), receiver);

		assertThat(sender.balance(), is(ZERO));
		assertThat(receiver.balance(), is(amountOf(10)));
	}

	@Test
	void transferShouldNotBeAppliedWhenItTakesSendingAccountOverdrawn() {
		Account sender = emptyAccount();
		Account receiver = emptyAccount();

		sender.transfer(amountOf(1), receiver);

		assertThat(sender.balance(), is(ZERO));
		assertThat(receiver.balance(), is(ZERO));
	}

	@Test
	void statementShouldBeWrittenToSuppliedWriter() throws IOException {
		Statement statement = (account, writer) -> writer.append("STATEMENT GENERATED ").append(account.balance().toString());
		String actual = accountWithBalance(amountOf(2123)).writeStatement(statement, new StringWriter());
		assertThat(actual, is("STATEMENT GENERATED 2,123.00"));
	}
	
	@Test
	void emptyAccountIncludesAnInitialDeposit() {
		Account account = emptyAccount();
		List<Transaction> transactions = account.transactions().collect(Collectors.toList());
		
		assertThat(transactions, contains(deposit(ZERO)));
	}	
	
}
