import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Tarefa } from '../models/tarefa';


export interface CriarAtividade {
  nome: string;
}

export interface CriarTarefa {
  materia: string;
  data: string;
  trimestre: number;
  atividades: CriarAtividade[];
}


export interface EditarAtividade {
  id?: number;
  nome: string;
}

export interface EditarTarefa {
  materia: string;
  data: string;
  trimestre: number;
  atividades: EditarAtividade[];
}


@Injectable({
  providedIn: 'root'
})

export class TarefaService {

  private apiUrl = 'https://squareplannerproject.onrender.com/tarefas';

  constructor(private http: HttpClient) {}

  listarTarefas(): Observable<Tarefa[]> {
    return this.http.get<Tarefa[]>(this.apiUrl);
  }

  criarTarefa(dados: CriarTarefa): Observable<any> {
    return this.http.post(this.apiUrl, dados);
  }

  deletarTarefa(id: number): Observable<any> {
    return this.http.delete(
      `https://squareplannerproject.onrender.com/tarefas/tarefas/${id}`
    );
  }

  editarEstadoAtividade(id: number, concluido: boolean): Observable<any> {
    return this.http.put(
      `https://squareplannerproject.onrender.com/tarefas/atividades/${id}/estado`,
      { concluido: concluido }
    );
  }

  deletarAtividade(id: number): Observable<any> {
    return this.http.delete(
      `https://squareplannerproject.onrender.com/tarefas/atividades/${id}`
    );
  }

  editarTarefa(id: number, dados: EditarTarefa): Observable<any> {
    return this.http.put(`${this.apiUrl}/${id}`, dados);
  }


}
