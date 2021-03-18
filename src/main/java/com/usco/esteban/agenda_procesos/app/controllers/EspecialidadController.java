package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.service.IEspecialidadService;
import com.usco.esteban.agenda_procesos.app.models.service.ITipoProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.TipoProcesoServiceImpl;

@Controller
@SessionAttributes("especialidad")
@RequestMapping("/especialidad")
public class EspecialidadController {
	
	@Autowired
	private IEspecialidadService especialidadService;
	
	private ITipoProcesoService tipoProcesoService;
	
	@ModelAttribute("numeroRoles")
	public int obtenerNumeroRoles()
	{	
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.size();
	}
	
	@GetMapping(value="/cargarTipoProceso")
	public @ResponseBody List<Especialidad> listarTipoProceso()
	{

		return especialidadService.findAll(); 
	}
	
	@RequestMapping(value ="/crear")
	public String crear(Map<String, Object> model) {
		
		Especialidad especialidad = new Especialidad();
		
		model.put("especialidad", especialidad);
		model.put("titulo", "Crear Especialidad");
		
		return "especialidad/formEspecialidad";
	}
	
	@RequestMapping(value="/guardar")
	public String guardar(@Valid Especialidad especialidad, BindingResult result, RedirectAttributes flash, SessionStatus status, Model model)
	{
		if(result.hasErrors())
		{
			model.addAttribute("titulo", "Crear Especialidad");
			
			return "especialidad/formEspecialidad";
		}
		
		String mensajeFlash = (especialidad.getId() != null) ? "La Especialidad fue editada con éxito" : "La Especialidad fue guardada con éxito";
		
		especialidadService.save(especialidad);
		
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		
		return "redirect:/especialidad/listar";
	}
	
	@RequestMapping(value ="/listar")
	public String listar(Model model)
	{
		List<Especialidad> especialidades = especialidadService.findAll();
		
		model.addAttribute("especialidades", especialidades);
		model.addAttribute("titulo", "Listado de Especialidades");
		return "especialidad/listar";
	}
	
	@RequestMapping(value ="/editar/{id}")
	public String editar(@PathVariable(value ="id") Long id, Model model, RedirectAttributes flash)
	{
		Especialidad especialidad = null;
		
		if(id > 0)
		{
			especialidad = especialidadService.findOne(id);
			if(especialidad == null)
			{
				flash.addFlashAttribute("error", "La Especialidad no existe");
				return "redirect:/especialidad/listar";
			}
				
		}
		else
		{
			flash.addFlashAttribute("error", "El ID de la especialidad no puede ser cero");
			return "redirect:/especialidad/listar";
		}
		
		model.addAttribute("especialidad", especialidad);
		model.addAttribute("titulo", "Editar Especialidad");
		
		return "especialidad/formEspecialidad";
	}
	
	@RequestMapping(value ="/eliminar/{id}")
	public String eliminar(@PathVariable(value ="id") Long id, RedirectAttributes flash)
	{
		if(id > 0)
		{
			especialidadService.delete(id);
			flash.addFlashAttribute("success", "La Especialidad se eliminó con exito");
		}
		
		
		return "redirect:/especialidad/listar";
	}

}
