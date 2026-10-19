package com.reserva.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice // Avisa o Spring que essa classe vigia todos os Controllers
public class GlobalExceptionHandler {

    // Intercepta erros de validação do @Valid (@Size, @NotBlank, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<DadosErroValidacao>> tratarErro400(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors().stream()
                .map(erro -> new DadosErroValidacao(erro.getField(), erro.getDefaultMessage()))
                .toList();

        return ResponseEntity.badRequest().body(erros);
    }

    // Intercepta quando você dá "throw new RecursoNaoEncontrado(...)"
    @ExceptionHandler(RecursoNaoEncontrado.class)
    public ResponseEntity<MensagemErro> tratarErro404(RecursoNaoEncontrado ex) {
        return ResponseEntity.status(404).body(new MensagemErro(ex.getMessage()));
    }

    // Intercepta quando há conflito de regras (como sala ocupada ou já existente)
    @ExceptionHandler({RecursoConflitantes.class, SalaExistente.class})
    public ResponseEntity<MensagemErro> tratarErro409(RuntimeException ex) {
        return ResponseEntity.status(409).body(new MensagemErro(ex.getMessage()));
    }

    // Intercepta um json incompleto ou mal formatado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<MensagemErro> tratarErroJsonInvalido(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest()
                .body(new MensagemErro("O corpo da requisição é inválido ou contém tipos incompatíveis!"));
    }


    // DTOs auxiliares para formatar a resposta no Postman:
    public record DadosErroValidacao(String campo, String mensagem) {}
    public record MensagemErro(String mensagem) {}
}
