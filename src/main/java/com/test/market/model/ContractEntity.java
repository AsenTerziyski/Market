package com.test.market.model;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.modelmapper.internal.bytebuddy.implementation.bind.MethodDelegationBinder;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class ContractEntity extends BaseEntity {

    @ManyToOne
    UserEntity seller;
    @ManyToOne
    UserEntity buyer;
    @OneToOne(mappedBy = "contractEntity", fetch = FetchType.LAZY)
    ItemEntity item;

    private BigDecimal price;
    private Boolean active;

    public UserEntity getSeller() {
        return seller;
    }

    public ContractEntity setSeller(UserEntity seller) {
        this.seller = seller;
        return this;
    }

    public UserEntity getBuyer() {
        return buyer;
    }

    public ContractEntity setBuyer(UserEntity buyer) {
        this.buyer = buyer;
        return this;
    }

    public ItemEntity getItem() {
        return item;
    }

    public ContractEntity setItem(ItemEntity item) {
        this.item = item;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public ContractEntity setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Boolean getActive() {
        return active;
    }

    public ContractEntity setActive(Boolean active) {
        this.active = active;
        return this;
    }
}
