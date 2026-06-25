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
  charts: Record<string, unknown[]> = {};

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
    ['/dashboard/charts/sales-by-month', '/dashboard/charts/profit-by-month', '/dashboard/charts/expenses-by-category', '/dashboard/charts/top-products'].forEach(endpoint => {
      this.api.list<unknown>(endpoint).subscribe(data => {
        this.charts[endpoint] = data;
        this.cdr.detectChanges();
      });
    });
  }
}
