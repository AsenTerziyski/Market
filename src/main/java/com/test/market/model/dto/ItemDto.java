package com.test.market.model.dto;

public class ItemDto {
    private String name;
    private String username;

    public ItemDto() {
    }

    public String getName() {
        return name;
    }

    public ItemDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public ItemDto setUsername(String username) {
        this.username = username;
        return this;
    }
}
