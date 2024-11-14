package med.voll.api.infra.security;

// Importações necessárias para implementar o filtro de segurança
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Anotação @Component para indicar que esta classe é um componente gerenciado pelo Spring
@Component
public class SecurityFilter extends OncePerRequestFilter {

    // Override do método doFilterInternal para aplicar o filtro a cada requisição
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Passa a requisição e resposta para o próximo filtro na cadeia
        filterChain.doFilter(request, response);
    }
}
