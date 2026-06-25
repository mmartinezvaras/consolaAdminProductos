import { Routes } from '@angular/router';
import { AdminLayout } from './layout/admin-layout/admin-layout';
import { Dashboard } from './features/dashboard/dashboard';
import { GenericResource } from './features/generic-resource/generic-resource';
import { Profitability } from './features/profitability/profitability';

const saleStatusOptions = ['PENDING', 'COMPLETED', 'CANCELLED', 'REFUNDED'];

export const routes: Routes = [
  {
    path: '',
    component: AdminLayout,
    children: [
      { path: '', pathMatch: 'full', redirectTo: 'dashboard' },
      { path: 'dashboard', component: Dashboard },
      { path: 'profitability', component: Profitability },
      {
        path: 'products',
        component: GenericResource,
        data: { config: { key: 'products', title: 'Productos', description: 'Alta rapida del inventario que tienes para vender.', endpoint: '/products', columns: ['id', 'name', 'productModelName', 'supplierName', 'currentStock', 'productUrl', 'usualPurchasePrice'], fields: [
          { key: 'name', label: 'Nombre', type: 'text', required: true },
          { key: 'productModelName', label: 'Modelo', type: 'text', required: true },
          { key: 'supplierId', label: 'Tienda', type: 'select', optionsEndpoint: '/suppliers', optionValueKey: 'id', optionLabelKey: 'name' },
          { key: 'currentStock', label: 'Stock', type: 'number', required: true },
          { key: 'productUrl', label: 'Enlace del producto', type: 'text' },
          { key: 'serialNumber', label: 'Numero de serie', type: 'text' },
          { key: 'usualPurchasePrice', label: 'Precio de compra', type: 'number', required: true }
        ] } }
      },
      {
        path: 'suppliers',
        component: GenericResource,
        data: { config: { key: 'suppliers', title: 'Tiendas', description: 'Lugares donde compras productos y enlaces utiles.', endpoint: '/suppliers', columns: ['id', 'name', 'website', 'purchaseUrl', 'email'], fields: [
          { key: 'name', label: 'Nombre', type: 'text', required: true },
          { key: 'website', label: 'Web', type: 'text' },
          { key: 'purchaseUrl', label: 'Enlace de compra', type: 'text' },
          { key: 'email', label: 'Correo', type: 'text' },
          { key: 'notes', label: 'Notas', type: 'textarea' }
        ] } }
      },
      {
        path: 'inventory',
        component: GenericResource,
        data: { config: { key: 'inventory', title: 'Existencias', description: 'Edita el stock final de cada producto de forma directa.', endpoint: '/inventory', columns: ['productId', 'productName', 'currentStock', 'availableStock', 'inventoryValue'], fields: [
          { key: 'productId', label: 'Producto', type: 'select', required: true, optionsEndpoint: '/products', optionValueKey: 'id', optionLabelKey: 'name' },
          { key: 'quantity', label: 'Stock final', type: 'number', required: true },
          { key: 'reason', label: 'Motivo', type: 'text', required: true },
          { key: 'notes', label: 'Notas', type: 'textarea' }
        ] } }
      },
      {
        path: 'sales',
        component: GenericResource,
        data: { config: { key: 'sales', title: 'Ventas realizadas', description: 'Registro simple de ventas, ingresos y beneficio.', endpoint: '/sales', columns: ['id', 'buyerReference', 'status', 'income', 'profit'], fields: [
          { key: 'buyerReference', label: 'Comprador', type: 'text' },
          { key: 'saleDate', label: 'Fecha', type: 'date', required: true },
          { key: 'status', label: 'Estado', type: 'select', options: saleStatusOptions },
          { key: 'productId', label: 'Producto', type: 'select', required: true, optionsEndpoint: '/products', optionValueKey: 'id', optionLabelKey: 'name' },
          { key: 'quantity', label: 'Cantidad', type: 'number', required: true },
          { key: 'unitSalePrice', label: 'Precio de venta', type: 'number', required: true },
          { key: 'commission', label: 'Comision', type: 'number' },
          { key: 'shippingCost', label: 'Envio', type: 'number' },
          { key: 'notes', label: 'Notas', type: 'textarea' }
        ] } }
      },
      { path: '**', redirectTo: 'dashboard' }
    ]
  }
];
