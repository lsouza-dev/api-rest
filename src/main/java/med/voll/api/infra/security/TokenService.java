package med.voll.api.infra.security;

// Importações necessárias para trabalhar com JWT (JSON Web Token)
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import med.voll.api.domain.usuario.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

// Anotação @Service para indicar que esta classe é um serviço do Spring
@Service
public class TokenService {

    // A anotação @Value carrega o valor da propriedade 'api.security.token.secret' do arquivo de configuração
    @Value("${api.security.token.secret}")
    private String secret;

    // Método para gerar um token JWT para um usuário
    public String gerarToken(Usuario usuario) {
        try {
            // Cria um algoritmo HMAC256 com a chave secreta
            var algoritmo = Algorithm.HMAC256(secret);

            // Cria o token JWT com o issuer, subject, data de expiração e assina com o algoritmo
            return JWT.create()
                    .withIssuer("API Voll.med")  // Emissor do token
                    .withSubject(usuario.getLogin())  // Sujeito do token (usuário)
                    .withExpiresAt(dataExpiracao())  // Data de expiração do token
                    .sign(algoritmo);  // Assina o token com o algoritmo
        } catch (JWTCreationException ex) {
            // Lança uma exceção em caso de erro na criação do token JWT
            throw new RuntimeException("Erro ao gerar token JWT", ex);
        }
    }

    // Método para obter o subject (usuário) do token JWT
    public String getSubject(String tokenJWT) {
        try {
            // Cria um algoritmo HMAC256 com a chave secreta
            var algoritmo = Algorithm.HMAC256(secret);
            // Verifica e valida o token JWT, retornando o subject
            return JWT.require(algoritmo)
                    .withIssuer("API Voll.med")  // Verifica o issuer do token
                    .build()
                    .verify(tokenJWT)  // Verifica o token JWT
                    .getSubject();  // Obtém o subject do token
        } catch (JWTVerificationException exception) {
            // Lança uma exceção em caso de erro na verificação do token JWT
            throw new RuntimeException("Token JWT inválido ou expirado!", exception);
        }
    }

    // Método privado para calcular a data de expiração do token
    private Instant dataExpiracao() {
        // Define a expiração para 2 horas a partir do horário atual
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
