import { Pedido } from '../models/pedido';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { PedidoNewComponent } from '../components/form/pedido-new.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { PedidoEditComponent } from '../components/form/pedido-edit.component';
import { ConfirmDialogService } from "../../../../../shared/confirm-dialog/confirm-dialog.service";
import { PedidoListComponent } from "../components";
import { PedidoService } from "../../../../../providers/services/setup/pedido.service";

@Component({
    selector: 'app-pedidos-container',
    standalone: true,
    imports: [
        CommonModule,
        RouterOutlet,
        PedidoListComponent,
        PedidoNewComponent,
        PedidoEditComponent,
        FormsModule,
        ReactiveFormsModule,
    ],
    template: `
        <app-pedidos-list
            class="w-full"
            [pedidos]="pedidos"
            (eventNew)="eventNew($event)"
            (eventEdit)="eventEdit($event)"
            (eventDelete)="eventDelete($event)"
        ></app-pedidos-list>
    `,
})
export class PedidoContainerComponent implements OnInit {
    public error: string = '';
    public pedidos: Pedido[] = [];
    public pedido = new Pedido();

    constructor(
        private _pedidoService: PedidoService,
        private _confirmDialogService: ConfirmDialogService,
        private _matDialog: MatDialog,
    ) {}

    ngOnInit() {
        this.getPedidos();
    }

    getPedidos(): void {
        this._pedidoService.getAll$().subscribe(
            (response) => {
                this.pedidos = response;
            },
            (error) => {
                this.error = error;
            }
        );
    }

    public eventNew($event: boolean): void {
        if ($event) {
            const pedidoForm = this._matDialog.open(PedidoNewComponent);
            pedidoForm.componentInstance.title = 'Nuevo Pedido';
            pedidoForm.afterClosed().subscribe((result: any) => {
                if (result) {
                    this.savePedido(result);
                }
            });
        }
    }

    savePedido(data: Object): void {
        this._pedidoService.add$(data).subscribe((response) => {
            if (response) {
                this.getPedidos();
            }
        });
    }

    eventEdit(idPedido: number): void {
        const listById = this._pedidoService
            .getById$(idPedido)
            .subscribe(async (response) => {
                this.pedido = (response) || {};
                this.openModalEdit(this.pedido);
                listById.unsubscribe();
            });
    }

    openModalEdit(data: Pedido) {
        console.log(data);
        if (data) {
            const pedidoForm = this._matDialog.open(PedidoEditComponent);
            pedidoForm.componentInstance.title = `Editar <b>${data.serie || data.id} </b>`;
            pedidoForm.componentInstance.pedido = data;
            pedidoForm.afterClosed().subscribe((result: any) => {
                if (result) {
                    this.editPedido(data.id, result);
                }
            });
        }
    }

    editPedido(idPedido: number, data: Object) {
        this._pedidoService.update$(idPedido, data).subscribe((response) => {
            if (response) {
                this.getPedidos();
            }
        });
    }

    public eventDelete(idPedido: number) {
        this._confirmDialogService.confirmDelete(
            {
                // title: 'Confirmación Personalizada',
                // message: `¿Quieres proceder con esta acción?`,
            }
        ).then(() => {
            this._pedidoService.delete$(idPedido).subscribe((response) => {
                this.pedidos = response;
            });
            this.getPedidos();
        }).catch(() => {});
    }
}
