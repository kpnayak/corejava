package com.threading.interthreadcommunication;

import java.util.Random;

class Deposit implements Runnable {

	Bank bank;

	public Deposit(Bank bank) {
		this.bank = bank;
	}

	@Override
	public void  run() {

		try {
			while (true) {
				synchronized (bank) {
					if (Bank.depositFlag) {
						Thread.sleep(1000);
						int depositAmount = new Random().nextInt(500);
						bank.depositAmount(depositAmount);
						Bank.depositFlag = false;
						bank.notify();
					} else {
						bank.wait();
					}

				}
			}

		} catch (Exception e) {
			System.out.println("Inside The Deposit " + e);
		}

	}

}

class Withdraw implements Runnable {
	Bank bank;

	public Withdraw(Bank bank) {
		this.bank = bank;
	}

	@Override
	public void run() {
		try {
			while (true) {
				synchronized (bank) {
					if (!Bank.depositFlag) {
						Thread.sleep(1000);
						int withdrwalAmount = new Random().nextInt(2000);
						bank.withdrawAmount(withdrwalAmount);
						Bank.depositFlag = true;
						bank.notify();
					} else {
						bank.wait();
					}

				}
			}
		} catch (Exception e) {
			System.out.println("Inside the Withdrwal " + e);
		}
	}

}

public class ProducerConsumerTest {

	public static void main(String[] args) {

		final Bank bank = new Bank(1000);

		Deposit deposit = new Deposit(bank);
		Withdraw withdraw = new Withdraw(bank);

		Thread t1 = new Thread(deposit, "depositor");
		Thread t2 = new Thread(withdraw, "withdrawer");

		t1.start();
		t2.start();

	}

}
