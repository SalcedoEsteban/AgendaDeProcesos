package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IPersonaDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Persona;

@Service
/* una clase service es un unico punto de acceso a distintos DAOS o repository*/
public class PersonaServiceImpl implements IPersonaService
{
	@Autowired 
	private IPersonaDao personaDao;
	/* podemos tener distintos DAOS y desde el Service podemos acceder a estos desde
	 * un unico punto de acceso, esta clase Service es como una 'fachada' */
	/* Con este service podemos quitar las anotaciones '@Transactional del DAO '*/
	/* podemos interactuar dentro de un metodo de esta clase service con diferentes DAOS
	 * y todos desde la misma transaccion */
	
	@Override
	@Transactional(readOnly = true)
	public List<Persona> findAll() {
		
		return (List<Persona>) personaDao.findAll();
	}

	@Override
	@Transactional
	public void save(Persona persona) {
		
		personaDao.save(persona);
	}

	@Override
	@Transactional(readOnly = true)
	public Persona findOne(Long id) {
		
		return personaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		personaDao.deleteById(id);
		
	}

}
