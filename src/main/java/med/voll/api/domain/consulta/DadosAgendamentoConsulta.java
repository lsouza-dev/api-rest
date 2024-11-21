package med.voll.api.domain.consulta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.medico.Especialidade;

import java.time.LocalDateTime;

public record DadosAgendamentoConsulta(
        Long id,
        Long idMedico,
        @NotNull Long idPaciente,
        @NotNull @Future LocalDateTime data,
        @NotNull Especialidade especialidade

) {
    public DadosAgendamentoConsulta(Consulta consulta) {
        this(consulta.getId(),consulta.getMedico().getId(),consulta.getPaciente().getId(),consulta.getData(),consulta.getMedico().getEspecialidade());
    }
}
