package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

public interface IUsuarioService {
	
	public void save(Usuario usuario);
	
	public Usuario findOne(Long id);
	
	public List<Usuario> findAll();
	
	public void delete(Long id);

	public Usuario findByUsername(String username);
	
	@Query("select u from Usuario u where u.juzgado = ?1 ")
	public List<Usuario> findByJuzgado(Juzgado juzgado);
	
	@Query("select u from Usuario u where u.juzgado = ?1 ")
	public Page<Usuario> findByJuzgadoPageable(Juzgado juzgado, Pageable pageable);
	
	public Page<Usuario> findAll(Pageable pageable);
	
	public Page<Usuario> findByNombreAndJuzgado(Juzgado juzgado, Pageable pageable, String nombre);
	
	public Page<Usuario> findByNombre(String nombre, Pageable pageable);
}
