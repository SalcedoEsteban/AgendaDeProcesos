package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.HistorialUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

public interface IHistorialUsuarioService {
	
	public List<HistorialUsuario> findAll();
	
	public HistorialUsuario findOne(Long id);
	
	public void save(HistorialUsuario historialUsuario);
	
	public void delete(Long id);

	public List<HistorialUsuario> findByUsuario(Usuario usuario);
	
	//public HistorialUsuario findByUsuarioAndEspecialidadAndFechaIngreso(Usuario usuario, Especialidad especialidad, Date fechaIngreso);
	
	//public HistorialUsuario findByUsuarioAndFechaIngreso(Usuario usuario, Date FechaIngreso);
}
