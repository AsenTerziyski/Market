package com.test.market.init;

import com.test.market.model.ContractEntity;
import com.test.market.model.ItemEntity;
import com.test.market.model.UserEntity;
import com.test.market.model.projections.UserEntityView;
import com.test.market.repository.ContractRepository;
import com.test.market.services.ContractService;
import com.test.market.services.ItemService;
import com.test.market.services.UserEntityService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Component
public class Init implements CommandLineRunner {
    private final UserEntityService userEntityService;
    private final ItemService itemService;
    private final ContractService contractService;

    private final ContractRepository contractRepository;

    public Init(UserEntityService userEntityService, ItemService itemService, ContractService contractService, ContractRepository contractRepository) {
        this.userEntityService = userEntityService;
        this.itemService = itemService;
        this.contractService = contractService;
        this.contractRepository = contractRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        // USERS
        //create user
        UserEntity user1 = new UserEntity();
        user1.setUsername("User1").setAccount(100);

        UserEntity user2 = new UserEntity();
        user2.setUsername("User2").setAccount(200);

        UserEntity user3 = new UserEntity();
        user3.setUsername("User3").setAccount(300);

        UserEntity user4 = new UserEntity();
        user4.setUsername("User4").setAccount(400);

        UserEntity userEntity1 = userEntityService.registerUser(user1);
        UserEntity userEntity2 = userEntityService.registerUser(user2);
        UserEntity userEntity3 = userEntityService.registerUser(user3);
        UserEntity userEntity4 = userEntityService.registerUser(user4);

        //delete user
//        userEntityService.removeUser(userEntity3.getId());

        //ITEMS
        // add items
        ItemEntity item1 = itemService.addItemToMarket("item1", user1.getUsername());
        ItemEntity item2 = itemService.addItemToMarket("item2", user1.getUsername());
        ItemEntity item3 = itemService.addItemToMarket("item3", user1.getUsername());
        ItemEntity item4 = itemService.addItemToMarket("item4", user4.getUsername());
        userEntityService.removeUser(userEntity4.getId());

        //remove items
//        boolean deleted = itemService.removeItemFromMarket(item1.getId(), item1.getOwner().getUsername());
//        System.out.println(deleted);

//        List<ItemEntity> allItemsByOwnerId = itemService.getAllItemsByOwnerId(userEntity1.getId());
//        for (ItemEntity itemEntity : allItemsByOwnerId) {
//            System.out.println(itemEntity.getName() + " -> " + itemEntity.getOwner().getUsername());
//        }

        //CONTRACTS
        //create contract
        double price = 10;
        contractService.createNewContract(userEntity1.getUsername(),item1.getId(), BigDecimal.valueOf(price));
//        contractService.createNewContract(userEntity1.getUsername(),item2.getId(), BigDecimal.valueOf(price));



        //getAllContracts
//        List<ContractEntity> allContractsFetchBuyerAndSellerEagerly = contractService.getAllContractsFetchBuyerAndSellerEagerly();
//        for (ContractEntity contract : allContractsFetchBuyerAndSellerEagerly) {
//            System.out.println("seller: " + contract.getSeller().getUsername());
//            if (contract.getBuyer() != null) {
//                System.out.println("buyer: " + contract.getBuyer().getUsername());
//            }
//        }

        //updateContractPrice
        String username = userEntity1.getUsername();
        contractService.updateContractPrice(username, 1L, 20);
        contractService.updateContractPrice(username, 1L, 10);

        //disableContract
//        boolean isActive = false;
//        contractService.disableContract(username, 1L, isActive);

        //deleteContract
//        System.out.println();
//        boolean b1 = contractService.removeContractById(1L);
//        boolean b2 = contractService.removeContractById(2L);
//        System.out.println(b1 + " " + b2);

        //create contract
        price = 70;
//        ContractEntity contractEntity11 = contractService.addContract(userEntity1.getUsername(), "item1", price);
//        ContractEntity contractEntity33 = contractService.addContract(userEntity1.getUsername(), "item3", price);
//        System.out.println(contractEntity11.getSeller().getUsername() + "->sells->" + contractEntity11.getItem().getName());
//        System.out.println(contractEntity33.getSeller().getUsername() + "->sells->" + contractEntity33.getItem().getName());

        // buyItem
//        ContractEntity ce1 = contractService.byItem(1, 2);
//        ContractEntity ce2 = contractService.byItem(2, 2);
//        ContractEntity ce3 = contractService.byItem(3, 3);

//        contractService.byItem(2, 3);
//        contractService.byItem(3,1);

//        List<UserEntityView> allRegisteredUsers = userEntityService.getAllRegisteredUsers();
//        for (UserEntityView user : allRegisteredUsers) {
//            System.out.println(user.getUsername());
//        }

    }

}
