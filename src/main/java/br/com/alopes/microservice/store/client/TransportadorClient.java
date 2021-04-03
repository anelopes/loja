package br.com.alopes.microservice.store.client;

import br.com.alopes.microservice.store.dto.InfoEntregaDTO;
import br.com.alopes.microservice.store.dto.VoucherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("transporter")
public interface TransportadorClient {

    @PostMapping("/entrega")
    VoucherDTO reservaEntrega(InfoEntregaDTO pedidoDTO);
}
