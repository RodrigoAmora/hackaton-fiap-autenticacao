package br.com.fiap.fiapautenticacao.dto.response;

import br.com.fiap.fiapautenticacao.model.role.Role;

public record LoginResponse(
        String token,
        String tipo,
        String email,
        String nome,
        Role role
) {}
