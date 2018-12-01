package unsl.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.math.BigDecimal;


@Entity
@Table(name = "balance", uniqueConstraints={@UniqueConstraint(columnNames ={"userId"})})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Balance {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @JsonProperty("user_id")
    private Long userId;
    private BigDecimal amount;
    private String currency;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency.toUpperCase();
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

}
