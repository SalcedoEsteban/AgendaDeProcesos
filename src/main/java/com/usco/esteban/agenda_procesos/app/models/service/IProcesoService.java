package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

public interface IProcesoService {

	/* este es un metodo que tienen que implementar todas las clases que implementan 
	 * la interfaz*/
	/* es un metodo sin implementar*/
	public List<Proceso> findAll();
	
	public Page<Proceso> findAll(Pageable pageable);
	
	/* contrato de implementacion para guardar en la base de datos */
	public void save(Proceso proceso);
	
	public Proceso findOne(Long id);
	
	public void delete(Long id);
	
	public Page<Proceso> findByRadicado(String radicado, Pageable pageable);
	
	//public Page<Proceso> findByRadicado(String radicado);
	
}
