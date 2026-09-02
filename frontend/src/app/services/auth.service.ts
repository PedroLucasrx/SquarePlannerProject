import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

interface LoginResponse {
  token: string;
}

interface LoginRequest {
  email: string;
  senha: string;
}

interface UsuarioLogado {
  nome: string;
  email: string;
  role: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private apiUrl = 'https://squareplannerproject.onrender.com/auth';

  private logadoSignal = signal(
    localStorage.getItem('token') !== null
  );

  logado = this.logadoSignal.asReadonly();

  private usuarioSignal = signal<UsuarioLogado | null>(
    this.carregarUsuario()
  );

  usuario = this.usuarioSignal.asReadonly();

  private carregarUsuario(): UsuarioLogado | null {

    const token = this.pegarToken();

    if (!token) {
      return null;
    }

    try {

      const payload = token.split('.')[1];

      const dados = JSON.parse(atob(payload));

      return {
        email: dados.sub,
        nome: dados.nome,
        role: dados.role
      };

    } catch {
      return null;
    }
  }

  constructor(private http: HttpClient) {}

  login(dados: LoginRequest): Observable<LoginResponse> {

    return this.http.post<LoginResponse>(
      `${this.apiUrl}/login`,
      dados
    ).pipe(

      tap((resposta) => {

        this.salvarToken(resposta.token);

        this.logadoSignal.set(true);

        this.usuarioSignal.set(
          this.carregarUsuario()
        );

      })

    );

  }

  salvarToken(token: string): void {

    localStorage.setItem('token', token);

  }

  pegarToken(): string | null {

    return localStorage.getItem('token');

  }

  logout(): void {

    localStorage.removeItem('token');

    this.logadoSignal.set(false);

    this.usuarioSignal.set(null);

  }

  estaAutenticado(): boolean {
    return this.logado();

  }
  ehAdmin(): boolean {
  return this.usuario()?.role === 'ADMIN';
  }

  

}