import { Routes } from '@angular/router';
import { AdminLayout } from './layout/admin-layout/admin-layout';
import { Dashboard } from './features/dashboard/dashboard';
import { GenericResource } from './features/generic-resource/generic-resource';
import { Profitability } from './features/profitability/profitability';

const saleStatusOptions = ['PENDING', 'COMPLETED', 'CANCELLED', 'REFUNDED'];
const expenseCategoryOptions = ['PRODUCT', 'SHIPPING', 'OTHER'];

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
        data: { config: { key: 'products', title: 'Productos', description: 'Alta rapida del inventario que tienes para vender.', endpoint: '/products', createLabel: 'Nuevo producto', titleField: 'name', subtitleField: 'productModelName', moneyFields: ['usualPurchasePrice', 'purchaseShippingCost', 'otherPurchaseCosts', 'totalPurchaseCost', 'recommendedSalePrice'], columns: ['name', 'productModelName', 'supplierName', 'currentStock', 'productUrl', 'totalPurchaseCost'], columnLabels: {
          name: 'Nombre',
          productModelName: 'Modelo',
          supplierName: 'Tienda',
          currentStock: 'Stock',
          productUrl: 'Enlace',
          usualPurchasePrice: 'Precio compra',
          purchaseShippingCost: 'Envio compra',
          otherPurchaseCosts: 'Otros gastos compra',
          totalPurchaseCost: 'Coste total',
          serialNumber: 'Numero de serie',
          recommendedSalePrice: 'Precio venta',
          createdAt: 'Creado',
          updatedAt: 'Actualizado'
        }, fields: [
          { key: 'name', label: 'Nombre', type: 'text', required: true },
          { key: 'productModelName', label: 'Modelo', type: 'text', required: true },
          { key: 'supplierId', label: 'Tienda', type: 'select', optionsEndpoint: '/suppliers', optionValueKey: 'id', optionLabelKey: 'name' },
          { key: 'currentStock', label: 'Stock', type: 'number', required: true },
          { key: 'productUrl', label: 'Enlace del producto', type: 'text' },
          { key: 'serialNumber', label: 'Numero de serie', type: 'text' },
          { key: 'usualPurchasePrice', label: 'Precio de compra', type: 'number', required: true },
          { key: 'purchaseShippingCost', label: 'Envio de compra', type: 'number' },
          { key: 'otherPurchaseCosts', label: 'Otros gastos de compra', type: 'number' }
        ] } }
      },
      {
        path: 'suppliers',
        component: GenericResource,
        data: { config: { key: 'suppliers', title: 'Tiendas', description: 'Lugares donde compras productos y enlaces utiles.', endpoint: '/suppliers', createLabel: 'Nueva tienda', titleField: 'name', subtitleField: 'website', columns: ['name', 'website', 'purchaseUrl', 'email'], columnLabels: {
          name: 'Nombre',
          website: 'Web',
          purchaseUrl: 'Enlace de compra',
          email: 'Correo',
          notes: 'Notas',
          createdAt: 'Creado',
          updatedAt: 'Actualizado'
        }, fields: [
          { key: 'name', label: 'Nombre', type: 'text', required: true },
          { key: 'website', label: 'Web', type: 'text' },
          { key: 'purchaseUrl', label: 'Enlace de compra', type: 'text' },
          { key: 'email', label: 'Correo', type: 'text' },
          { key: 'notes', label: 'Notas', type: 'textarea' }
        ] } }
      },
      {
        path: 'expenses',
        component: GenericResource,
        data: { config: { key: 'expenses', title: 'Gastos', description: 'Gastos generales del negocio: embalaje, envios, comisiones o extras.', endpoint: '/expenses', createLabel: 'Nuevo gasto', titleField: 'concept', subtitleField: 'category', moneyFields: ['amount'], columns: ['concept', 'category', 'amount', 'expenseDate'], columnLabels: {
          concept: 'Concepto',
          category: 'Categoria',
          amount: 'Importe',
          expenseDate: 'Fecha',
          notes: 'Notas',
          createdAt: 'Creado',
          updatedAt: 'Actualizado'
        }, fields: [
          { key: 'concept', label: 'Concepto', type: 'text', required: true },
          { key: 'category', label: 'Categoria', type: 'select', options: expenseCategoryOptions },
          { key: 'amount', label: 'Importe', type: 'number', required: true },
          { key: 'expenseDate', label: 'Fecha', type: 'date', required: true },
          { key: 'notes', label: 'Notas', type: 'textarea' }
        ] } }
      },
      {
        path: 'inventory',
        component: GenericResource,
        data: { config: { key: 'inventory', title: 'Existencias', description: 'Edita el stock final de cada producto de forma directa.', endpoint: '/inventory', createLabel: 'Ajustar stock', titleField: 'productName', subtitleField: 'currentStock', moneyFields: ['inventoryValue'], columns: ['productName', 'currentStock', 'availableStock', 'inventoryValue'], columnLabels: {
          productId: 'ID producto',
          productName: 'Producto',
          currentStock: 'Stock actual',
          availableStock: 'Stock disponible',
          inventoryValue: 'Valor inventario'
        }, fields: [
          { key: 'productId', label: 'Producto', type: 'select', required: true, optionsEndpoint: '/products', optionValueKey: 'id', optionLabelKey: 'name' },
          { key: 'quantity', label: 'Stock final', type: 'number', required: true },
          { key: 'reason', label: 'Motivo', type: 'text', required: true },
          { key: 'notes', label: 'Notas', type: 'textarea' }
        ] } }
      },
      {
        path: 'sales',
        component: GenericResource,
        data: { config: { key: 'sales', title: 'Ventas realizadas', description: 'Registro simple de ventas, ingresos y beneficio.', endpoint: '/sales', createLabel: 'Nueva venta', titleField: 'buyerReference', subtitleField: 'status', moneyFields: ['income', 'profit', 'unitSalePrice', 'commission', 'shippingCost', 'otherCosts'], columns: ['buyerReference', 'status', 'income', 'profit'], columnLabels: {
          buyerReference: 'Comprador',
          saleDate: 'Fecha',
          status: 'Estado',
          income: 'Ingresos',
          profit: 'Beneficio',
          productId: 'Producto',
          quantity: 'Cantidad',
          unitSalePrice: 'Precio venta',
          commission: 'Comision',
          shippingCost: 'Envio',
          notes: 'Notas',
          createdAt: 'Creado',
          updatedAt: 'Actualizado'
        }, fields: [
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
