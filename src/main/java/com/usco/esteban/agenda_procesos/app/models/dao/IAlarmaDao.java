package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Alarma;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;

public interface IAlarmaDao extends JpaRepository<Alarma, Long>{

	public List<Alarma> findByProceso(Proceso proceso);
	
	public Alarma findByDescripcionAndProceso(String descripcion, Proceso proceso);
}
