package br.com.g4f.carrinho.repository;

import br.com.g4f.carrinho.entity.ProductInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InformacaoProdutoRepository extends JpaRepository<ProductInfo, String> {
    ProductInfo findByProductId(String id);
    Page<ProductInfo> findAllByProductStatusOrderByProductIdAsc(Integer productStatus, Pageable pageable);

    Page<ProductInfo> findAllByCategoryTypeOrderByProductIdAsc(Integer categoryType, Pageable pageable);

    Page<ProductInfo> findAllByOrderByProductId(Pageable pageable);

}
