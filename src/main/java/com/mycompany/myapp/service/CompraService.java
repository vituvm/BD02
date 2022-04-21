package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CompraDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Compra}.
 */
public interface CompraService {
    /**
     * Save a compra.
     *
     * @param compraDTO the entity to save.
     * @return the persisted entity.
     */
    CompraDTO save(CompraDTO compraDTO);

    /**
     * Updates a compra.
     *
     * @param compraDTO the entity to update.
     * @return the persisted entity.
     */
    CompraDTO update(CompraDTO compraDTO);

    /**
     * Partially updates a compra.
     *
     * @param compraDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CompraDTO> partialUpdate(CompraDTO compraDTO);

    /**
     * Get all the compras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompraDTO> findAll(Pageable pageable);

    /**
     * Get all the compras with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CompraDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" compra.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CompraDTO> findOne(Long id);

    /**
     * Delete the "id" compra.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
