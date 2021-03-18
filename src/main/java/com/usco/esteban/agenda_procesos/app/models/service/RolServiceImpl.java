package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IRolDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

@Service
public class RolServiceImpl implements IRolService {
	@Autowired
	private IRolDao rolDao;

	@Override
	public List<Rol> findAll() {

		return (List<Rol>) rolDao.findAll();
	}

	@Override
	public Page<Rol> findAll(Pageable pageable) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(Rol rol) {
		rolDao.save(rol);
	}

	@Override
	public Rol findOne(Long id) {

		return rolDao.findById(id).orElse(null);
	}

	@Override
	public void delete(Long id) {
		rolDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Rol> findByUsuario(Usuario usuario) {
		
		return rolDao.findByUsuario(usuario);
	}

}
