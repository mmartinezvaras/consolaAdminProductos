import { Injectable } from '@angular/core';
import { ApiClientService } from './api-client.service';

@Injectable({ providedIn: 'root' }) export class ProductService { constructor(public api: ApiClientService) {} }
@Injectable({ providedIn: 'root' }) export class ProductModelService { constructor(public api: ApiClientService) {} }
@Injectable({ providedIn: 'root' }) export class SupplierService { constructor(public api: ApiClientService) {} }
@Injectable({ providedIn: 'root' }) export class PurchaseService { constructor(public api: ApiClientService) {} }
@Injectable({ providedIn: 'root' }) export class InventoryService { constructor(public api: ApiClientService) {} }
@Injectable({ providedIn: 'root' }) export class SaleService { constructor(public api: ApiClientService) {} }
@Injectable({ providedIn: 'root' }) export class ExpenseService { constructor(public api: ApiClientService) {} }
@Injectable({ providedIn: 'root' }) export class ProfitabilityService { constructor(public api: ApiClientService) {} }
@Injectable({ providedIn: 'root' }) export class DashboardService { constructor(public api: ApiClientService) {} }
@Injectable({ providedIn: 'root' }) export class SettingsService { constructor(public api: ApiClientService) {} }
