package med.voll.api.medico;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico,Long> {

    @Query("SELECT m FROM Medico m ORDER BY m.nome LIMIT 10")
    List<Medico> listarMedicos();

    Page<Medico> findAllByAtivoTrue(Pageable paginacao);
}
