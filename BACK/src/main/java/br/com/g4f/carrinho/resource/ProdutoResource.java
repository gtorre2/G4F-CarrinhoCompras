package br.com.g4f.carrinho.resource;

import br.com.g4f.carrinho.entity.ProductInfo;
import br.com.g4f.carrinho.service.CategoriaService;
import br.com.g4f.carrinho.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
public class ProdutoResource {
    @Autowired
    CategoriaService categoriaService;
    @Autowired
    ProdutoService produtoService;

    @GetMapping("/product")
    public Page<ProductInfo> findAll(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                     @RequestParam(value = "size", defaultValue = "3") Integer size) {
        PageRequest request = PageRequest.of(page - 1, size);
        return produtoService.findAll(request);
    }

    @GetMapping("/product/{productId}")
    public ProductInfo showOne(@PathVariable("productId") String productId) {

        ProductInfo productInfo = produtoService.findOne(productId);

        return productInfo;
    }

    @PostMapping("/seller/product/new")
    public ResponseEntity create(@Valid @RequestBody ProductInfo product,
                                 BindingResult bindingResult) {
        ProductInfo productIdExists = produtoService.findOne(product.getProductId());
        if (productIdExists != null) {
            bindingResult
                    .rejectValue("productId", "error.product",
                            "There is already a product with the code provided");
        }
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }
        return ResponseEntity.ok(produtoService.save(product));
    }

    @PutMapping("/seller/product/{id}/edit")
    public ResponseEntity edit(@PathVariable("id") String productId,
                               @Valid @RequestBody ProductInfo product,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }
        if (!productId.equals(product.getProductId())) {
            return ResponseEntity.badRequest().body("Id Not Matched");
        }

        return ResponseEntity.ok(produtoService.update(product));
    }

    @DeleteMapping("/seller/product/{id}/delete")
    public ResponseEntity delete(@PathVariable("id") String productId) {
        produtoService.delete(productId);
        return ResponseEntity.ok().build();
    }

}
