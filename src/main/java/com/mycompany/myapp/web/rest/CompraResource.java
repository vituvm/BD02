package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CompraRepository;
import com.mycompany.myapp.service.CompraQueryService;
import com.mycompany.myapp.service.CompraService;
import com.mycompany.myapp.service.criteria.CompraCriteria;
import com.mycompany.myapp.service.dto.CompraDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Compra}.
 */
@RestController
@RequestMapping("/api")
public class CompraResource {

    private final Logger log = LoggerFactory.getLogger(CompraResource.class);

    private static final String ENTITY_NAME = "compra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompraService compraService;

    private final CompraRepository compraRepository;

    private final CompraQueryService compraQueryService;

    public CompraResource(CompraService compraService, CompraRepository compraRepository, CompraQueryService compraQueryService) {
        this.compraService = compraService;
        this.compraRepository = compraRepository;
        this.compraQueryService = compraQueryService;
    }

    /**
     * {@code POST  /compras} : Create a new compra.
     *
     * @param compraDTO the compraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new compraDTO, or with status {@code 400 (Bad Request)} if the compra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/compras")
    public ResponseEntity<CompraDTO> createCompra(@RequestBody CompraDTO compraDTO) throws URISyntaxException {
        log.debug("REST request to save Compra : {}", compraDTO);
        if (compraDTO.getId() != null) {
            throw new BadRequestAlertException("A new compra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompraDTO result = compraService.save(compraDTO);
        return ResponseEntity
            .created(new URI("/api/compras/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /compras/:id} : Updates an existing compra.
     *
     * @param id the id of the compraDTO to save.
     * @param compraDTO the compraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compraDTO,
     * or with status {@code 400 (Bad Request)} if the compraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the compraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/compras/{id}")
    public ResponseEntity<CompraDTO> updateCompra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompraDTO compraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Compra : {}, {}", id, compraDTO);
        if (compraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompraDTO result = compraService.update(compraDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compraDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /compras/:id} : Partial updates given fields of an existing compra, field will ignore if it is null
     *
     * @param id the id of the compraDTO to save.
     * @param compraDTO the compraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated compraDTO,
     * or with status {@code 400 (Bad Request)} if the compraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the compraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the compraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/compras/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompraDTO> partialUpdateCompra(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompraDTO compraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Compra partially : {}, {}", id, compraDTO);
        if (compraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, compraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!compraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompraDTO> result = compraService.partialUpdate(compraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, compraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /compras} : get all the compras.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of compras in body.
     */
    @GetMapping("/compras")
    public ResponseEntity<List<CompraDTO>> getAllCompras(
        CompraCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Compras by criteria: {}", criteria);
        Page<CompraDTO> page = compraQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /compras/count} : count all the compras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/compras/count")
    public ResponseEntity<Long> countCompras(CompraCriteria criteria) {
        log.debug("REST request to count Compras by criteria: {}", criteria);
        return ResponseEntity.ok().body(compraQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /compras/:id} : get the "id" compra.
     *
     * @param id the id of the compraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the compraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/compras/{id}")
    public ResponseEntity<CompraDTO> getCompra(@PathVariable Long id) {
        log.debug("REST request to get Compra : {}", id);
        Optional<CompraDTO> compraDTO = compraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(compraDTO);
    }

    /**
     * {@code DELETE  /compras/:id} : delete the "id" compra.
     *
     * @param id the id of the compraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/compras/{id}")
    public ResponseEntity<Void> deleteCompra(@PathVariable Long id) {
        log.debug("REST request to delete Compra : {}", id);
        compraService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
