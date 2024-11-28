import { Inventario } from '../models/inventario';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { InventarioNewComponent } from '../components/form/inventario-new.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { InventarioEditComponent } from '../components/form/inventario-edit.component';
import { ConfirmDialogService } from "../../../../../shared/confirm-dialog/confirm-dialog.service";
import { InventarioListComponent } from "../components";
import { InventarioService } from "../../../../../providers/services/setup/inventario.service";

@Component({
    selector: 'app-inventarios-container',
    standalone: true,
    imports: [
        CommonModule,
        RouterOutlet,
        InventarioListComponent,
        InventarioNewComponent,
        InventarioEditComponent,
        FormsModule,
        ReactiveFormsModule,
    ],
    template: `
        <app-inventarios-list
            class="w-full"
            [inventarios]="inventarios"
            (eventNew)="eventNew($event)"
            (eventEdit)="eventEdit($event)"
            (eventDelete)="eventDelete($event)"
        ></app-inventarios-list>
    `,
})
export class InventarioContainerComponent implements OnInit {
    public error: string = '';
    public inventarios: Inventario[] = [];
    public inventario = new Inventario();

    constructor(
        private _inventarioService: InventarioService,
        private _confirmDialogService: ConfirmDialogService,
        private _matDialog: MatDialog,
    ) {}

    ngOnInit() {
        this.getInventarios();
    }

    getInventarios(): void {
        this._inventarioService.getAll$().subscribe(
            (response) => {
                this.inventarios = response;
            },
            (error) => {
                this.error = error;
            }
        );
    }

    public eventNew($event: boolean): void {
        if ($event) {
            const inventarioForm = this._matDialog.open(InventarioNewComponent);
            inventarioForm.componentInstance.title = 'Nuevo Inventario';
            inventarioForm.afterClosed().subscribe((result: any) => {
                if (result) {
                    this.saveInventario(result);
                }
            });
        }
    }

    saveInventario(data: Object): void {
        this._inventarioService.add$(data).subscribe((response) => {
            if (response) {
                this.getInventarios();
            }
        });
    }

    eventEdit(idInventario: number): void {
        const listById = this._inventarioService
            .getById$(idInventario)
            .subscribe(async (response) => {
                this.inventario = (response) || {};
                this.openModalEdit(this.inventario);
                listById.unsubscribe();
            });
    }

    openModalEdit(data: Inventario) {
        console.log(data);
        if (data) {
            const inventarioForm = this._matDialog.open(InventarioEditComponent);
            inventarioForm.componentInstance.title = `Editar <b>${data.nombre || data.id} </b>`;
            inventarioForm.componentInstance.inventario = data;
            inventarioForm.afterClosed().subscribe((result: any) => {
                if (result) {
                    this.editInventario(data.id, result);
                }
            });
        }
    }

    editInventario(idInventario: number, data: Object) {
        this._inventarioService.update$(idInventario, data).subscribe((response) => {
            if (response) {
                this.getInventarios();
            }
        });
    }

    public eventDelete(idInventario: number) {
        this._confirmDialogService.confirmDelete(
            {
                // title: 'Confirmación Personalizada',
                // message: `¿Quieres proceder con esta acción?`,
            }
        ).then(() => {
            this._inventarioService.delete$(idInventario).subscribe((response) => {
                this.inventarios = response;
            });
            this.getInventarios();
        }).catch(() => {});
    }
}
