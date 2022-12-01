import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CardComponent} from './pages/card/card.component';
import {LoginComponent} from './pages/login/login.component';
import {RegistroComponent} from './pages/registrar/registro.component';
import {DetalheComponent} from './pages/detalhes-produto/detalhe.component';
import {AuthGuard} from "./_guards/auth.guard";
import {PedidoComponent} from "./pages/pedido/pedido.component";
import {DetalhePedidoComponent} from "./pages/detalhes-pedido/detalhe-pedido.component";
import {ProdutosComponent} from "./pages/produtos/produtos.component";
import {DetalhesUsuarioComponent} from "./pages/detalhes-usuario/detalhes-usuario.component";
import {Role} from "./enum/Role";
import { CarrinhoComponent } from './pages/carrinho/carrinho.component';
import { EditarProdutoComponent } from './pages/editar-produto/editar-produto.component';

const routes: Routes = [
    {path: '', redirectTo: '/produto', pathMatch: 'full'},
    {path: 'produto/:id', component: DetalheComponent},
    {path: 'category/:id', component: CardComponent},
    {path: 'produto', component: CardComponent},
    {path: 'category', component: CardComponent},
    {path: 'login', component: LoginComponent},
    {path: 'logout', component: LoginComponent},
    {path: 'register', component: RegistroComponent},
    {path: 'carrinho', component: CarrinhoComponent},
    {path: 'success', component: RegistroComponent},
    {path: 'pedido/:id', component: DetalhePedidoComponent, canActivate: [AuthGuard]},
    {path: 'pedido', component: PedidoComponent, canActivate: [AuthGuard]},
    {path: 'comprador', redirectTo: 'comprador/produto', pathMatch: 'full'},
    {
        path: 'comprador/produto',
        component: ProdutosComponent,
        canActivate: [AuthGuard],
        data: {roles: [Role.Manager, Role.Employee]}
    },
    {
        path: 'profile',
        component: DetalhesUsuarioComponent,
        canActivate: [AuthGuard]
    },
    {
        path: 'comprador/produto/:id/edit',
        component: EditarProdutoComponent,
        canActivate: [AuthGuard],
        data: {roles: [Role.Manager, Role.Employee]}
    },
    {
        path: 'comprador/produto/:id/new',
        component: EditarProdutoComponent,
        canActivate: [AuthGuard],
        data: {roles: [Role.Employee]}
    },

];

@NgModule({
    declarations: [],
    imports: [
        RouterModule.forRoot(routes)//{onSameUrlNavigation: 'reload'}
    ],
    exports: [RouterModule]
})
export class AppRoutingModule {
}
