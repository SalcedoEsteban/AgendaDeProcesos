package com.usco.esteban.agenda_procesos.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

public interface IRolDao extends JpaRepository<Rol, Long>
{
	public List<Rol> findByUsuario(Usuario usuario);
}
