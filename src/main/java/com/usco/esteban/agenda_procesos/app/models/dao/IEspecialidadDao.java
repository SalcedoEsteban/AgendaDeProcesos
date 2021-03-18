package com.usco.esteban.agenda_procesos.app.models.dao;



import org.springframework.data.jpa.repository.JpaRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;

public interface IEspecialidadDao extends JpaRepository<Especialidad, Long>{
	
	
}
