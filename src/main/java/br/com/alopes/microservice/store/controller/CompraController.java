package br.com.alopes.microservice.store.controller;

import br.com.alopes.microservice.store.model.Compra;
import br.com.alopes.microservice.store.service.CompraService;
import br.com.alopes.microservice.store.dto.CompraDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/compra")
public class CompraController {

    @Autowired
    private CompraService compraService;

    @GetMapping("/{id}")
    public Compra buscaCompra(@PathVariable("id") Long id) {
        return compraService.buscaCompra(id);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Compra realizaCompra(@RequestBody CompraDTO compra) {
        return compraService.realizaCompra(compra);
    }
}
