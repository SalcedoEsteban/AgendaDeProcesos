package com.usco.esteban.agenda_procesos.app.models.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

public interface ITerminoDao extends JpaRepository<Termino, Long>{

	
	public List<Termino> findByEspecialidadAndTipoProcesoAndBasico(Especialidad especialidad, 
			TipoProceso tipoProceso, boolean basico);
	
	public Termino findByNombreAndEspecialidadAndTipoProceso(String nombre,Especialidad especialidad,
			 TipoProceso tipoProceso);
}
