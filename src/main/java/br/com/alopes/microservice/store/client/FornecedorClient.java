package br.com.alopes.microservice.store.client;

import java.util.List;

import br.com.alopes.microservice.store.dto.InfoPedidoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.alopes.microservice.store.dto.InfoFornecedorDTO;
import br.com.alopes.microservice.store.dto.ItemDaCompraDTO;

@FeignClient("provider")
public interface FornecedorClient {

	@RequestMapping("/info/{estado}")
	InfoFornecedorDTO getInfoPorEstado(@PathVariable String estado);

	@RequestMapping(method=RequestMethod.POST, value="/pedido")
    InfoPedidoDTO realizaPedido(List<ItemDaCompraDTO> itens);
}
