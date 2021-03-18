package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IEspecialidadDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;

@Service
public class EspecialidadServiceImpl implements IEspecialidadService {

	@Autowired
	private IEspecialidadDao especialidadDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Especialidad> findAll() {
		
		return especialidadDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Especialidad findOne(Long id) {
		
		return especialidadDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		especialidadDao.deleteById(id);
	}

	@Override
	@Transactional
	public void save(Especialidad especialidad) {
		
		especialidadDao.save(especialidad);

	}

}
