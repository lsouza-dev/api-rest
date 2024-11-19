package med.voll.api.domain.consulta;

import jakarta.validation.constraints.NotNull;

public record DadosCancelamentoDeConsulta(
        Long idConsulta,
        @NotNull MotivoCancelamento motivoCancelamento,
        String justificativa
) {
}
