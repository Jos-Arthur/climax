package com.agitex.climax.repositories;

import com.agitex.climax.entities.Client;
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
}
