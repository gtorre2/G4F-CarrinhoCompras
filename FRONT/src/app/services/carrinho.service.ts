import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { apiUrl } from '../../environments/environment';
import { CookieService } from 'ngx-cookie-service';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { catchError, map, tap } from 'rxjs/operators';
import { UsuarioService } from './usuario.service';
import { Carrinho } from '../models/carrinho';
import { Item } from '../models/Item';
import { JwtResponse } from '../response/JwtResponse';
import { ProdutoCompra } from '../models/produtoCompra';

@Injectable({
    providedIn: 'root'
})
export class CarrinhoService {

    private cartUrl = `${apiUrl}/carrinho`;

    localMap = {};

    private itemsSubject: BehaviorSubject<Item[]>;
    private totalSubject: BehaviorSubject<number>;
    public items: Observable<Item[]>;
    public total: Observable<number>;

    private currentUser: JwtResponse;

    constructor(private http: HttpClient,
        private cookieService: CookieService,
        private UsuarioService: UsuarioService) {
        this.itemsSubject = new BehaviorSubject<Item[]>(null);
        this.items = this.itemsSubject.asObservable();
        this.totalSubject = new BehaviorSubject<number>(null);
        this.total = this.totalSubject.asObservable();
        this.UsuarioService.currentUser.subscribe(user => this.currentUser = user);
    }

    private getLocalCart(): ProdutoCompra[] {
        if (this.cookieService.check('cart')) {
            this.localMap = JSON.parse(this.cookieService.get('cart'));
            return Object.values(this.localMap);
        } else {
            this.localMap = {};
            return [];
        }
    }

    getCart(): Observable<ProdutoCompra[]> {
        const localCart = this.getLocalCart();
        if (this.currentUser) {
            if (localCart.length > 0) {
                return this.http.post<Carrinho>(this.cartUrl, localCart).pipe(
                    tap(_ => {
                        this.clearLocalCart();
                    }),
                    map(cart => cart.products),
                    catchError(_ => of([]))
                );
            } else {
                return this.http.get<Carrinho>(this.cartUrl).pipe(
                    map(cart => cart.products),
                    catchError(_ => of([]))
                );
            }
        } else {
            return of(localCart);
        }
    }

    adicionarItem(ProdutoCompra): Observable<boolean> {
        if (!this.currentUser) {
            if (this.cookieService.check('cart')) {
                this.localMap = JSON.parse(this.cookieService.get('cart'));
            }
            if (!this.localMap[ProdutoCompra.productId]) {
                this.localMap[ProdutoCompra.productId] = ProdutoCompra;
            } else {
                this.localMap[ProdutoCompra.productId].count += ProdutoCompra.count;
            }
            this.cookieService.set('cart', JSON.stringify(this.localMap));
            return of(true);
        } else {
            const url = `${this.cartUrl}/adicionar`;
            return this.http.post<boolean>(url, {
                'quantity': ProdutoCompra.count,
                'productId': ProdutoCompra.productId
            });
        }
    }

    atualizar(ProdutoCompra): Observable<ProdutoCompra> {

        if (this.currentUser) {
            const url = `${this.cartUrl}/${ProdutoCompra.productId}`;
            return this.http.put<ProdutoCompra>(url, ProdutoCompra.count);
        }
    }

    remover(ProdutoCompra) {
        if (!this.currentUser) {
            delete this.localMap[ProdutoCompra.productId];
            return of(null);
        } else {
            const url = `${this.cartUrl}/${ProdutoCompra.productId}`;
            return this.http.delete(url).pipe();
        }
    }

    pagamento(): Observable<any> {
        const url = `${this.cartUrl}/checkout`;
        return this.http.post(url, null).pipe();
    }

    storeLocalCart() {
        this.cookieService.set('cart', JSON.stringify(this.localMap));
    }

    clearLocalCart() {
        console.log('clear local cart');
        this.cookieService.delete('cart');
        this.localMap = {};
    }

}
