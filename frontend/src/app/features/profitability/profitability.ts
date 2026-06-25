import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ApiClientService } from '../../core/services/api-client.service';

@Component({
  selector: 'app-profitability',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './profitability.html',
  styleUrl: '../generic-resource/generic-resource.scss'
})
export class Profitability {
  result?: Record<string, unknown>;
  error = '';
  form: FormGroup;

  constructor(
    private readonly fb: FormBuilder,
    private readonly api: ApiClientService,
    private readonly cdr: ChangeDetectorRef
  ) {
    this.form = this.fb.group({
      productId: ['', Validators.required],
      units: [1, Validators.required],
      unitPurchasePrice: [0, Validators.required],
      purchaseShippingCost: [0, Validators.required],
      estimatedUnitSalePrice: [0, Validators.required]
    });
  }

  calculate(save = false): void {
    const endpoint = save ? '/profitability-estimations' : '/profitability-estimations/calculate';
    this.api.postAction<Record<string, unknown>>(endpoint, this.payload()).subscribe({
      next: result => {
        this.result = result;
        this.error = '';
        this.cdr.detectChanges();
      },
      error: err => {
        this.error = err.message;
        this.cdr.detectChanges();
      }
    });
  }

  metric(key: string): string {
    const value = this.result?.[key];
    if (value === null || value === undefined) {
      return '-';
    }
    return typeof value === 'number' ? value.toFixed(2).replace(/\.00$/, '') : String(value);
  }

  private payload(): Record<string, unknown> {
    const body = this.form.getRawValue();
    return {
      name: `Estimacion producto ${body.productId}`,
      productId: Number(body.productId),
      units: Number(body.units),
      unitPurchasePrice: Number(body.unitPurchasePrice),
      purchaseShippingCost: Number(body.purchaseShippingCost),
      estimatedUnitSalePrice: Number(body.estimatedUnitSalePrice),
      otherPurchaseCosts: 0,
      fixedCommission: 0,
      percentageCommission: 0,
      buyerShippingCost: 0,
      otherCosts: 0,
      estimatedLossPercentage: 0
    };
  }
}
