import { Routes } from '@angular/router';
import { PedidoContainerComponent } from "./containers/pedido-container.component";
import { PedidoComponent } from "./pedido.component";

export default [

  {
    path     : '',
    component: PedidoComponent,
    children: [
      {
        path: '',
        component: PedidoContainerComponent,
        data: {
          title: 'Pedidos'
        }
      },
    ],
  },
] as Routes;
