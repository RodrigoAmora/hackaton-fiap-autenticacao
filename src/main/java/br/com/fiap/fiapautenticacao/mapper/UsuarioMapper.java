package br.com.fiap.fiapautenticacao.mapper;

import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.UsuarioRequest;
import br.com.fiap.fiapautenticacao.model.Usuario;
import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    public UsuarioDTO mapearParaUsuarioDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(),
                usuario.getCpf(), usuario.getDataNascimento());
    }

    public Usuario mapearParaUsurio(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setSenha(request.senha());
        usuario.setEmail(request.email());
        usuario.setCpf(request.cpf());
        usuario.setDataNascimento(request.dataNascimento());

        return usuario;
    }
}