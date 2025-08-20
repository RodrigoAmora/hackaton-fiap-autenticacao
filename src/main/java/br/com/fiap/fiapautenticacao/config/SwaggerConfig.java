package br.com.fiap.fiapautenticacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    OpenAPI custonOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Autenticação")
                        .description("Documentação da API")
                        .version("1.0"));
    }

}
