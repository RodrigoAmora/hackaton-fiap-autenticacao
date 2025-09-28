package br.com.fiap.fiapautenticacao.controller.api;

import br.com.fiap.fiapautenticacao.controller.api.doc.UsuarioControllerDoc;
import br.com.fiap.fiapautenticacao.dto.UsuarioDTO;
import br.com.fiap.fiapautenticacao.dto.request.UsuarioRequest;
import br.com.fiap.fiapautenticacao.service.UsuarioService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuario")
public class UsuarioController implements UsuarioControllerDoc {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    @PostMapping
    public ResponseEntity<UsuarioDTO> create(@RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.salvarUsuario(request));
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> edit(@PathVariable(name = "id") String id, @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.editarUsuario(id, request));
    }

    @Override
    @DeleteMapping
    public HttpStatus remover(@PathVariable(name = "id") String id) {
        usuarioService.removerUsuario(id);
        return HttpStatus.NO_CONTENT;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> buscarUsuarioPeloId(@PathVariable(name = "id") String id) {
        return ResponseEntity.ok(usuarioService.buscarUsuarioPeloId(id));
    }

    @Override
    @GetMapping("/me")
    public UsuarioDTO getUsuarioLogado(Authentication authentication) {
        return usuarioService.buscarUsuarioPorEmail(authentication.getName());
    }

    @Override
    @GetMapping
    @PreAuthorize("hasRole('MODERATOR') || hasRole('ADMIN')")
    public ResponseEntity<Page<UsuarioDTO>> buscarUsuarios(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                           @RequestParam(value = "size", required = false, defaultValue = "20") int size) {
        return ResponseEntity.ok(usuarioService.buscarUsuarios(page, size));
    }

}
