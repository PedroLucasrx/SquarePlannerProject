import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Ad } from '../models/ad';

export interface CriarAd {
  materia: string;
  data: string;
  trimestre: number;
  proposta: string;
}

export interface AdsResponse {
  ads: Ad[];
  adsConcluidas: number;
  totalAds: number;
  progresso: number;
}

@Injectable({providedIn: 'root'})

export class AdService {

  private apiUrl = 'http://localhost:8081/ads';

  constructor(private http: HttpClient) {}

  listarAds(): Observable<AdsResponse> {
    return this.http.get<AdsResponse>(this.apiUrl);
  }
  
  criarAd(dados: CriarAd): Observable<any> {
    return this.http.post(this.apiUrl, dados);
  }

  editarAd(id: number, dados: CriarAd): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, dados);
  }

  deletarAd(id: number): Observable<any> {
    return this.http.delete(
      `http://localhost:8081/ads/${id}`
    );
  }

  editarEstadoAd(id: number, concluido: boolean): Observable<any> {
    return this.http.put( 
      `http://localhost:8081/ads/${id}/estado`,
      { concluido: concluido }
    );
  }
}
