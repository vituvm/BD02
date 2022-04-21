package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Compra;
import com.mycompany.myapp.repository.CompraRepository;
import com.mycompany.myapp.service.criteria.CompraCriteria;
import com.mycompany.myapp.service.dto.CompraDTO;
import com.mycompany.myapp.service.mapper.CompraMapper;
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
 * Service for executing complex queries for {@link Compra} entities in the database.
 * The main input is a {@link CompraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CompraDTO} or a {@link Page} of {@link CompraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CompraQueryService extends QueryService<Compra> {

    private final Logger log = LoggerFactory.getLogger(CompraQueryService.class);

    private final CompraRepository compraRepository;

    private final CompraMapper compraMapper;

    public CompraQueryService(CompraRepository compraRepository, CompraMapper compraMapper) {
        this.compraRepository = compraRepository;
        this.compraMapper = compraMapper;
    }

    /**
     * Return a {@link List} of {@link CompraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CompraDTO> findByCriteria(CompraCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Compra> specification = createSpecification(criteria);
        return compraMapper.toDto(compraRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CompraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CompraDTO> findByCriteria(CompraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Compra> specification = createSpecification(criteria);
        return compraRepository.findAll(specification, page).map(compraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CompraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Compra> specification = createSpecification(criteria);
        return compraRepository.count(specification);
    }

    /**
     * Function to convert {@link CompraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Compra> createSpecification(CompraCriteria criteria) {
        Specification<Compra> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Compra_.id));
            }
            if (criteria.getPercentualDesconto() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPercentualDesconto(), Compra_.percentualDesconto));
            }
            if (criteria.getValorFinal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValorFinal(), Compra_.valorFinal));
            }
            if (criteria.getDataCriacao() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDataCriacao(), Compra_.dataCriacao));
            }
            if (criteria.getFormaPagamento() != null) {
                specification = specification.and(buildSpecification(criteria.getFormaPagamento(), Compra_.formaPagamento));
            }
            if (criteria.getEstado() != null) {
                specification = specification.and(buildSpecification(criteria.getEstado(), Compra_.estado));
            }
            if (criteria.getCursoId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCursoId(), root -> root.join(Compra_.curso, JoinType.LEFT).get(Curso_.id))
                    );
            }
            if (criteria.getUsuarioId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUsuarioId(), root -> root.join(Compra_.usuario, JoinType.LEFT).get(Usuario_.id))
                    );
            }
        }
        return specification;
    }
}
