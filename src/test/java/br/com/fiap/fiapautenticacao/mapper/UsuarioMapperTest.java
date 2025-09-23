package br.com.fiap.fiapautenticacao.mapper;

import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.UsuarioRequest;
import br.com.fiap.fiapautenticacao.model.Usuario;
import br.com.fiap.fiapautenticacao.model.role.ERole;
import br.com.fiap.fiapautenticacao.model.role.Role;
import br.com.fiap.fiapautenticacao.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioMapperTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UsuarioMapper usuarioMapper;

    private Usuario usuario;

    @Nested
    @DisplayName("Testes para mapearParaUsuarioDTO")
    class MapearParaUsuarioDTOTests {

        @BeforeEach
        void init() {
            UUID id = UUID.randomUUID();
            LocalDateTime dataCadastro = LocalDateTime.now();
            LocalDate dataNascimento = LocalDate.of(1990, 1, 1);

            Role role = getRoleUser();

            usuario = new Usuario();
            usuario.setId(id.toString());
            usuario.setNome("João Silva");
            usuario.setEmail("joao@email.com");
            usuario.setCpf("12345678900");
            usuario.setDataNascimento(dataNascimento);
            usuario.setDataCadastro(dataCadastro);
            usuario.setRole(role);
        }

        @Test
        @DisplayName("Deve mapear Usuario para UsuarioDTO corretamente")
        void deveMapearUsuarioParaDTO() {
            // Arrange


            // Act
            UsuarioDTO dto = usuarioMapper.mapearParaUsuarioDTO(usuario);

            // Assert
            assertNotNull(dto);
            assertEquals(usuario.getId(), dto.id());
            assertEquals("João Silva", dto.nome());
            assertEquals("joao@email.com", dto.email());
            assertEquals("12345678900", dto.cpf());
            assertEquals(usuario.getDataNascimento(), dto.dataNascimento());
            assertEquals(usuario.getDataCadastro(), dto.dataCadastro());
            assertEquals(usuario.getRole(), dto.role());
        }
    }

    @Nested
    @DisplayName("Testes para mapearParaUsuario")
    class MapearParaUsuarioTests {

        @Test
        @DisplayName("Deve mapear UsuarioRequest para Usuario com role específica")
        void deveMapearRequestParaUsuarioComRole() {
            // Arrange
            Role userRole = getRoleUser();
            UsuarioRequest request = new UsuarioRequest(
                    "João Silva",
                    "senha123",
                    "joao@email.com",
                    "12345678900",
                    LocalDate.of(1990, 1, 1),
                    userRole
            );

            when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(userRole));

            // Act
            Usuario usuario = usuarioMapper.mapearParaUsuario(request);

            // Assert
            assertNotNull(usuario);
            assertEquals("João Silva", usuario.getNome());
            assertEquals("senha123", usuario.getSenha());
            assertEquals("joao@email.com", usuario.getEmail());
            assertEquals("12345678900", usuario.getCpf());
            assertEquals(LocalDate.of(1990, 1, 1), usuario.getDataNascimento());
            assertNotNull(usuario.getDataCadastro());
            assertEquals(userRole, usuario.getRole());
        }

        @Test
        @DisplayName("Deve mapear UsuarioRequest para Usuario com role padrão quando role é nula")
        void deveMapearRequestParaUsuarioComRolePadrao() {
            // Arrange
            Role defaultRole = getRoleUser();
            UsuarioRequest request = new UsuarioRequest(
                    "João Silva",
                    "senha123",
                    "joao@email.com",
                    "12345678900",
                    LocalDate.of(1990, 1, 1),
                    null
            );

            when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(defaultRole));

            // Act
            Usuario usuario = usuarioMapper.mapearParaUsuario(request);

            // Assert
            assertNotNull(usuario);
            assertEquals(defaultRole, usuario.getRole());
        }

        @Test
        @DisplayName("Deve lançar RuntimeException quando role padrão não é encontrada")
        void deveLancarExcecaoQuandoRolePadraoNaoEncontrada() {
            // Arrange
            UsuarioRequest request = new UsuarioRequest(
                    "João Silva",
                    "senha123",
                    "joao@email.com",
                    "12345678900",
                    LocalDate.of(1990, 1, 1),
                    null
            );

            when(roleRepository.findByName(any())).thenReturn(Optional.empty());

            // Assert
            assertThrows(RuntimeException.class, () -> {
                usuarioMapper.mapearParaUsuario(request);
            });
        }
    }

    private Role getRoleUser() {
        ERole eRole = ERole.ROLE_USER;
        Role role = new Role();
        role.setName(eRole);
        return role;
    }
}
