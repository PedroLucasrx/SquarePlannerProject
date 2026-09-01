import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {

  const authService = inject(AuthService);
  const router = inject(Router);

  const token = authService.pegarToken();

  if (
    !token ||
    req.url.includes('/auth/login') ||
    req.url.includes('/auth/cadastro')
  ) {
    return next(req);
  }

  const requestComToken = req.clone({
    headers: req.headers.set(
      'Authorization',
      `Bearer ${token}`
    )
  });

  return next(requestComToken).pipe(

    catchError((erro) => {

      if (
        erro.status === 401 &&
        erro.error?.erro === 'UNAUTHORIZED' &&
        erro.error?.mensagem === 'Token inválido ou expirado.'
      ) {

        authService.logout();
        router.navigate(['/login']);

      }

      return throwError(() => erro);
    })

  );
};