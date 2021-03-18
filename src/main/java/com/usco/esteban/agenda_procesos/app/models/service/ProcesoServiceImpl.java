package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IProcesoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

/* este es un unico punto de acceso a diferentes DAOS o repositorios*/
@Service
public class ProcesoServiceImpl implements IProcesoService{

	@Autowired
	private IProcesoDao procesoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Proceso> findAll() {
		
		return (List<Proceso>) procesoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Proceso proceso) {
		
		procesoDao.save(proceso);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Proceso findOne(Long id) {
		
		return procesoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		procesoDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Proceso> findAll(Pageable pageable) {
		
		return procesoDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Proceso> findByRadicado(String radicado, Pageable pageable) {
		
		return procesoDao.findByRadicado(radicado, pageable);
	}


}
