package com.example.squarePlanner.infra;

import com.example.squarePlanner.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ProvaJaExisteException.class)
    public ResponseEntity<RestErrorMessage> provaJaExiste(ProvaJaExisteException exception){
        RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.CONFLICT,"prova ja exixte");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(threatMessage);
    }
    @ExceptionHandler(ConteudoJaExisteException.class)
    public ResponseEntity<RestErrorMessage> conteudoJaExiste(ConteudoJaExisteException exception){
        RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.CONFLICT,"conteudo ja exixte");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(threatMessage);
    }

    @ExceptionHandler(JaExisteException.class)
    public ResponseEntity<RestErrorMessage> JaExiste(JaExisteException exception){
        RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.CONFLICT,"ja exixte(termina de criar o resto das custon exceptions seu preguiçoso)");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(threatMessage);
    }



    @ExceptionHandler(FormatoInvalidoException.class)
    public ResponseEntity<RestErrorMessage> trimestreInvalido(FormatoInvalidoException exception){
       RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.CONFLICT,"Trimestre Invalido");

        return ResponseEntity.status(HttpStatus.CONFLICT).body(threatMessage);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RestErrorMessage> formatoInvalido(HttpMessageNotReadableException exception){
        RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.BAD_REQUEST,"Formato Invalido");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(threatMessage);
    }

    @ExceptionHandler(AdNotFound.class)
    public ResponseEntity<RestErrorMessage> adNaoEncontrada(AdNotFound exception){
        RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.NOT_FOUND,"Ad não encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatMessage);
    }
    @ExceptionHandler(ConteudoNotFound.class)
    public ResponseEntity<RestErrorMessage> conteudoNaoEncontrado(ConteudoNotFound exception){
        RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.NOT_FOUND,"Conteudo não encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatMessage);
    }

    @ExceptionHandler(ProvaNotFound.class)
    public ResponseEntity<RestErrorMessage> provaNaoEncontrada(ProvaNotFound exception){
        RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.NOT_FOUND,"Prova não encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatMessage);
    }

    @ExceptionHandler(EventoNotFound.class)
    public ResponseEntity<RestErrorMessage> eventoNaoEncontrado(EventoNotFound exception){
        RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.NOT_FOUND,"Evento não encontrado");

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threatMessage);
    }

    @ExceptionHandler(TarefaNotFound.class)
    public ResponseEntity<RestErrorMessage> tarefaNaoEncontrada(TarefaNotFound exception){
        RestErrorMessage threatMessage = new RestErrorMessage(HttpStatus.NOT_FOUND,"Tarefa não encontrada");
        return ResponseEntity.status(HttpStatus.OK).body(threatMessage);
    }

    @ExceptionHandler(AtividadeNotFound.class)
    public ResponseEntity<RestErrorMessage> atividadeNaoEcontrada(AtividadeNotFound exception){
        RestErrorMessage threat = new RestErrorMessage(HttpStatus.NOT_FOUND,"Atividade não encontrada");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threat);
    }

    @ExceptionHandler(UsuarioNotFound.class)
    public ResponseEntity<RestErrorMessage> usuarioNaoEncontrado(UsuarioNotFound exception){
        RestErrorMessage threat = new RestErrorMessage(HttpStatus.NOT_FOUND,"Usuario não encontrado");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(threat);
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<RestErrorMessage> tratarDadosInvalidos( DadosInvalidosException exception) {

    RestErrorMessage erro = new RestErrorMessage(HttpStatus.BAD_REQUEST,"DADOS_INVALIDOS");

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

}
