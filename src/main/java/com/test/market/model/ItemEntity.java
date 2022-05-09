package com.test.market.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
public class ItemEntity extends BaseEntity {

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserEntity owner;

    @OneToOne
    private ContractEntity contractEntity;

    public String getName() {
        return name;
    }

    public ItemEntity setName(String name) {
        this.name = name;
        return this;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public ItemEntity setOwner(UserEntity owner) {
        this.owner = owner;
        return this;
    }

    public ContractEntity getContractEntity() {
        return contractEntity;
    }

    public ItemEntity setContractEntity(ContractEntity contractEntity) {
        this.contractEntity = contractEntity;
        return this;
    }
}
