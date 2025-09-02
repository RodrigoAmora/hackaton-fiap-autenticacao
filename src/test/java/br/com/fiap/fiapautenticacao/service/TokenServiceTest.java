package br.com.fiap.fiapautenticacao.service;

import br.com.fiap.fiapautenticacao.model.Usuario;
import br.com.fiap.fiapautenticacao.model.role.ERole;
import br.com.fiap.fiapautenticacao.model.role.Role;
import br.com.fiap.fiapautenticacao.repository.UsuarioRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private Authentication authentication;

    private static final String SECRET_KEY = "chave-secreta-teste";
    private static final String TEST_EMAIL = "teste@teste.com";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(tokenService, "secretKey", SECRET_KEY);
    }

    @Test
    @DisplayName("Deve gerar token JWT com role USER com sucesso")
    void deveGerarTokenComRoleUserComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        usuario.setRole(role);

        User userDetails = new User(TEST_EMAIL, "senha",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(usuario));

        // Act
        String token = tokenService.generateToken(authentication);

        // Assert
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        assertThat(token).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(TEST_EMAIL);
        assertThat(claims.get("roles", List.class)).contains("ROLE_USER");
    }

    @Test
    @DisplayName("Deve gerar token JWT com role ADMIN com sucesso")
    void deveGerarTokenComRoleAdminComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        Role role = new Role();
        role.setName(ERole.ROLE_ADMIN);
        usuario.setRole(role);

        User userDetails = new User(TEST_EMAIL, "senha",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(usuario));

        // Act
        String token = tokenService.generateToken(authentication);

        // Assert
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        assertThat(token).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(TEST_EMAIL);
        assertThat(claims.get("roles", List.class)).contains("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Deve gerar token JWT com role MODERATOR com sucesso")
    void deveGerarTokenComRoleModeratorComSucesso() {
        // Arrange
        Usuario usuario = new Usuario();
        Role role = new Role();
        role.setName(ERole.ROLE_MODERATOR);
        usuario.setRole(role);

        User userDetails = new User(TEST_EMAIL, "senha",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_MODERATOR")));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(usuario));

        // Act
        String token = tokenService.generateToken(authentication);

        // Assert
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        assertThat(token).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(TEST_EMAIL);
        assertThat(claims.get("roles", List.class)).contains("ROLE_MODERATOR");
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado")
    void deveLancarExcecaoQuandoUsuarioNaoForEncontrado() {
        // Arrange
        User userDetails = new User(TEST_EMAIL, "senha",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.empty());

        // Assert
        assertThatThrownBy(() -> tokenService.generateToken(authentication))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    @DisplayName("Deve verificar se o token gerado contém data de expiração")
    void deveVerificarSeTokenContemDataExpiracao() {
        // Arrange
        Usuario usuario = new Usuario();
        Role role = new Role();
        role.setName(ERole.ROLE_USER);
        usuario.setRole(role);

        User userDetails = new User(TEST_EMAIL, "senha",
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(usuarioRepository.findByEmail(TEST_EMAIL)).thenReturn(Optional.of(usuario));

        // Act
        String token = tokenService.generateToken(authentication);

        // Assert
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getExpiration()).isNotNull();
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration().after(claims.getIssuedAt())).isTrue();
    }
}
