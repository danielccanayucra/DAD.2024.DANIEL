export class PedidoDetalle {
    id?: number;
    cantidad?: number;
    precio?: number;
    productoId?: number;
}

export class Pedido {
    id?: number;
    serie?: string;
    numero?: string;
    descripcion?: string;
    clienteId?: number;
    pedidodetalle?: PedidoDetalle[];
}
