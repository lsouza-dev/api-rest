package med.voll.api.domain.paciente;

import med.voll.api.domain.endereco.DadosEndereco;
import med.voll.api.domain.endereco.Endereco;

public record DadosDetalhamentoPaciente(Long id,
                                        String nome,
                                        String email,
                                        String telefone,
                                        String cpf,
                                        Endereco endereco) {
    public  DadosDetalhamentoPaciente(Paciente p){
        this(p.getId(),
                p.getNome(),
                p.getEmail(),
                p.getTelefone(),
                p.getCpf(),
                p.getEndereco());
    }
}
