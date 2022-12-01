package br.com.g4f.carrinho.service;

import br.com.g4f.carrinho.entity.ProductCategory;

import java.util.List;

public interface CategoriaService {

    List<ProductCategory> findAll();

    ProductCategory findByCategoryType(Integer categoryType);

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);

}
