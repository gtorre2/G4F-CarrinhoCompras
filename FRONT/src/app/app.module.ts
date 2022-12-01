import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppComponent} from './app.component';
import {NavigationComponent} from './parts/navigation/navigation.component';
import {CardComponent} from './pages/card/card.component';
import {PaginationComponent} from './parts/pagination/pagination.component';
import {AppRoutingModule} from './app-routing.module';
import {LoginComponent} from './pages/login/login.component';
import {RegistroComponent} from './pages/registrar/registro.component';
import {DetalheComponent} from './pages/detalhes-produto/detalhe.component';
import {FormsModule} from '@angular/forms';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {CookieService} from "ngx-cookie-service";
import {ErrorInterceptor} from "./_interceptors/error-interceptor.service";
import {JwtInterceptor} from "./_interceptors/jwt-interceptor.service";
import {PedidoComponent} from './pages/pedido/pedido.component';
import {DetalhePedidoComponent} from './pages/detalhes-pedido/detalhe-pedido.component';
import {ProdutosComponent} from './pages/produtos/produtos.component';
import {DetalhesUsuarioComponent} from './pages/detalhes-usuario/detalhes-usuario.component';
import { CarrinhoComponent } from './pages/carrinho/carrinho.component';
import { EditarProdutoComponent } from './pages/editar-produto/editar-produto.component';

@NgModule({
    declarations: [
        AppComponent,
        NavigationComponent,
        CardComponent,
        PaginationComponent,
        LoginComponent,
        RegistroComponent,
        DetalheComponent,
        CarrinhoComponent,
        PedidoComponent,
        DetalhePedidoComponent,
        ProdutosComponent,
        DetalhesUsuarioComponent,
        EditarProdutoComponent,

    ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,

    ],
    providers: [CookieService,
        {provide: HTTP_INTERCEPTORS, useClass: JwtInterceptor, multi: true},
        {provide: HTTP_INTERCEPTORS, useClass: ErrorInterceptor, multi: true}],
    bootstrap: [AppComponent]
})
export class AppModule {
}
