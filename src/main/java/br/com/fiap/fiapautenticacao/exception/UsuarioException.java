package br.com.fiap.fiapautenticacao.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UsuarioException extends RuntimeException {
    public UsuarioException(String message) {
        super(message);
    }
}
