package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

public interface ITipoProcesoDao extends JpaRepository<TipoProceso, Long> {

	/* acá van todos los metodos que deben implementar todas las clases que implementan
	 * la interfaz */
	public List<TipoProceso> findByEspecialidad(Especialidad especialidad);
	
}
