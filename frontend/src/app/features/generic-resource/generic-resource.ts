import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ApiRecord, ResourceConfig } from '../../core/models/api.models';
import { ApiClientService } from '../../core/services/api-client.service';

@Component({
  selector: 'app-generic-resource',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './generic-resource.html',
  styleUrl: './generic-resource.scss'
})
export class GenericResource implements OnInit {
  config!: ResourceConfig;
  records: ApiRecord[] = [];
  remoteOptions: Record<string, ApiRecord[]> = {};
  loading = false;
  saving = false;
  error = '';
  notice = '';
  selected?: ApiRecord;
  expandedKey?: string;
  modalOpen = false;
  form!: FormGroup;

  constructor(
    private readonly route: ActivatedRoute,
    private readonly api: ApiClientService,
    private readonly fb: FormBuilder,
    private readonly cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(data => {
      this.config = data['config'];
      this.buildForm();
      this.loadOptions();
      this.load();
    });
  }

  load(): void {
    this.loading = true;
    this.error = '';
    this.notice = '';
    this.api.list<ApiRecord>(this.config.endpoint).subscribe({
      next: records => {
        this.records = records;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.message;
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  edit(record: ApiRecord): void {
    this.selected = record;
    if (this.config.key === 'inventory') {
      this.form.patchValue({
        productId: record['productId'],
        quantity: record['currentStock'],
        reason: 'Ajuste manual',
        notes: ''
      });
      this.modalOpen = true;
      return;
    }
    this.form.patchValue(record);
    this.modalOpen = true;
  }

  reset(): void {
    this.selected = undefined;
    this.form.reset();
  }

  openCreate(): void {
    this.reset();
    this.modalOpen = true;
  }

  closeModal(): void {
    if (this.saving) {
      return;
    }
    this.modalOpen = false;
    this.reset();
  }

  toggleDetails(record: ApiRecord): void {
    const key = this.rowKey(record);
    this.expandedKey = this.expandedKey === key ? undefined : key;
  }

  save(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.saving = true;
    const body = this.payload(this.cleanDates(this.form.getRawValue()));
    if (!body) {
      this.error = 'No hay cambios que guardar';
      this.saving = false;
      this.cdr.detectChanges();
      return;
    }
    const request = this.config.key === 'inventory'
      ? this.api.postAction<ApiRecord>('/inventory/adjustments', body)
      : this.config.key === 'settings' && this.selected?.['settingKey']
      ? this.api.update<ApiRecord>(this.config.endpoint, this.selected['settingKey'] as string, body)
      : this.selected?.['id']
      ? this.api.update<ApiRecord>(this.config.endpoint, this.selected['id'] as number, body)
      : this.api.create<ApiRecord>(this.config.endpoint, body);
    request.subscribe({
      next: () => {
        this.saving = false;
        this.modalOpen = false;
        this.reset();
        this.load();
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.message;
        this.saving = false;
        this.cdr.detectChanges();
      }
    });
  }

  remove(record: ApiRecord): void {
    if (this.config.key === 'inventory') {
      return;
    }
    if (!record['id'] || !confirm('Confirmar eliminacion')) {
      return;
    }
    this.api.delete(this.config.endpoint, record['id'] as number).subscribe({
      next: () => {
        this.records = this.records.filter(item => item['id'] !== record['id']);
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.message;
        this.cdr.detectChanges();
      }
    });
  }

  status(record: ApiRecord, status: string): void {
    if (!record['id']) {
      return;
    }
    const path = this.config.key === 'sales' || this.config.key === 'purchases' ? '/status' : '';
    this.api.patch<ApiRecord>(this.config.endpoint, record['id'] as number, path, { status }).subscribe({
      next: () => this.load(),
      error: err => {
        this.error = err.message;
        this.cdr.detectChanges();
      }
    });
  }

  value(record: ApiRecord, column: string): string {
    const value = record[column];
    if (this.isMoneyField(column)) {
      return this.money(value);
    }
    if (column === 'status') {
      return this.statusLabel(value);
    }
    if (column === 'category') {
      return this.categoryLabel(value);
    }
    if (Array.isArray(value)) {
      return `${value.length}`;
    }
    if (typeof value === 'object' && value !== null) {
      return JSON.stringify(value);
    }
    return value === null || value === undefined || value === '' ? '-' : String(value);
  }

  detailKeys(record: ApiRecord): string[] {
    return Object.keys(record)
      .filter(key => !this.isEmpty(record[key]))
      .filter(key => !['createdAt', 'updatedAt', 'active'].includes(key));
  }

  visibleKeys(record: ApiRecord): string[] {
    return this.config.columns.filter(key => !this.isEmpty(record[key]));
  }

  title(record: ApiRecord): string {
    return this.value(record, this.config.titleField ?? this.config.columns[1] ?? this.config.columns[0]);
  }

  subtitle(record: ApiRecord): string {
    const key = this.config.subtitleField ?? this.config.columns[2];
    return key ? this.value(record, key) : '';
  }

  labelFor(key: string): string {
    const fieldLabel = this.config.fields.find(field => field.key === key)?.label;
    return this.config.columnLabels?.[key] ?? fieldLabel ?? this.defaultLabel(key);
  }

  actionLabel(): string {
    if (this.selected) {
      return 'Guardar cambios';
    }
    if (this.config.createLabel) {
      return this.config.createLabel;
    }
    const name = this.config.title.toLowerCase().replace(/s$/, '');
    return `Nuevo ${name}`;
  }

  rowKey(record: ApiRecord): string {
    return String(record['id'] ?? record['productId'] ?? record['settingKey'] ?? JSON.stringify(record));
  }

  isExpanded(record: ApiRecord): boolean {
    return this.expandedKey === this.rowKey(record);
  }

  isLinkValue(value: unknown): boolean {
    return typeof value === 'string' && /^(https?:\/\/|www\.)/i.test(value.trim());
  }

  linkHref(value: unknown): string {
    const text = String(value ?? '').trim();
    return text.toLowerCase().startsWith('http') ? text : `https://${text}`;
  }

  copyLink(value: unknown): void {
    const link = this.linkHref(value);
    navigator.clipboard?.writeText(link).then(() => {
      this.notice = 'Enlace copiado';
      this.cdr.detectChanges();
    }).catch(() => {
      this.notice = 'No se pudo copiar el enlace';
      this.cdr.detectChanges();
    });
  }

  private isMoneyField(key: string): boolean {
    return this.config.moneyFields?.includes(key) ?? /price|cost|income|profit|amount|value|investment|commission/i.test(key);
  }

  private money(value: unknown): string {
    const amount = Number(value ?? 0);
    if (Number.isNaN(amount)) {
      return '-';
    }
    return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(amount);
  }

  private defaultLabel(key: string): string {
    const labels: Record<string, string> = {
      id: 'ID',
      name: 'Nombre',
      productModelName: 'Modelo',
      supplierName: 'Tienda',
      currentStock: 'Stock',
      availableStock: 'Stock disponible',
      inventoryValue: 'Valor inventario',
      productUrl: 'Enlace',
      serialNumber: 'Numero de serie',
      usualPurchasePrice: 'Precio de compra',
      buyerReference: 'Comprador',
      status: 'Estado',
      income: 'Ingresos',
      profit: 'Beneficio',
      website: 'Web',
      purchaseUrl: 'Enlace de compra',
      email: 'Correo',
      productId: 'Producto',
      productName: 'Producto',
      productModelId: 'ID modelo',
      supplierId: 'ID tienda',
      description: 'Descripcion',
      color: 'Color',
      reservedStock: 'Stock reservado',
      minimumStock: 'Stock minimo',
      active: 'Activo',
      notes: 'Notas',
      saleDate: 'Fecha',
      quantity: 'Cantidad',
      unitSalePrice: 'Precio venta',
      commission: 'Comision',
      shippingCost: 'Envio',
      otherCosts: 'Otros gastos',
      purchaseShippingCost: 'Envio compra',
      otherPurchaseCosts: 'Otros gastos compra',
      totalPurchaseCost: 'Coste total',
      concept: 'Concepto',
      category: 'Categoria',
      amount: 'Importe',
      expenseDate: 'Fecha'
    };
    return labels[key] ?? key.replace(/([A-Z])/g, ' $1').trim();
  }

  private statusLabel(value: unknown): string {
    const labels: Record<string, string> = {
      PENDING: 'Pendiente',
      COMPLETED: 'Completada',
      CANCELLED: 'Cancelada',
      REFUNDED: 'Reembolsada',
      PAID: 'Pagada',
      SHIPPED: 'Enviada',
      RECEIVED: 'Recibida'
    };
    const key = String(value ?? '');
    return labels[key] ?? (key || '-');
  }

  private categoryLabel(value: unknown): string {
    const labels: Record<string, string> = {
      PRODUCT: 'Producto',
      SHIPPING: 'Envio',
      COMMISSION: 'Comision',
      PACKAGING: 'Embalaje',
      TRANSPORT: 'Transporte',
      ADVERTISING: 'Publicidad',
      TOOLS: 'Herramientas',
      RETURNS: 'Devoluciones',
      REPAIRS: 'Reparaciones',
      OTHER: 'Otro'
    };
    const key = String(value ?? '');
    return labels[key] ?? (key || '-');
  }

  optionValue(option: ApiRecord, fieldKey: string): string {
    const field = this.config.fields.find(item => item.key === fieldKey);
    const key = field?.optionValueKey ?? 'id';
    const value = option[key];
    return value === null || value === undefined ? '' : String(value);
  }

  optionLabel(option: ApiRecord, fieldKey: string): string {
    const field = this.config.fields.find(item => item.key === fieldKey);
    const key = field?.optionLabelKey ?? 'name';
    const value = option[key];
    return value === null || value === undefined ? this.optionValue(option, fieldKey) : String(value);
  }

  staticOptionLabel(fieldKey: string, option: string): string {
    if (fieldKey === 'status') {
      return this.statusLabel(option);
    }
    if (fieldKey === 'category') {
      return this.categoryLabel(option);
    }
    return option;
  }

  private isEmpty(value: unknown): boolean {
    return value === null || value === undefined || value === '';
  }

  private buildForm(): void {
    const controls: Record<string, unknown> = {};
    this.config.fields.forEach(field => {
      controls[field.key] = ['', field.required ? Validators.required : []];
    });
    this.form = this.fb.group(controls);
  }

  private cleanDates(body: ApiRecord): ApiRecord {
    const cleaned = { ...body };
    this.config.fields.forEach(field => {
      const value = cleaned[field.key];
      if (field.type === 'date' && typeof value === 'string' && value.length === 16) {
        cleaned[field.key] = `${value}:00`;
      }
      if (field.type === 'number' && value !== '' && value !== null && value !== undefined) {
        cleaned[field.key] = this.parseNumber(value);
      }
    });
    return cleaned;
  }

  private payload(body: ApiRecord): ApiRecord | null {
    if (this.config.key === 'products') {
      const purchasePrice = body['usualPurchasePrice'];
      return {
        ...body,
        color: body['color'] || 'Sin color',
        recommendedSalePrice: body['recommendedSalePrice'] || purchasePrice || 0,
        purchaseShippingCost: body['purchaseShippingCost'] || 0,
        otherPurchaseCosts: body['otherPurchaseCosts'] || 0,
        reservedStock: body['reservedStock'] || 0,
        minimumStock: body['minimumStock'] || 0
      };
    }
    if (this.config.key === 'inventory') {
      const productId = Number(body['productId']);
      const targetStock = Number(body['quantity']);
      const currentRecord = this.records.find(record => Number(record['productId']) === productId);
      const currentStock = Number(currentRecord?.['currentStock'] ?? 0);
      const adjustment = targetStock - currentStock;
      if (adjustment === 0) {
        return null;
      }
      return {
        productId,
        quantity: adjustment,
        reason: body['reason'] || 'Ajuste manual',
        notes: body['notes'] || `Stock final actualizado a ${targetStock}`
      };
    }
    if (this.config.key === 'purchases') {
      return {
        ...body,
        items: [{
          productId: Number(body['productId']),
          quantity: Number(body['quantity']),
          unitPurchasePrice: Number(body['unitPurchasePrice'])
        }]
      };
    }
    if (this.config.key === 'sales') {
      return {
        ...body,
        items: [{
          productId: Number(body['productId']),
          quantity: Number(body['quantity']),
          unitSalePrice: Number(body['unitSalePrice'])
        }]
      };
    }
    return body;
  }

  private loadOptions(): void {
    this.remoteOptions = {};
    this.config.fields
      .filter(field => field.optionsEndpoint)
      .forEach(field => {
        this.api.list<ApiRecord>(field.optionsEndpoint as string).subscribe({
          next: options => {
            this.remoteOptions[field.key] = options;
            this.cdr.detectChanges();
          },
          error: err => {
            this.error = err.message;
            this.cdr.detectChanges();
          }
        });
      });
  }

  private parseNumber(value: unknown): number {
    if (typeof value === 'number') {
      return value;
    }
    return Number(String(value).replace(',', '.'));
  }
}
