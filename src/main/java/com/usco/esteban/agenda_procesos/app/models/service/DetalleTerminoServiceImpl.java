package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IDetalleTerminoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;

@Service
public class DetalleTerminoServiceImpl implements IDetalleTerminoService {

	@Autowired
	private IDetalleTerminoDao detalleTerminoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<DetalleTermino> findAll() {
		
		return detalleTerminoDao.findAll();
	}

	@Override
	@Transactional 
	public void save(DetalleTermino detalleTermino) {
		
		detalleTerminoDao.save(detalleTermino);

	}

	@Override
	@Transactional
	public void delete(Long id) {
		detalleTerminoDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public DetalleTermino findOne(Long id) {
		
		return detalleTerminoDao.findById(id).orElse(null);
	}

}
