package br.com.fiap.fiapautenticacao.service;

import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.UsuarioRequest;
import br.com.fiap.fiapautenticacao.exception.UsuarioException;
import br.com.fiap.fiapautenticacao.mapper.UsuarioMapper;
import br.com.fiap.fiapautenticacao.model.Usuario;
import br.com.fiap.fiapautenticacao.model.role.ERole;
import br.com.fiap.fiapautenticacao.model.role.Role;
import br.com.fiap.fiapautenticacao.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioMapper usuarioMapper;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;
    private UsuarioDTO usuarioDTO;
    private UsuarioRequest usuarioRequest;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId("1");
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("senha123");

        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        usuarioDTO = new UsuarioDTO("1", "Fulano Silva", "teste@teste.com", "11122233344455", LocalDate.now(), LocalDateTime.now(), role);

        usuarioRequest = new UsuarioRequest("Fulano Silva","senha123", "teste@teste.com", "11122233344455", LocalDate.now(), role);
    }

    @Test
    @DisplayName("Deve salvar um novo usuário com sucesso")
    void deveSalvarNovoUsuarioComSucesso() {
        when(usuarioRepository.findByEmail(usuarioRequest.email())).thenReturn(Optional.empty());
        when(usuarioMapper.mapearParaUsuario(usuarioRequest)).thenReturn(usuario);
        when(passwordEncoder.encode(usuario.getSenha())).thenReturn("senhaEncriptada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);
        when(usuarioMapper.mapearParaUsuarioDTO(usuario)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.salvarUsuario(usuarioRequest);

        assertThat(resultado).isNotNull();
        assertThat(resultado.email()).isEqualTo(usuarioRequest.email());
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar salvar usuário com email já cadastrado")
    void deveLancarExcecaoAoSalvarUsuarioComEmailJaCadastrado() {
        when(usuarioRepository.findByEmail(usuarioRequest.email())).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.salvarUsuario(usuarioRequest))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("já está cadastrado");
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        when(usuarioRepository.findById("1")).thenReturn(Optional.of(usuario));
        when(usuarioMapper.mapearParaUsuarioDTO(usuario)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.buscarUsuarioPeloId("1");

        assertThat(resultado).isNotNull();
        assertThat(resultado.id()).isEqualTo("1");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário com ID inexistente")
    void deveLancarExcecaoAoBuscarUsuarioComIdInexistente() {
        when(usuarioRepository.findById("999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarUsuarioPeloId("999"))
                .isInstanceOf(UsuarioException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    @DisplayName("Deve buscar usuários paginados com sucesso")
    void deveBuscarUsuariosPaginadosComSucesso() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.Direction.ASC, "id");
        Page<Usuario> usuarioPage = new PageImpl<>(List.of(usuario));

        when(usuarioRepository.findAll(pageRequest)).thenReturn(usuarioPage);
        when(usuarioMapper.mapearParaUsuarioDTO(usuario)).thenReturn(usuarioDTO);

        Page<UsuarioDTO> resultado = usuarioService.buscarUsuarios(0, 10);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).email()).isEqualTo(usuarioDTO.email());
    }

    @Test
    @DisplayName("Deve buscar usuário por email com sucesso")
    void deveBuscarUsuarioPorEmailComSucesso() {
        when(usuarioRepository.findByEmail("teste@teste.com")).thenReturn(Optional.of(usuario));
        when(usuarioMapper.mapearParaUsuarioDTO(usuario)).thenReturn(usuarioDTO);

        UsuarioDTO resultado = usuarioService.buscarUsuarioPorEmail("teste@teste.com");

        assertThat(resultado).isNotNull();
        assertThat(resultado.email()).isEqualTo("teste@teste.com");
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar usuário com email inexistente")
    void deveLancarExcecaoAoBuscarUsuarioComEmailInexistente() {
        when(usuarioRepository.findByEmail("inexistente@teste.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> usuarioService.buscarUsuarioPorEmail("inexistente@teste.com"))
                .isInstanceOf(UsuarioException.class)
                .hasMessageContaining("não cadastrado");
    }
}
