package med.voll.api.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.endereco.DadosEndereco;

// @NotBlank = Define que o campo não pode ser nulo nem vazio
// @Email = Define que o campo precisa ter alguns caracteres especificos de email
// @Patern(regex = "...") = Define uma regra específica através de um regex (\\d{4,6}) - Números de 4 a 6 digitos
// @Valid define que o Jakarta precisa realizar uma validação o objeto definido no parâmetro
public record DadosCadastroMedico(@NotBlank String nome,
                                  @NotBlank @Email String email,
                                  @NotBlank String telefone,
                                  @NotBlank @Pattern(regexp = "\\d{4,6}") String crm,
                                  @NotNull Especialidade especialidade,
                                  @NotNull @Valid DadosEndereco endereco) {
}
