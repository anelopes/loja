package br.com.alopes.microservice.store.repository;

import br.com.alopes.microservice.store.model.Compra;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends CrudRepository<Compra, Long> {
}
