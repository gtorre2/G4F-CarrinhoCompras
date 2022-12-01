import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError} from "rxjs/operators";
import {Observable, of} from "rxjs";
import {Pedido} from "../models/pedido";
import {apiUrl} from "../../environments/environment";

@Injectable({
    providedIn: 'root'
})
export class PedidoService {

    private orderUrl = `${apiUrl}/order`;

    constructor(private http: HttpClient) {
    }

    getPage(page = 1, size = 10): Observable<any> {
        return this.http.get(`${this.orderUrl}?page=${page}&size=${size}`).pipe();
    }

    show(id): Observable<Pedido> {
        return this.http.get<Pedido>(`${this.orderUrl}/${id}`).pipe(
            catchError(_ => of(null))
        );
    }

    cancel(id): Observable<Pedido> {
        return this.http.patch<Pedido>(`${this.orderUrl}/cancel/${id}`, null).pipe(
            catchError(_ => of(null))
        );
    }

    finalizar(id): Observable<Pedido> {
        return this.http.patch<Pedido>(`${this.orderUrl}/finish/${id}`, null).pipe(
            catchError(_ => of(null))
        );
    }
}
