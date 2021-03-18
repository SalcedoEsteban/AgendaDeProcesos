package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Persona;

public interface IPersonaDao extends CrudRepository<Persona, Long>
{
	/* en esta clase se encuentran los metodos del CRUD los cuales están implicitos
	 * y se ejecutan 'por debajo'*/
	/* aqui podemos dejar los metodos de consultas personzalidados */
}
