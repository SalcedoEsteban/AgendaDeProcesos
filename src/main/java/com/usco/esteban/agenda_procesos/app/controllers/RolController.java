package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IRolService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.JpaUsuarioDetailsService;



@Controller
@SessionAttributes("rol")
public class RolController
{
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IRolService rolService;
	
	@ModelAttribute("numeroRoles")
	public int obtenerNumeroRoles()
	{	
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.size();
	}
	
	@ModelAttribute("roles")
	public List<String> obtenerRoles()
	{
		return Arrays.asList("ROLE_USER", "ROLE_ADMIN", "ROLE_SUPER_ADMIN");
	}
	
	/*@RequestMapping(value ="/guardarRol", method = RequestMethod.POST)
	public String guardarRoles(Rol rol, SessionStatus status, RedirectAttributes flash)
	{
		String mensajeFlash = (rol.getId() != null) ? "Rol editado con exito" : "Rol creado con exito";

		rolService.save(rol);
		String nombreUsuario = rol.getUsuario().getNombre().concat(" ").concat(rol.getUsuario().getApellido());
		
		status.setComplete();
		flash.addFlashAttribute("success", "Rol ".concat(rol.getRol()).concat(" asigando con éxito para el usuario: ".concat(nombreUsuario)));
		flash.addFlashAttribute("success", mensajeFlash);
		
		return "redirect:/listarUsuarios";
	}*/
	
	
	private Usuario usuario = new Usuario();
	
	@RequestMapping(value ="/guardarRol", method = RequestMethod.GET)
	public String guardarRoles(@RequestParam List<String> roles, SessionStatus status, RedirectAttributes flash)
	{
		//String mensajeFlash = (rol.getId() != null) ? "Rol editado con exito" : "Rol creado con exito";

		//rolService.save(rol);
		String nombreUsuario = usuario.getNombre().concat(" ").concat(usuario.getApellido());
		
		for(String rol: roles)
		{
			Rol nuevoRol = new Rol();
			nuevoRol.setRol(rol);
			nuevoRol.setUsuario(usuario);
			rolService.save(nuevoRol);
		}
		
		status.setComplete();
		flash.addFlashAttribute("success", "Roles asigandos con éxito para el usuario: ".concat(nombreUsuario));
		//flash.addFlashAttribute("success", mensajeFlash);
		
		return "redirect:/listarUsuarios";
	}
	
	
	
	/*@RequestMapping(value="/crearRol/{idUsuario}")
	public String crearRol(@PathVariable(value ="idUsuario") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Usuario usuario = usuarioService.findOne(id);
		
		if(usuario == null)
		{
			flash.addFlashAttribute("error", "El usuario no existe");
			return "redirect:/listarUsuarios";
		}
		
		Rol rol = new Rol();
		
		rol.setUsuario(usuario);
		
		model.put("titulo", "Asignar Rol al usuario: " + usuario.getNombre()+ " " + usuario.getApellido());;
		model.put("rol", rol);
		
		return "rol/formRol";
	}*/
	
	@RequestMapping(value="/crearRol/{idUsuario}")
	public String crearRol(@PathVariable(value ="idUsuario") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		usuario = usuarioService.findOne(id);
		
		if(usuario == null)
		{
			flash.addFlashAttribute("error", "El usuario no existe");
			return "redirect:/listarUsuarios";
		}
		
		
		//Rol rol = new Rol();
		
		//rol.setUsuario(usuario);
		
		model.put("titulo", "Asignar Rol al usuario: " + usuario.getNombre()+ " " + usuario.getApellido());;
		//model.put("rol", rol);
		
		
		return "rol/formRol";
	}
	
	@RequestMapping(value="/verRoles/{id}")
	public String verRoles(@PathVariable(value ="id") Long id, Model model, RedirectAttributes flash)
	{
		Usuario usuario = usuarioService.findOne(id);
		String nombreUsuario = usuario.getNombre();
		
		List<Rol> roles = rolService.findByUsuario(usuario);
		
		model.addAttribute("titulo", "Roles del usuario: ".concat(nombreUsuario).concat(" ").concat(usuario.getApellido()));
		model.addAttribute("roles", roles);
		
		return "rol/verRolesUsuario";
	}
	
	@RequestMapping(value="/eliminarRol/{id}")
	public String eliminarRol(@PathVariable(value ="id") Long id, RedirectAttributes flash)
	{
		
		if(id > 0)
		{
			rolService.delete(id);
			flash.addFlashAttribute("success", "El rol ha sido eliminado exitosamente");
		}
		
		return "redirect:/listarUsuarios";
	}
	
	@RequestMapping(value="/editarRol/{id}")
	public String editarRol(@PathVariable(value ="id") Long id, Model model, RedirectAttributes flash)
	{
		Rol rol = null;
		
		if(id > 0)
		{
			rol = rolService.findOne(id);
			
			if(rol == null)
			{
				flash.addFlashAttribute("error", "El rol no existe");
				return "redirect:/listarUsuarios";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID del rol no existe, no puede ser cero");
			return "redirect:/listarUsuarios";
		}
		
		model.addAttribute("titulo", "Editar Rol");
		model.addAttribute("rol", rol);
		
		return "rol/formRol";
	}
}
