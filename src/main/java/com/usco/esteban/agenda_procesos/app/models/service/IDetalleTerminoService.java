package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;

public interface IDetalleTerminoService {
	
	public List<DetalleTermino> findAll();
	
	public void save(DetalleTermino detalleTermino);
	
	public void delete(Long id);
	
	public DetalleTermino findOne(Long id);

}
