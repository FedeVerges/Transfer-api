package unsl.Entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "accounts", uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class Account {
    public static enum Currency {
        PESO_AR,
        DOLAR,
        EURO
    }

    public static enum Status {
        ACTIVA,
        BAJA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double account_balance;

    @JsonProperty("titular")
    private Long holder;

    @Enumerated(EnumType.STRING)
    @JsonProperty("tipo_moneda")
    private Currency currency;

    @Enumerated(EnumType.STRING)
    private Status status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(double account_balance) {
        this.account_balance = account_balance;
    }

    public Long getHolder() {
        return holder;
    }

    public void setHolder(Long holder) {
        this.holder = holder;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
