package com.test.market.model.dto;

import java.math.BigDecimal;

public class ContractDto {
    private String username;
    private Long itemID;
    private BigDecimal price;

    public ContractDto() {
    }

    public String getUsername() {
        return username;
    }

    public ContractDto setUsername(String username) {
        this.username = username;
        return this;
    }

    public Long getItemID() {
        return itemID;
    }

    public ContractDto setItemID(Long itemID) {
        this.itemID = itemID;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ContractDto setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }
}
