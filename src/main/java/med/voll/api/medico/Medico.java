package med.voll.api.medico;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.endereco.Endereco;

@Entity (name = "Medico")
@Table(name = "medico")
// Lombok methods
@Getter // Cria os getters de todos os atributos
@NoArgsConstructor // Cria o construtor padrão
@AllArgsConstructor // Cria o construtor com todos os atributos
@EqualsAndHashCode(of = "id")
public class Medico {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String crm;

    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    // Define que os dados da classe endereço pertencem ao médico
    @Embedded
    private Endereco endereco;

}
