package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.ITerminoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

@Service
public class TerminoServiceImpl implements ITerminoService {

	@Autowired
	private ITerminoDao terminoDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Termino> findAll() {
		
		return terminoDao.findAll();
	}

	@Override
	@Transactional
	public void save(Termino termino) {
		
		terminoDao.save(termino);

	}

	@Override
	@Transactional(readOnly = true)
	public Termino findOne(Long id) {
		
		return terminoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		terminoDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public List<Termino> findByEspecialidadAndTipoProcesoAndBasico(Especialidad especialidad, TipoProceso tipoProceso,
			boolean basico) {
		
		return terminoDao.findByEspecialidadAndTipoProcesoAndBasico(especialidad, tipoProceso, basico);
	}

	@Override
	@Transactional(readOnly = true)
	public Termino findByNombreAndEspecialidadAndTipoProceso(String nombre, Especialidad especialidad, 
			TipoProceso tipoProceso) {
		
		return terminoDao.findByNombreAndEspecialidadAndTipoProceso(nombre, especialidad, tipoProceso);
	}

}
