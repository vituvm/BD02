package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Compra;
import com.mycompany.myapp.domain.Curso;
import com.mycompany.myapp.domain.Usuario;
import com.mycompany.myapp.service.dto.CompraDTO;
import com.mycompany.myapp.service.dto.CursoDTO;
import com.mycompany.myapp.service.dto.UsuarioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Compra} and its DTO {@link CompraDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompraMapper extends EntityMapper<CompraDTO, Compra> {
    @Mapping(target = "curso", source = "curso", qualifiedByName = "cursoTitulo")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "usuarioNome")
    CompraDTO toDto(Compra s);

    @Named("cursoTitulo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "titulo", source = "titulo")
    CursoDTO toDtoCursoTitulo(Curso curso);

    @Named("usuarioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    UsuarioDTO toDtoUsuarioNome(Usuario usuario);
}
