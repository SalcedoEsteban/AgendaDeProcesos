package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;

public interface IEspecialidadService {
	
	public List<Especialidad> findAll();
	
	public Especialidad findOne(Long id);
	
	public void delete(Long id);
	
	public void save(Especialidad especialidad);

}
