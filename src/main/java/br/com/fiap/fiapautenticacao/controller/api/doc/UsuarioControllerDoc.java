package br.com.fiap.fiapautenticacao.controller.api.doc;

import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.UsuarioRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

@Tag(name = "Endpoints de Usuário")
public interface UsuarioControllerDoc {

    @Operation(summary = "Cadastrar Usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cadastro de Usuário.", content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
    })
    ResponseEntity<?> create(UsuarioRequest request);

    @Operation(summary = "Editar Usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Edição das infomações de Usuário.", content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
    })
    ResponseEntity<UsuarioDTO> edit(String id, UsuarioRequest request);

    @Operation(summary = "Buscar Usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buscar Usuário pelo id.", content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
    })
    ResponseEntity<UsuarioDTO> buscarUsuarioPeloId(@PathVariable(name = "id") String id);

    @Operation(summary = "Recuperar Usuário Logado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Recuperar as informações do usuário logado.", content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
    })
    UsuarioDTO getUsuarioLogado(Authentication authentication);

    @Operation(summary = "Listar Usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listar todos os Usuários.", content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
    })
    ResponseEntity<Page<UsuarioDTO>> buscarUsuarios(int page, int size);

}
