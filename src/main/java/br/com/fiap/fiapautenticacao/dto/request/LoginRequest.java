package br.com.fiap.fiapautenticacao.dto.request;

public record LoginRequest(
        String email,
        String senha
) {}
