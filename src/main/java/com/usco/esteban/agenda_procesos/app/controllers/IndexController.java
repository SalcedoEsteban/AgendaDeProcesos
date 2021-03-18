package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	
	//private int numeroRoles = 0;
	
	
	@GetMapping("/")
	public String index(Authentication authentication, Model model)
	{
		//if(authentication != null)
		//{
			/* implementamos el nombre del ususario en el controlador para que muestre un
			 * mensaje en log cuando se inicia sesion correctamente */
		//	logger.info("Hola usuario autenticado, tu username es:".concat(authentication.getName()));
		//}
		
		/* usando forma estática para obtener el nombre del usuario autenticado e imprimirlo 
		 * en el log */
		Authentication auth	 = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null)
		{
			logger.info("Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Usuario autenticado, tu username es:".concat(authentication.getName()));
		}
		
		if(hasRole("ROLE_ADMIN"))
		{
			logger.info("Hola ".concat(auth.getName()).concat(" tienes acceso"));
		}
		else
		{
			logger.info("Hola".concat(auth.getName()).concat(" NO tienes acceso"));
		}
		
		System.out.println("numero de roles: " + obtenerNumeroRoles());
		model.addAttribute("numeroRoles", obtenerNumeroRoles());
		return "index";
	}
	
	/* metodo que valida qué roles tiene un usuario y que recursos puede y no acceder 
	 * en este caso en el controlador y no en las vista como se hizo con el helper 
	 * hasRole() en los botones */
	public boolean hasRole(String role)
	{
		SecurityContext context = SecurityContextHolder.getContext();
		
		if(context == null)
		{
			return false;
		}
		
		Authentication auth = context.getAuthentication();
		
		if(auth == null)
		{
			return false;
		}
		
		/* a través del objeto auth se obtiene una coleccion de roles o authorities*/
		/* esta es una coleccion de cualquier tipo de objeto que implemente o herede de la interfaz
		 * GrantedAuthority*/
		/* toda clase Rol o que represente un rol, debe implementar la interfaz GrantedAuthority*/
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		
		
		//System.out.println("numero de autorities: " + authorities.size());
		/* iteramos por cada authoritie y preguntamos si el rol es igual al rol que se le 
		 * pasa por parametro al metodo hasRole()*/
		for(GrantedAuthority authority: authorities)
		{
			/* valida si el rol que pasa por argumento es igual al al obtenido en la coleccion */
			if(role.equals(authority.getAuthority()))
			{
				logger.info("Hola usuario ".concat(auth.getName()).concat(" Tu rol es: ").concat(authority.getAuthority()));
				return true;
			}
		}
	
		/* al retornar false quiere decir que el rol no tiene acceso a determinado recurso*/
		return false;
		
	}
	
	public int obtenerNumeroRoles()
	{	
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.size();
	}
	
}
