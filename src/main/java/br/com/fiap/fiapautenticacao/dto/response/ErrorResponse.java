package br.com.fiap.fiapautenticacao.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        int status,
        String mensagem,
        LocalDateTime timestamp
) {}
