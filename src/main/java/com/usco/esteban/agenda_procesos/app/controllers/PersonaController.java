package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.models.dao.ITipoProcesoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Persona;
import com.usco.esteban.agenda_procesos.app.models.service.IPersonaService;

@Controller
@SessionAttributes("persona")
public class PersonaController {

	/* atributo de PersonaDao para poder realizar la consulta*/
	@Autowired
	private IPersonaService personaService;
	
	
	@RequestMapping(value = "/listar", method = RequestMethod.GET)
	public String listar(Model model)
	{
		model.addAttribute("titulo", "Listado de Personas");
		model.addAttribute("personas", personaService.findAll());
		
		return "listar";
	}
	
	@RequestMapping(value = "/formPersona")
	public String crear(Map<String, Object> model)
	{
		Persona persona = new Persona();
		
		model.put("persona", persona);
		model.put("titulo", "Formulario de Registro");
		
		
		return "formPersona";
	}
	
	@RequestMapping(value = "/formPersona/{id}")
	public String editar(@PathVariable(value="id") long id,Map<String, Object> model, RedirectAttributes flash)
	{
		Persona persona = null;
		
		/* si el id es mayor que cero se hace la consulta a la base de datos*/
		if(id>0)
		{
			/* si el id es mayor a hacer, se retorna un objeto persona*/
			persona = personaService.findOne(id);
			/* luego se debe validar que este id exista en la base de datos
			 * por lo tanto se valida que el objeto que retorna despues de
			 * la consulta es null*/
			if( persona == null)
			{
				flash.addFlashAttribute("error", "Error, el id de la persona no existe en la base de datos");
				return "redirect:/listar";
			}
		}
		else
		{
			/* al no existir el registro, este metodo redirige a la url /listar de nuevo*/
			flash.addFlashAttribute("error", "Error, el id no puede ser cero");
			return "redirect:/listar";
		}
		
		model.put("persona", persona);
		model.put("titulo", "Editar Persona");
		
		
		return "formPersona";
	}
	
	@RequestMapping(value ="/guardarPersona", method = RequestMethod.POST)
	public String guardar(@Valid Persona persona, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status)
	{
		
		if(result.hasErrors())
		{
			model.addAttribute("titulo", "Formulario de Registro");
			
			return "formPersona";
		}
		
		/* se valida el id de las persona para saber si se edita o se guarda*/
		String mensajeFlash = null;
		
		if(persona.getId() != null)
		{
			mensajeFlash = "¡La persona ha sido guardada con exito!";
		}
		else if(persona.getId() == null)
		{
			mensajeFlash = "¡La persona ha sido editada con exito!";
		}
			
		
		personaService.save(persona);
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:/listar";
	}
	
	@RequestMapping(value = "/eliminarPersona/{id}")
	public String eliminarPersona(@PathVariable(value="id") long id, RedirectAttributes flash)
	{
		if(id > 0)
		{
			personaService.delete(id);
			flash.addFlashAttribute("error", "Persona eliminada con exito");
		}
		
		
		return "redirect:/listar";
	}
}
