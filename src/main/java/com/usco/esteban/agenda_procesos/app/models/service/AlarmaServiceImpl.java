package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IAlarmaDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Alarma;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;

@Service
public class AlarmaServiceImpl implements IAlarmaService {

	@Autowired
	private IAlarmaDao alarmaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Alarma> findAll() {
		
		return alarmaDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Alarma findOne(Long id) {
		
		return alarmaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(Alarma alarma) {
		
		alarmaDao.save(alarma);

	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		alarmaDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Alarma> findByProceso(Proceso proceso) {
		
		return alarmaDao.findByProceso(proceso);
	}

	@Override
	@Transactional(readOnly = true)
	public Alarma findByDescripcionAndProceso(String descripcion, Proceso proceso) {
		
		return alarmaDao.findByDescripcionAndProceso(descripcion, proceso);
	}

}
