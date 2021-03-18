package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.Alarma;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;

public interface IAlarmaService {

	
	public List<Alarma> findAll();
	
	public Alarma findOne(Long id);
	
	public void save(Alarma alarma);
	
	public void delete(Long id);
	
	public List<Alarma> findByProceso(Proceso proceso);
	
	public Alarma findByDescripcionAndProceso(String descripcion, Proceso proceso);
}
