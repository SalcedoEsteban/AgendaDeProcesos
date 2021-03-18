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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.editors.EspecialidadPropertyEditor;
import com.usco.esteban.agenda_procesos.app.editors.TipoProcesoPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.dao.IEspecialidadDao;
import com.usco.esteban.agenda_procesos.app.models.dao.ITerminoDao;
import com.usco.esteban.agenda_procesos.app.models.dao.ITipoProcesoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.service.IEspecialidadService;
import com.usco.esteban.agenda_procesos.app.models.service.ITerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.ITipoProcesoService;

@Controller
@SessionAttributes("termino")
public class TerminoController
{
	@Autowired
	private ITerminoService terminoService;
	
	@Autowired
	private ITipoProcesoService tipoProcesoService;
	
	@Autowired
	private IEspecialidadService especialidadService;
	
	@Autowired
	private EspecialidadPropertyEditor especialidadEditor;
	
	@Autowired
	private TipoProcesoPropertyEditor tipoProcesoEditor;
	
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		binder.registerCustomEditor(Especialidad.class, "especialidad", especialidadEditor);
		binder.registerCustomEditor(TipoProceso.class, "tipoProceso", tipoProcesoEditor);
	}
	
	@ModelAttribute("numeroRoles")
	public int obtenerNumeroRoles()
	{	
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.size();
	}
	
	/*Metodo REST para select dinamico con ajax y jquery*/
	@GetMapping(value ="/listarTiposProcesoRest")
	public @ResponseBody List<TipoProceso> listarTiposProcesoRest(@RequestParam(name = "especialidad", required = true)
	Long especialidad)
	{
		//Long id = Long.parseLong(especialidad);
		Especialidad especialidad1 = especialidadService.findOne(especialidad);
		
		return tipoProcesoService.findByEspecialidad(especialidad1);
	}
	
	@GetMapping("/formTermino")
	public String crear(Map<String, Object> model, RedirectAttributes flash)
	{
		
		/* FALTA PONER LOS MENSAJES DE ERROR*/
		
		/*se obtiene el tipoProceso por el id*/
		/*TipoProceso tipoProceso = tipoProcesoDao.findOne(tipoProcesoId);
		
		if(tipoProceso == null)
		{
			return "redirect:/listarTiposProceso";
		}*/
		
		Termino termino = new Termino();
		/* esta es la relacion, se asigna un tipoProceso a un término */
		//termino.setTipoProceso(tipoProceso);
		
		//Long id = (long) 1;
		
		//Especialidad especialidad = especialidadService.findOne(id);
		
		//List<TipoProceso> tiposProceso = tipoProcesoService.findByEspecialidad(especialidad);
		//List<TipoProceso> tiposProceso = tipoProcesoService.findAll();
		
		model.put("termino", termino);
		model.put("especialidades", especialidadService.findAll());
		//model.put("tiposProceso", tipoProcesoService.findAll());
		//model.put("tiposProceso", tiposProceso);
		model.put("titulo", "Formulario de terminos");
		
		return "formTermino";
	}
	
	@RequestMapping(value ="/listarTerminos", method = RequestMethod.GET)
	public String listar(Model model)
	{
		model.addAttribute("titulo", "Listado de Terminos");
		model.addAttribute("terminos", terminoService.findAll());
		
		return "listarTerminos";
	}
	
	/*@RequestMapping(value ="/formTermino")
	public String crearTermino(Map<String, Object> model)
	{
		Termino termino = new Termino();
		
		model.put("termino", termino);
		model.put("tiposProceso", tipoProcesoDao.findAll());
		model.put("titulo", "Formulario de Terminos");
		
		return "formTermino";
	}*/
	
	@PostMapping(value ="/guardarTermino")
	public String guardar(@Valid Termino termino, BindingResult result, SessionStatus status, Model model, RedirectAttributes flash)
	{
		/*Long esp = Long.parseLong(termino.getEsp());
		Especialidad especialidad = especialidadService.findOne(esp);
		termino.setEspecialidad(especialidad);
		
		Long tipoPro = Long.parseLong(termino.getTipPro());
		TipoProceso tipoProceso = tipoProcesoService.findOne(tipoPro);
		termino.setTipoProceso(tipoProceso);*/
		if(result.hasErrors())
		{
			
			model.addAttribute("titulo", "Formulario de terminos");
			model.addAttribute("especialidades", especialidadService.findAll());
			//model.addAttribute("tiposProceso", tipoProcesoService.findAll());
			return "formTermino";
		}
		
		terminoService.save(termino);
		
		String nombreTipoProceso = termino.getTipoProceso().getNombre();
		String nombreEspecialidad = termino.getEspecialidad().getNombre();
		String nombreTermino = termino.getNombre();
		
		status.setComplete();
		flash.addFlashAttribute("success", "Se creó el Termino \"".concat(nombreTermino).concat("\" correctamente")
				.concat(" Para el tipo Proceso: ").concat(nombreTipoProceso).concat(" de la especialidad: ").concat(nombreEspecialidad));
		
		return "redirect:/listarTiposProceso";
	}
	
	@GetMapping(value="/eliminarTermino/{id}")
	public String eliminar(@PathVariable(name = "id") Long id)
	{
		if(id > 0)
		{
			terminoService.delete(id);
		}
		
		return "redirect:/listarTiposProceso";
	}
	
	@GetMapping(value ="/editarTermino/{id}")
	public String editar(@PathVariable(value = "id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Termino termino = null;
		
		
		/* si el id es mayor a cero, se busca en la base de datos */
		if(id > 0)
		{
			termino = terminoService.findOne(id);
			
			if(termino == null)
			{	
				flash.addFlashAttribute("error", "El termino no existe en la base de datos");
				return "redirect:/listarTiposProceso";
			}
		}
		else
		{
			flash.addFlashAttribute("eror", "el id del proceso no puede ser cero");
			return "redirect:/listarTiposProceso";
		}
		
		model.put("termino", termino);
		model.put("especialidades", especialidadService.findAll());
		model.put("tiposProceso", tipoProcesoService.findAll());
		model.put("titulo", "Editar Termino");
		
		
		System.out.println("el id del termino es: " + termino.getId());
		
		
		return "formTermino";
	}
	
}
