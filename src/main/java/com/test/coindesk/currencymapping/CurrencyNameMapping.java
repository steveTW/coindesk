package com.test.coindesk.currencymapping;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

@Data
@Accessors(chain = true)
@Entity
@NoArgsConstructor
public class CurrencyNameMapping {
    @Id
    private String currency;
    private String name;

    public CurrencyNameMapping(String currency, String name) {
        this.currency = currency;
        this.name = name;
    }
}
