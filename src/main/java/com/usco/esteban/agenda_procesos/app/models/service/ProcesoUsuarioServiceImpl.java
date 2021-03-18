package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IProcesoUsuarioDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;

@Service
public class ProcesoUsuarioServiceImpl implements IProcesoUsuarioService {

	@Autowired
	private IProcesoUsuarioDao procesoUsuarioDao;
	
	
	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllById(Long id, Pageable pageable, Juzgado juzgado) {
		
		return procesoUsuarioDao.findAllById(id, pageable, juzgado);
	}

	@Override
	@Transactional
	public void save(ProcesoUsuario procesoUsuario) {
		
		procesoUsuarioDao.save(procesoUsuario);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		procesoUsuarioDao.deleteById(id);

	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findByIdAndRadicado(Long id, Pageable pageable, String radicado) {
		
		return procesoUsuarioDao.findByIdAndRadicado(id, pageable, radicado);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findByUsuarioAndEstadoAndJuzgadoAndPrioritario(Long id, Pageable pageable,
			Juzgado juzgado) {
		
		return procesoUsuarioDao.findByUsuarioAndEstadoAndJuzgadoAndPrioritario(id, pageable, juzgado);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllBy(Long id, Pageable pageable, Juzgado juzgado) {
		
		return procesoUsuarioDao.findAllBy(id, pageable, juzgado);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProcesoUsuario> findAll(Juzgado juzgado)
	{	
		return procesoUsuarioDao.findAll(juzgado);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllByPrioritario(Juzgado juzgado, Pageable pageable)
	{	
		return procesoUsuarioDao.findAllByPrioritario(juzgado, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllByJuzgado(Juzgado juzgado, Pageable pageable) {
		
		return procesoUsuarioDao.findAllByJuzgado(juzgado, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllBySuperAdmin(Pageable pageable) {
		
		return procesoUsuarioDao.findAllBySuperAdmin(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ProcesoUsuario> findAllById(Long id, Juzgado juzgado) {
		
		return procesoUsuarioDao.findAllById(id, juzgado);
	}

	@Override
	@Transactional(readOnly = true)
	public ProcesoUsuario findOne(Long id) {
		
		return procesoUsuarioDao.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findByIdAndRadicadoAndEstadoFalse(Long id, Pageable pageable, String radicado) {
		
		return procesoUsuarioDao.findByIdAndRadicadoAndEstadoFalse(id, pageable, radicado);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllByAdmisionToJuez(Juzgado juzgado, Pageable pageable) {
		
		return procesoUsuarioDao.findAllByAdmisionToJuez(juzgado, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllByAdmision(Long id, Juzgado juzgado, Pageable pageable) {
		
		return procesoUsuarioDao.findAllByAdmision(id, juzgado, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllBySubsanarDemandaToJuez(Juzgado juzgado, Pageable pageable) {
		
		return procesoUsuarioDao.findAllBySubsanarDemandaToJuez(juzgado, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<ProcesoUsuario> findAllBySubsanarDemandaToUsuario(Long id, Juzgado juzgado, Pageable pageable) {
		
		return procesoUsuarioDao.findAllBySubsanarDemandaToUsuario(id, juzgado, pageable);
	}

}
