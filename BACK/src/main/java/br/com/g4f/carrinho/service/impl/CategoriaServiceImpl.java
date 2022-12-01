package br.com.g4f.carrinho.service.impl;

import br.com.g4f.carrinho.entity.ProductCategory;
import br.com.g4f.carrinho.enums.ResultEnum;
import br.com.g4f.carrinho.exception.ValidacaoCustomizada;
import br.com.g4f.carrinho.repository.CategoriaProdutoRepository;
import br.com.g4f.carrinho.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoriaServiceImpl implements CategoriaService {
    @Autowired
    CategoriaProdutoRepository categoriaProdutoRepository;

    @Override
    public List<ProductCategory> findAll() {
        List<ProductCategory> res = categoriaProdutoRepository.findAllByOrderByCategoryType();
       return res;
    }

    @Override
    public ProductCategory findByCategoryType(Integer categoryType) {
        ProductCategory res = categoriaProdutoRepository.findByCategoryType(categoryType);
        if(res == null) throw new ValidacaoCustomizada(ResultEnum.CATEGORY_NOT_FOUND);
        return res;
    }

    @Override
    public List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList) {
        List<ProductCategory> res = categoriaProdutoRepository.findByCategoryTypeInOrderByCategoryTypeAsc(categoryTypeList);
        return res;
    }

    @Override
    @Transactional
    public ProductCategory save(ProductCategory productCategory) {
        return categoriaProdutoRepository.save(productCategory);
    }

}
