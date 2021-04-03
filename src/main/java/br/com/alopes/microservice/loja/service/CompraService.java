package br.com.alopes.microservice.loja.service;

import br.com.alopes.microservice.loja.client.FornecedorClient;
import br.com.alopes.microservice.loja.client.TransportadorClient;
import br.com.alopes.microservice.loja.dto.*;
import br.com.alopes.microservice.loja.model.Compra;
import br.com.alopes.microservice.loja.model.CompraState;
import br.com.alopes.microservice.loja.repository.CompraRepository;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CompraService {

    @Autowired
    private FornecedorClient fornecedorClient;

    @Autowired
    private TransportadorClient transportadorClient;

    @Autowired
    private CompraRepository compraRepository;

    @HystrixCommand(threadPoolKey = "getByIdThreadPool")
    public Compra buscaCompra(Long id) {
        return compraRepository.findById(id).orElse(new Compra());
    }

    @HystrixCommand(fallbackMethod = "realizaCompraFallback", threadPoolKey = "realizaCompraThreadPool")
    public Compra realizaCompra(CompraDTO compra) {
        Compra compraSalva = new Compra();
        compraSalva.setEnderecoDestino(compra.getEndereco().toString());
        compraSalva.setState(CompraState.RECEBIDO);
        compraRepository.save(compraSalva);
        compra.setCompraId(compraSalva.getId());

        InfoFornecedorDTO fornecedor = fornecedorClient.getInfoPorEstado(compra.getEndereco().getEstado());
        InfoPedidoDTO pedido = fornecedorClient.realizaPedido(compra.getItens());
        compraSalva.setPedidoId(pedido.getId());
        compraSalva.setTempoDePreparo(pedido.getTempoDePreparo());
        compraSalva.setState(CompraState.PEDIDO_REALIZADO);
        compraRepository.save(compraSalva);

        InfoEntregaDTO entrega = new InfoEntregaDTO();
        entrega.setPedidoId(pedido.getId());
        entrega.setDataParaEntrega(LocalDate.now().plusDays(pedido.getTempoDePreparo()));
        entrega.setEnderecoOrigem(fornecedor.getEndereco());
        entrega.setEnderecoDestino(compra.getEndereco().toString());

        VoucherDTO voucher = transportadorClient.reservaEntrega(entrega);
        compraSalva.setDataParaEntrega(voucher.getPrevisaoParaEntrega());
        compraSalva.setVoucherId(voucher.getNumero());
        compraSalva.setState(CompraState.RESERVA_ENTREGA_REALIZADA);
        compraRepository.save(compraSalva);

        return compraSalva;
    }

    public Compra realizaCompraFallback(CompraDTO compra) {
        if (compra.getCompraId() != null) {
            return compraRepository.findById(compra.getCompraId()).get();
        }

        Compra compraFallback = new Compra();
        compraFallback.setEnderecoDestino(compra.getEndereco().toString());
        return compraFallback;
    }
}
