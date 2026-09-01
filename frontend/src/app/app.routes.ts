import { Routes } from '@angular/router';
import { HomeComponent } from './pages/home/home.component';
import { ProvasComponent } from './pages/provas/provas.component';
import { AdComponent } from './pages/ads/ad.component';
import { TarefasComponent } from './pages/tarefas/tarefas.component';
import { EventosComponent } from './pages/eventos/eventos.component';
import { LoginComponent } from './pages/login/login.component';
import { authGuard } from './guards/auth.guard';
import { CadastroComponent } from './pages/cadastro/cadastro.component';

export const routes: Routes = [
  {
    path: '',
    component: HomeComponent
    
  },
  {
    path: 'provas',
    component: ProvasComponent,
    canActivate: [authGuard]
  },
  {
    path: 'ads',
    component: AdComponent,
    canActivate: [authGuard]
  },
  {
    path: 'tarefas',
    component: TarefasComponent,
    canActivate: [authGuard]
  },
  {
    path: 'eventos',
    component: EventosComponent,
    canActivate: [authGuard]
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'cadastro',
    component: CadastroComponent
  }
];
