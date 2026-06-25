import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

interface NavItem {
  label: string;
  path: string;
  icon: string;
}

@Component({
  selector: 'app-admin-layout',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, RouterOutlet],
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.scss'
})
export class AdminLayout {
  menuOpen = false;
  readonly navItems: NavItem[] = [
    { label: 'Dashboard', path: '/dashboard', icon: 'pi pi-chart-line' },
    { label: 'Productos', path: '/products', icon: 'pi pi-box' },
    { label: 'Ventas', path: '/sales', icon: 'pi pi-wallet' },
    { label: 'Tiendas', path: '/suppliers', icon: 'pi pi-truck' },
    { label: 'Existencias', path: '/inventory', icon: 'pi pi-warehouse' },
    { label: 'Estimacion', path: '/profitability', icon: 'pi pi-calculator' }
  ];
}
