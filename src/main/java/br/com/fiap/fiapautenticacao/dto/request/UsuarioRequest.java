package br.com.fiap.fiapautenticacao.dto.request;

import br.com.fiap.fiapautenticacao.model.role.ERole;
import br.com.fiap.fiapautenticacao.model.role.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Requisição para operações com usuário")
public record UsuarioRequest(
        @Schema(description = "Nome do usuário", example = "João Silva")
        @JsonProperty("nome")
        String nome,

        @Schema(description = "Senha do usuário", example = "senha123")
        @JsonProperty("senha")
        String senha,

        @Schema(description = "Email do usuário", example = "joao@email.com")
        @JsonProperty("email")
        String email,

        @Schema(description = "CPF do usuário", example = "12345678900")
        @JsonProperty("cpf")
        String cpf,

        @Schema(description = "Data de nascimento do usuário", example = "1988-07-20")
        @JsonFormat(pattern = "yyyy-MM-dd")
        @JsonProperty("data_nascimento")
        LocalDate dataNascimento,

        @Schema(
                description = "Role/papel do usuário no sistema",
                example = "{\"name\": \"ROLE_USER\"}",
                implementation = Role.class
        )
        @JsonProperty("role")
        Role role
) {}
