package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;

public interface IDetalleTerminoDao extends JpaRepository<DetalleTermino, Long>
{
	
}
