package br.com.fiap.fiapautenticacao.controller.api.doc;

import br.com.fiap.fiapautenticacao.dto.request.LoginRequest;
import br.com.fiap.fiapautenticacao.dto.response.ErrorResponse;
import br.com.fiap.fiapautenticacao.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Endpoints de Autenticação")
public interface AuthControllerDoc {

    @Operation(summary = "Autenticação")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação de Usuário.", content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "403", description = "Erro na autenticação.", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    ResponseEntity<?> login(LoginRequest request);

}
