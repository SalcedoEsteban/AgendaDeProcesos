package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

public interface IRolService
{
	public List<Rol> findAll();
	
	public Page<Rol> findAll(Pageable pageable);
	
	public void save(Rol rol);
	
	public Rol findOne(Long id);
	
	public void delete(Long id);
	
	public List<Rol> findByUsuario(Usuario usuario);
}
