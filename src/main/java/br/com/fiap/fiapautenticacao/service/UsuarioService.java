package br.com.fiap.fiapautenticacao.service;

import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.UsuarioRequest;
import br.com.fiap.fiapautenticacao.exception.UsuarioException;
import br.com.fiap.fiapautenticacao.exception.UsuarioNaoEncontradoException;
import br.com.fiap.fiapautenticacao.mapper.UsuarioMapper;
import br.com.fiap.fiapautenticacao.model.Usuario;
import br.com.fiap.fiapautenticacao.repository.UsuarioRepository;
import br.com.fiap.fiapautenticacao.validator.PasswordValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioMapper usuarioMapper,
                          UsuarioRepository usuarioRepository,
                          PasswordEncoder passwordEncoder) {
        this.usuarioMapper = usuarioMapper;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioDTO salvarUsuario(UsuarioRequest request) {
        usuarioRepository.findByEmail(request.email())
                         .ifPresent(usuario -> {
                             throw new UsuarioException("Usuário com e-mail "+request.email()+" já está cadastrado");
                         });

        Usuario usuario = usuarioMapper.mapearParaUsuario(request);
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.mapearParaUsuarioDTO(usuario);
    }

    public UsuarioDTO buscarUsuarioPeloId(String id) {
        Usuario usuario = buscarUsuarioId(id);
        return usuarioMapper.mapearParaUsuarioDTO(usuario);
    }

    public Page<UsuarioDTO> buscarUsuarios(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.Direction.ASC, "id");
        return this.usuarioRepository.findAll(pageRequest).map(usuarioMapper::mapearParaUsuarioDTO);
    }

    public UsuarioDTO buscarUsuarioPorEmail(String email) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent()) {
            return usuarioMapper.mapearParaUsuarioDTO(usuario.get());
        } else {
            throw new UsuarioNaoEncontradoException("Usuário com e-mail "+email+" não cadastrado");
        }
    }

    private Usuario buscarUsuarioId(String usuarioId) {
        return this.usuarioRepository.findById(usuarioId)
                                     .orElseThrow(() -> new UsuarioNaoEncontradoException("Usuário não encontrado"));
    }

    private boolean validarSenha(String senha) {
        return PasswordValidator.validatePassword(senha);
    }

    private void lancarUsuarioException(String message) {
        throw new UsuarioException(message);
    }

}
