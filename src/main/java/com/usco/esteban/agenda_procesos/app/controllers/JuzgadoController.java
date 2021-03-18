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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.editors.EspecialidadPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.service.IEspecialidadService;
import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;

@Controller
@RequestMapping("/juzgado")
@SessionAttributes("juzgado")
public class JuzgadoController {
	
	@Autowired
	private IEspecialidadService especialidadService;
	
	@Autowired
	private IJuzgadoService juzgadoService;
	
	@Autowired
	private EspecialidadPropertyEditor especialidadEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		binder.registerCustomEditor(Especialidad.class, "especialidad", especialidadEditor);
	}

	@ModelAttribute("numeroRoles")
	public int obtenerNumeroRoles()
	{	
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.size();
	}
	
	@GetMapping(value="/cargar-juzgados/{idEspecialidad}", produces = { "application/json" })
	public @ResponseBody List<Juzgado> cargarJuzgadosPorEspecialidad(@PathVariable Long idEspecialidad)
	{
		Especialidad especialidad = especialidadService.findOne(idEspecialidad);
		
		return juzgadoService.findByEspecialidad(especialidad);
	}
	
	@RequestMapping(value="/listarJuzgados/{id}")
	public String listarJuzgados(@PathVariable(value ="id") Long id, Model model)
	{
		Especialidad especialidad = especialidadService.findOne(id);
		System.out.println("el id de la especialidad es: " + id);
		System.out.println("el nombre de la especialidad es: " + especialidad.getNombre());
		
		List<Juzgado> juzgados = juzgadoService.findByEspecialidad(especialidad);
		
		
		model.addAttribute("juzgados", juzgados);
		model.addAttribute("titulo", "Listado de juzgados de la especialidad: " + especialidad.getNombre());
		
		
		return "juzgado/listarJuzgados";
	}

	@RequestMapping(value="/verEspecialidad")
	public String listarEspecialidades(Map<String, Object> model)
	{
		
		List<Especialidad> especialidades = especialidadService.findAll();
		
		model.put("titulo", "Listado de especialidades");
		model.put("especialidades", especialidades);
		
		return "juzgado/juzgadoPorEspecialidad";
	}
	
	@RequestMapping(value="/crear")
	public String crearJuzgado(Model model)
	{
		Juzgado juzgado = new Juzgado();
		List<Especialidad> especialidades = especialidadService.findAll();
		
		model.addAttribute("juzgado", juzgado);
		model.addAttribute("especialidades", especialidades);
		model.addAttribute("titulo", "Formulario de Juzgado");
		
		
		return "juzgado/formJuzgado";
	}
	
	@RequestMapping(value="/guardarJuzgado")
	public String guardarJuzgado(@Valid Juzgado juzgado, BindingResult result, RedirectAttributes flash, SessionStatus status, Model model)
	{
		if(result.hasErrors())
		{
			model.addAttribute("titulo", "Formulario de Juzgado");
			model.addAttribute("especialidades", especialidadService.findAll());
			
			return "juzgado/formJuzgado";
		}
		
		String mensajeFlash = (juzgado.getId() != null) ? "Juzgado editado con exito" : "Juzgado creado con exito";
		
		juzgadoService.save(juzgado);
		
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/juzgado/verEspecialidad";
	}
	
	@RequestMapping(value="/editar/{id}")
	public String editarJuzgado(@PathVariable(value ="id") Long id, Model model, RedirectAttributes flash)
	{
		Juzgado juzgado = null;
		
		if(id > 0)
		{
			juzgado = juzgadoService.findOne(id);
			
			if(juzgado == null)
			{
				flash.addFlashAttribute("error", "El juzgado no existe");
				return "redirect:/juzgado/verEspecialidad";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID del juzgado no puede ser cero");
			return "redirect:/juzgado/verEspecialidad";
		}
		
		model.addAttribute("juzgado", juzgado);
		model.addAttribute("titulo", "Editar Juzgado");
		model.addAttribute("especialidades", especialidadService.findAll());
		
		
		return "juzgado/formJuzgado";
	}
	
	@RequestMapping(value ="/eliminar/{id}")
	public String eliminar(@PathVariable(value= "id") Long id, RedirectAttributes flash)
	{
		if(id > 0)
		{
			juzgadoService.delete(id);
			flash.addFlashAttribute("success", "El Juzgado se ha eliminado con èxito");
		}
		
		return "redirect:/juzgado/verEspecialidad";
	}
	
}
