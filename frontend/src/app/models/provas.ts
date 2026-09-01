import { Conteudo } from "./conteudo";

export interface Prova {
    id: number;
    materia: string;
    data: string;
    trimestre: number;
    conteudos: Conteudo[];

    conteudosConcluidos: number;
    totalConteudos: number;
    progresso: number;
}