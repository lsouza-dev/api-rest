package med.voll.api.domain.controller;

// Importações necessárias para a configuração do controller
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.domain.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.SQLOutput;

// Anotação para indicar que esta classe é um controlador REST
@RestController
// Define a rota base para este controlador
@RequestMapping("/pacientes")
public class PacienteController {

    // Injeção de dependência do repositório de pacientes
    @Autowired
    private PacienteRepository repository;

    // Método para cadastrar um novo paciente
    @PostMapping()
    @Transactional
    public ResponseEntity cadastrarPaciente(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder) {
        // Cria um novo objeto Paciente com os dados fornecidos
        Paciente paciente = new Paciente(dados);
        // Salva o novo paciente no repositório
        repository.save(paciente);
        // Constrói a URI para o novo recurso criado
        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        // Retorna uma resposta HTTP 201 (Created) com a URI do novo recurso e os detalhes do paciente
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
    }

    // Método para listar os pacientes
    @GetMapping()
    public ResponseEntity<Page<DadosListagemPaciente>> listarPacientes(@PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        // Obtém uma página de pacientes ativos, mapeada para DadosListagemPaciente
        var page = repository.findAllByAtivoTrue(pageable).map(DadosListagemPaciente::new);
        // Retorna uma resposta HTTP 200 (OK) com a página de pacientes
        return ResponseEntity.ok(page);
    }

    // Método para deletar um paciente
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarPaciente(@PathVariable Long id) {
        // Obtém a referência do paciente pelo ID
        var paciente = repository.getReferenceById(id);
        // Marca o paciente como apagado
        paciente.apagarPaciente();
        // Retorna uma resposta HTTP 204 (No Content) indicando que a operação foi bem-sucedida
        return ResponseEntity.noContent().build();
    }

    // Método para obter detalhes de um paciente específico
    @GetMapping("/{id}")
    public ResponseEntity descreverPaciente(@PathVariable Long id) {
        // Obtém a referência do paciente pelo ID
        var paciente = repository.getReferenceById(id);
        // Retorna uma resposta HTTP 200 (OK) com os detalhes do paciente
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    // Método para atualizar os dados de um paciente
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarPaciente(@RequestBody @Valid DadosAtualizacaoPaciente dados) {
        // Obtém a referência do paciente pelo ID
        Paciente paciente = repository.getReferenceById(dados.id());
        // Atualiza os dados do paciente
        paciente.atualizarDados(dados);
        // Retorna uma resposta HTTP 200 (OK) com os detalhes atualizados do paciente
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }
}