package com.test.market.services;

import com.test.market.model.ContractEntity;
import com.test.market.model.projections.ContractView;
import com.test.market.model.projections.ItemView;

import java.math.BigDecimal;
import java.util.List;

public interface ContractService {

    ContractEntity updateContractPrice(String username, long contractId, double price) throws Exception;

    boolean removeContractById(Long contractId) throws Exception;

    List<ContractView> getAllContractsFetchBuyerAndSeller();

    void createNewContract(String username, Long itemID, BigDecimal price) throws Exception;

    ContractEntity updateContract(String username, Long itemID, BigDecimal price) throws Exception;

    List<ContractView> getAllContractsBySellerFetchSellerEagerly(String username) throws Exception;

    boolean removeContractByItemIdAndSellerUsername(Long itemID, String username) throws Exception;

    ItemView buyItem(String username, Long itemId) throws Exception;

    List<ContractView> getAllContractsBySellerId(Long sellerId) throws Exception;
}
