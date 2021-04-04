package br.com.alopes.microservice.store.controller;

import br.com.alopes.microservice.store.dto.PurchaseDTO;
import br.com.alopes.microservice.store.model.Purchase;
import br.com.alopes.microservice.store.service.PurchaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/purchase")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/{id}")
    public Purchase getById(@PathVariable("id") Long id) {
        return purchaseService.getById(id);
    }

    @PostMapping
    public Purchase doPurchase(@RequestBody PurchaseDTO purchase) {
        return purchaseService.doPurchase(purchase);
    }
}
