package br.com.fiap.fiapautenticacao.validator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class PasswordValidatorTest {

    @Nested
    @DisplayName("Testes de validação de senha")
    class ValidatePasswordTests {

        @Test
        @DisplayName("Deve validar senha com todos os requisitos")
        void deveValidarSenhaValida() {
            // Arrange
            String senha = "Teste@123";

            // Act & Assert
            assertTrue(PasswordValidator.validatePassword(senha));
        }

        @Test
        @DisplayName("Deve rejeitar senha vazia")
        void deveRejeitarSenhaVazia() {
            // Arrange
            String senha = "";

            // Act & Assert
            assertFalse(PasswordValidator.validatePassword(senha));
        }

        @Test
        @DisplayName("Deve lançar NullPointerException quando a senha for nula")
        void deveLancarExcecaoQuandoSenhaNula() {
            assertThrows(NullPointerException.class, () -> {
                PasswordValidator.validatePassword(null);
            });
        }

        @ParameterizedTest
        @DisplayName("Deve rejeitar senhas sem letra maiúscula")
        @ValueSource(strings = {
                "teste@123",
                "minuscula@123",
                "apenas@minuscula123"
        })
        void deveRejeitarSenhaSemLetraMaiuscula(String senha) {
            assertFalse(PasswordValidator.validatePassword(senha));
        }

        @ParameterizedTest
        @DisplayName("Deve rejeitar senhas sem letra minúscula")
        @ValueSource(strings = {
                "TESTE@123",
                "MAIUSCULA@123",
                "APENAS@MAIUSCULA123"
        })
        void deveRejeitarSenhaSemLetraMinuscula(String senha) {
            assertFalse(PasswordValidator.validatePassword(senha));
        }

        @ParameterizedTest
        @DisplayName("Deve rejeitar senhas sem caracteres especiais")
        @ValueSource(strings = {
                "Teste123",
                "SemCaracterEspecial123",
                "ApenasLetrasENumeros123"
        })
        void deveRejeitarSenhaSemCaracterEspecial(String senha) {
            assertFalse(PasswordValidator.validatePassword(senha));
        }

        @ParameterizedTest
        @DisplayName("Deve rejeitar senhas sem números")
        @ValueSource(strings = {
                "Teste@abc",
                "SemNumero@",
                "ApenasLetras@Especiais"
        })
        void deveRejeitarSenhaSemNumero(String senha) {
            assertFalse(PasswordValidator.validatePassword(senha));
        }

        @ParameterizedTest
        @DisplayName("Deve rejeitar senhas muito curtas (menos de 8 caracteres)")
        @ValueSource(strings = {
                "Te@123",
                "Ab@12",
                "Cx@1"
        })
        void deveRejeitarSenhaMuitoCurta(String senha) {
            assertFalse(PasswordValidator.validatePassword(senha));
        }

        @ParameterizedTest
        @DisplayName("Deve rejeitar senhas muito longas (mais de 30 caracteres)")
        @ValueSource(strings = {
                "Teste@123456789012345678901234567890",
                "MuitoLonga@123456789012345678901234567890",
                "SuperLonga@123456789012345678901234567890"
        })
        void deveRejeitarSenhaMuitoLonga(String senha) {
            assertFalse(PasswordValidator.validatePassword(senha));
        }
    }

    @Nested
    @DisplayName("Testes de senhas válidas")
    class SenhasValidasTests {

        @ParameterizedTest
        @DisplayName("Deve aceitar senhas válidas com diferentes combinações")
        @ValueSource(strings = {
                "Teste@123",
                "Senha#2023",
                "Fiap@1234",
                "Complexa$123",
                "Validador!456"
        })
        void deveAceitarSenhasValidas(String senha) {
            assertTrue(PasswordValidator.validatePassword(senha));
        }
    }

    @Nested
    @DisplayName("Testes de caracteres especiais")
    class CaracteresEspeciaisTests {

        @ParameterizedTest
        @DisplayName("Deve aceitar diferentes caracteres especiais")
        @ValueSource(strings = {
                "Teste@123",
                "Teste#123",
                "Teste$123",
                "Teste!123",
                "Teste*123",
                "Teste&123"
        })
        void deveAceitarDiferentesCaracteresEspeciais(String senha) {
            assertTrue(PasswordValidator.validatePassword(senha));
        }
    }

    @Nested
    @DisplayName("Testes de limite de tamanho")
    class LimiteTamanhoTests {

        @Test
        @DisplayName("Deve aceitar senha com tamanho mínimo (8 caracteres)")
        void deveAceitarSenhaTamanhoMinimo() {
            assertTrue(PasswordValidator.validatePassword("Teste@12"));
        }

        @Test
        @DisplayName("Deve aceitar senha com tamanho máximo (30 caracteres)")
        void deveAceitarSenhaTamanhoMaximo() {
            // Arrange
            String senha = "Teste@123456789012345678901234";

            // Act & Assert
            assertTrue(PasswordValidator.validatePassword(senha));
        }
    }
}
