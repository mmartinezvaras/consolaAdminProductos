import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component } from '@angular/core';
import { ApiClientService } from '../../core/services/api-client.service';

@Component({
  selector: 'app-reports',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './reports.html',
  styleUrl: '../dashboard/dashboard.scss'
})
export class Reports {
  data?: unknown;
  constructor(private readonly api: ApiClientService, private readonly cdr: ChangeDetectorRef) {}

  load(): void {
    this.api.path<unknown>('/profits/summary').subscribe(result => {
      this.data = result;
      this.cdr.detectChanges();
    });
  }
}
