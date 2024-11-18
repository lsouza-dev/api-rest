package med.voll.api.domain.controller;

// Importações necessárias para trabalhar com validação, autenticação e geração de tokens JWT
import jakarta.validation.Valid;
import med.voll.api.domain.usuario.DadosAutenticacao;
import med.voll.api.domain.usuario.Usuario;
import med.voll.api.infra.security.DadosTokenJWT;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Anotação @RestController para indicar que esta classe é um controlador REST gerenciado pelo Spring
@RestController
// Anotação @RequestMapping para mapear a classe para o endpoint "/login"
@RequestMapping("/login")
public class AutenticacaoController {

    // Injeção de dependência para o gerenciador de autenticação
    @Autowired
    private AuthenticationManager manager;

    // Injeção de dependência para o serviço de token
    @Autowired
    private TokenService tokenService;

    // Método para lidar com requisições POST no endpoint "/login"
    @PostMapping()
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        try {
            // Cria um objeto de autenticação com os dados fornecidos (login e senha)
            var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
            // Autentica o token de autenticação
            var authentication = manager.authenticate(authenticationToken);
            // Gera um token JWT para o usuário autenticado
            var tokenJWT = tokenService.gerarToken((Usuario) authentication.getPrincipal());

            // Retorna uma resposta OK (200) com o token JWT no corpo da resposta
            return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
        } catch (Exception e) {
            // Imprime a pilha de exceção no console
            e.printStackTrace();
            // Retorna uma resposta BAD REQUEST (400) com a mensagem de erro no corpo da resposta
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
