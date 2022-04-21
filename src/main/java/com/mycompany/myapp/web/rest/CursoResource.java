package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.CursoRepository;
import com.mycompany.myapp.service.CursoQueryService;
import com.mycompany.myapp.service.CursoService;
import com.mycompany.myapp.service.criteria.CursoCriteria;
import com.mycompany.myapp.service.dto.CursoDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Curso}.
 */
@RestController
@RequestMapping("/api")
public class CursoResource {

    private final Logger log = LoggerFactory.getLogger(CursoResource.class);

    private static final String ENTITY_NAME = "curso";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CursoService cursoService;

    private final CursoRepository cursoRepository;

    private final CursoQueryService cursoQueryService;

    public CursoResource(CursoService cursoService, CursoRepository cursoRepository, CursoQueryService cursoQueryService) {
        this.cursoService = cursoService;
        this.cursoRepository = cursoRepository;
        this.cursoQueryService = cursoQueryService;
    }

    /**
     * {@code POST  /cursos} : Create a new curso.
     *
     * @param cursoDTO the cursoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cursoDTO, or with status {@code 400 (Bad Request)} if the curso has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cursos")
    public ResponseEntity<CursoDTO> createCurso(@Valid @RequestBody CursoDTO cursoDTO) throws URISyntaxException {
        log.debug("REST request to save Curso : {}", cursoDTO);
        if (cursoDTO.getId() != null) {
            throw new BadRequestAlertException("A new curso cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CursoDTO result = cursoService.save(cursoDTO);
        return ResponseEntity
            .created(new URI("/api/cursos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cursos/:id} : Updates an existing curso.
     *
     * @param id the id of the cursoDTO to save.
     * @param cursoDTO the cursoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cursoDTO,
     * or with status {@code 400 (Bad Request)} if the cursoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cursoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cursos/{id}")
    public ResponseEntity<CursoDTO> updateCurso(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CursoDTO cursoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Curso : {}, {}", id, cursoDTO);
        if (cursoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cursoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cursoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CursoDTO result = cursoService.update(cursoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cursoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cursos/:id} : Partial updates given fields of an existing curso, field will ignore if it is null
     *
     * @param id the id of the cursoDTO to save.
     * @param cursoDTO the cursoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cursoDTO,
     * or with status {@code 400 (Bad Request)} if the cursoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cursoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cursoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cursos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CursoDTO> partialUpdateCurso(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CursoDTO cursoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Curso partially : {}, {}", id, cursoDTO);
        if (cursoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cursoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cursoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CursoDTO> result = cursoService.partialUpdate(cursoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cursoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cursos} : get all the cursos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cursos in body.
     */
    @GetMapping("/cursos")
    public ResponseEntity<List<CursoDTO>> getAllCursos(
        CursoCriteria criteria,
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get Cursos by criteria: {}", criteria);
        Page<CursoDTO> page = cursoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cursos/count} : count all the cursos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/cursos/count")
    public ResponseEntity<Long> countCursos(CursoCriteria criteria) {
        log.debug("REST request to count Cursos by criteria: {}", criteria);
        return ResponseEntity.ok().body(cursoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cursos/:id} : get the "id" curso.
     *
     * @param id the id of the cursoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cursoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cursos/{id}")
    public ResponseEntity<CursoDTO> getCurso(@PathVariable Long id) {
        log.debug("REST request to get Curso : {}", id);
        Optional<CursoDTO> cursoDTO = cursoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cursoDTO);
    }

    /**
     * {@code DELETE  /cursos/:id} : delete the "id" curso.
     *
     * @param id the id of the cursoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cursos/{id}")
    public ResponseEntity<Void> deleteCurso(@PathVariable Long id) {
        log.debug("REST request to delete Curso : {}", id);
        cursoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
