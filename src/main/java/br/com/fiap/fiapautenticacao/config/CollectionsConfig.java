package br.com.fiap.fiapautenticacao.config;

import br.com.fiap.fiapautenticacao.model.Usuario;
import br.com.fiap.fiapautenticacao.model.role.ERole;
import br.com.fiap.fiapautenticacao.model.role.Role;
import br.com.fiap.fiapautenticacao.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@ConditionalOnClass(Mono.class)
public class CollectionsConfig {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RoleRepository roleRepository;

    @Bean
    Boolean createCollectionsIfDontExist() {
        Class<?>[] classes = {Role.class, Usuario.class};

        for (Class<?> c : classes) {
            String collectionName = this.mongoTemplate.getCollectionName(c);

            if (!this.mongoTemplate.collectionExists(collectionName)) {
                this.mongoTemplate.createCollection(collectionName);
            }
        }

        this.createRolesIfDontExist();

        return true;
    }

    private void createRolesIfDontExist() {
        List<ERole> eRoles = List.of(ERole.ROLE_ADMIN, ERole.ROLE_MODERATOR, ERole.ROLE_USER);
        for (ERole eRole : eRoles) {
            if (this.roleRepository.findByName(eRole).isEmpty()) {
                Role role = new Role();
                role.setName(eRole);
                this.roleRepository.save(role);
            }
        }
    }

}
