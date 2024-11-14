package med.voll.api.infra.security;

// Importações necessárias para a configuração de segurança
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// Anotação para indicar que esta classe é uma classe de configuração do Spring
@Configuration
// Anotação para habilitar a segurança web no Spring Security
@EnableWebSecurity
public class SecurityConfigurations {

    // Definindo um Bean para a cadeia de filtros de segurança
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // Desabilitando a proteção CSRF (Cross-Site Request Forgery)
                .csrf(AbstractHttpConfigurer::disable)
                // Configurando a política de criação de sessão como STATELESS, ou seja, sem estado
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    // Definindo um Bean para o gerenciador de autenticação
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Retorna o gerenciador de autenticação do Spring
        return configuration.getAuthenticationManager();
    }

    // Definindo um Bean para o codificador de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        // Retorna uma instância do codificador de senhas BCrypt
        return new BCryptPasswordEncoder();
    }
}
