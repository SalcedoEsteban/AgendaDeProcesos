package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.editors.TerminoPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.dao.IDetalleTerminoDao;
import com.usco.esteban.agenda_procesos.app.models.dao.ITerminoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IDetalleTerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.ITerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.UsuarioServiceImpl;
import com.usco.esteban.agenda_procesos.app.util.paginator.PageRender;

@Controller
@SessionAttributes("detalleTermino")
public class DetalleTerminoController {

	@Autowired
	private ITerminoService terminoService;
	
	@Autowired
	private IProcesoService procesoService;
	
	@Autowired
	private IProcesoUsuarioService procesoUsuarioService;
	
	@Autowired
	private IDetalleTerminoService detalleTerminoService;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private TerminoPropertyEditor terminoEditor;
	
	private Usuario usuario;
	
	//private Proceso proceso;
	
	public Long getUserId()
	{
		Long id;
	
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		id = usuario.getId();
		return id;
	}
	
	@ModelAttribute("numeroRoles")
	public int obtenerNumeroRoles()
	{	
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.size();
	}
	
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		binder.registerCustomEditor(Termino.class, "termino", terminoEditor);
	}
	
	private Proceso proceso = null;
	private List<Termino> terminos = new ArrayList<>();
	private boolean banderaCrearTermino = false;
	
	@RequestMapping(value="/crearDetalleTermino/{procesoId}")
	public String crear(@PathVariable(value="procesoId") Long procesoId, Model model, RedirectAttributes flash)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		
		boolean basico = true;
		this.banderaCrearTermino = true;
		
		if(procesoId > 0)
		{
			proceso = procesoService.findOne(procesoId);
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID del proceso no puede ser cero");
			return "redirect:/listarProcesos";
		}
		
		
		
		TipoProceso tipoProceso = proceso.getTipoProceso();
		
		terminos = terminoService.findByEspecialidadAndTipoProcesoAndBasico(especialidad, tipoProceso, basico);
		
		DetalleTermino detalleTermino = new DetalleTermino();
		
		detalleTermino.setProceso(proceso);
		
		model.addAttribute("titulo", "Formulario Detalle Termino para el proceso numero: " + proceso.getId() + " Con radicado: " + proceso.getRadicado());
		model.addAttribute("terminos", terminos);
		
		model.addAttribute("detalleTermino", detalleTermino);
		
		
		return "detalleTermino/formDetalleTermino";
	}
	
	@RequestMapping(value ="/guardarDetalleTermino", method = RequestMethod.POST)
	public String guardar(@RequestParam(name = "page", defaultValue = "0") int page, @Valid DetalleTermino detalleTermino, BindingResult result, Model model, SessionStatus status, RedirectAttributes flash)
	{
		
		if(result.hasErrors())
		{
			model.addAttribute("titulo", "Formulario Detalle Termino para el proceso numero: " + proceso.getId() + " Con radicado: " + proceso.getRadicado());
			model.addAttribute("terminos", terminos);
			
			return "detalleTermino/formDetalleTermino";
		}
		
		/*Long idTermino = Long.parseLong(detalleTermino.getTer());
		Termino termino = terminoService.findOne(idTermino);*/
		detalleTermino.setTermino(detalleTermino.getTermino());
		
		if(this.banderaCrearTermino)
		{
			String nombreNotificacion = detalleTermino.getTermino().getNombre();
			if(nombreNotificacion.equalsIgnoreCase("Notificacion demandado"))
			{
				Calendar fechaNotificacion = detalleTermino.getFechaFinal();
				detalleTermino.setObservacion("Notificado el: " + fechaNotificacion.get(Calendar.DATE) + "-" +
				((fechaNotificacion.get(Calendar.MONTH))+1) + "-" + fechaNotificacion.get(Calendar.YEAR));
			}
		}
		
		this.banderaCrearTermino = false;
		detalleTerminoService.save(detalleTermino);
		
		Proceso proceso = detalleTermino.getProceso();
		String radicado = proceso.getRadicado();
		Long idProceso = proceso.getId();
		
		// ======= bloque de código para listar procesos desde DetalleTerminoControler =========
		//List<Proceso> procesos = procesoService.findAll();
		
		Pageable pageRequest = PageRequest.of(page, 3);
		
		Long id = getUserId();
		//Page<Proceso> procesos = procesoService.findAll(pageRequest);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		
		Page<ProcesoUsuario> procesosUsuario = procesoUsuarioService.findAllById(id, pageRequest, juzgado);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/listarProcesos", procesosUsuario);
		
		
		
		
		/* ===============================================*/
		status.setComplete();
		flash.addFlashAttribute("success", "Se creo el Detalle termino para el proceso numero: "+ idProceso + " Con radicado: " + radicado);
		model.addAttribute("procesos", procesosUsuario);
		model.addAttribute("page", pageRender);
		
		this.proceso = null;
		this.terminos = null;
		
		
		return "redirect:/listarProcesos";
	}

	@RequestMapping(value ="/editarDetalleTermino/{id}")
	public String editarDetalleTermino(@PathVariable(value ="id") Long id, Model model, RedirectAttributes flash)
	{
		DetalleTermino detalleTermino = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		boolean basico = true;
		
		if(id > 0)
		{
			detalleTermino = detalleTerminoService.findOne(id);
			
			if(detalleTermino == null)
			{
				flash.addFlashAttribute("error", "El detalle termino no existe en la base de datos");
				return "redirect:/listarProcesos";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El id del detalle termino no puede ser cero");
			return "redirect:/listarProcesos";
		}
		Proceso proceso = detalleTermino.getProceso();
		TipoProceso tipoProceso = proceso.getTipoProceso();
		
		List<Termino> terminos = terminoService.findByEspecialidadAndTipoProcesoAndBasico(especialidad, tipoProceso, basico);
		
		model.addAttribute("detalleTermino", detalleTermino);
		model.addAttribute("titulo", "Editar Detalle Termino");
		model.addAttribute("terminos", terminos);
		
		return "detalleTermino/formDetalleTermino";
	}
	
	@RequestMapping(value="/eliminarDetalleTermino/{id}")
	public String eliminarDetalleTermino(@PathVariable(value ="id") Long id, RedirectAttributes flash)
	{
		if(id > 0)
		{
			detalleTerminoService.delete(id);
			flash.addFlashAttribute("succes", "Detalle Temrino eliminado con éxito");
		}
		
		return "redirect:/listarProcesos";
	}
}
