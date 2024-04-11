/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.agitex.climax.mapper;

import java.util.List;

/**
 * @param <D>
 * @param <E>
 * @author :  <A HREF="mailto:josearthur22.oued@gmail.com">José Arthur OUEDRAOGO </A>
 * @version : 1.0
 * @since : 2024/04/11 à 01:47
 */
public interface EntityMapper<D, E> {

    /**
     * @param dto
     * @return Entity
     */
    E toEntity(D dto);

    /**
     * @param entity
     * @return DTO
     */
    D toDto(E entity);

    /**
     * @param dtoList
     * @return list of Entity
     */
    List<E> toEntity(List<D> dtoList);

    /**
     * @param entityList
     * @return List of DTO
     */
    List<D> toDto(List<E> entityList);
}
