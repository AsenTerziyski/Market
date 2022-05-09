package com.test.market.web;

import com.test.market.model.dto.ContractDto;
import com.test.market.model.projections.ContractView;
import com.test.market.services.ContractService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market/contracts")
public class ContractController {

    private final ContractService contractService;

    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ContractView> getAllActiveContracts() {
        return contractService.getAllContractsFetchBuyerAndSeller();
    }

    @RequestMapping(value = "/seller/{sellerId}", method = RequestMethod.GET)
    public List<ContractView> getAllContractsByOwner(@PathVariable Long sellerId) throws Exception {
        return contractService.getAllContractsBySellerId(sellerId);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public List<ContractView> createContract(@RequestBody ContractDto contractDto) throws Exception {
        contractService.createNewContract(contractDto.getUsername(), contractDto.getItemID(), contractDto.getPrice());
        return contractService.getAllContractsFetchBuyerAndSeller();
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH)
    public List<ContractView> updateContract(@RequestBody ContractDto contractDto) throws Exception {
        contractService.updateContract(contractDto.getUsername(), contractDto.getItemID(), contractDto.getPrice());
        return contractService.getAllContractsBySellerFetchSellerEagerly(contractDto.getUsername());
    }

    @RequestMapping(value = "/delete/{contractID}", method = RequestMethod.DELETE)
    public List<ContractView> deleteContract(@PathVariable(value = "contractID") Long contractID,
                                             @RequestBody ContractDto contractDto) throws Exception {
        contractService.removeContractById(contractID);
        return contractService.getAllContractsBySellerFetchSellerEagerly(contractDto.getUsername());
    }

}
