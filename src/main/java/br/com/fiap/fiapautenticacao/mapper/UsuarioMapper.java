package br.com.fiap.fiapautenticacao.mapper;

import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.UsuarioRequest;
import br.com.fiap.fiapautenticacao.model.Usuario;
import br.com.fiap.fiapautenticacao.model.role.ERole;
import br.com.fiap.fiapautenticacao.model.role.Role;
import br.com.fiap.fiapautenticacao.repository.RoleRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class UsuarioMapper {

    private final RoleRepository roleRepository;

    public UsuarioMapper(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public UsuarioDTO mapearParaUsuarioDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome(), usuario.getEmail(),
                usuario.getCpf(), usuario.getDataNascimento(), usuario.getDataCadastro(), usuario.getRole());
    }

    public Usuario mapearParaUsuario(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setSenha(request.senha());
        usuario.setEmail(request.email());
        usuario.setCpf(request.cpf());
        usuario.setDataNascimento(request.dataNascimento());
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setRole(buscarRole(request.role()));

        return usuario;
    }

    private Role buscarRole(Role role) {
        return Optional.ofNullable(role)
                .map(Role::getName)
                .flatMap(roleRepository::findByName)
                .orElseGet(this::getDefaultRole);
    }

    private Role getDefaultRole() {
        return roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role padrão não encontrada"));
    }

}