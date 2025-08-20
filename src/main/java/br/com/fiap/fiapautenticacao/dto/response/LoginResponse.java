package br.com.fiap.fiapautenticacao.dto.response;

public record LoginResponse(
        String token,
        String tipo,
        String nome,
        String email
) {}
