package br.com.alopes.microservice.store.client;

import br.com.alopes.microservice.store.dto.OrderDTO;
import br.com.alopes.microservice.store.dto.OrderItemDTO;
import br.com.alopes.microservice.store.dto.ProviderInfoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@FeignClient("provider")
public interface ProviderClient {

    @GetMapping("/info/{state}")
    ProviderInfoDTO getInfoByState(@PathVariable String state);

    @PostMapping("/order")
    OrderDTO doOrder(List<OrderItemDTO> items);
}
