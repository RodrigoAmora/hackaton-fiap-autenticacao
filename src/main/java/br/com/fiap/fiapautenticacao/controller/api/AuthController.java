package br.com.fiap.fiapautenticacao.controller.api;

import br.com.fiap.fiapautenticacao.controller.api.doc.AuthControllerDoc;
import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.LoginRequest;
import br.com.fiap.fiapautenticacao.dto.response.ErrorResponse;
import br.com.fiap.fiapautenticacao.dto.response.LoginResponse;
import br.com.fiap.fiapautenticacao.service.TokenService;
import br.com.fiap.fiapautenticacao.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthControllerDoc {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private TokenService tokenService;


    @Autowired
    private UsuarioService usuarioService;

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken loginData = new UsernamePasswordAuthenticationToken(
                    request.email(),
                    request.senha()
            );

            Authentication authentication = authManager.authenticate(loginData);
            String token = tokenService.generateToken(authentication);
            User user = (User) authentication.getPrincipal();
            UsuarioDTO usuario = usuarioService.buscarUsuarioPorEmail(request.email());

            LoginResponse loginResponse = new LoginResponse(token, "Bearer", user.getUsername(), usuario.id(), usuario.role());
            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),"Credenciais inválidas", LocalDateTime.now()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),"Erro ao realizar login", LocalDateTime.now()));
        }
    }

}
