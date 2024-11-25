import { Proveedor } from '../models/proveedor';
import { CommonModule } from '@angular/common';
import { RouterOutlet } from '@angular/router';
import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ProveedorNewComponent } from '../components/form/proveedor-new.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { ProveedorEditComponent } from '../components/form/proveedor-edit.component';
import { ConfirmDialogService } from "../../../../../shared/confirm-dialog/confirm-dialog.service";
import { ProveedorListComponent } from "../components";
import { ProveedorService } from "../../../../../providers/services/setup/proveedor.service";

@Component({
    selector: 'app-proveedores-container',
    standalone: true,
    imports: [
        CommonModule,
        RouterOutlet,
        ProveedorListComponent,
        ProveedorNewComponent,
        ProveedorEditComponent,
        FormsModule,
        ReactiveFormsModule,
    ],
    template: `
        <app-proveedores-list
            class="w-full"
            [proveedores]="proveedores"
            (eventNew)="eventNew($event)"
            (eventEdit)="eventEdit($event)"
            (eventDelete)="eventDelete($event)"
        ></app-proveedores-list>
    `,
})
export class ProveedorContainerComponent implements OnInit {
    public error: string = '';
    public proveedores: Proveedor[] = [];
    public proveedor = new Proveedor();

    constructor(
        private _proveedorService: ProveedorService,
        private _confirmDialogService: ConfirmDialogService,
        private _matDialog: MatDialog,
    ) {}

    ngOnInit() {
        this.getProveedores();
    }

    getProveedores(): void {
        this._proveedorService.getAll$().subscribe(
            (response) => {
                this.proveedores = response;
            },
            (error) => {
                this.error = error;
            }
        );
    }

    public eventNew($event: boolean): void {
        if ($event) {
            const proveedorForm = this._matDialog.open(ProveedorNewComponent);
            proveedorForm.componentInstance.title = 'Nuevo Proveedor';
            proveedorForm.afterClosed().subscribe((result: any) => {
                if (result) {
                    this.saveProveedor(result);
                }
            });
        }
    }

    saveProveedor(data: Object): void {
        this._proveedorService.add$(data).subscribe((response) => {
            if (response) {
                this.getProveedores();
            }
        });
    }

    eventEdit(idProveedor: number): void {
        const listById = this._proveedorService
            .getById$(idProveedor)
            .subscribe(async (response) => {
                this.proveedor = (response) || {};
                this.openModalEdit(this.proveedor);
                listById.unsubscribe();
            });
    }

    openModalEdit(data: Proveedor) {
        console.log(data);
        if (data) {
            const proveedorForm = this._matDialog.open(ProveedorEditComponent);
            proveedorForm.componentInstance.title = `Editar <b>${data.nombre || data.id} </b>`;
            proveedorForm.componentInstance.proveedor = data;
            proveedorForm.afterClosed().subscribe((result: any) => {
                if (result) {
                    this.editProveedor(data.id, result);
                }
            });
        }
    }

    editProveedor(idProveedor: number, data: Object) {
        this._proveedorService.update$(idProveedor, data).subscribe((response) => {
            if (response) {
                this.getProveedores();
            }
        });
    }

    public eventDelete(idProveedor: number) {
        this._confirmDialogService.confirmDelete(
            {
                // title: 'Confirmación Personalizada',
                // message: `¿Quieres proceder con esta acción?`,
            }
        ).then(() => {
            this._proveedorService.delete$(idProveedor).subscribe((response) => {
                this.proveedores = response;
            });
            this.getProveedores();
        }).catch(() => {
        });
    }
}
