package com.agitex.climax.repositories;

import com.agitex.climax.entities.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClientRepository extends AbstractRepository<Client, Long> {
    /**
     * List of clients.
     *
     * @return {@link List <Client>}
     */
    List<Client> findAllByDeletedFalse();

    /**
     * Average of salary by client profession.
     *
     * @return {@link List <Client>}
     */
    @Query("SELECT c.profession, AVG(c.salaire) FROM Client c GROUP BY c.profession")
    List<Object[]> findAverageSalaryByProfession();
}
