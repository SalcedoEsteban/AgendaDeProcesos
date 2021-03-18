package com.usco.esteban.agenda_procesos.app.models.dao;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;

public interface IJuzgadoDao extends JpaRepository<Juzgado, Long>{
	
	//@Query("select j from Juzgado j where j.especialidad = ?1 ")
	public List<Juzgado> findByEspecialidad(Especialidad especialidad);
}
