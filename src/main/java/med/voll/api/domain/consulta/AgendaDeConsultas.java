package med.voll.api.domain.consulta;

import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.consulta.validacoes.ValidadorAgendamentoDeConsulta;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Service
public class AgendaDeConsultas {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private List<ValidadorAgendamentoDeConsulta> validadores;


    public DadosAgendamentoConsulta agendar(DadosAgendamentoConsulta dados){

        if(!pacienteRepository.existsById(dados.idPaciente()))throw new ValidacaoException("Id do paciente informado não existe");
        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico()))
            throw new ValidacaoException("Id do médico informado não existe");

        validadores.forEach(v -> v.validar(dados));

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());
        var medico = escolherMedico(dados);
        if(medico == null) throw new ValidacaoException("Não existe médico disponível nessa data.");


        var consulta = new Consulta(null,medico,paciente,dados.data());
        consultaRepository.save(consulta);

        return new DadosAgendamentoConsulta(consulta);
    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {
        if(dados.idMedico() != null) return  medicoRepository.getReferenceById(dados.idMedico());

        if(dados.especialidade() == null) throw new ValidacaoException("Especialidade é obrigatória quando o médico não é escolhido.");

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(),dados.data());
    }

    public void cancelar(DadosCancelamentoDeConsulta dados){
        if(!consultaRepository.existsById(dados.idConsulta())) throw new ValidacaoException("Não foi possível encontrar a consulta.");
        var consulta = consultaRepository.getReferenceById(dados.idConsulta());
        var diaConsulta = consulta.getData();
        var diaAtual = LocalDateTime.now();

        Duration diferenca = Duration.between(diaAtual,diaConsulta);
        if(diferenca.toHours() >= 24) System.out.println("Cancelando o agendamento...");
        else throw new RuntimeException("Não foi possível cancelar a consulta, pois há menos de 24 horas de diferença");

    }

}
