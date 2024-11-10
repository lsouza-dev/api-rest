package med.voll.api.medico;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.endereco.DadosEndereco;
import med.voll.api.endereco.Endereco;

@Entity (name = "Medico")
@Table(name = "medicos")
// Lombok methods
@Getter // Cria os getters de todos os atributos
@NoArgsConstructor // Cria o construtor padrão
@AllArgsConstructor // Cria o construtor com todos os atributos
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nome;
    private String email;
    private String telefone;
    private String crm;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    // Define que os dados da classe endereço pertencem ao médico
    @Embedded
    private Endereco endereco;

    private boolean ativo;

    public Medico(DadosCadastroMedico dados) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.telefone = dados.telefone();
        this.crm = dados.crm();
        this.especialidade = dados.especialidade();
        this.endereco = new Endereco(dados.endereco());
        this.ativo = true;
    }

    public void atualizarDados(@Valid DadosAtualizacaoMedico dados) {
        if(dados.nome() != null)this.nome = dados.nome();
        if(dados.telefone() != null)this.telefone = dados.telefone();
        if(dados.endereco() != null)this.endereco.atualizarEndereco(dados.endereco());
    }

    public void excluir() {
        this.ativo = false;
    }
}
