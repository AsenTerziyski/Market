package com.test.market.repository;

import com.test.market.model.ItemEntity;
import com.test.market.model.projections.ItemView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long> {

    @Query("select i from ItemEntity i where i.owner.id = :userId")
    List<ItemEntity> getALlItems(Long userId);

    @Query("select i from ItemEntity i where i.name = :itemName")
    Optional<ItemEntity> findExistingItemByItemName(String itemName);

    @Query("select i from ItemEntity i LEFT join fetch i.owner owner where i.owner.username =:username")
    List<ItemEntity> getAllItemsByOwnerUsernameEagerly(String username);

    @Query("select i from ItemEntity i LEFT join fetch i.owner owner where i.owner.id =:id")
    List<ItemEntity> getAllItemsByOwnerIdEagerly(Long id);

    @Query("select i from ItemEntity i left join fetch i.owner owner where i.id =:id")
    ItemEntity getItemByIdFetchOwnerEagerly(Long id);

    @Query("select i from ItemEntity i left join fetch i.owner owner where i.id =:id")
    ItemEntity getItemById(Long id);

    @Query("select i from ItemEntity i left join fetch i.owner owner where i.name =:itemName")
    ItemEntity getExistingItemByNameFetchOwnerEagerly(String itemName);

    @Modifying
    @Query("delete from ItemEntity i where i.id = :id")
    void removeItemById(@Param("id") Long id);

    @Query("select i from ItemEntity i")
    List<ItemView> getAllItems();

    @Query("select i from ItemEntity i left join fetch i.owner owner where owner.id =:userId")
    List<ItemView> getItemViewByUserId(Long userId);

    @Query("select i from ItemEntity i left join fetch i.contractEntity contract where i.id =:itemId")
    ItemEntity getContractIdByItemId(@Param("itemId") Long itemId);

    @Query("select i from ItemEntity i left join fetch i.contractEntity contract where contract.id = :contractId")
    ItemEntity getItemByContractId(@Param("contractId") Long contractId);
}
