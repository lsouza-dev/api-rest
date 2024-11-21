package med.voll.api.domain.consulta;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.medico.Especialidade;

import javax.management.Query;
import java.time.LocalDateTime;

public record DadosDetalhamentoConsulta(
        Long id,
        Long idMedico,
        Long idPaciente,
        LocalDateTime data
) {

    public  DadosDetalhamentoConsulta(Consulta consulta){
        this(consulta.getId(),consulta.getMedico().getId(),consulta.getPaciente().getId(),consulta.getData());
    }
}
