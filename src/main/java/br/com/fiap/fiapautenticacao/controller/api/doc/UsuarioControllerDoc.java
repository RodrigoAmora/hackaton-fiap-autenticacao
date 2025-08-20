package br.com.fiap.fiapautenticacao.controller.api.doc;

import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.UsuarioRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Endpoints de Usuário")
public interface UsuarioControllerDoc {

    @Operation(summary = "Cadastro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro de Usuário.", content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
    })
    ResponseEntity<?> create(UsuarioRequest request);

}
