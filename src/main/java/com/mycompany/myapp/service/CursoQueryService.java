package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Curso;
import com.mycompany.myapp.repository.CursoRepository;
import com.mycompany.myapp.service.criteria.CursoCriteria;
import com.mycompany.myapp.service.dto.CursoDTO;
import com.mycompany.myapp.service.mapper.CursoMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Curso} entities in the database.
 * The main input is a {@link CursoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CursoDTO} or a {@link Page} of {@link CursoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CursoQueryService extends QueryService<Curso> {

    private final Logger log = LoggerFactory.getLogger(CursoQueryService.class);

    private final CursoRepository cursoRepository;

    private final CursoMapper cursoMapper;

    public CursoQueryService(CursoRepository cursoRepository, CursoMapper cursoMapper) {
        this.cursoRepository = cursoRepository;
        this.cursoMapper = cursoMapper;
    }

    /**
     * Return a {@link List} of {@link CursoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CursoDTO> findByCriteria(CursoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Curso> specification = createSpecification(criteria);
        return cursoMapper.toDto(cursoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CursoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CursoDTO> findByCriteria(CursoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Curso> specification = createSpecification(criteria);
        return cursoRepository.findAll(specification, page).map(cursoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CursoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Curso> specification = createSpecification(criteria);
        return cursoRepository.count(specification);
    }

    /**
     * Function to convert {@link CursoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Curso> createSpecification(CursoCriteria criteria) {
        Specification<Curso> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Curso_.id));
            }
            if (criteria.getTitulo() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTitulo(), Curso_.titulo));
            }
            if (criteria.getDuracaoCH() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDuracaoCH(), Curso_.duracaoCH));
            }
            if (criteria.getDescricao() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescricao(), Curso_.descricao));
            }
            if (criteria.getValor() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValor(), Curso_.valor));
            }
            if (criteria.getCriacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCriacao(), Curso_.criacao));
            }
            if (criteria.getProfessorId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getProfessorId(), root -> root.join(Curso_.professor, JoinType.LEFT).get(Usuario_.id))
                    );
            }
            if (criteria.getAlunoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAlunoId(), root -> root.join(Curso_.aluno, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
