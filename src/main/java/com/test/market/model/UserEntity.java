package com.test.market.model;


import org.hibernate.annotations.*;
import org.springframework.data.jpa.repository.EntityGraph;

import javax.persistence.*;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UserEntity extends BaseEntity {

    private String username;
    private double account;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    //https://stackoverflow.com/questions/38562338/cascade-remove-with-spring-data-jpa-bi-directional
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ItemEntity> items;

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<ContractEntity> contracts;

    public UserEntity() {
    }

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public double getAccount() {
        return account;
    }

    public UserEntity setAccount(double account) {
        this.account = account;
        return this;
    }

    public List<ItemEntity> getItems() {
        return items;
    }

    public UserEntity setItems(List<ItemEntity> items) {
        this.items = items;
        return this;
    }

    public List<ContractEntity> getContracts() {
        return contracts;
    }

    public UserEntity setContracts(List<ContractEntity> contracts) {
        this.contracts = contracts;
        return this;
    }
}
