package br.com.fiap.fiapautenticacao.repository;

import br.com.fiap.fiapautenticacao.model.role.ERole;
import br.com.fiap.fiapautenticacao.model.role.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

    Optional<Role> findByName(ERole name);

}
