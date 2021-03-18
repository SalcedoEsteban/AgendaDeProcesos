package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

public interface IUsuarioDao extends  JpaRepository<Usuario, Long>
{
	public Usuario findByUsername(String username);
	
	@Query("select u from Usuario u where u.juzgado = ?1 ")
	public List<Usuario> findByJuzgado(Juzgado juzgado);
	
	@Query("select u from Usuario u where u.juzgado = ?1 ")
	public Page<Usuario> findByJuzgadoPageable(Juzgado juzgado, Pageable pageable);
	
	
	public Page<Usuario> findAll(Pageable pageable);
	
	@Query("select u from Usuario u where u.juzgado = ?1  and u.nombre like %?2%")
	public Page<Usuario> findByNombreAndJuzgado(Juzgado juzgado, Pageable pageable, String nombre);
	
	@Query("select u from Usuario u where u.nombre like %?1%")
	public Page<Usuario> findByNombre(String nombre, Pageable pageable);
}
