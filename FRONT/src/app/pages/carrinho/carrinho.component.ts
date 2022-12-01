import {AfterContentChecked, Component, OnDestroy, OnInit} from '@angular/core';

import {Subject, Subscription} from 'rxjs';
import {UsuarioService} from '../../services/usuario.service';
import {JwtResponse} from '../../response/JwtResponse';

import {debounceTime, switchMap} from 'rxjs/operators';
import {Router} from '@angular/router';
import {Role} from '../../enum/Role';
import { CarrinhoService } from 'src/app/services/carrinho.service';
import { ProdutoCompra } from 'src/app/models/produtoCompra';

@Component({
    selector: 'app-carrinho',
    templateUrl: './carrinho.component.html',
    styleUrls: ['./carrinho.component.css']
})
export class CarrinhoComponent implements OnInit, OnDestroy, AfterContentChecked {

    constructor(private carrinhoService: CarrinhoService,
                private usuarioService: UsuarioService,
                private router: Router) {
        this.usuarioSistema = this.usuarioService.currentUser.subscribe(user => this.currentUser = user);
    }

    produtos = [];
    total = 0;
    currentUser: JwtResponse;
    usuarioSistema: Subscription;

    private updateTerms = new Subject<ProdutoCompra>();
    sub: Subscription;

    static validarContador(produto) {
        const max = produto.productStock;
        if (produto.count > max) {
            produto.count = max;
        } else if (produto.count < 1) {
            produto.count = 1;
        }
        console.log(produto.count);
    }

    ngOnInit() {
        this.carrinhoService.getCart().subscribe(produtos => {
            this.produtos = produtos;
        });

        this.sub = this.updateTerms.pipe(
            debounceTime(300),
            switchMap((ProdutoCompra: ProdutoCompra) => this.carrinhoService.atualizar(ProdutoCompra))
        ).subscribe(prod => {
                if (prod) { throw new Error(); }
            },
            _ => console.log('Falha ao atualizar o produto'));
    }

    ngOnDestroy() {
        if (!this.currentUser) {
            this.carrinhoService.storeLocalCart();
        }
        this.usuarioSistema.unsubscribe();
    }

    ngAfterContentChecked() {
        this.total = this.produtos.reduce(
            (prev, cur) => prev + cur.count * cur.productPrice, 0);
    }

    incrmenetarProduto(produto) {
        produto.count++;
        CarrinhoComponent.validarContador(produto);
        if (this.currentUser) { this.updateTerms.next(produto); }
    }

    decrementarProduto(produto) {
        produto.count--;
        CarrinhoComponent.validarContador(produto);
        if (this.currentUser) { this.updateTerms.next(produto); }
    }

    onChange(ProdutoCompra) {
        CarrinhoComponent.validarContador(ProdutoCompra);
        if (this.currentUser) { this.updateTerms.next(ProdutoCompra); }
    }

    remover(produto: ProdutoCompra) {
        this.carrinhoService.remover(produto).subscribe(
            success => {
               this.produtos = this.produtos.filter(e => e.productId !== produto.productId);
                console.log('Cart: ' + this.produtos);
            },
            _ => console.log('Falha ao remover o produto do carrinho'));
    }

    pagamento() {
        if (!this.currentUser) {
            this.router.navigate(['/login'], {queryParams: {returnUrl: this.router.url}});
        } else if (this.currentUser.role !== Role.Customer) {
            this.router.navigate(['/seller']);
        } else {
            this.carrinhoService.pagamento().subscribe(
                _ => {
                    this.produtos = [];
                },
                error1 => {
                    console.log('Pagamento falhou');
                });
            this.router.navigate(['/']);
        }

    }
}