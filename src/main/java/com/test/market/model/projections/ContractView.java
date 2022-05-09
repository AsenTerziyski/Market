package com.test.market.model.projections;

import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;

import java.math.BigDecimal;

public interface ContractView {

    UserEntityView getSeller();
    UserEntityView getBuyer();
    ItemView getItem();
    BigDecimal getPrice();

}
