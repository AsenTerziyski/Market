package com.test.market.repository;

import com.test.market.model.ContractEntity;
import com.test.market.model.projections.ContractView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<ContractEntity, Long> {

    @Query("select c from ContractEntity c left join c.seller seller where c.active = :active")
    List<ContractEntity> getAllActiveContracts(boolean active);

    @Query("select c from ContractEntity c left join c.seller seller left join c.buyer buyer")
    List<ContractEntity> getAllActiveContractsFetchBuyerAndSellerEagerly();

    @Query("select c from ContractEntity c left join c.seller seller where c.id = :contractId")
    Optional<ContractEntity> getContractByIdFetchSellerEagerly(long contractId);

    @Query("select c from ContractEntity c where c.id = :contractId")
    Optional<ContractEntity> getContractById(long contractId);

    @Modifying
    @Query("delete from ContractEntity c where c.id = :id")
    void removeContractById(@Param("id") Long id);

    @Query("select c from ContractEntity c left join fetch c.item item where c.item.id = :id")
    Optional<ContractEntity> findContractByItemId(Long id);

    @Query("select c from ContractEntity c left join c.buyer left join c.seller where c.active =:active")
    List<ContractView> getAllActiveContractsView(boolean active);

    @Query("select c from ContractEntity c left join c.buyer buyer left join c.seller seller where c.seller.username =:sellerUsername")
    List<ContractView> getAllContractsBySellerUsername(String sellerUsername);

    @Query("select c from ContractEntity c left join fetch c.seller s where s.id =:sellerId")
    List<ContractView> getContractBySellerId(@Param("sellerId") Long sellerId);
}
