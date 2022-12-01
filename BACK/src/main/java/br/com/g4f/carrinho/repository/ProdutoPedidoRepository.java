package br.com.g4f.carrinho.repository;

import br.com.g4f.carrinho.entity.ProductInOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoPedidoRepository extends JpaRepository<ProductInOrder, Long> {

}
