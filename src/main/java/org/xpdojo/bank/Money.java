package org.xpdojo.bank;

import java.text.DecimalFormat;
import java.util.Objects;

/**
 * Immutable class to represent Money as a concept.
 * This class should have no accessor methods.
 */
public class Money {

    private long amount;

    private Money(long amount) {
        this.amount = amount;
    }

    public static final Money ZERO = amountOf(0);

    public static Money amountOf(long amount) {
        return new Money(amount);
    }

    public Money plus(Money other) {
        return new Money(this.amount + other.amount);
    }

    public Money minus(Money other) {
        return new Money(this.amount - other.amount);
    }

    public boolean isLessThan(Money other) {
        return this.amount < other.amount;
    }
    
    @Override
    public String toString() {
        return new DecimalFormat("#,###.00").format(amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
