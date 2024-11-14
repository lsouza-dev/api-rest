package med.voll.api.infra.exception;

// Importações necessárias para tratar exceções e retornar respostas HTTP
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Anotação @RestControllerAdvice para indicar que esta classe fornece conselhos globais para controladores REST
@RestControllerAdvice
public class TratadorDeErros {

    // Método para tratar exceções EntityNotFoundException (erro 404 - Não Encontrado)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarErro404(){
        // Retorna uma resposta HTTP 404 (Não Encontrado)
        return ResponseEntity.notFound().build();
    }

    // Método para tratar exceções MethodArgumentNotValidException (erro 400 - Solicitação Inválida)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarErro400(MethodArgumentNotValidException ex){
        // Obtém a lista de erros de campo da exceção
        var erros = ex.getFieldErrors();
        // Retorna uma resposta HTTP 400 (Solicitação Inválida) com os erros mapeados para DadosErroValidacao
        return ResponseEntity.badRequest().body(erros.stream().map(DadosErroValidacao::new).toList());
    }

    // Classe interna para representar dados de erro de validação
    private record DadosErroValidacao(String campo, String mensagem) {
        // Construtor que inicializa os campos com base em um FieldError
        public DadosErroValidacao(FieldError fieldError) {
            this(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}
