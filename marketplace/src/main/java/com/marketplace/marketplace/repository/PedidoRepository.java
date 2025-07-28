package com.marketplace.marketplace.repository;

import com.marketplace.marketplace.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}
