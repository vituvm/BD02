package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Compra;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Compra entity.
 */
@Repository
public interface CompraRepository extends JpaRepository<Compra, Long>, JpaSpecificationExecutor<Compra> {
    default Optional<Compra> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Compra> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Compra> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select distinct compra from Compra compra left join fetch compra.curso left join fetch compra.usuario",
        countQuery = "select count(distinct compra) from Compra compra"
    )
    Page<Compra> findAllWithToOneRelationships(Pageable pageable);

    @Query("select distinct compra from Compra compra left join fetch compra.curso left join fetch compra.usuario")
    List<Compra> findAllWithToOneRelationships();

    @Query("select compra from Compra compra left join fetch compra.curso left join fetch compra.usuario where compra.id =:id")
    Optional<Compra> findOneWithToOneRelationships(@Param("id") Long id);
}
