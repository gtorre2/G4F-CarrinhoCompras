import {Component, OnDestroy, OnInit} from '@angular/core';
import {UsuarioService} from "../../services/usuario.service";
import {ProdutoService} from "../../services/produto.service";
import {JwtResponse} from "../../response/JwtResponse";
import {Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {StatusProduto} from "../../enum/StatusProduto";
import {ProductInfo} from "../../models/informacaoProduto";
import {Role} from "../../enum/Role";
import { TipoCategoria } from 'src/app/enum/CategoryType';

@Component({
    selector: 'app-produtos',
    templateUrl: './produtos.component.html',
    styleUrls: ['./produtos.component.css']
})
export class ProdutosComponent implements OnInit, OnDestroy {

    constructor(private UsuarioService: UsuarioService,
                private produtoService: ProdutoService,
                private route: ActivatedRoute) {
    }

    Role = Role;
    currentUser: JwtResponse;
    page: any;
    TipoCategoria = TipoCategoria;
    StatusProduto = StatusProduto;
    private querySub: Subscription;

    ngOnInit() {
        this.querySub = this.route.queryParams.subscribe(() => {
            this.atualizar();
        });
    }

    ngOnDestroy(): void {
        this.querySub.unsubscribe();
    }

    atualizar() {
        if (this.route.snapshot.queryParamMap.get('page')) {
            const currentPage = +this.route.snapshot.queryParamMap.get('page');
            const size = +this.route.snapshot.queryParamMap.get('size');
            this.getProduto(currentPage, size);
        } else {
            this.getProduto();
        }
    }

    getProduto(page: number = 1, size: number = 5) {
        this.produtoService.getAllInPage(+page, +size)
            .subscribe(page => {
                this.page = page;
            });

    }

    remover(produtos: ProductInfo[], productInfo) {
        this.produtoService.delelte(productInfo).subscribe(_ => {
            produtos = produtos.filter(e => e.productId != productInfo);
            },
            err => {
            });
    }


}
