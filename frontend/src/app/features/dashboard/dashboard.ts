import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ApiClientService } from '../../core/services/api-client.service';

interface Summary {
  totalStockUnits: number;
  inventoryValue: number;
  totalIncome: number;
  totalExpenses: number;
  grossProfit: number;
  netProfit: number;
  totalSales: number;
  pendingPurchases: number;
  lowStockProducts: number;
}

interface ChartItem {
  label: string;
  value: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './dashboard.html',
  styleUrl: './dashboard.scss'
})
export class Dashboard implements OnInit {
  summary?: Summary;
  loading = false;
  error = '';
  salesByMonth: ChartItem[] = [];
  profitByMonth: ChartItem[] = [];
  expensesByCategory: ChartItem[] = [];
  topProducts: ChartItem[] = [];
  readonly pieColors = ['#2dd4bf', '#60a5fa', '#f59e0b', '#f472b6', '#a78bfa', '#34d399'];

  constructor(private readonly api: ApiClientService, private readonly cdr: ChangeDetectorRef) {}

  ngOnInit(): void {
    this.loading = true;
    this.api.path<Summary>('/dashboard/summary').subscribe({
      next: summary => {
        this.summary = summary;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.message;
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
    this.loadChart('/dashboard/charts/sales-by-month', data => this.salesByMonth = data);
    this.loadChart('/dashboard/charts/profit-by-month', data => this.profitByMonth = data);
    this.loadChart('/dashboard/charts/expenses-by-category', data => this.expensesByCategory = data);
    this.loadChart('/dashboard/charts/top-products', data => this.topProducts = data);
  }

  expensePieBackground(): string {
    const total = this.expenseTotal();
    if (!total) {
      return 'conic-gradient(var(--surface-strong) 0 100%)';
    }
    let current = 0;
    const segments = this.expensesByCategory.map((item, index) => {
      const start = current;
      current += (Number(item.value) / total) * 100;
      return `${this.pieColors[index % this.pieColors.length]} ${start}% ${current}%`;
    });
    return `conic-gradient(${segments.join(', ')})`;
  }

  expenseTotal(): number {
    return this.expensesByCategory.reduce((total, item) => total + Number(item.value || 0), 0);
  }

  money(value: unknown): string {
    const amount = Number(value ?? 0);
    return new Intl.NumberFormat('es-ES', { style: 'currency', currency: 'EUR' }).format(Number.isNaN(amount) ? 0 : amount);
  }

  categoryLabel(value: string): string {
    const labels: Record<string, string> = {
      PRODUCT: 'Producto',
      SHIPPING: 'Envio',
      OTHER: 'Otro'
    };
    return labels[value] ?? value;
  }

  private loadChart(endpoint: string, assign: (data: ChartItem[]) => void): void {
    this.api.list<ChartItem>(endpoint).subscribe(data => {
      assign(data);
      this.cdr.detectChanges();
    });
  }
}
