package br.com.fiap.fiapautenticacao.model;

import br.com.fiap.fiapautenticacao.model.role.Role;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "usuarios")
@Data
@RequiredArgsConstructor
public class Usuario {

    @Id
    private String id;
    private String nome;
    private String senha;
    private String email;
    private String cpf;
    private LocalDate dataNascimento;
    private LocalDateTime dataCadastro;
    private Role role;

}
