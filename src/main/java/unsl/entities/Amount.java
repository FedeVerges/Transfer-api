package unsl.entities;

import java.math.BigDecimal;

public class Amount {

    BigDecimal amount;

    public Amount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
