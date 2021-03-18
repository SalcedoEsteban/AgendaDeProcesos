package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.IUsuarioDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

/* no se necesita implentar ninguna interfaz porque la interfaz la provee spring security 
 * para trabajar con JPA o con cualquier otro proveedor (como JDBC) para implementar el proceso 
 * de login/autenticación */
@Service("jpaUsuarioDetailsService")
public class JpaUsuarioDetailsService implements UserDetailsService
{

	@Autowired
	private IUsuarioDao usuarioDao;
	
	private Logger logger = LoggerFactory.getLogger(JpaUsuarioDetailsService.class);
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
	{
		/* se obtiene el usuario por el username */
		Usuario usuario = usuarioDao.findByUsername(username);
		
		if (usuario == null)
		{
			logger.error("Error login: no existe el usuario: '"+ username +"'");
			throw new UsernameNotFoundException("Username: " + username +  " no existe en el sistema");
		}
		
		List<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
		
		/* obtenemos todos los roles del usuario y lo guardamos */
		/* se obtienen los roles a traves de la relacion de usuario y los roles por medio del
		 * metodo getRoles de la clase usuario */
		for(Rol role: usuario.getRoles())
		{
			logger.info("Rol: ".concat(role.getRol()));
			roles.add(new SimpleGrantedAuthority(role.getRol()));
		}
		
		if (roles.isEmpty())
		{
			logger.error("Error login: el usuario: '"+ username +"' no tiene roles asignados");
			throw new UsernameNotFoundException("Error login: el usuario: '" + username + "' no tiene roles asignados");
		}
		
		return new User(username, usuario.getPassword(), usuario.isEnabled(), true, true, true, roles);
	}
	
	@Transactional(readOnly = true)
	public List<Usuario> findAll()
	{
		return (List<Usuario>) usuarioDao.findAll();
	}
	
	@Transactional
	public void save(Usuario usuario)
	{
		usuarioDao.save(usuario);
	}
	
	@Transactional(readOnly = true)
	public Usuario findOne(Long id)
	{
		return usuarioDao.findById(id).orElse(null);
	}
	
	@Transactional
	public void delete(Long id)
	{
		usuarioDao.deleteById(id);
	}
	
	@Transactional(readOnly = true)
	public List<Usuario> findByJuzgado(Juzgado juzgado)
	{
		return usuarioDao.findByJuzgado(juzgado);
	}
}
