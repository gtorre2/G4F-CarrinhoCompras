package br.com.g4f.carrinho.resource;

import br.com.g4f.carrinho.entity.ProductCategory;
import br.com.g4f.carrinho.entity.ProductInfo;
import br.com.g4f.carrinho.service.CategoriaService;
import br.com.g4f.carrinho.service.ProdutoService;
import br.com.g4f.carrinho.vo.response.CategoryPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class CategoriaResource {
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ProdutoService produtoService;


    @GetMapping("/category/{type}")
    public CategoryPage showOne(@PathVariable("type") Integer categoryType,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "3") Integer size) {

        ProductCategory cat = categoriaService.findByCategoryType(categoryType);
        PageRequest request = PageRequest.of(page - 1, size);
        Page<ProductInfo> productInCategory = produtoService.findAllInCategory(categoryType, request);
        var tmp = new CategoryPage("", productInCategory);
        tmp.setCategory(cat.getCategoryName());
        return tmp;
    }
}
