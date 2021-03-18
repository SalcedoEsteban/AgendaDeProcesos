package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IJuzgadoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;

@Service
public class JuzgadoServiceImpl implements IJuzgadoService {

	@Autowired
	private IJuzgadoDao juzgadoDao;

	@Override
	@Transactional(readOnly = true)
	public List<Juzgado> findAll() {

		return juzgadoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Juzgado juzgado) {

		juzgadoDao.save(juzgado);

	}

	@Override
	@Transactional(readOnly = true)
	public Juzgado findOne(Long id) {

		return juzgadoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		juzgadoDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Juzgado> findByEspecialidad(Especialidad especialidad) {
		
		return juzgadoDao.findByEspecialidad(especialidad);
	}

}
