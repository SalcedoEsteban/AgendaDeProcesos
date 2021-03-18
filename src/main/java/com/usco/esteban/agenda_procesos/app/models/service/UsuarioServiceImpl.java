package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IUsuarioDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Override
	@Transactional
	public void save(Usuario usuario){
		
		usuarioDao.save(usuario);

	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findOne(Long id)
	{
		
		return usuarioDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		
		return usuarioDao.findAll();
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		usuarioDao.deleteById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findByUsername(String username) {
		
		return usuarioDao.findByUsername(username);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findByJuzgado(Juzgado juzgado) {
		
		return usuarioDao.findByJuzgado(juzgado);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> findByJuzgadoPageable(Juzgado juzgado, Pageable pageable) {
		
		return usuarioDao.findByJuzgadoPageable(juzgado, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> findAll(Pageable pageable) {
		
		return usuarioDao.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> findByNombreAndJuzgado(Juzgado juzgado, Pageable pageable,  String nombre) {
		
		return usuarioDao.findByNombreAndJuzgado(juzgado, pageable, nombre);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Usuario> findByNombre(String nombre, Pageable pageable) {
		
		return usuarioDao.findByNombre(nombre, pageable);
	}

}
