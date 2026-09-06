import { DOCUMENT } from '@angular/common';
import { Injectable, inject, signal } from '@angular/core';

export type Theme = 'light' | 'dark';

@Injectable({ providedIn: 'root' })
export class ThemeService {
  private readonly document = inject(DOCUMENT);
  private readonly storageKey = 'square-planner-theme';

  readonly theme = signal<Theme>(this.getSavedTheme());

  constructor() {
    this.applyTheme(this.theme());
  }

  toggleTheme(): void {
    const theme: Theme = this.theme() === 'dark' ? 'light' : 'dark';

    this.theme.set(theme);
    this.applyTheme(theme);
    localStorage.setItem(this.storageKey, theme);
  }

  private getSavedTheme(): Theme {
    return localStorage.getItem(this.storageKey) === 'dark' ? 'dark' : 'light';
  }

  private applyTheme(theme: Theme): void {
    this.document.documentElement.classList.toggle('theme-dark', theme === 'dark');
  }
}
