package br.com.alopes.microservice.store.service;

import br.com.alopes.microservice.store.client.ProviderClient;
import br.com.alopes.microservice.store.client.TransporterClient;
import br.com.alopes.microservice.store.dto.*;
import br.com.alopes.microservice.store.model.Purchase;
import br.com.alopes.microservice.store.model.PurchaseState;
import br.com.alopes.microservice.store.repository.PurchaseRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PurchaseService {

    @Autowired
    private ProviderClient providerClient;

    @Autowired
    private TransporterClient transporterClient;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @HystrixCommand(threadPoolKey = "getByIdThreadPool")
    public Purchase getById(Long id) {
        return purchaseRepository.findById(id).orElse(new Purchase());
    }

    @HystrixCommand(fallbackMethod = "doPurchaseFallback", threadPoolKey = "doPurchaseThreadPool")
    public Purchase doPurchase(PurchaseDTO purchaseDto) {
        Purchase purchase = new Purchase();
        purchase.setDestinationAddress(purchaseDto.getAddress().toString());
        purchase.setState(PurchaseState.RECEIVED);
        purchaseRepository.save(purchase);
        purchaseDto.setPurchaseId(purchase.getId());

        ProviderInfoDTO provider = providerClient.getInfoByState(purchaseDto.getAddress().getState());
        OrderDTO order = providerClient.doOrder(purchaseDto.getItems());
        purchase.setOrderId(order.getId());
        purchase.setPreparationTime(order.getPreparationTime());
        purchase.setState(PurchaseState.ORDER_COMPLETED);
        purchaseRepository.save(purchase);

        DeliveryDTO delivery = new DeliveryDTO();
        delivery.setOrderId(order.getId());
        delivery.setDeliveryDate(LocalDate.now().plusDays(order.getPreparationTime()));
        delivery.setSourceAddress(provider.getAddress());
        delivery.setDestinationAddress(purchaseDto.getAddress().toString());

        VoucherDTO voucher = transporterClient.bookDelivery(delivery);
        purchase.setDeliveryDate(voucher.getDeliveryScheduled());
        purchase.setVoucherId(voucher.getNumber());
        purchase.setState(PurchaseState.RESERVATION_COMPLETED);
        purchaseRepository.save(purchase);

        return purchase;
    }

    public Purchase doPurchaseFallback(PurchaseDTO purchase) {
        if (purchase.getPurchaseId() != null) {
            return purchaseRepository.findById(purchase.getPurchaseId()).get();
        }

        Purchase purchaseFallback = new Purchase();
        purchaseFallback.setDestinationAddress(purchase.getAddress().toString());
        return purchaseFallback;
    }
}
