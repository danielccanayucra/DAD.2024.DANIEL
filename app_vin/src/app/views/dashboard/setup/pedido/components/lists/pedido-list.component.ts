import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { abcForms } from '../../../../../../../environments/generals';
import { Pedido } from '../../models/pedido';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatDialog } from '@angular/material/dialog';

@Component({
    selector: 'app-pedidos-list',
    imports: [CommonModule, RouterOutlet, MatButtonModule, MatIconModule],
    standalone: true,
    template: `
        <div class="w-full mx-auto p-6 bg-white rounded overflow-hidden shadow-lg">
            <!-- Encabezado principal -->
            <div class="flex justify-between items-center mb-2 bg-slate-300 text-black p-4 rounded">
                <h2 class="text-2xl font-bold">
                    Lista de <span class="text-primary">Pedidos</span>
                </h2>
                <button mat-flat-button [color]="'primary'" (click)="goNew()">
                    <mat-icon [svgIcon]="'heroicons_outline:plus'"></mat-icon>
                    <span class="ml-2">Nuevo Pedido</span>
                </button>
            </div>
            <div class="bg-white rounded overflow-hidden shadow-lg">
                <div class="p-2 overflow-scroll px-0">
                    <table class="w-full table-fixed">
                        <thead class="bg-primary-600 text-white">
                        <tr>
                            <th class="w-1/6 table-head text-center px-5 border-r">#</th>
                            <th class="w-2/6 table-header text-center px-5 border-r">Serie</th>
                            <th class="w-1/6 table-header text-center border-r">Número</th>
                            <th class="w-1/6 table-header text-center border-r">Descripción</th>
                            <th class="w-1/6 table-header text-center border-r">Cliente ID</th>
                            <th class="w-2/6 table-header text-center">Acciones</th>
                        </tr>
                        </thead>
                        <tbody class="bg-white" *ngFor="let r of pedidos; let i = index">
                        <tr class="hover:bg-gray-100">
                            <td class="w-1/6 p-2 text-center border-b">{{ i + 1 }}</td>
                            <td class="w-2/6 p-2 text-start border-b text-sm">{{ r.serie }}</td>
                            <td class="w-2/6 p-2 text-start border-b text-sm">{{ r.numero }}</td>
                            <td class="w-2/6 p-2 text-start border-b text-sm">{{ r.descripcion }}</td>
                            <td class="w-2/6 p-2 text-start border-b text-sm">{{ r.clienteId }}</td>
                            <td class="w-2/6 p-2 text-center border-b text-sm">
                                <div class="flex justify-center space-x-3">
                                    <mat-icon class="text-amber-400 hover:text-amber-500 cursor-pointer"
                                              (click)="goEdit(r.id)">edit</mat-icon>
                                    <mat-icon class="text-rose-500 hover:text-rose-600 cursor-pointer"
                                              (click)="goDelete(r.id)">delete_sweep</mat-icon>
                                    <mat-icon class="text-blue-500 hover:text-blue-600 cursor-pointer"
                                              (click)="toggleDetalle(i)">list_alt</mat-icon>
                                </div>
                            </td>
                        </tr>
                        <!-- Tabla secundaria para los detalles del pedido -->
                        <tr *ngIf="isDetalleVisible[i]">
                            <td colspan="6" class="p-2 bg-gray-100">
                                <table class="w-full table-fixed border">
                                    <thead class="bg-gray-200">
                                    <tr>
                                        <th class="w-1/6 text-center px-5 border-r">#</th>
                                        <th class="w-2/6 text-center px-5 border-r">Cantidad</th>
                                        <th class="w-2/6 text-center px-5 border-r">Precio</th>
                                        <th class="w-2/6 text-center px-5 border-r">Producto ID</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr *ngFor="let detalle of r.pedidodetalle; let j = index" class="hover:bg-white">
                                        <td class="w-1/6 p-2 text-center border-b">{{ j + 1 }}</td>
                                        <td class="w-2/6 p-2 text-center border-b">{{ detalle.cantidad }}</td>
                                        <td class="w-2/6 p-2 text-center border-b">{{ detalle.precio }}</td>
                                        <td class="w-2/6 p-2 text-center border-b">{{ detalle.productoId }}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    `,
})
export class PedidoListComponent implements OnInit {
    abcForms: any;
    @Input() pedidos: Pedido[] = [];
    isDetalleVisible: boolean[] = [];
    @Output() eventNew = new EventEmitter<boolean>();
    @Output() eventEdit = new EventEmitter<number>();
    @Output() eventDelete = new EventEmitter<number>();

    constructor(private _matDialog: MatDialog) {}

    ngOnInit() {
        this.abcForms = abcForms;
    // Inicializa el estado del despliegue de detalles
    this.isDetalleVisible = new Array(this.pedidos.length).fill(false);
    }

    toggleDetalle(index: number): void {
        this.isDetalleVisible[index] = !this.isDetalleVisible[index];
    }

    public goNew(): void {
        this.eventNew.emit(true);
    }

    public goEdit(id: number): void {
        this.eventEdit.emit(id);
    }

    public goDelete(id: number): void {
        this.eventDelete.emit(id);
    }
}
