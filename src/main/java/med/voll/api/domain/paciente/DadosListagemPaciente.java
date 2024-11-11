package med.voll.api.domain.paciente;

public record DadosListagemPaciente(String nome,
                                    String email,
                                    String cpf) {
    public DadosListagemPaciente(Paciente p ){
        this(p.getNome(),p.getEmail(),p.getCpf());
    }
}
