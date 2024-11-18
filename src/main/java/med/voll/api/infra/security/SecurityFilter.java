package med.voll.api.infra.security;

// Importações necessárias para implementar o filtro de segurança
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.usuario.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Anotação @Component para indicar que esta classe é um componente gerenciado pelo Spring
@Component
public class SecurityFilter extends OncePerRequestFilter {

    // Injeção de dependência para o serviço de token
    @Autowired
    private TokenService tokenService;

    // Injeção de dependência para o repositório de usuários
    @Autowired
    private UsuarioRepository repository;

    // Override do método doFilterInternal para aplicar o filtro a cada requisição
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Recupera o token JWT da requisição
        var tokenJWT = recuperarToken(request);
        if (tokenJWT != null) {
            // Extrai o subject (usuário) do token
            var subject = tokenService.getSubject(tokenJWT);
            // Busca o usuário no repositório pelo login
            var usuario = repository.findByLogin(subject);

            // Cria um objeto de autenticação com as informações do usuário
            var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
            // Define o contexto de segurança com a autenticação criada
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Passa a requisição e resposta para o próximo filtro na cadeia
        filterChain.doFilter(request, response);
    }

    // Método para recuperar o token JWT da requisição
    private String recuperarToken(HttpServletRequest request) {
        // Obtém o cabeçalho de autorização da requisição
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            // Remove o prefixo "Bearer" do token e retorna o token limpo
            return authorizationHeader.replace("Bearer", "").trim();
        }
        return null;
    }
}
