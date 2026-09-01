import { Atividade } from './atividade';

export interface Tarefa {
    id: number;
    materia: string;
    data: string;
    trimestre: number;
    atividades: Atividade[];
    atividadesConcluidas: number;
    totalAtividades: number;
    progresso: number;
}