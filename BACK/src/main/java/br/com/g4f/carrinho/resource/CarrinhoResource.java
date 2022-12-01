package br.com.g4f.carrinho.resource;

import br.com.g4f.carrinho.entity.Cart;
import br.com.g4f.carrinho.entity.ProductInOrder;
import br.com.g4f.carrinho.entity.User;
import br.com.g4f.carrinho.form.ItemForm;
import br.com.g4f.carrinho.repository.ProdutoPedidoRepository;
import br.com.g4f.carrinho.service.CarrinhoService;
import br.com.g4f.carrinho.service.ProdutoPedidorService;
import br.com.g4f.carrinho.service.ProdutoService;
import br.com.g4f.carrinho.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collection;
import java.util.Collections;

@CrossOrigin
@RestController
@RequestMapping("/carrinho")
public class CarrinhoResource {
    @Autowired
    CarrinhoService carrinhoService;
    @Autowired
    UsuarioService usuarioService;
    @Autowired
    ProdutoService produtoService;
    @Autowired
    ProdutoPedidorService produtoPedidorService;
    @Autowired
    ProdutoPedidoRepository produtoPedidoRepository;

    @PostMapping("")
    public ResponseEntity<Cart> mergeCart(@RequestBody Collection<ProductInOrder> productInOrders, Principal principal) {
        User user = usuarioService.findOne(principal.getName());
        try {
            carrinhoService.mergeLocalCart(productInOrders, user);
        } catch (Exception e) {
            ResponseEntity.badRequest().body("Merge Cart Failed");
        }
        return ResponseEntity.ok(carrinhoService.getCart(user));
    }

    @GetMapping("")
    public Cart getCart(Principal principal) {
        User user = usuarioService.findOne(principal.getName());
        return carrinhoService.getCart(user);
    }

    @PostMapping("/adicionar")
    public boolean addToCart(@RequestBody ItemForm form, Principal principal) {
        var productInfo = produtoService.findOne(form.getProductId());
        try {
            mergeCart(Collections.singleton(new ProductInOrder(productInfo, form.getQuantity())), principal);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @PutMapping("/{itemId}")
    public ProductInOrder modifyItem(@PathVariable("itemId") String itemId, @RequestBody Integer quantity, Principal principal) {
        User user = usuarioService.findOne(principal.getName());
         produtoPedidorService.update(itemId, quantity, user);
        return produtoPedidorService.findOne(itemId, user);
    }

    @DeleteMapping("/{itemId}")
    public void deleteItem(@PathVariable("itemId") String itemId, Principal principal) {
        User user = usuarioService.findOne(principal.getName());
         carrinhoService.delete(itemId, user);
         // flush memory into DB
    }


    @PostMapping("/checkout")
    public ResponseEntity checkout(Principal principal) {
        User user = usuarioService.findOne(principal.getName());// Email as username
        carrinhoService.checkout(user);
        return ResponseEntity.ok(null);
    }


}
