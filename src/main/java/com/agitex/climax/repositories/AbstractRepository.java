package com.agitex.climax.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import java.util.Optional;

/**
 * @param <ID>
 * @param <T>
 * @author :  <A HREF="mailto:josearthur22.oued@gmail.com">José Arthur OUEDRAOGO </A>
 * @version : 1.0
 * @since : 2024/04/11 à 01:47
 */
@NoRepositoryBean
public interface AbstractRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * Récupération de la liste des éléments non supprimer.
     *
     * @param pageable
     * @return la liste obtenue
     */
    Page<T> findAllByDeletedFalse(Pageable pageable);

    /**
     * Récupération de la liste des éléments non supprimer.
     *
     * @return la liste obtenue
     */
    List<T> findAllByDeletedFalse();


    /**
     * Récupération d'un élément selon son ID.
     *
     * @param id
     * @return l'élément recherché
     */
    Optional<T> findByIdAndDeletedFalse(ID id);
}
