package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.usco.esteban.agenda_procesos.app.models.entity.HistorialUsuario;
import com.usco.esteban.agenda_procesos.app.models.service.IHistorialUsuarioService;

@Controller
@RequestMapping("/historialUsuario")
public class HistorialUsuarioController {
	
	@Autowired
	private IHistorialUsuarioService historialUsuarioService;
	
	@ModelAttribute("numeroRoles")
	public int obtenerNumeroRoles()
	{	
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.size();
	}
	
	@RequestMapping(value ="/guardar")
	public String guardar()
	{
		HistorialUsuario historialUsuario = new HistorialUsuario();
		
		return "";
	}

}
