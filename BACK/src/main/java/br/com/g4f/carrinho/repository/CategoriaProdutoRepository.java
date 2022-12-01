package br.com.g4f.carrinho.repository;

import br.com.g4f.carrinho.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriaProdutoRepository extends JpaRepository<ProductCategory, Integer> {
    List<ProductCategory> findByCategoryTypeInOrderByCategoryTypeAsc(List<Integer> categoryTypes);
    List<ProductCategory> findAllByOrderByCategoryType();
    ProductCategory findByCategoryType(Integer categoryType);
}
