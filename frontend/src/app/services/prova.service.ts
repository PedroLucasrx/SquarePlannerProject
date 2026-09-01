import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Prova } from '../models/provas';

export interface CriarConteudo {
    nome: string;
}

export interface CriarProva {
    materia: string;
    data: string;
    trimestre: number;
    conteudos: CriarConteudo[];
}

export interface EditarConteudo {
    id?: number;
    nome: string;
}

export interface EditarProva {
    materia: string;
    data: string;
    trimestre: number;
    conteudos: EditarConteudo[];
}


@Injectable({ providedIn: 'root'})

export class ProvaService {

    private apiUrl = 'http://localhost:8081/provas';

    constructor(private http: HttpClient) {}

    listarProvas(): Observable<Prova[]> {
        return this.http.get<Prova[]>(this.apiUrl);
    }

    criarProva(dados: CriarProva): Observable<any> {
        return this.http.post(this.apiUrl, dados);
    }

    buscarProva(id: number): Observable<Prova> {
        return this.http.get<Prova>(`${this.apiUrl}/${id}`);
    }
    
    editarEstadoConteudo(id: number, concluido: boolean): Observable<any> {
        return this.http.put(
            `http://localhost:8081/provas/conteudos/${id}/estado`,
            { concluido: concluido }
        );
    }

    deletarConteudo(id: number): Observable<any> {
        return this.http.delete(
            `http://localhost:8081/provas/conteudos/${id}`
        );
    }

    editarProva(id: number, dados: EditarProva): Observable<any> {
        return this.http.put(`${this.apiUrl}/${id}`, dados);
    }

    deletarProva(id: number): Observable<any> {
        return this.http.delete(
            `http://localhost:8081/provas/${id}`
        );
    }
}