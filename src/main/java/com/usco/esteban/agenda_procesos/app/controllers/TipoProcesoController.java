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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.editors.EspecialidadPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IEspecialidadService;
import com.usco.esteban.agenda_procesos.app.models.service.ITipoProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.UsuarioServiceImpl;

@Controller
@SessionAttributes("tipoProceso")
public class TipoProcesoController {
	
	@Autowired
	/* la anotacion @qualifier se usa para cuando hay varios repositorios que implementan
	 * la misma interfaz pero con diferente forma de trabajar, uno puede trabajar con JPA
	 * pero otro puede ser con JDBC, esto se usa para elegir cual es el que se desea usar*/
	private ITipoProcesoService tipoProcesoService;
	
	@Autowired
	private IEspecialidadService especialidadService;
	
	@Autowired
	private EspecialidadPropertyEditor especialidadEditor;
	
	/*@Autowired
	private IUsuarioService usuarioService;
	
	private Usuario usuario;*/
	
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
	
	@GetMapping(value="/cargar-tipo-proceso/{idEspecialidad}", produces = { "application/json" })
	public @ResponseBody List<TipoProceso> cargarTipoProceso(@PathVariable Long idEspecialidad)
	{
		Especialidad especialidad = especialidadService.findOne(idEspecialidad);
		
		return tipoProcesoService.findByEspecialidad(especialidad);
	}
	
	
	@RequestMapping(value ="/listarTiposProceso", method = RequestMethod.GET)
	public String listar(Model model)
	{
		
		List<Especialidad> especialidades = especialidadService.findAll();
		
		model.addAttribute("especialidades", especialidades);
		model.addAttribute("titulo", "Listado de Especialidades para ver Tipos de Proceso");
		//model.addAttribute("tiposProceso", tipoProcesoService.findAll());
		//model.addAttribute("tiposProceso", tipoProcesoService.findByEspecialidad(especialidad));
		
		return "tipoProcesoPorEspecialidad";
	}
	
	/* como primera fase, se le muestra el formulario al usuario, y en este se le pasa
	 * el objeto que se quiere guardar, a la vista*/
	@GetMapping(value ="/formTipoProceso")
	public String crear(Map<String, Object> model)
	{
		
		TipoProceso tipoProceso = new TipoProceso();
		
		model.put("titulo", "Formulario de Tipo de Proceso");
		model.put("tipoProceso", tipoProceso);
		model.put("especialidades", especialidadService.findAll());
		
		
		return "formTipoProceso";
	}
	
	
	@RequestMapping(value ="/formTipoProceso/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		
		TipoProceso tipoProceso = null;
		
		if(id > 0)
		{
			tipoProceso = tipoProcesoService.findOne(id);
			if(tipoProceso == null)
			{
				flash.addFlashAttribute("error", "El Tipo Proceso no existe");
				return "redirect:/listarTiposProceso";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID del Tipo Proceso no puede ser cero");
			return "redirect:/listarTiposProceso";
		}
		
		model.put("tipoProceso", tipoProceso);
		model.putIfAbsent("especialidades", especialidadService.findAll());
		model.put("titulo", "Editar Tipo Proceso");
		return "formTipoProceso";
	}
	
	@RequestMapping(value ="/guardarTipoProceso",method = RequestMethod.POST)
	/* aqui se le pasa como parametro el objeto con los datos guardados desde el 
	 * formulario */
	public String guardar(@Valid TipoProceso tipoProceso, BindingResult result, SessionStatus status, RedirectAttributes flash, Model model)
	{
		
		if(result.hasErrors())
		{
			model.addAttribute("titulo", "Formulario de Tipo Proceso");
			model.addAttribute("especialidades", especialidadService.findAll());
			
			return "formTipoProceso";
		}
		
		String mensajeFlash = (tipoProceso.getId() != null) ? "Tipo Proceso editado con exito" : "Tipo Proceso creado con exito";
		
		
		tipoProcesoService.save(tipoProceso);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		
		/* se hace el redirect a la url*/
		return "redirect:listarTiposProceso";
	}
	
	@RequestMapping(value ="/eliminarTipoProceso/{id}")
	public String eliminar(@PathVariable(value="id") Long id)
	{
		if(id > 0)
		{
			tipoProcesoService.delete(id);
		}
		return "redirect:/listarTiposProceso";
	}
	
	@RequestMapping(value="/verTerminosTipoProceso/{id}")
	public String verTerminos(@PathVariable(name="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		TipoProceso tipoProceso = tipoProcesoService.findOne(id);
		
		if (tipoProceso == null)
		{
			flash.addFlashAttribute("error", "El tipo Proceso no existe en la base de datos");
		}
		
		model.put("tipoProceso", tipoProceso);
		model.put("titulo","Terminos para el tipo de proceso: ".concat(tipoProceso.getNombre()));
		
		return "listarTerminos";
	}
	
	@RequestMapping(value="/verTipoProcesoPorEspecialidad/{id}")
	public String verTiposProcesoPorEspecialidad(@PathVariable(value="id") Long id, Model model)
	{
		Especialidad especialidad = especialidadService.findOne(id);
		
		List<TipoProceso> tiposProceso = tipoProcesoService.findByEspecialidad(especialidad);
		
		model.addAttribute("tiposProceso", tiposProceso);
		model.addAttribute("titulo", "Tipos de proceso pertenecientes a la especialidad: " + especialidad.getNombre());
		
		return "listarTipoProcesos";
	}

}
