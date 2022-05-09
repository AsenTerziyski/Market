package com.test.market.services.impl;

import com.test.market.model.ContractEntity;
import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;
import com.test.market.model.projections.ContractView;
import com.test.market.model.projections.ItemView;
import com.test.market.model.projections.UserEntityView;
import com.test.market.repository.ContractRepository;
import com.test.market.repository.ItemRepository;
import com.test.market.repository.UserRepository;
import com.test.market.services.ContractService;
import com.test.market.services.ItemService;
import com.test.market.services.UserEntityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ContractServiceImpl implements ContractService {
    private final ContractRepository contractRepository;
    private final UserRepository userRepository;

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final UserEntityService userEntityService;

    public ContractServiceImpl(ContractRepository contractRepository, UserRepository userRepository, ItemService itemService, ItemRepository itemRepository, UserEntityService userEntityService) {
        this.contractRepository = contractRepository;
        this.userRepository = userRepository;
        this.itemService = itemService;
        this.itemRepository = itemRepository;
        this.userEntityService = userEntityService;
    }

    public ContractEntity updateContractPrice(String username, long contractId, double price) throws Exception {
        Optional<ContractEntity> contract = contractRepository.getContractByIdFetchSellerEagerly(contractId);
        if (contract.isEmpty()) {
            throw new Exception("The contract does not exist.");
        }

        if (!contract.get().getSeller().getUsername().equals(username)) {
            throw new Exception("User is not the seller. Not allowed to update the contract.");
        }

        contract.get().setPrice(BigDecimal.valueOf(price));
        return contractRepository.save(contract.get());
    }

    @Transactional
    @Override
    public boolean removeContractById(Long contractId) throws Exception {
        if (contractId == null) {
            throw new Exception("The contract does not exist.");
        }

        Optional<ContractEntity> contractById = contractRepository.getContractById(contractId);
        if (contractById.isEmpty()) {
            throw new Exception("The contract does not exist.");
        }

        ItemEntity item = itemRepository.getItemByContractId(contractId);
        item.setContractEntity(null);
        contractById.get().setItem(null);
        contractRepository.save(contractById.get());
        itemRepository.save(item);
        contractRepository.delete(contractById.get());
        return true;
    }

    @Override
    public List<ContractView> getAllContractsFetchBuyerAndSeller() {
        return contractRepository.getAllActiveContractsView(true);
    }

    @Override
    public void createNewContract(String username, Long itemID, BigDecimal price) throws Exception {
        ItemEntity item = itemService.getItemByIdFetchOwnerEagerly(itemID);
        Optional<ContractEntity> contractByItemId = contractRepository.findContractByItemId(itemID);
        if (contractByItemId.isPresent()) {
            throw new Exception("Contract with this item exists.");
        }

        if (!username.equals(item.getOwner().getUsername())) {
            throw new Exception("User is not item owner. Can not create contract with this item.");
        }

        ContractEntity contract = new ContractEntity()
                .setActive(true)
                .setPrice(price)
                .setSeller(item.getOwner());
        contractRepository.save(contract);
        item.setContractEntity(contract);
        ItemEntity savedItem = itemRepository.save(item);
        contract.setItem(savedItem);
        contractRepository.save(contract);
    }

    @Override
    public ContractEntity updateContract(String username, Long itemID, BigDecimal price) throws Exception {
        Optional<ContractEntity> contractByItemId = contractRepository.findContractByItemId(itemID);
        if (contractByItemId.isEmpty()) {
            throw new Exception(String.format("Contract with item with id: %d does not exist.", itemID));
        }

        contractByItemId.get().setPrice(price);
        return contractRepository.save(contractByItemId.get());
    }

    @Override
    public List<ContractView> getAllContractsBySellerFetchSellerEagerly(String username) throws Exception {
        List<ContractView> allContractsBySeller = contractRepository.getAllContractsBySellerUsername(username);
        if (allContractsBySeller.isEmpty()) {
            throw new Exception("User does not own contracts.");
        }

        return allContractsBySeller;
    }

    @Transactional
    @Override
    public boolean removeContractByItemIdAndSellerUsername(Long itemID, String username) throws Exception {
        System.out.println();
        String ownerUsername = itemService.getItemByIdFetchOwnerEagerly(itemID).getOwner().getUsername();
        String userUsername = userEntityService.getUserByUsername(username).getUsername();
        if (!ownerUsername.equals(userUsername)) {
            throw new Exception(String.format("User with username: %s is not allowed to remove contract with item with id: %d",
                    userUsername, itemID));
        }

        Optional<ContractEntity> contractByItemId = contractRepository.findContractByItemId(itemID);
        if (contractByItemId.isPresent()) {
            contractRepository.removeContractById(contractByItemId.get().getId());
            return true;
        }

        return false;
    }

    @Override
    public ItemView buyItem(String buyerUsername, Long itemId) throws Exception {
        ItemEntity item = itemService.getItemByIdFetchOwnerEagerly(itemId);

        UserEntity buyer = userEntityService.getUserByUsername(buyerUsername);
        UserEntity seller = userEntityService.getUserByIdFetchItemsEagerly(item.getOwner().getId());

        if (buyer.getUsername().equals(seller.getUsername())) {
            throw new Exception("Buyer is the owner ot this item.");
        }

        Optional<ContractEntity> contract = contractRepository.findContractByItemId(itemId);
        if (contract.isEmpty()) {
            throw new Exception("The contract does not exist.");
        }

        if (!contract.get().getActive()) {
            throw new Exception("Contract is not active.");
        }

        double account = buyer.getAccount();
        double price = contract.get().getPrice().doubleValue();

        if (price > account) {
            throw new Exception("Not enough money in your account.");
        }

        List<ItemEntity> items = seller.getItems();
        for (ItemEntity anItem : items) {
            if (anItem.getId() == item.getId()) {
                items.remove(anItem);
                buyer.getItems().add(anItem);
                break;
            }
        }

        UserEntity updatedBuyer = buyer.setAccount(account - price);
        seller.setAccount(seller.getAccount() + price);
        UserEntity savedBuyer = userRepository.save(updatedBuyer);
        userRepository.save(seller);

        item.setOwner(savedBuyer);
        ItemEntity savedItem = itemRepository.save(item);

        contract.get().setActive(false).setBuyer(savedBuyer);
        contractRepository.save(contract.get());

        return getItemView(buyerUsername, savedItem);
    }

    static ItemView getItemView(String buyerUsername, ItemEntity savedItem) {
        return new ItemView() {
            @Override
            public String getName() {
                return savedItem.getName();
            }

            @Override
            public UserEntityView getOwner() {
                return () -> buyerUsername;
            }
        };
    }

    @Override
    public List<ContractView> getAllContractsBySellerId(Long sellerId) throws Exception {
        userEntityService.getUserById(sellerId);

        List<ContractView> contractsBySellerId = contractRepository.getContractBySellerId(sellerId);
        if (contractsBySellerId.isEmpty()) {
            throw new Exception("User does not owns contracts.");
        }
        return contractsBySellerId;
    }

}
