package ru.otus;

import ru.otus.domain.Banknote;

import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) {
        Logger logger = Logger.getLogger(Main.class.getName());
        final Atm atm = atmFactory.initAtm();

        System.out.println("balance: " + atm.calculateBalance());
        System.out.println("balance: " + atm.putOnBalance(Banknote.FIVE_THOUSAND));
        System.out.println("balance: " + atm.debitFromBalance(4500));
        System.out.println("balance: " + atm.calculateBalance());
    }
}