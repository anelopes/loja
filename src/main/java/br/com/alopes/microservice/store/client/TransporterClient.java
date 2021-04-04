package br.com.alopes.microservice.store.client;

import br.com.alopes.microservice.store.dto.DeliveryDTO;
import br.com.alopes.microservice.store.dto.VoucherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("transporter")
public interface TransporterClient {

    @PostMapping("/delivery")
    VoucherDTO bookDelivery(DeliveryDTO delivery);
}
