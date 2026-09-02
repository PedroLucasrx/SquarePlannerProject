import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Evento } from '../models/evento';
import { Observable } from 'rxjs';


export interface CriarEvento {
  nome: string;
  data: string;
}

@Injectable({
  providedIn: 'root'
})
export class EventoService {
  private apiUrl = 'https://squareplannerproject.onrender.com/eventos';

  constructor(private http: HttpClient) {}

  listarEventos(): Observable<Evento[]> {
    return this.http.get<Evento[]>(this.apiUrl);
  } 
    
  criarEvento(dados: CriarEvento): Observable<any> {
    return this.http.post(this.apiUrl, dados);
  }
  
  editarEvento(id: number, dados: CriarEvento): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, dados);
  }
  
  deletarEvento(id: number): Observable<any> {
    return this.http.delete(
      `https://squareplannerproject.onrender.com/eventos/${id}`
    );
  }
}
