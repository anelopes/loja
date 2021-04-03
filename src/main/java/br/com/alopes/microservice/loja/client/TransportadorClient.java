package br.com.alopes.microservice.loja.client;

import br.com.alopes.microservice.loja.dto.InfoEntregaDTO;
import br.com.alopes.microservice.loja.dto.VoucherDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("transportador")
public interface TransportadorClient {

    @PostMapping("/entrega")
    VoucherDTO reservaEntrega(InfoEntregaDTO pedidoDTO);
}
