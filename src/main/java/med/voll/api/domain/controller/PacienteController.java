package med.voll.api.domain.controller;


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

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping()
    @Transactional
    public ResponseEntity cadastrarPaciente(@RequestBody @Valid DadosCadastroPaciente dados, UriComponentsBuilder uriBuilder){
        Paciente paciente = new Paciente(dados);
        repository.save(paciente);
        var uri = uriBuilder.path("/pacientes/{id}").buildAndExpand(paciente.getId()).toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoPaciente(paciente));
    }

    @GetMapping()
    public ResponseEntity<Page<DadosListagemPaciente>> listarPacientes(@PageableDefault(size = 10,sort = "nome") Pageable pageable){
        var page =  repository.findAllByAtivoTrue(pageable).map(DadosListagemPaciente::new);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deletarPaciente(@PathVariable Long id){
        var paciente = repository.getReferenceById(id);
        paciente.apagarPaciente();

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity descreverPaciente(@PathVariable Long id){
        var paciente = repository.getReferenceById(id);
        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity atualizarPaciente(@RequestBody @Valid DadosAtualizacaoPaciente dados){
        Paciente paciente = repository.getReferenceById(dados.id());
        paciente.atualizarDados(dados);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }
}
