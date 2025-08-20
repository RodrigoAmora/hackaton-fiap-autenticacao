package br.com.fiap.fiapautenticacao.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public record UsuarioDTO(
        @JsonProperty("id")
        String id,

        @JsonProperty("nome")
        String nome,

        @JsonProperty("email")
        String email,

        @JsonProperty("cpf")
        String cpf,

        @JsonProperty("data_nascimento")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dataNascimento
) {}
