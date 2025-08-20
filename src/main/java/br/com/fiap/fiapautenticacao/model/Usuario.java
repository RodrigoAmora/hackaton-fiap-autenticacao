package br.com.fiap.fiapautenticacao.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

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

}
