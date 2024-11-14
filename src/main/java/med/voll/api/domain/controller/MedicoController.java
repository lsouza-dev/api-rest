package med.voll.api.domain.controller;

// Importações necessárias para a configuração do controller
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

// Anotação para indicar que esta classe é um controlador REST
@RestController
// Define a rota base para este controlador
@RequestMapping("/medicos")
public class MedicoController {

    // Injeção de dependência do repositório de médicos
    @Autowired
    private MedicoRepository repository;

    // Método para cadastrar um novo médico
    @PostMapping()
    @Transactional
    public ResponseEntity cadastrarMedico(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder){
        // Cria um novo objeto Medico com os dados fornecidos
        var medico = new Medico(dados);
        // Salva o novo médico no repositório
        repository.save(medico);

        // Constrói a URI para o novo recurso criado
        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        // Retorna uma resposta HTTP 201 (Created) com a URI do novo recurso e os detalhes do médico
        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

    // Método para listar os médicos
    @GetMapping()
    public ResponseEntity<Page<DadosListagemMedico>> listarMedicos(@PageableDefault(size = 10, sort = {"nome"}) Pageable paginacao){
        // Obtém uma página de médicos ativos, mapeada para DadosListagemMedico
        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
        // Retorna uma resposta HTTP 200 (OK) com a página de médicos
        return ResponseEntity.ok(page);
    }

    // Método para atualizar os dados de um médico
    @PutMapping()
    @Transactional
    public ResponseEntity atualizarMedico(@RequestBody @Valid DadosAtualizacaoMedico dados){
        // Obtém a referência do médico pelo ID
        Medico medico = repository.getReferenceById(dados.id());
        // Atualiza os dados do médico
        medico.atualizarDados(dados);

        // Retorna uma resposta HTTP 200 (OK) com os detalhes atualizados do médico
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    // Método para deletar um médico
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarMedico(@PathVariable Long id){
        // Obtém a referência do médico pelo ID
        Medico medico = repository.getReferenceById(id);
        // Marca o médico como excluído
        medico.excluir();

        // Retorna uma resposta HTTP 204 (No Content) indicando que a operação foi bem-sucedida
        return ResponseEntity.noContent().build();
    }

    // Método para obter detalhes de um médico específico
    @GetMapping("/{id}")
    public ResponseEntity detalharMedico(@PathVariable Long id){
        // Obtém a referência do médico pelo ID
        var medico = repository.getReferenceById(id);
        // Retorna uma resposta HTTP 200 (OK) com os detalhes do médico
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }
}
