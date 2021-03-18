package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IHistorialUsuarioDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.HistorialUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

@Service
public class HistorialUsuarioServiceImpl implements IHistorialUsuarioService {

	@Autowired
	private IHistorialUsuarioDao historialUsuarioDao;
	
	
	@Override
	@Transactional(readOnly = true)
	public List<HistorialUsuario> findAll() {
		
		return historialUsuarioDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public HistorialUsuario findOne(Long id) {
		
		return historialUsuarioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void save(HistorialUsuario historialUsuario)
	{	
		historialUsuarioDao.save(historialUsuario);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		historialUsuarioDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public List<HistorialUsuario> findByUsuario(Usuario usuario) {
		
		return historialUsuarioDao.findByUsuario(usuario);
	}

	/*@Override
	@Transactional(readOnly = true)
	public HistorialUsuario findByUsuarioAndEspecialidadAndFechaIngreso(Usuario usuario, Especialidad especialidad, Date fechaIngreso) {
		
		return historialUsuarioDao.findByUsuarioAndEspecialidadAndFechaIngreso(usuario, especialidad, fechaIngreso);
	}*/

	/*@Override
	@Transactional(readOnly = true)
	public HistorialUsuario findByUsuarioAndFechaIngreso(Usuario usuario, Date FechaIngreso) {
		
		return historialUsuarioDao.findByUsuarioAndFechaIngreso(usuario, FechaIngreso);
	}*/

}
