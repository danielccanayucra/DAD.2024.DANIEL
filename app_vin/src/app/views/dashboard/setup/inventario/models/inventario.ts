export class InventarioDetalle {
    id?: number;
    categoria?: string;
    precio?: number;
    productoId?: number;
}

export class Inventario {
    id?: number;
    nombre?: string;
    descripcion?: string;
    estadoProducto?: number;
    marca?: string;
    codigo?: number;
    proveedorId?: number;
    inventarioDetalle?: InventarioDetalle[];
}
