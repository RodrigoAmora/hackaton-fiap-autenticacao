package br.com.fiap.fiapautenticacao.service;

import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.UsuarioRequest;
import br.com.fiap.fiapautenticacao.exception.UsuarioException;
import br.com.fiap.fiapautenticacao.exception.UsuarioNaoEncontradoException;
import br.com.fiap.fiapautenticacao.mapper.UsuarioMapper;
import br.com.fiap.fiapautenticacao.model.Usuario;
import br.com.fiap.fiapautenticacao.model.role.ERole;
import br.com.fiap.fiapautenticacao.model.role.Role;
import br.com.fiap.fiapautenticacao.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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
        usuario.setId("aed6e6b2-ab03-4575-bcbc-46e50eb519cf");
        usuario.setCpf("11122233344455");
        usuario.setDataNascimento(LocalDate.now());
        usuario.setDataCadastro(LocalDateTime.now());
        usuario.setNome("Fulano Silva");
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("fiap@2025");

        Role role = new Role();
        role.setName(ERole.ROLE_USER);

        usuarioDTO = new UsuarioDTO("1", "Fulano Silva", "teste@teste.com", "11122233344455", LocalDate.now(), LocalDateTime.now(), role);

        usuarioRequest = new UsuarioRequest("Fulano Silva","senha123", "teste@teste.com", "11122233344455", LocalDate.now(), role);
    }

    @Test
    @DisplayName("Deve salvar um novo usuário com sucesso")
    void deveSalvarNovoUsuarioComSucesso() {
        // Arrange
        String senhaEncriptada = "senhaEncriptada123";

        UsuarioRequest usuarioRequest = new UsuarioRequest(
                "João Silva",
                "Teste@123", // Senha válida com os requisitos necessários
                "joao@email.com",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                getRoleUser()
        );

        Usuario usuarioMapeado = new Usuario();
        usuarioMapeado.setNome("João Silva");
        usuarioMapeado.setSenha("Teste@123");
        usuarioMapeado.setEmail("joao@email.com");
        usuarioMapeado.setCpf("12345678900");
        usuarioMapeado.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuarioMapeado.setRole(getRoleUser());

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                "123",
                "João Silva",
                "joao@email.com",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now(),
                getRoleUser()
        );

        // Mocks
        when(usuarioRepository.findByEmail(usuarioRequest.email())).thenReturn(Optional.empty());
        when(usuarioMapper.mapearParaUsuario(usuarioRequest)).thenReturn(usuarioMapeado);
        when(passwordEncoder.encode(anyString())).thenReturn(senhaEncriptada);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMapeado);
        when(usuarioMapper.mapearParaUsuarioDTO(any(Usuario.class))).thenReturn(usuarioDTO);

        // Act
        UsuarioDTO resultado = usuarioService.salvarUsuario(usuarioRequest);

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.nome()).isEqualTo(usuarioRequest.nome());
        assertThat(resultado.email()).isEqualTo(usuarioRequest.email());
        assertThat(resultado.cpf()).isEqualTo(usuarioRequest.cpf());
        assertThat(resultado.dataNascimento()).isEqualTo(usuarioRequest.dataNascimento());
        assertThat(resultado.role().getName()).isEqualTo(ERole.ROLE_USER);

        verify(usuarioRepository).findByEmail(usuarioRequest.email());
        verify(usuarioMapper).mapearParaUsuario(usuarioRequest);
        verify(usuarioRepository).save(argThat(usuario ->
                usuario.getNome().equals(usuarioRequest.nome()) &&
                        usuario.getEmail().equals(usuarioRequest.email()) &&
                        usuario.getSenha().equals(senhaEncriptada)
        ));
        verify(usuarioMapper).mapearParaUsuarioDTO(any(Usuario.class));
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
                .isInstanceOf(UsuarioNaoEncontradoException.class)
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
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessageContaining("não cadastrado");
    }

    @Test
    @DisplayName("Deve editar usuário com sucesso")
    void deveEditarUsuarioComSucesso() {
        String usuarioId = usuario.getId();
        String senhaEncriptada = "senhaEncriptada123";

        UsuarioRequest request = new UsuarioRequest(
                "João Silva",
                "Teste@123", // Senha válida com os requisitos necessários
                "joao@email.com",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                getRoleUser()
        );

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(usuarioId);

        Usuario usuarioMapeado = new Usuario();
        usuarioMapeado.setId(usuarioId);
        usuarioMapeado.setNome("João Silva");
        usuarioMapeado.setSenha("Teste@123");
        usuarioMapeado.setEmail("joao@email.com");
        usuarioMapeado.setCpf("12345678900");
        usuarioMapeado.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuarioMapeado.setRole(getRoleUser());

        Usuario usuarioSalvo = new Usuario();
        usuarioSalvo.setId(usuarioId);
        usuarioSalvo.setNome("João Silva");
        usuarioSalvo.setSenha(senhaEncriptada);
        usuarioSalvo.setEmail("joao@email.com");
        usuarioSalvo.setCpf("12345678900");
        usuarioSalvo.setDataNascimento(LocalDate.of(1990, 1, 1));
        usuarioSalvo.setRole(getRoleUser());

        UsuarioDTO usuarioDTO = new UsuarioDTO(
                usuarioId,
                "João Silva",
                "joao@email.com",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                LocalDateTime.now(),
                getRoleUser()
        );

        // Mock dos comportamentos
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioMapper.mapearParaUsuario(request)).thenReturn(usuarioMapeado);
        when(passwordEncoder.encode(usuarioMapeado.getSenha())).thenReturn(senhaEncriptada);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioSalvo);
        when(usuarioMapper.mapearParaUsuarioDTO(usuarioSalvo)).thenReturn(usuarioDTO);

        // Act
        UsuarioDTO resultado = usuarioService.editarUsuario(usuarioId, request);

        // Assert
        assertNotNull(resultado);
        assertEquals(usuarioId, resultado.id());
        assertEquals("João Silva", resultado.nome());
        assertEquals("joao@email.com", resultado.email());
        assertEquals("12345678900", resultado.cpf());
        assertEquals(LocalDate.of(1990, 1, 1), resultado.dataNascimento());
        assertEquals(ERole.ROLE_USER, resultado.role().getName());

        // Verifica a ordem das chamadas
        InOrder inOrder = inOrder(usuarioRepository, usuarioMapper, passwordEncoder);
        inOrder.verify(usuarioRepository).findById(usuarioId);
        inOrder.verify(usuarioMapper).mapearParaUsuario(request);
        inOrder.verify(usuarioRepository).save(any(Usuario.class));
        inOrder.verify(usuarioMapper).mapearParaUsuarioDTO(usuarioSalvo);
    }

    @Test
    @DisplayName("Deve lançar exceção ao editar usuário com senha inválida")
    void deveLancarExcecaoAoEditarUsuarioComSenhaInvalida() {
        // Arrange
        String usuarioId = usuario.getId();
        UsuarioRequest request = new UsuarioRequest(
                "João Silva",
                "senha123", // Senha inválida (falta caractere especial e maiúscula)
                "joao@email.com",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                getRoleUser()
        );

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(usuarioId);

        Usuario usuarioMapeado = new Usuario();
        usuarioMapeado.setSenha("senha123");

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioMapper.mapearParaUsuario(request)).thenReturn(usuarioMapeado);

        // Act & Assert
        UsuarioException exception = assertThrows(UsuarioException.class, () -> {
            usuarioService.editarUsuario(usuarioId, request);
        });

        assertTrue(exception.getMessage().contains("Senha inválida"));

        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioMapper).mapearParaUsuario(request);
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }


    @Test
    @DisplayName("Deve lançar exceção quando usuário não encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        String usuarioId = usuario.getId();
        UsuarioRequest request = new UsuarioRequest(
                "João Silva",
                "senha123",
                "joao@email.com",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                getRoleUser()
        );

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            usuarioService.editarUsuario(usuarioId, request);
        });

        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioMapper, never()).mapearParaUsuario(any());
        verify(passwordEncoder, never()).encode(anyString());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve manter o mesmo ID ao editar usuário")
    void deveManterMesmoIdAoEditarUsuario() {
        // Arrange
        String usuarioId = usuario.getId();

        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId(usuarioId);

        UsuarioRequest request = new UsuarioRequest(
                "João Silva",
                "Teste@123",
                "joao@email.com",
                "12345678900",
                LocalDate.of(1990, 1, 1),
                getRoleUser()
        );

        Usuario usuarioMapeado = new Usuario();
        usuarioMapeado.setNome(request.nome());
        usuarioMapeado.setSenha(request.senha());

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuarioExistente));
        when(usuarioMapper.mapearParaUsuario(request)).thenReturn(usuarioMapeado);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaEncriptada");
        when(usuarioRepository.save(any())).thenAnswer(invocation -> {
            Usuario usuarioParaSalvar = invocation.getArgument(0);
            assertThat(usuarioParaSalvar.getId()).isEqualTo(usuarioId);
            return usuarioParaSalvar;
        });

        // Act
        usuarioService.editarUsuario(usuarioId, request);

        // Assert
        verify(usuarioRepository).save(argThat(usuario ->
                usuarioId.equals(usuario.getId())
        ));
    }

    @Test
    @DisplayName("Deve anonimizar dados do usuário com sucesso")
    void deveAnonimizarDadosDoUsuarioComSucesso() {
        // Arrange
        String usuarioId = usuario.getId();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.removerUsuario(usuarioId);

        // Assert
        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository).save(argThat(usuario -> {
            return usuario.getId().equals(usuarioId) &&
                    usuario.getEmail() == null &&
                    usuario.getCpf() == null &&
                    usuario.getDataNascimento() == null &&
                    usuario.getRole() == null &&
                    usuario.getSenha() == null &&
                    usuario.getNome() != null; // Nome não deve ser anonimizado
        }));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar remover usuário inexistente")
    void deveLancarExcecaoAoRemoverUsuarioInexistente() {
        // Arrange
        String usuarioId = usuario.getId();
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> usuarioService.removerUsuario(usuarioId))
                .isInstanceOf(UsuarioNaoEncontradoException.class)
                .hasMessage("Usuário não encontrado");

        verify(usuarioRepository).findById(usuarioId);
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve manter o ID do usuário após anonimização")
    void deveManterIdUsuarioAposAnonimizacao() {
        // Arrange
        String usuarioId = usuario.getId();

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        // Act
        usuarioService.removerUsuario(usuarioId);

        // Assert
        verify(usuarioRepository).save(argThat(usuario ->
                usuario.getId().equals(usuarioId)
        ));
    }

    private Role getRoleUser() {
        ERole eRole = ERole.ROLE_USER;
        Role role = new Role();
        role.setName(eRole);
        return role;
    }

}
