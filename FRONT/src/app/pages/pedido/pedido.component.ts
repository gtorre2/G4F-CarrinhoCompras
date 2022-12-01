import {Component, OnDestroy, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {PedidoService} from "../../services/pedido.service";
import {Pedido} from "../../models/pedido";
import {StatusPedido} from "../../enum/StatusPedido";
import {UsuarioService} from "../../services/usuario.service";
import {JwtResponse} from "../../response/JwtResponse";
import {Subscription} from "rxjs";
import {ActivatedRoute} from "@angular/router";
import {Role} from "../../enum/Role";

@Component({
    selector: 'app-pedido',
    templateUrl: './pedido.component.html',
    styleUrls: ['./pedido.component.css']
})
export class PedidoComponent implements OnInit, OnDestroy {

    page: any;
    StatusPedido = StatusPedido;
    currentUser: JwtResponse;
    Role = Role;
    constructor(private httpClient: HttpClient,
                private pedidoService: PedidoService,
                private UsuarioService: UsuarioService,
                private route: ActivatedRoute
    ) {
    }

    querySub: Subscription;

    ngOnInit() {
        this.currentUser = this.UsuarioService.currentUserValue;
        this.querySub = this.route.queryParams.subscribe(() => {
            this.update();
        });

    }

    update() {
        let nextPage = 1;
        let size = 10;
        if (this.route.snapshot.queryParamMap.get('page')) {
            nextPage = +this.route.snapshot.queryParamMap.get('page');
            size = +this.route.snapshot.queryParamMap.get('size');
        }
        this.pedidoService.getPage(nextPage, size).subscribe(page => this.page = page, _ => {
            console.log("Falha")
        });
    }

    cancel(order: Pedido) {
        this.pedidoService.cancel(order.orderId).subscribe(res => {
            if (res) {
                order.orderStatus = res.orderStatus;
            }
        });
    }

    finish(order: Pedido) {
        this.pedidoService.finalizar(order.orderId).subscribe(res => {
            if (res) {
                order.orderStatus = res.orderStatus;
            }
        })
    }

    ngOnDestroy(): void {
        this.querySub.unsubscribe();
    }

}
