package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

public interface ITerminoService
{
	public List<Termino> findAll();
	
	public void save(Termino termino);
	
	public Termino findOne(Long id);
	
	public void delete(Long id);
	
	public List<Termino> findByEspecialidadAndTipoProcesoAndBasico(Especialidad especialidad, 
			TipoProceso tipoProceso, boolean basico);
	
	public Termino findByNombreAndEspecialidadAndTipoProceso(String nombre, Especialidad especialidad,
			TipoProceso tipoProceso);
}
