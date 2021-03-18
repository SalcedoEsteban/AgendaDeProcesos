package com.usco.esteban.agenda_procesos.app.controllers;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.editors.TipoProcesoPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.dao.IUsuarioDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Alarma;
import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IAlarmaService;
import com.usco.esteban.agenda_procesos.app.models.service.IDetalleTerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.IRolService;
import com.usco.esteban.agenda_procesos.app.models.service.ITerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.ITipoProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;
import com.usco.esteban.agenda_procesos.app.util.paginator.PageRender;
import com.usco.esteban.agenda_procesos.app.validation.CalculaDiasHabiles;

@Controller
/* con este atributo se pasa el objeto (y sus datos) mapeado al formulario a la sesion
 * esto se hace para que los datos queden persistentes hasta que se llame el metodo
 * guardarProceso, donde se cierra la sesion y los datos se borran */
@SessionAttributes("proceso")
public class ProcesoController {

	/* atributo del cliente dao para poder realizar la consulta */
	@Autowired
	private IProcesoService procesoService;
	
	/* con esta anotacion se busca un componente registrado en un contenedor
	 * (el cual es TipoProcesoDaoImpl que esta anotado con @Repository)
	 * que implemente la interfaz ITipoProcesoDao */
	@Autowired
	private ITipoProcesoService tipoProcesoDao;
	
	@Autowired
	private IJuzgadoService juzgadoService;
	
	@Autowired
	private IProcesoUsuarioService procesoUsuarioService;
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IAlarmaService alarmaService;
	
	@Autowired
	private IDetalleTerminoService detalleTerminoService;
	
	@Autowired
	private ITerminoService terminoService;
	
	@Autowired
	private IRolService rolService;
	
	@Autowired
	private TipoProcesoPropertyEditor tipoProcesoEditor;
	
	//private JpaUsuarioDetailsService usuarioService;
	
	private Usuario usuario;

	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		binder.registerCustomEditor(TipoProceso.class, "tipoProceso", tipoProcesoEditor);
	}
	
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
	
	@GetMapping(value="/verDetalleProceso/{id}")
	public String verDetalleProceso(@PathVariable(name = "id") Long id, Map<String, Object> model)
	{
		Proceso proceso = procesoService.findOne(id);
		
		ProcesoUsuario procesoUsuario = procesoUsuarioService.findOne(id);
		
		String nombreUsuarioProceso = procesoUsuario.getUsuario().getNombre();
		String apellidoUsuarioProceso = procesoUsuario.getUsuario().getApellido();	
		
		List<Alarma>alarmas = proceso.getAlarma();
		boolean alarmaAdmision = false;
		
		for(Alarma alarma: alarmas)
		{
			if(alarma.getDescripcion().equalsIgnoreCase("Admision"))
			{
				alarmaAdmision = true;
			}
		}
		
		List<DetalleTermino> detalleTerminos = proceso.getDetalleTerminos();
		boolean terminoAdmision = false;
		
		for(DetalleTermino detalle: detalleTerminos)
		{
			if(detalle.getTermino().getNombre().equalsIgnoreCase("admision"))
			{	
				terminoAdmision = true;
				model.put("terminoAdmision", terminoAdmision);
			}
		}
		
		String radicado = proceso.getRadicado();
		
		Long idTipoProceso = proceso.getTipoProceso().getId();
		TipoProceso tipo = tipoProcesoDao.findOne(idTipoProceso);
		String tipoProceso = tipo.getNombre();
		
		Long idJuzgado = proceso.getJuzgado().getId();
		Juzgado juzgado = juzgadoService.findOne(idJuzgado);
		String nombreJuzgado = juzgado.getNombre();
		
		model.put("alarmaAdmision", alarmaAdmision);
		model.put("proceso", proceso);
		model.put("tipoProceso", tipoProceso);
		model.put("juzgado", nombreJuzgado);
		model.put("titulo", "Detalle de proceso con el Radicado: " + radicado);
		model.put("nombreUsuario", nombreUsuarioProceso);
		model.put("apellidoUsuario", apellidoUsuarioProceso);
		
		return "detalleProceso";
	}
	
	
	@GetMapping(value = "/verDetalleTerminosProceso/{id}")
	public String verDetalleTerminos(@PathVariable(name ="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Proceso proceso = null;
		
		if(id > 0)
		{
			proceso = procesoService.findOne(id);
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe en la base de datos");
				return "redirect:/listarProcesos";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID del proceso no puede ser cero");
			return "redirect:/listarProcesos";
		}
		
		model.put("proceso", proceso);
		model.put("titulo", "Terminos del proceso con el radicado: " + proceso.getRadicado());
		
		return "verTerminos";
	}
	
	@RequestMapping(value="/buscarProceso")
	public String buscarProceso(@RequestParam(value="radicado") String radicado,
			Map<String, Object> model, @RequestParam(name = "page", defaultValue = "0") int page)
	{
		Long id = getUserId();
		
		Usuario usuario = usuarioService.findOne(id);
		Juzgado juzgado = usuario.getJuzgado();
		
		List<Proceso> procesosAVencer = new ArrayList<Proceso>();
		List<Proceso> procesosAdmitir = new ArrayList<Proceso>();
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		Pageable pageRequest = PageRequest.of(page, 15);

		//List<Proceso> proceso = procesoService.findByRadicado(radicado);
		Page<ProcesoUsuario> procesoUsuario = procesoUsuarioService.findByIdAndRadicado(id, pageRequest, radicado);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/buscarProceso", procesoUsuario);
		
		model.put("usuarios", usuarios);
		model.put("procesosAVencer", procesosAVencer);
		model.put("procesosAdmitir", procesosAdmitir);
		model.put("procesos", procesoUsuario);
		model.put("page", pageRender);
		model.put("titulo", "Proceso(s) encontrado(s)"); 
		
		return "verProcesos";
	}
	
	@RequestMapping(value="/buscarProcesoDesactivado")
	public String buscarProcesoDesactivado(@RequestParam(value="radicado") String radicado,
			Map<String, Object> model, @RequestParam(name = "page", defaultValue = "0") int page)
	{
		Long id = getUserId();
		
		Usuario usuario = usuarioService.findOne(id);
		Juzgado juzgado = usuario.getJuzgado();
		
		List<Proceso> procesosAVencer = new ArrayList<Proceso>();
		List<Proceso> procesosAdmitir = new ArrayList<Proceso>();
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		Pageable pageRequest = PageRequest.of(page, 15);

		//List<Proceso> proceso = procesoService.findByRadicado(radicado);
		Page<ProcesoUsuario> procesoUsuario = procesoUsuarioService.findByIdAndRadicadoAndEstadoFalse(id, pageRequest, radicado);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/buscarProceso", procesoUsuario);
		
		model.put("usuarios", usuarios);
		model.put("procesosAVencer", procesosAVencer);
		model.put("procesosAdmitir", procesosAdmitir);
		model.put("procesos", procesoUsuario);
		model.put("page", pageRender);
		model.put("titulo", "Proceso(s) encontrado(s)"); 
		
		return "verProcesosDesactivados";
	}
	
	@RequestMapping(value ="/formAgregarDiasHabiles")
	public String formAgregarDiasHabiles(Model model)
	{
		model.addAttribute("titulo", "Agregar Días Habiles a los terminos de los Procesos");
		
		return "formAgregarDiasHabiles";
	}
	
	@RequestMapping(value="/agregarDiasHabiles", method = RequestMethod.POST)
	public String agregarDiasHabiles(@RequestParam(value="dias") int dias, Map<String, Object> model, RedirectAttributes flash)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		Juzgado juzgado = usuario.getJuzgado();
		
		
		Calendar fechaInicial = Calendar.getInstance(timeZone, locale);
		fechaInicial.set(Calendar.HOUR, 0);
		fechaInicial.set(Calendar.HOUR_OF_DAY, 0);
		fechaInicial.set(Calendar.MINUTE, 0);
		fechaInicial.set(Calendar.SECOND, 0);
		//long fechaInicialMS = fechaInicial.getTimeInMillis();
		
		Calendar fechaFinal = Calendar.getInstance(timeZone, locale);
		fechaFinal.set(Calendar.HOUR, 0);
		fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
		fechaFinal.set(Calendar.MINUTE, 0);
		fechaFinal.set(Calendar.SECOND, 0);
		
		//int diasHabiles = CalculaDiasHabiles.diasHabiles(fechaInicial, fechaFinal);
		
		List<ProcesoUsuario> procesos = procesoUsuarioService.findAll(juzgado);
		
		int diasNoHabiles = 0;
		int diasHabiles = 0;
		int totalDias;
		int mes = 0;
        int dia = 0; 
		
		for(ProcesoUsuario proceso : procesos)
		{
			List<DetalleTermino> detalles = proceso.getProceso().getDetalleTerminos();
			
			for(DetalleTermino detalle: detalles)
			{
				if(detalle.getTermino().getNombre().equalsIgnoreCase("admision"))
				{
					Calendar fechaInicialAdmision = Calendar.getInstance(timeZone, locale);
					fechaInicialAdmision = detalle.getFechaFinal();
					System.out.println("LA FECHA INCIAL ADMISION ES: " + fechaInicialAdmision.getTime());
					
					//Calendar fechaFinalAdmision = Calendar.getInstance(timeZone, locale);
					int day = detalle.getFechaFinal().get(Calendar.DATE);
					int month = detalle.getFechaFinal().get(Calendar.MONTH);
					int year = detalle.getFechaFinal().get(Calendar.YEAR);
					/*fechaFinalAdmision.set(Calendar.DATE, day);
					fechaFinalAdmision.set(Calendar.MONTH, month);
					fechaFinalAdmision.set(Calendar.YEAR, year);*/
					
					for (int i = 0; i < dias; i++)
					{
						fechaInicialAdmision.add(Calendar.DAY_OF_YEAR, 1);
						System.out.println("LE FECHA INICIAL MAS UN DIA ES: " + fechaInicialAdmision.getTime());
						mes = fechaInicialAdmision.get(Calendar.MONTH);
			            dia = fechaInicialAdmision.get(Calendar.DATE);
			            if(fechaInicialAdmision.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
			                    && !(mes == 0 && dia == 1) && !(mes == 4 && dia == 1) &&!(mes == 6 && dia == 20) &&!(mes == 7 && dia == 7)  && !(mes == 11 && dia == 8)
			                    && !(mes == 11 && dia == 25) /*segundo bloque*/ && !(mes == 0 && dia == 6) && !(mes == 2 && dia == 23) && !(mes == 5 && dia == 29)
			                    && !(mes == 7 && dia == 15) && !(mes == 9 && dia == 12) && !(mes == 10 && dia == 2) &&!(mes == 10 && dia == 16)
			                    /* semana santa */ &&! (mes == 3 && dia == 9) && !(mes == 3 && dia == 10)  && !(mes == 3 && dia == 6) && !(mes == 3 && dia == 7) 
			                    && !(mes == 3 && dia == 8) /*fin semana santa*/ && !(mes == 4 && dia == 25) && !(mes == 5 && dia == 15) && !(mes == 5 && dia == 22))
			            {
			                //se aumentan los dias de diferencia entre min y max
			                diasHabiles++;
			            }
			            if(fechaInicialAdmision.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			            {
			            	
			            	System.out.println("HAY UN SABADO EN LOS DIAS");
			            	diasNoHabiles = diasNoHabiles + 1;
			            }
						
					}
					
					
					fechaInicialAdmision.set(Calendar.DATE, day);
					fechaInicialAdmision.set(Calendar.MONTH, month);
					fechaInicialAdmision.set(Calendar.YEAR, year);
					
					diasNoHabiles = (diasNoHabiles) + (dias - diasHabiles);
					
					totalDias = dias + diasNoHabiles;
					System.out.println("LOS DIAS NO HABILES SON: " + diasNoHabiles);
					System.out.println("LOS DIAS TOTALES SON: " + totalDias);
					//fechaFinalAdmision = detalle.getFechaFinal();
					//fechaInicialAdmision = detalle.getFechaFinal();
					System.out.println("LA FECHA INICIAL ADMISION ES: " + fechaInicialAdmision.getTime());
					//System.out.println("LA FECHA FINAL ADMISION ES: " + fechaFinalAdmision.getTime());
					fechaInicialAdmision.add(Calendar.DATE, totalDias);
					System.out.println("LA FECHA FINAL ADMISION MAS " + totalDias + " es " + fechaInicialAdmision.getTime());
					mes = fechaInicialAdmision.get(Calendar.MONTH);
					dia = fechaInicialAdmision.get(Calendar.DATE);
					
					if((mes == 0 && dia == 1) || (mes == 4 && dia == 1) || (mes == 6 && dia == 20) || (mes == 7 && dia == 7)  || (mes == 11 && dia == 8)
                    || (mes == 11 && dia == 25) /*segundo bloque*/ || (mes == 0 && dia == 6) || (mes == 2 && dia == 23) || (mes == 5 && dia == 29)
                    || (mes == 7 && dia == 15) || (mes == 9 && dia == 12) || (mes == 10 && dia == 2) || (mes == 10 && dia == 16)
                    /* semana santa */ || (mes == 3 && dia == 9) || (mes == 3 && dia == 10)  || (mes == 3 && dia == 6) || (mes == 3 && dia == 7) 
                    || (mes == 3 && dia == 8) /*fin semana santa*/ || (mes == 4 && dia == 25) || (mes == 5 && dia == 15) || (mes == 5 && dia == 22))
					{
						System.out.println("EL ULTIMO DIA ES FESTIVO, SE AGREGA UN DIA MAS");
						fechaInicialAdmision.add(Calendar.DATE, 1);
					}
					
					mes = fechaInicialAdmision.get(Calendar.MONTH);
					dia = fechaInicialAdmision.get(Calendar.DATE);
					
					if((mes == 0 && dia == 1) || (mes == 4 && dia == 1) || (mes == 6 && dia == 20) || (mes == 7 && dia == 7)  || (mes == 11 && dia == 8)
		                    || (mes == 11 && dia == 25) /*segundo bloque*/ || (mes == 0 && dia == 6) || (mes == 2 && dia == 23) || (mes == 5 && dia == 29)
		                    || (mes == 7 && dia == 15) || (mes == 9 && dia == 12) || (mes == 10 && dia == 2) || (mes == 10 && dia == 16)
		                    /* semana santa */ || (mes == 3 && dia == 9) || (mes == 3 && dia == 10)  || (mes == 3 && dia == 6) || (mes == 3 && dia == 7) 
		                    || (mes == 3 && dia == 8) /*fin semana santa*/ || (mes == 4 && dia == 25) || (mes == 5 && dia == 15) || (mes == 5 && dia == 22))
							{
								System.out.println("EL ULTIMO DIA TAMBIEN ES FESTIVO, SE AGREGA UN DIA MAS");
								fechaInicialAdmision.add(Calendar.DATE, 1);
							}
					
					if(fechaInicialAdmision.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
		            {
						System.out.println("EL ULTIMO DIA ES UN SABADO, SE AGREGA UN DIA");
						fechaInicialAdmision.add(Calendar.DATE, 1);
		            }
					
					if(fechaInicialAdmision.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		            {
						System.out.println("EL ULTIMO DIA ES UN DOMINGO, SE AGREGA UN DIA");
						fechaInicialAdmision.add(Calendar.DATE, 1);
						if(dias == 2 )
						{
							System.out.println("LOS DIAS SON INGUALES A DOS");
							fechaInicialAdmision.add(Calendar.DATE, 1);
						}
		            }
					
					//detalleTerminoService.save(detalle);
					diasNoHabiles = 0;
					totalDias = 0;
					//flash.addFlashAttribute("success", "Fueron agregados " + dias +" días habiles correctamente a los terminos de los procesos");	
				}
				detalleTerminoService.save(detalle);
				if(detalle.getTermino().getNombre().equalsIgnoreCase("subsanar demanda"))
				{
					
					Calendar fechaInicialSubsanar = Calendar.getInstance(timeZone, locale);
					fechaInicialSubsanar = detalle.getFechaFinal();
					System.out.println("LA FECHA INCIAL SUBSANAR ES: " + fechaInicialSubsanar.getTime());
					
					Calendar fechaFinalAdmision = Calendar.getInstance(timeZone, locale);
					int day = detalle.getFechaFinal().get(Calendar.DATE);
					int month = detalle.getFechaFinal().get(Calendar.MONTH);
					int year = detalle.getFechaFinal().get(Calendar.YEAR);
					fechaFinalAdmision.set(Calendar.DATE, day);
					fechaFinalAdmision.set(Calendar.MONTH, month);
					fechaFinalAdmision.set(Calendar.YEAR, year);
					
					for (int i = 0; i < dias; i++)
					{
						fechaInicialSubsanar.add(Calendar.DAY_OF_YEAR, 1);
						System.out.println("LE FECHA INICIAL MAS UN DIA ES: " + fechaInicialSubsanar.getTime());
						mes = fechaInicialSubsanar.get(Calendar.MONTH);
			            dia = fechaInicialSubsanar.get(Calendar.DATE);
			            if(fechaInicialSubsanar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY
			                    && !(mes == 0 && dia == 1) && !(mes == 4 && dia == 1) &&!(mes == 6 && dia == 20) &&!(mes == 7 && dia == 7)  && !(mes == 11 && dia == 8)
			                    && !(mes == 11 && dia == 25) /*segundo bloque*/ && !(mes == 0 && dia == 6) && !(mes == 2 && dia == 23) && !(mes == 5 && dia == 29)
			                    && !(mes == 7 && dia == 15) && !(mes == 9 && dia == 12) && !(mes == 10 && dia == 2) &&!(mes == 10 && dia == 16)
			                    /* semana santa */ &&! (mes == 3 && dia == 9) && !(mes == 3 && dia == 10)  && !(mes == 3 && dia == 6) && !(mes == 3 && dia == 7) 
			                    && !(mes == 3 && dia == 8) /*fin semana santa*/ && !(mes == 4 && dia == 25) && !(mes == 5 && dia == 15) && !(mes == 5 && dia == 22))
			            {
			                //se aumentan los dias de diferencia entre min y max
			                diasHabiles++;
			            }
			            if(fechaInicialSubsanar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
			            {
			            	
			            	System.out.println("HAY UN SABADO EN LOS DIAS");
			            	diasNoHabiles = diasNoHabiles + 1;
			            }
						
					}
					
					
					fechaInicialSubsanar.set(Calendar.DATE, day);
					fechaInicialSubsanar.set(Calendar.MONTH, month);
					fechaInicialSubsanar.set(Calendar.YEAR, year);
					
					diasNoHabiles = (diasNoHabiles) + (dias - diasHabiles);
					
					totalDias = dias + diasNoHabiles;
					System.out.println("LOS DIAS NO HABILES SON: " + diasNoHabiles);
					System.out.println("LOS DIAS TOTALES SON: " + totalDias);
					//fechaFinalAdmision = detalle.getFechaFinal();
					//fechaInicialAdmision = detalle.getFechaFinal();
					System.out.println("LA FECHA INICIAL SUBSANAR ES: " + fechaInicialSubsanar.getTime());
					//System.out.println("LA FECHA FINAL ADMISION ES: " + fechaFinalAdmision.getTime());
					fechaInicialSubsanar.add(Calendar.DATE, totalDias);
					System.out.println("LA FECHA FINAL SUBSANAR MAS " + totalDias + " es " + fechaInicialSubsanar.getTime());
					mes = fechaInicialSubsanar.get(Calendar.MONTH);
					dia = fechaInicialSubsanar.get(Calendar.DATE);
					
					if((mes == 0 && dia == 1) || (mes == 4 && dia == 1) || (mes == 6 && dia == 20) || (mes == 7 && dia == 7)  || (mes == 11 && dia == 8)
                    || (mes == 11 && dia == 25) /*segundo bloque*/ || (mes == 0 && dia == 6) || (mes == 2 && dia == 23) || (mes == 5 && dia == 29)
                    || (mes == 7 && dia == 15) || (mes == 9 && dia == 12) || (mes == 10 && dia == 2) || (mes == 10 && dia == 16)
                    /* semana santa */ || (mes == 3 && dia == 9) || (mes == 3 && dia == 10)  || (mes == 3 && dia == 6) || (mes == 3 && dia == 7) 
                    || (mes == 3 && dia == 8) /*fin semana santa*/ || (mes == 4 && dia == 25) || (mes == 5 && dia == 15) || (mes == 5 && dia == 22))
					{
						System.out.println("EL ULTIMO DIA ES FESTIVO, SE AGREGA UN DIA MAS");
						fechaInicialSubsanar.add(Calendar.DATE, 1);
					}
					
					mes = fechaInicialSubsanar.get(Calendar.MONTH);
					dia = fechaInicialSubsanar.get(Calendar.DATE);
					
					if((mes == 0 && dia == 1) || (mes == 4 && dia == 1) || (mes == 6 && dia == 20) || (mes == 7 && dia == 7)  || (mes == 11 && dia == 8)
		                    || (mes == 11 && dia == 25) /*segundo bloque*/ || (mes == 0 && dia == 6) || (mes == 2 && dia == 23) || (mes == 5 && dia == 29)
		                    || (mes == 7 && dia == 15) || (mes == 9 && dia == 12) || (mes == 10 && dia == 2) || (mes == 10 && dia == 16)
		                    /* semana santa */ || (mes == 3 && dia == 9) || (mes == 3 && dia == 10)  || (mes == 3 && dia == 6) || (mes == 3 && dia == 7) 
		                    || (mes == 3 && dia == 8) /*fin semana santa*/ || (mes == 4 && dia == 25) || (mes == 5 && dia == 15) || (mes == 5 && dia == 22))
							{
								System.out.println("EL ULTIMO DIA TAMBIEN ES FESTIVO, SE AGREGA UN DIA MAS");
								fechaInicialSubsanar.add(Calendar.DATE, 1);
							}
					
					if(fechaInicialSubsanar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY)
		            {
						System.out.println("EL ULTIMO DIA ES UN SABADO, SE AGREGA UN DIA");
						fechaInicialSubsanar.add(Calendar.DATE, 1);
		            }
					
					if(fechaInicialSubsanar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		            {
						System.out.println("EL ULTIMO DIA ES UN DOMINGO, SE AGREGA UN DIA");
						fechaInicialSubsanar.add(Calendar.DATE, 1);
						if(dias == 2 )
						{
							System.out.println("LOS DIAS SON INGUALES A DOS");
							fechaInicialSubsanar.add(Calendar.DATE, 1);
						}
		            }
					
					//detalleTerminoService.save(detalle);
					diasNoHabiles = 0;
					totalDias = 0;
					//flash.addFlashAttribute("success", "Fueron agregados " + dias +" días habiles correctamente a los terminos de los procesos");					
				}
				detalleTerminoService.save(detalle);
			}
			
			
		}
		
		flash.addFlashAttribute("success", "Fueron agregados " + dias +" días habiles correctamente a los terminos de los procesos");
		return "redirect:/listarProcesos";
	}
	
	//===========================================
	@RequestMapping(value ="/formAgregarDiasCalendario")
	public String formAgregarDias(Model model)
	{
		model.addAttribute("titulo", "Agregar Días Calendario a los terminos de los Procesos");
		
		return "formAgregarDiasCalendario";
	}
	
	
	@RequestMapping(value="/agregarDiasCalendario", method = RequestMethod.POST)
	public String agregarDiasCalendario(@RequestParam(value="dias") int dias, Map<String, Object> model, RedirectAttributes flash)
	{
			//Long id = getUserId();
			//int diasParaAgregar = dias;
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			usuario = this.usuarioService.findByUsername(userDetail.getUsername());
			
			Juzgado juzgado = usuario.getJuzgado();
			
			List<ProcesoUsuario> procesos = procesoUsuarioService.findAll(juzgado);
			
			TimeZone timeZone = TimeZone.getDefault();
			Locale locale = Locale.getDefault();
			
			for(ProcesoUsuario proceso : procesos)
			{
				List<DetalleTermino> detalles = proceso.getProceso().getDetalleTerminos();
				
				for(DetalleTermino detalle: detalles)
				{
					
					if(detalle.getTermino().getNombre().equalsIgnoreCase("Termino 121"))
					{
						Calendar fechaFinal = Calendar.getInstance(timeZone, locale);
						fechaFinal = detalle.getFechaFinal();
						fechaFinal.add(Calendar.DAY_OF_YEAR, dias);
						detalleTerminoService.save(detalle);
					} 
					
				}
			}
			
			flash.addFlashAttribute("success", "Fueron agregados " + dias +" días Calendario correctamente al termino 121 C.G.P de todos los procesos");
			return "redirect:/listarProcesos";
		}
	
	@RequestMapping(value="/agregarProrroga/{id}")
	public String agregarProrroga(@PathVariable(value ="id")Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		//Long id = getUserId();
		//int diasParaAgregar = dias;
		Proceso proceso = procesoService.findOne(id);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		//Juzgado juzgado = usuario.getJuzgado();
		
		//List<ProcesoUsuario> procesos = procesoUsuarioService.findAllByPrioritario(juzgado);
		
		TimeZone timeZone = TimeZone.getDefault();
		Locale locale = Locale.getDefault();
		
		List<DetalleTermino> detalles = proceso.getDetalleTerminos();
			
		for(DetalleTermino detalle: detalles)
		{
			String nombre = detalle.getTermino().getNombre();
				
			if(nombre.equalsIgnoreCase("Termino 121"))
			{
				Calendar fecha = Calendar.getInstance(timeZone, locale);
				fecha = detalle.getFechaFinal();
				fecha.add(Calendar.MONTH, 5);
				detalleTerminoService.save(detalle);
			}	
				
		}
		
		flash.addFlashAttribute("success", "Fueron agregado la Prorroga correctamente a los terminos del proceso");
		return "redirect:/listarProcesos";
	}
	
	@RequestMapping(value="/dictarSentencia/{id}")
	public String dictarSentencia(@PathVariable(value ="id") Long id, RedirectAttributes flash)
	{
		Proceso proceso = null;
		
		if(id > 0)
		{
			proceso = procesoService.findOne(id);
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El id del proceso no puede ser cero");
			return "redirect:/listarProcesos";
		}
		
		List<DetalleTermino> terminos = proceso.getDetalleTerminos();
		Calendar fechaSentencia = Calendar.getInstance();
		
		for(DetalleTermino termino: terminos)
		{
			String nombre = termino.getTermino().getNombre();
			
			if(nombre.equalsIgnoreCase("Termino 121"))
			{
				termino.setObservacion("Sentencia dictada el: " + fechaSentencia.get(Calendar.DATE) + "-" + ((fechaSentencia.get(Calendar.MONTH))+1) + "-" + fechaSentencia.get(Calendar.YEAR));
				detalleTerminoService.save(termino);
			}
		}
		
		//String strSentencia = "Se hizo la sentencia";
		Calendar fechaSentenciaProceso = Calendar.getInstance(timeZone, locale);
		
		proceso.setActuacionUltima("Sentencia dictada");
		proceso.setActualEstado("Sentencia dictada el: " + fechaSentenciaProceso.get(Calendar.DATE)
		+ "-" + (fechaSentenciaProceso.get(Calendar.MONTH)+1) + "-" + fechaSentenciaProceso.get(Calendar.YEAR));
		
		proceso.setSentencia(true);
		proceso.setEstado(false);
		procesoService.save(proceso);
		
		flash.addFlashAttribute("success", "Ha sido dicatada la sentencia del proceso con radicado: ".concat(proceso.getRadicado()));
		
		return "redirect:/listarProcesos";
	}
	

//	private int page;
//	private Pageable pageRequest = PageRequest.of(page, 3);
//	private List<ProcesoUsuario> list = new ArrayList<ProcesoUsuario>();
//	private Page<ProcesoUsuario> procesosUsuarioListar = new PageImpl<>(list, pageRequest, list.size());
	private Usuario usuarioListar;
	private Page<ProcesoUsuario> procesosUsuarioListar;
	private Long idUsuario;
//	private Page<ProcesoUsuario> procesosUsuarioListarAsignar;
	
	@RequestMapping(value="/listarProcesosPorUsuario")
	public String listarProcesosPorUsuario(@RequestParam(value="idUsuario", required = false, defaultValue = "0")  Long idUsuario, @RequestParam(value="page", defaultValue = "0") int page, Model model, RedirectAttributes flash)
	{
		
		Pageable pageRequest = PageRequest.of(page, 15);
		//Long id = getUserId();
		
		if(idUsuario > 0)
		{
			this.idUsuario = idUsuario;
		}
		
		boolean botonBandera = true;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		//Usuario usuario = usuarioService.findOne(idUsuario);
		
//		if(idUsuario == null)
//		{
//			model.addAttribute("titulo", )
//		}
		
		System.out.println("EL ID DESDE EL ARGUMENTO ES: " + idUsuario);
		System.out.println("EL ID DESDE LA VARIABLE DE INSTANCIA ES: " + this.idUsuario);
		
		if(usuarioListar == null)
		{
			if(idUsuario.toString().contentEquals("0")) {
				System.out.println("EL DEFAULT VALUE ES CERO");
				usuarioListar = usuarioService.findOne(this.idUsuario);
			}
			else
			{
				System.out.println("EL DEFAULT VALUE NO ES CERO");
				usuarioListar = usuarioService.findOne(idUsuario);
			}
				
		}
		
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		List<Proceso> procesosAVencer = new ArrayList<Proceso>();
		List<Proceso> procesosAdmitir = new ArrayList<Proceso>();
		
//		if(procesosUsuarioListar.isEmpty()) {
//			procesosUsuarioListar = procesoUsuarioService.findAllById(idUsuario, pageRequest, juzgado);
//		}
		
		//System.out.println("procesosUsuarioListar está vacio?: " + procesosUsuarioListar.isEmpty());
		
//		if(procesosUsuarioListar == null)
//		{
//			System.out.println("SE HIZO LA CONSULTA DE PROCESOUSUARIOLISTAR PORQUE ES NULL");
//			procesosUsuarioListar = procesoUsuarioService.findAllById(idUsuario, pageRequest, juzgado);
//		}
		
		
		
		procesosUsuarioListar = procesoUsuarioService.findAllById(this.idUsuario, pageRequest, juzgado);
		
		
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/listarProcesosPorUsuario", procesosUsuarioListar);
		
		model.addAttribute("procesosAVencer", procesosAVencer);
		model.addAttribute("procesosAdmitir", procesosAdmitir);
		//model.addAttribute("usuario", usuario);
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("botonBandera", botonBandera);
		model.addAttribute("procesos", procesosUsuarioListar);
		model.addAttribute("page", pageRender);
		model.addAttribute("titulo", "Listado de procesos del Usuario: ".concat(usuarioListar.getNombre().concat(usuarioListar.getApellido())));
		
		usuarioListar = null;
		
		botonBandera = false;
		
		return "listarProcesos";
	}

	@RequestMapping(value="/formAsignarProcesos")
	public String formAsignarProceso(Model model)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		Juzgado juzgado = usuario.getJuzgado();
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		
		model.addAttribute("titulo", "Asignar procesos a usuario");
		model.addAttribute("usuarios", usuarios);
		
		return "formAsignarProceso";
	}
	
	@PostMapping(value="/asignarProcesos")
	public String asignarProcesos(@RequestParam(value="idUsuario") String idUsuario, RedirectAttributes flash)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Long id = Long.parseLong(idUsuario);
		Usuario usuario = usuarioService.findOne(id);
		
		for(ProcesoUsuario proceso: procesosUsuarioListar)
		{
			proceso.setUsuario(usuario);
			procesoUsuarioService.save(proceso);
		}
		
		procesosUsuarioListar = null;
		
		flash.addFlashAttribute("success", "Los procesos fueron asignados al usuario: ".concat(usuario.getNombre().concat(" ").concat(usuario.getApellido())));
		
		
		return "redirect:/listarProcesos";
	}
	
	//DetalleTermino detalleTermino = new DetalleTermino();
	private boolean terminoAnio;
	private Proceso proceso1 = null;
	private Calendar fechaActual1;
	private Calendar fechaFinal1;
	private Calendar fechaAdmision = null;
	private Locale locale = Locale.getDefault();
	private TimeZone timeZone = TimeZone.getDefault();
	//private Calendar fechaInicial121 = Calendar.getInstance();
	
	//private boolean bandera2 = false;
	
	
	@RequestMapping(value = "/listarProcesos", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, RedirectAttributes flash)
	{
		Pageable pageRequest = PageRequest.of(page, 15);
		Long id = getUserId();
		//Page<Proceso> procesos = procesoService.findAll(pageRequest);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		
//		Page<ProcesoUsuario> procesosUsuario = procesoUsuarioService.findAllById(id, pageRequest, juzgado);
		
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		model.addAttribute("usuarios", usuarios);
		
		
		Page<ProcesoUsuario> procesosUsuario = null;
		
		
		/* ========== bloque de codigo que valida los procesos que se listaran 
		 * dependiendo del tipo de usuario ====================================*/
		List<Rol> roles = rolService.findByUsuario(usuario);
		//System.out.println("la lista roles es vacia: " + roles.isEmpty());
		
		String nombre = null; 
		
		for (Rol rol : roles)
		{
			
			//System.out.println(rol.getRol());
			
			//nombre = rol.getRol();
			
			if(rol.getRol().contentEquals("ROLE_USER"))
			{
				//System.out.println("rol desde el if por SUPER_USER: " + rol.getRol());
				nombre = rol.getRol();
				break;
			}
			else if(rol.getRol().contentEquals("ROLE_ADMIN"))
			{
				//System.out.println("rol desde el if por ROLE_ADMIN: " + rol.getRol());
				nombre = rol.getRol();
				break;
			}
			else if(rol.getRol().contentEquals("ROLE_SUPER_ADMIN"))
			{
				//System.out.println("rol desde el if por ROLE_SUPER_ADMIN: " + rol.getRol());
				nombre = rol.getRol();
				break;
			}
		}
		
		//System.out.println("el rol es: " + nombre);
		
		if(nombre.contentEquals("ROLE_SUPER_ADMIN"))
		{
			//System.out.println("se hace la consulta de todos los procesos activos de todos los juzgados");
			procesosUsuario = procesoUsuarioService.findAllBySuperAdmin(pageRequest);
		}
		else if(nombre.contentEquals("ROLE_ADMIN"))
		{
			//System.out.println("se hace la consulta de los procesos por juzgado");
			/*se ejecuta la consulta de proceos por id y juzgado para que el admin tenga la opcion
			 * de filtrar los procesos por usuario */
			procesosUsuario = procesoUsuarioService.findAllById(id, pageRequest, juzgado);
		}
		else if(nombre.contentEquals("ROLE_USER"))
		{
			//System.out.println("se hace la consulta de los proceos por usuario");
			procesosUsuario = procesoUsuarioService.findAllById(id, pageRequest, juzgado);
		}
		
		/*====== fin de bloque de codigo de validacion de los procesos que si listaran*/
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/listarProcesos", procesosUsuario); 
		
		for (ProcesoUsuario procesoUsuario : procesosUsuario) {
			System.out.println(procesoUsuario.getProceso().getRadicado());
		}
		
		
		/* ====== codigo para probar las notficaciones de vencimiento de terminos =====*/
		
		//boolean terminoNotificacionDemandado = false;
		
		boolean estado = false;
		int dias1 = 0;
		//Termino termino1 = new Termino();
		
		//Alarma alarma = new Alarma();
		//int dias1 = 0;
		//Proceso proceso = null;
		boolean bandera = false;
		Termino termino = new Termino();
		boolean bandera1 = false;
		boolean subsanaDemanda = false;
		int dias = 0;
		int diasSubsanarDemanda = 0;
		//boolean banderaFechaTermino121 = false;
		//boolean banderaSubsanaDemanda = false;
		int procesosAdmitidos =0;
		int procesosConSentencia = 0;
		String radicado = null;
		Long idProceso = null;
		//Calendar fechaInicialTermino121 = Calendar.getInstance();
		
		/*lista de procesos que estarn cercanos a vencer*/
		List<Proceso> procesosAVencer = new ArrayList<Proceso>();
		List<Proceso> procesosAdmitir = new ArrayList<Proceso>();
		
		for (ProcesoUsuario procesoUsuario : procesosUsuario)
		{
			List<DetalleTermino> detallesTermino = procesoUsuario.getProceso().getDetalleTerminos();
			//boolean detalleBoolean = false;
			
			Proceso proceso = procesoUsuario.getProceso();
			proceso1 = procesoUsuario.getProceso();
			//List<Alarma> alarmas = proceso.getAlarma();
			for(DetalleTermino detalle: detallesTermino)
			{
				termino = detalle.getTermino();
				
				//System.out.println("los terminos del proceso son: "+ detalle.getTermino().getNombre());
				
				/*if(detalle.getTermino().getNombre().contentEquals("notificacion demandado"))
				{
					terminoNotificacionDemandado = true;
					System.out.println("notificacion igual a true");
				}*/
				
				if(termino.getNombre().equalsIgnoreCase("Termino 121"))
				{
					
					this.terminoAnio = true;
					
					System.out.println("contiene el termino 121" + terminoAnio);
				}else
				{
					
					this.terminoAnio = false;
					System.out.println("no contiene el termino 121" + terminoAnio);
				}
				
				if(termino.getNombre().equalsIgnoreCase("admision"))
				{
					if(proceso.getUltimaActuacion().isEmpty())
					{
						proceso.setAdmision(true);
					}
					
					
					System.out.println("si contiene admision en el detalle termino");
					
					Calendar fechaActual = Calendar.getInstance();
					
					//Calendar fechaInicial = detalle.getFechaInicial();
					Calendar fechaFinal = detalle.getFechaFinal();
					
					fechaActual.set(Calendar.HOUR, 0);
					fechaActual.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual.set(Calendar.MINUTE, 0);
					fechaActual.set(Calendar.SECOND, 0);
					
					fechaFinal.set(Calendar.HOUR, 0);
					fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal.set(Calendar.MINUTE, 0);
					fechaFinal.set(Calendar.SECOND, 0);

					/*long fechaFinalMS = fechaFinal.getTimeInMillis();
					long fechaActualMS = fechaActual.getTimeInMillis();*/
					
					//int dias = (int) ((Math.abs(fechaFinalMS - fechaActualMS)) / (1000 * 60 * 60* 24));
//					dias = (int) ((fechaFinalMS - fechaActualMS) / (1000 * 60 * 60* 24));

					//Calendar fechaAdmision = obtenerFechaAdmision();
					//int diasAdmision = 0;
					
					dias = CalculaDiasHabiles.diasHabiles(fechaActual, fechaFinal);
					System.out.println("============= fecha inicial: " + fechaActual.getTime());
					System.out.println("============= fecha final: " + fechaFinal.getTime());
					System.out.println("============= numero días de admision: " + dias);
						
					if(proceso.getUltimaActuacion().equalsIgnoreCase("admitido")
								&& dias > 0)
					{ 
						System.out.println("ultima actuacion es: Admitido");
						proceso.setAdmisionEnTreintaDias(true);
					}
					else if(proceso.getUltimaActuacion().equalsIgnoreCase("admitido") && dias == 0)
					{
						if(proceso.isAdmisionEnTreintaDias())
						{
							proceso.setAdmisionEnTreintaDias(true);
						}
						else {
							proceso.setAdmisionEnTreintaDias(false);
						}
						
					}
					
					
					//System.out.println("los terminos del proceso de nuevo son: "+ detalle.getTermino().getNombre());	
				}
				
				if(termino.getNombre().equalsIgnoreCase("subsanar demanda"))
				{
					//proceso.setActualEstado("Por subsanar demanda");
					if(proceso.getUltimaActuacion().isEmpty())
					{
						proceso.setSubsanarDemanda(true);
					}
					
					
					Calendar fechaActual = Calendar.getInstance(timeZone, locale);
					Calendar fechaFinal = detalle.getFechaFinal();
					
					fechaActual.set(Calendar.HOUR, 0);
					fechaActual.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual.set(Calendar.MINUTE, 0);
					fechaActual.set(Calendar.SECOND, 0);
					//long fechaActualMS = fechaActual.getTimeInMillis();
					
					fechaFinal.set(Calendar.HOUR, 0);
					fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal.set(Calendar.MINUTE, 0);
					fechaFinal.set(Calendar.SECOND, 0);
					//long fechaFinalMS = fechaFinal.getTimeInMillis();
					
//					diasSubsanarDemanda = (int) ((fechaFinalMS - fechaActualMS) / (1000 * 60 * 60* 24));
					diasSubsanarDemanda = CalculaDiasHabiles.diasHabiles(fechaActual, fechaFinal);
					System.out.println("los numero de días para subsana demanda son: " + diasSubsanarDemanda);
					
					/*if(diasSubsanarDemanda > 0)
					{
						banderaSubsanaDemanda = true;
					}*/
					
				}
				estado = proceso.isAdmisionEnTreintaDias();
				
				if(termino.getNombre().equalsIgnoreCase("notificacion demandado") && estado == true ) // && dias1 != 0 && dias1 == 30
				{
					proceso.setSentenciaBandera(true);
					proceso.setActuacionUltima("Notificacion al demandado");
					proceso.setActualEstado("En espera de vencimiento del año");
					
					System.out.println("si hay termino de notificacion demandado");
					System.out.println("Para el termino 121, Se está tomando desde la fecha de notificación al demandado");
					
					
					/*la fecha final cuando esté en producción la aplicación será esta, más 365
					 * días que es el año del 121  */
//					Calendar notificacionDemandado = Calendar.getInstance();
//					notificacionDemandado = detalle.getFechaFinal();
//					int dia = notificacionDemandado.get(Calendar.DATE);
//					int mes = notificacionDemandado.get(Calendar.MONTH);
//					int anio = notificacionDemandado.get(Calendar.YEAR);
					
					/*la fecha actual si está bien (es decir, zona horaria de colombia y zona local de bogotá, lo cual si es asi porqe por defecto trae esas zona horaria y zona local), 
					 * los calculos se hacen con la fecha final que 'es la de notificacion
					 * demandado + los 365 días' menos la fecha actual */
					fechaActual1 = Calendar.getInstance(timeZone, locale);
					
					/*para efectos de las pruebas, se establecio la fecha final de esta forma
					 * para que los días den 30 para que asi se pueda ejecutar la alarma y notificacion
					 * de 30 días antes del vencimiento de términos */
					fechaFinal1 = Calendar.getInstance(timeZone, locale);
					
					
					/*para poner en produccion estas variables se quitan o comentarean*/
					int dia = 11;
					int mes = 1;
					int anio = 2021;
					
					//this.fechaInicial121.set(anio, mes, dia);
					fechaFinal1.set(anio, mes, dia);
//					System.out.println("la fecha final establecida para que de 30 es: " + fechaFinal1.getTime());
					//============== esta es la fecha cuando esté en produccion =======================
					//fechaFinal1.add(Calendar.DAY_OF_YEAR, 365);
//					System.out.println("fecha final + 365: " + fechaFinal1.getTime());
					
					//System.out.println("termino año es igual a: " + terminoAnio);
					
					//System.out.println("if de termino año igual a true");
					
					fechaActual1.set(Calendar.HOUR, 0);
					fechaActual1.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual1.set(Calendar.MINUTE, 0);
					fechaActual1.set(Calendar.SECOND, 0);
					long fechaActual1MS = fechaActual1.getTimeInMillis();
					
					fechaFinal1.set(Calendar.HOUR, 0);
					fechaFinal1.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal1.set(Calendar.MINUTE, 0);
					fechaFinal1.set(Calendar.SECOND, 0);
					long fechaFinal1MS = fechaFinal1.getTimeInMillis();
					
					dias1 = (int) ((Math.abs(fechaFinal1MS - fechaActual1MS)) / (1000 * 60 * 60* 24));
					System.out.println("numero de días para el vencimiento del 121 son: " + dias1);
					bandera1 = true;
					
				}
				else if(termino.getNombre().equalsIgnoreCase("notificacion demandado") && estado == false)
				{
					proceso.setSentenciaBandera(true);
					proceso.setActuacionUltima("Notificacion al demandado");
					proceso.setActualEstado("En espera de vencimiento del año");
					
					System.out.println("Para el termino 121, Se está tomando desde la fecha de REPARTO" + " y la variable termino año es: " + this.terminoAnio);
					
					//Calendar fechaReparto = Calendar.getInstance(timeZone, locale);
					Calendar fechaNotificacionDemandado = Calendar.getInstance(timeZone, locale);
					//fechaReparto = proceso.getFechaReparto();
					/* se toma la fecha de notificacion al demandado como fecha a la que se le van a sumar los 336 días, (es decir un mes menos
					 * debido a que se admitio despues de los 30 día del termino de admision) porque pueden pasar meses hasta que se notifique al o los demandados y si se toma
					 * desde la fecha de reparto, esos meses que se demora en notificarle al demandado serán meses menos cuando se calculen los dias restantes de los 336
					 * que se agregaron con anterioridad*/
					fechaNotificacionDemandado = detalle.getFechaFinal();
					int dia = fechaNotificacionDemandado.get(Calendar.DATE);
					int mes = fechaNotificacionDemandado.get(Calendar.MONTH);
					int anio = fechaNotificacionDemandado.get(Calendar.YEAR);
					
					fechaActual1 = Calendar.getInstance(timeZone, locale);
					fechaFinal1 = Calendar.getInstance(timeZone, locale);
					
					fechaFinal1.set(Calendar.DATE, dia);
					fechaFinal1.set(Calendar.MONTH, mes);
					fechaFinal1.set(Calendar.YEAR, anio);
					
					fechaFinal1.add(Calendar.DAY_OF_YEAR, 336);
					
					//fechaActual1 = Calendar.getInstance(timeZone, locale);
					//fechaFinal1 = Calendar.getInstance(timeZone, locale);
					
					fechaActual1.set(Calendar.HOUR, 0);
					fechaActual1.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual1.set(Calendar.MINUTE, 0);
					fechaActual1.set(Calendar.SECOND, 0);
					long fechaActual1MS = fechaActual1.getTimeInMillis();
					
					fechaFinal1.set(Calendar.HOUR, 0);
					fechaFinal1.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal1.set(Calendar.MINUTE, 0);
					fechaFinal1.set(Calendar.SECOND, 0);
					long fechaFinal1MS = fechaFinal1.getTimeInMillis();
					
					dias1 = (int) ((Math.abs(fechaFinal1MS - fechaActual1MS)) / (1000 * 60 * 60* 24));
					System.out.println("numero de días para el vencimiento del 121 son: " + dias1);
					bandera1 = true;
				}
			}
			
			procesoService.save(proceso);
			
			System.out.println("LA BANDERA1 ES: " + bandera1);
			
			if(terminoAnio == false && bandera1 == true)
			{
				guardarDetalleTermino();
			}
			bandera1 = false;
			
			/*============= aqui va el codigo de dias = 15 para la admision ========*/
			if(dias <= 15 && dias > 0)
			{
				//this.bandera2 = true;
				procesosAdmitir.add(proceso);
				
				if(proceso.getUltimaActuacion().isEmpty())
				{
					proceso.setActuacionUltima("");
					proceso.setActualEstado("Cerca a vencer la admisiónn");
				}

				//proceso.setEstadoActual("Por admitir");
				/*bloque de codido para crear la alarma del proceso*/
				
				String descripcion = "Admision";
				Alarma alarma1 = alarmaService.findByDescripcionAndProceso(descripcion, proceso);
				
				if(alarma1 == null)
				{
					guardarAlarmaAdmision(proceso);
				}

				//System.out.println("dias igual a 15");
				/*flash.addFlashAttribute("error", "el termino " + detalle.getTermino().getNombre() + " del proceso con radicado: "+ 
				procesoUsuario.getProceso().getRadicado()+ " está a punto de vencer");*/
				model.addAttribute("procesosAdmitir", procesosAdmitir);
			}
			dias = 0;
			/*=========================================================*/
			
			
			/*============== codigo de alarma de 30 dias para el vencimiento de terminos ======*/
			System.out.println("dias para el vencimiento 121 son: " + dias1);
			if(dias1 <= 30 && dias1 > 0)
			{
				proceso.setActuacionUltima("Notificación demandado");
				proceso.setActualEstado("Cerca a vencer el año");
				procesosAVencer.add(proceso);
				proceso.setSentenciaBandera(true);
				//System.out.println("el radicado es "+ proceso.getRadicado());
				
				proceso.setBanderaPerdidaCompetencia(true);
				String descripcion = "Vencimiento anio";
				Alarma alarma1 = alarmaService.findByDescripcionAndProceso(descripcion, proceso);
				
				if(alarma1 == null)
				{
					guardarAlarma121(proceso);
				}
				
					
				proceso.setPrioritario(true);
				procesoService.save(proceso);
					
					//this.bandera = false;
				
				
				//System.out.println("dias igual a 30");
//				model.addAttribute("vence", "Listado de procesos Cercanos a Vencer en " +
//				dias1 + " días.");
				model.addAttribute("procesosAVencer", procesosAVencer);
				model.addAttribute("bandera", bandera);
				
				
			}
			if(dias1 <= 30 && dias1 > 0)
			{
				proceso.setProrroga(true);
				procesoService.save(proceso);
				//model.addAttribute("bandera", bandera);
			}
			if(dias1 == 0 && proceso.isBanderaPerdidaCompetencia())
			{
				Calendar fechaPerdiaCompetencia = Calendar.getInstance();
				proceso.setActualEstado("Perdida de competencia en: "+ fechaPerdiaCompetencia.get(Calendar.DATE)
				+ "-" + ((fechaPerdiaCompetencia.get(Calendar.MONTH))+1) + "-" + fechaPerdiaCompetencia.get(Calendar.YEAR));
				proceso.setActuacionUltima("Termino 121");
				proceso.setEstadoActual("Perdida de competencia 121");
				proceso.setPerdidaCompetencia(true);
				proceso.setEstado(false);
				proceso.setSentenciaBandera(false);
				proceso.setProrroga(false);
				procesoService.save(proceso);
				
				List<DetalleTermino> terminos = proceso.getDetalleTerminos();
				
				for(DetalleTermino detalleTermino: terminos)
				{
					if(detalleTermino.getTermino().getNombre().equalsIgnoreCase("Termino 121"))
					{
						detalleTermino.setObservacion("Vencido el: " + fechaPerdiaCompetencia.get(Calendar.DATE)
						+ "-" + ((fechaPerdiaCompetencia.get(Calendar.MONTH))+1) + "-" + fechaPerdiaCompetencia.get(Calendar.YEAR));
					}
					
					detalleTerminoService.save(detalleTermino);
				}
			}
			//
			dias1 = 0;
			//banderaPerdidaCompetencia = false;
			/*============== fin de codigo de alarma de 30 dias para el vencimiento de terminos ======*/
			
			/*========= CODIGO PARA LOS DIAS DE SUBSANAR DEMANDA ============= */
			
//			if(diasSubsanarDemanda =< 0)
//			{
//				proceso.setUltimaActuacion("inadmitido");
//				proceso.setEstado(false);
//				procesoService.save(proceso);
//				flash.addFlashAttribute("error", "El proceso con radicado: ".concat(proceso.getRadicado()) + " se ha inadmitido " +
//				"porque no se subsanó la demanda");
//				return "redirect:/listarProcesos";
//			}
//			diasSubsanarDemanda = 0;
			
			if(diasSubsanarDemanda <= 15 && diasSubsanarDemanda > 0)
			{
				radicado = proceso.getRadicado();
				idProceso = proceso.getId();
				subsanaDemanda = true;
				if(!proceso.getUltimaActuacion().equalsIgnoreCase("admitido"))
				{
					proceso.setEstadoActual("Por subsanar demanda");
					proceso.setActualEstado("Por subsanar demanda");
				}
				
				procesoService.save(proceso);
				
			}
			
//			if(diasSubsanarDemanda == 0 && banderaSubsanaDemanda == true)
//			{
//				proceso.setUltimaActuacion("inadmitido");
//				proceso.setEstado(false);
//				procesoService.save(proceso);
//				flash.addFlashAttribute("error", "El proceso con radicado: ".concat(proceso.getRadicado()) + " se ha inadmitido " +
//				"porque no se subsanó la demanda");
//				return "redirect:/listarProcesos";
//			}
			diasSubsanarDemanda = 0;
			/*========= FIN DE CODIGO PARA SUBSANAR DEMANDA =====================*/
			
		}
		
		if(subsanaDemanda)
		{
			flash.addFlashAttribute("warning", "El demandante del proceso numero: " + idProceso + " con radicado: " + radicado + " debe subsanar la demanda");
//			return "redirect:/listarProcesos";
		}
		subsanaDemanda = false;
		//estado = false;
		//List<DetalleTermino> detalleTerminos = proceso.getDetalleTerminos();
		
		/*========== Bloque de codigo para vaciar la lista ProcesosAdmitir y asi no mostrar
		 * en la vista de listar procesos =================*/
		int procesoAAdmitir = procesosAdmitir.size();
		System.out.println("numero procesos a admitir: " + procesoAAdmitir);
		
		for(Proceso proceso: procesosAdmitir)
		{	//System.out.println("admitir proceso: " + proceso.getUltimaActuacion());
			
			if(proceso.getUltimaActuacion().equalsIgnoreCase("admitido"))
			{
				procesosAdmitidos++;
			}
		}
		System.out.println("Numero de procesos admitidos: " + procesosAdmitidos);
		
		if(procesoAAdmitir == procesosAdmitidos)
		{
			//System.out.println("son iguales");
			procesosAdmitir.clear();
			model.addAttribute("procesosAdmitir", procesosAdmitir);
		}
		
		/* ======================= fin de bloque de codigo ====================================== */
		
		/*========== Bloque de codigo para vaciar la lista ProcesosAVencer y asi no mostrar
		 * en la vista de listar procesos =================*/
		
		int procesoASentencia = procesosAVencer.size();
		//System.out.println("numero procesos a vencer: " + procesoASentencia);
		
		for(Proceso proceso: procesosAVencer)
		{
			//System.out.println("sentencia proceso: " + proceso.isSentencia());
			if(proceso.isSentencia())
			{
				procesosConSentencia++;
			}
		}
		//System.out.println("Numero de procesos con sentencia: " + procesosConSentencia);
		
		if(procesoASentencia == procesosConSentencia)
		{
			//System.out.println("son iguales");
			procesosAVencer.clear();	
			model.addAttribute("procesosAVencer", procesosAVencer);
		}
		
		/* ======================= fin de bloque de codigo ====================================== */
		
		/*if(this.terminoAnio == false)
		{
			System.out.println("TERMINO AÑO FALSE");
		}else if(this.terminoAnio == true)
		{
			System.out.println("TERMINO AÑO TRUE");
		}*/
		
		model.addAttribute("titulo", "Listado de procesos Activos");
		//model.addAttribute("procesos", procesoService.findAll());
		model.addAttribute("procesos", procesosUsuario);
		model.addAttribute("page", pageRender);
		return "listarProcesos";
		
		
	}
	
	@RequestMapping(value ="/subsanarDemanda/{id}")
	public String subsanarDemanda(@PathVariable(value="id") Long id, RedirectAttributes flash, Model model)
	{
		Proceso proceso = null;
		
		if(id > 0)
		{
			proceso = procesoService.findOne(id);
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe en la base de datos");
				return "redirect:/listarProcesos";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID del proceso no puede ser cero");
			return "redirect:/listarProcesos";
		}
		
		List<DetalleTermino> terminos = proceso.getDetalleTerminos();
		Calendar fechaSubsanaDemanda = Calendar.getInstance();
		
		for(DetalleTermino termino: terminos)
		{
			String nombre = termino.getTermino().getNombre();
			
			if(nombre.equalsIgnoreCase("Subsanar demanda"))
			{
				termino.setObservacion("Subsanado y admitido el: " + fechaSubsanaDemanda.get(Calendar.DATE) + "-" + ((fechaSubsanaDemanda.get(Calendar.MONTH))+1) + "-" + fechaSubsanaDemanda.get(Calendar.YEAR));
				detalleTerminoService.save(termino);
			}
		}
		
		proceso.setEstadoActual("demanda subsanada");
		proceso.setUltimaActuacion("admitido");
		proceso.setSubsanarDemanda(false);
		proceso.setAdmision(false);
		proceso.setActuacionUltima("Admitido");
		proceso.setActualEstado("En espera de notificación al demandado");
		procesoService.save(proceso);
		
		flash.addFlashAttribute("success", "El proceso con radicado: ".concat(proceso.getRadicado()+  " y ID #" + proceso.getId() +" subsanó la demanda y fue admitido con éxito"));
		
		return "redirect:/listarProcesos";
	}
	
	public void guardarDetalleTermino()
	{
		Long idUser = getUserId();
		Usuario usuario = usuarioService.findOne(idUser);
		String nombre = "Termino 121";
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		TipoProceso tipoProceso = proceso1.getTipoProceso();
			
		Termino termino1 = terminoService.findByNombreAndEspecialidadAndTipoProceso(nombre, especialidad, tipoProceso);
		DetalleTermino detalleTermino = new DetalleTermino();
			
		detalleTermino.setProceso(proceso1);
		detalleTermino.setTermino(termino1);
		detalleTermino.setDiasHabiles(true);
		detalleTermino.setFechaInicial(fechaActual1);
		detalleTermino.setFechaFinal(fechaFinal1);
		detalleTerminoService.save(detalleTermino);
		System.out.println("SE GUARDO EL DETALLE TERMINO");
		
	}
	
	public void guardarAlarmaAdmision(Proceso proceso)
	{
		Alarma alarma = new Alarma();
		String descripcion = "Admision";
		alarma.setProceso(proceso);
		alarma.setDescripcion(descripcion);
		alarmaService.save(alarma);	
		//System.out.println("SE GUARDO LA ALARMA DE ADMISION");
		
	}
	
	public void guardarAlarma121(Proceso proceso)
	{
		Alarma alarma = new Alarma();
		String descripcion = "Vencimiento año";
		alarma.setProceso(proceso);
		alarma.setDescripcion(descripcion);
		alarmaService.save(alarma);	
		//System.out.println("SE GUARDO LA ALARMA DE TERMINO 121");
		
	}
	
	/* ==========================================================================*/
	
	@RequestMapping(value="/adminEstadoProceso/{id}")
	public String administrarEstadoProceso(@PathVariable(value="id") Long id, RedirectAttributes flash)
	{
		Proceso proceso = null;
		
		if(id > 0)
		{
			proceso = procesoService.findOne(id);
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "el proceso no existe");
				return "redirect:/verProcesos";
			}
		}
		
		boolean estado = proceso.getEstado();
		if(estado)
		{
			proceso.setEstado(false);
			String radicado = proceso.getRadicado();
			procesoService.save(proceso);
			flash.addFlashAttribute("success", "El proceso con radicado: " + radicado + " fue DESACTIVADO exitosamente");
			return "redirect:/listarProcesos";
		}
		else
		{
			proceso.setEstado(true);
			String radicado = proceso.getRadicado();
			procesoService.save(proceso);
			flash.addFlashAttribute("success", "El proceso con radicado: " + radicado + " fue ACTIVADO exitosamente");
			return "redirect:/listarProcesos";
		}
		
	}
	
	@RequestMapping("/verProcesosAAdmitir")
	public String verProcesosAAdmitir(@RequestParam(name = "page", defaultValue = "0") int page, Model model)
	{
		Pageable pageRequest = PageRequest.of(page, 15);
		Long id = getUserId();
		Usuario usuario = usuarioService.findOne(id);
		
		List<Rol> roles = usuario.getRoles();
		Page<ProcesoUsuario> procesos = null;
		
		if(roles.size() == 1)
		{
			procesos = procesoUsuarioService.findAllByAdmision(usuario.getId(), usuario.getJuzgado(), pageRequest);
		}
		else if(roles.size() == 2)
		{
			procesos = procesoUsuarioService.findAllByAdmisionToJuez(usuario.getJuzgado(), pageRequest);
		}
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/verProcesosAAdmitir", procesos);
		
		model.addAttribute("procesos", procesos);
		model.addAttribute("page", pageRender);
		model.addAttribute("titulo", "Listado de Procesos a Admitir");
		
		return "verProcesos";
	}
	
	@RequestMapping("/verProcesosASubsanarDemanda")
	public String verProcesosASubsanarDemanda(@RequestParam(name = "page", defaultValue = "0") int page, Model model)
	{
		Pageable pageRequest = PageRequest.of(page, 15);
		Long id = getUserId();
		Usuario usuario = usuarioService.findOne(id);
		
		List<Rol> roles = usuario.getRoles();
		Page<ProcesoUsuario> procesos = null;
		
		if(roles.size() == 1)
		{
			procesos = procesoUsuarioService.findAllBySubsanarDemandaToUsuario(usuario.getId(), usuario.getJuzgado(), pageRequest);
		}
		else if(roles.size() == 2)
		{
			procesos = procesoUsuarioService.findAllBySubsanarDemandaToJuez(usuario.getJuzgado(), pageRequest);
		}
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/verProcesosASubsanarDemanda", procesos);
		
		model.addAttribute("procesos", procesos);
		model.addAttribute("page", pageRender);
		model.addAttribute("titulo", "Listado de Procesos a Subsanar Demanda");
		
		return "verProcesos";
	}
	
	@RequestMapping(value="/verProcesosPrioritarios", method = RequestMethod.GET)
	public String verProcesosPrioritarios(@RequestParam(name = "page", defaultValue = "0") int page, Model model)
	{
		Pageable pageRequest = PageRequest.of(page, 15);
		
		Long id = getUserId();
		//Page<Proceso> procesos = procesoService.findAll(pageRequest);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		Page<ProcesoUsuario> procesosUsuario = null;
		int roles = usuario.getRoles().size();
		
		
		if(roles == 1)
		{
			procesosUsuario = procesoUsuarioService.findByUsuarioAndEstadoAndJuzgadoAndPrioritario(id, pageRequest, juzgado);
		}
		else if(roles == 2)
		{
			procesosUsuario = procesoUsuarioService.findAllByPrioritario(juzgado, pageRequest);
		}
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/verProcesosPrioritarios", procesosUsuario);
		
		model.addAttribute("titulo", "Listado de procesos Prioritarios");
		//model.addAttribute("procesos", procesoService.findAll());
		model.addAttribute("procesos", procesosUsuario);
		model.addAttribute("page", pageRender);
		return "verProcesosPrioritarios";
	}
	
	@RequestMapping(value="/verProcesos")
	public String verTodosProcesos(@RequestParam(name = "page", defaultValue = "0") int page, Model model)
	{
		Pageable pageRequest = PageRequest.of(page, 15);
		
		//Long id = getUserId();
		//Page<Proceso> procesos = procesoService.findAll(pageRequest);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		
		Page<ProcesoUsuario> procesosUsuario = procesoUsuarioService.findAllByJuzgado(juzgado, pageRequest);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/verProcesos", procesosUsuario);
		
		model.addAttribute("titulo", "Listado de Todos los procesos");
		//model.addAttribute("procesos", procesoService.findAll());
		model.addAttribute("procesos", procesosUsuario);
		model.addAttribute("page", pageRender);
		
		return "verProcesos";
	}
	
	
	
	@RequestMapping(value="/admitirProceso/{id}")
	public String admitirProceso(@PathVariable(value="id") Long id, RedirectAttributes flash)
	{
		Proceso proceso = null;
		if(id>0)
		{
			proceso = procesoService.findOne(id);
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}
		}
		
		List<DetalleTermino> terminos = proceso.getDetalleTerminos();
		fechaAdmision = Calendar.getInstance();
		/*fechaAdmision.set(Calendar.DATE, 28);
		fechaAdmision.set(Calendar.MONTH, 9);
		fechaAdmision.set(Calendar.YEAR, 2020);*/
		boolean noContieneTerminoAdmision = false;
		
		if(terminos.isEmpty())
		{
			flash.addFlashAttribute("error", "El proceso numero: " + proceso.getId() + " con radicado: " + proceso.getRadicado() + " NO contiene el termino de admisión, se debe agregar para poder admitirse");
			return "redirect:/listarProcesos";
		}
		else
		{
			for(DetalleTermino termino: terminos)
			{
				String nombreTermino = termino.getTermino().getNombre();
				
				if(!nombreTermino.equalsIgnoreCase("Admision"))
				{
					noContieneTerminoAdmision = true;
				}
				
			}
		}
		
		if(noContieneTerminoAdmision)
		{
			flash.addFlashAttribute("error", "El proceso numero: " + proceso.getId() + " con radicado: " + proceso.getRadicado() + " NO contiene el termino de admisión, se debe agregar para poder admitirse");
			return "redirect:/listarProcesos";
		}
		
		for(DetalleTermino termino: terminos)
		{
			String nombre = termino.getTermino().getNombre();
			
			
			
			if(nombre.equalsIgnoreCase("Admision"))
			{
				termino.setObservacion("Admitido el: " + fechaAdmision.get(Calendar.DATE) + "-" + ((fechaAdmision.get(Calendar.MONTH))+1) + "-" + fechaAdmision.get(Calendar.YEAR));
				detalleTerminoService.save(termino);
			}
		}

		String radicado = proceso.getRadicado();
		String admitido = proceso.getUltimaActuacion();
		if(admitido.isEmpty())
		{
			System.out.println("ADMITIDO ES EMPTY: "+ admitido.isEmpty() + " del radicado: " + radicado);
			proceso.setActuacionUltima("Admitido");
			proceso.setActualEstado("En espera de notificación al demandado");
			
			proceso.setUltimaActuacion("admitido");
			proceso.setEstadoActual("");
			proceso.setAdmision(false);
			procesoService.save(proceso);
			flash.addFlashAttribute("success", "El proceso con el radicado: "+ radicado+ " fue ADMITIDO");
			return "redirect:/listarProcesos";
		}
		else if(admitido.equalsIgnoreCase("admitido"))
		{
			flash.addFlashAttribute("warning", "El proceso con el radicado: "+ radicado+ " YA FUE ADMITIDO");
		}

		return "redirect:/listarProcesos";
	}
	
	/*metodo que obtiene la fecha en la que se realiza la admision de un proceso*/
	/*public Calendar obtenerFechaAdmision()
	{
		return this.fechaAdmision;
	}*/
	
	@RequestMapping("/limpiarProceso/{id}")
	public String limpiarProceso(@PathVariable(value = "id") Long id, RedirectAttributes flash)
	{
		Proceso proceso = null;
		
		if(id > 0)
		{
			proceso = procesoService.findOne(id);
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID del proceso debe ser mayor a cero");
			return "redirect:/listarProcesos";
		}
		
		proceso.setUltimaActuacion("");
		proceso.setEstadoActual("Por admitir");
		proceso.setActuacionUltima("");
		proceso.setActualEstado("Cerca a vencer la admision");
		proceso.setSentenciaBandera(false);
		proceso.setProrroga(false);
		proceso.setAdmisionEnTreintaDias(false);
		proceso.setPrioritario(false);
		proceso.setBanderaPerdidaCompetencia(false);
		proceso.setPerdidaCompetencia(false);
		//proceso.setAdmision(false);
		procesoService.save(proceso);
		
		List<DetalleTermino> terminos = proceso.getDetalleTerminos();
		
		for(DetalleTermino termino: terminos)
		{
			String nombreTermino = termino.getTermino().getNombre();
			
			if(nombreTermino.equalsIgnoreCase("Admision"))
			{
				termino.setObservacion(null);
				detalleTerminoService.save(termino);
			}
		}
		
		flash.addFlashAttribute("success", "Se ha limpiado correctamente el proceso con ID: " +
		proceso.getId() + " con radicado: " + proceso.getRadicado());
		
		return "redirect:/listarProcesos";
	}
	
	@RequestMapping(value="/inadmitirProceso/{id}")
	public String inadmitirProceso(@PathVariable(value="id") Long id, RedirectAttributes flash)
	{
		Proceso proceso = null;
		if(id>0)
		{
			proceso = procesoService.findOne(id);
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}
		}
		
		List<DetalleTermino> terminos = proceso.getDetalleTerminos();
		Calendar fechaInadmision = Calendar.getInstance();
		
		for(DetalleTermino termino: terminos)
		{
			String nombre = termino.getTermino().getNombre();
			
			if(nombre.equalsIgnoreCase("Admision"))
			{
				termino.setObservacion("Inadmitido el: " + fechaInadmision.get(Calendar.DATE) + "-" + ((fechaInadmision.get(Calendar.MONTH))+1) + "-" + fechaInadmision.get(Calendar.YEAR));
				detalleTerminoService.save(termino);
			}
		}
		
		String radicado = proceso.getRadicado();
		String admitido = proceso.getUltimaActuacion();
		
		Calendar fechaInadmisionProceso = Calendar.getInstance(timeZone, locale);
		
		if(admitido.isEmpty())
		{
			proceso.setActuacionUltima("Inadmitido");
			proceso.setActualEstado("Inadmitido el: " + fechaInadmisionProceso.get(Calendar.DATE)
				+ "-" + (fechaInadmisionProceso.get(Calendar.MONTH)+1) + "-" + fechaInadmisionProceso.get(Calendar.YEAR));
			proceso.setUltimaActuacion("inadmitido");
			proceso.setEstado(false);
			procesoService.save(proceso);
			flash.addFlashAttribute("success", "El proceso con el radiaco: "+ radicado+ " fue INADMITIDO y DESACTIVADO");
			return "redirect:/listarProcesos";
		}
		else if(admitido.equalsIgnoreCase("admitido"))
		{
			flash.addFlashAttribute("warning", "El proceso con el radiaco: "+ radicado+ " YA FUE ADMITIDO");
		}
		
		return "redirect:/listarProcesos";
	}
	
	/*===============================================================================*/
	@RequestMapping(value="/verAlarmasProceso/{id}")
	public String verAlarmasProceso(@PathVariable(value="id") Long id, Model model)
	{
		Proceso proceso = procesoService.findOne(id);
		List<Alarma> alarmas = alarmaService.findByProceso(proceso);
		String radicado = proceso.getRadicado();
		
		model.addAttribute("titulo", "Alarmas del Proceso con radicado: " + radicado);
		model.addAttribute("alarmas", alarmas);
		return "alarma/verAlarmas";
	}
	
	@RequestMapping(value ="/formProceso")
	public String crear(Map<String, Object> model)
	{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Long id = usuario.getJuzgado().getId();
		System.out.println("id del juzgado: " + id);
		
		String nombre = usuario.getNombre();
		System.out.println("nombre usuario: " + nombre);
		
		Juzgado juzgado = usuario.getJuzgado();
		System.out.println("el juzgado es: " + juzgado);
		
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		
		boolean banderaEditar = true;
		
		List<Usuario> usuarios = usuarioDao.findByJuzgado(juzgado);
		/* esta es una primera fase en la que se muestra el formulario al ususario, se hace
		 * una instancia de un proceso y se envia a la vista*/
		Proceso proceso = new Proceso();
		
		model.put("banderaEditar", banderaEditar);
		model.put("proceso", proceso);
		model.put("usuarios", usuarios);
		model.put("tipoProcesos", tipoProcesoDao.findByEspecialidad(especialidad));
		model.put("juzgados", juzgadoService.findAll());
		model.put("titulo", "Crear Proceso");
		return "formProceso";
	}
	
	private Proceso procesoReasignar = null;
	
	@RequestMapping(value="/reasignarProceso/{id}")
	public String reAsignarProceso(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Long idUsuario = getUserId();
		Usuario usuarioConSesion = usuarioService.findOne(idUsuario);
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(usuarioConSesion.getJuzgado());
		
		/* si el id es mayor que cero se busca en la base de datos*/
		if(id > 0)
		{
			
			procesoReasignar = procesoService.findOne(id);
			System.out.println("se encontró el proceso");
			
			if(procesoReasignar == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}		
		}
		else
		{
			flash.addFlashAttribute("error", "El id del proceso no puede ser cero");
			/* si no se encuentra se redirige al listado */
			return "redirect:/listarProcesos";
		}
	
		model.put("usuarios", usuarios);
		
		return "reasignarProceso";
	}
	
	@PostMapping(value = "/guardarProcesoReasignado")
	public String guardarProcesoReasignado(@RequestParam(required = false) String usuario,  Model model, RedirectAttributes flash)
	{

		ProcesoUsuario procesoUsuarioEdit = new ProcesoUsuario();
		Usuario usu = new Usuario();
		
		if(!usuario.isEmpty())
		{
			Long id = Long.parseLong(usuario);
			
			usu = usuarioService.findOne(id);
		}
		else if(usuario.isEmpty())
		{
			flash.addFlashAttribute("warning", "El usuario dado no puede ser vacio");
			return "forward:/reasignarProceso";
		}
		
		if(procesoReasignar.getId() != null)
		{
			
			procesoUsuarioEdit = procesoUsuarioService.findOne(procesoReasignar.getId());
		}
		
		procesoUsuarioEdit.setUsuario(usu);
		procesoUsuarioService.save(procesoUsuarioEdit);

		flash.addFlashAttribute("success", "El proceso se ha reasignado con éxito");
		return "redirect:/listarProcesos";
	}
	
	
	@RequestMapping(value="/editarProceso/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Proceso proceso = null;
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		boolean banderaEditar = false;
		
		/* si el id es mayor que cero se busca en la base de datos*/
		if(id > 0)
		{
			
			proceso = procesoService.findOne(id);
			System.out.println("se encontró el proceso");
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}		
		}
		else
		{
			flash.addFlashAttribute("error", "El id del proceso no puede ser cero");
			/* si no se encuentra se redirige al listado */
			return "redirect:/listarProcesos";
		}
		
		Juzgado juzgado = usuario.getJuzgado();
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		
		model.put("banderaEditar", banderaEditar);
		model.put("proceso", proceso);
		model.put("usuarios", usuarios);
		model.put("titulo", "Editar Proceso");
		model.put("tipoProcesos", tipoProcesoDao.findByEspecialidad(especialidad));
		model.put("juzgados", juzgadoService.findAll());

		return "formProceso";
	}
	
	@PostMapping(value = "/guardarProceso")
	public String guardar(@Valid Proceso proceso, BindingResult result, @RequestParam(value = "usuario", required = false) String usuario,  Model model, RedirectAttributes flash, SessionStatus status)
	{
		Usuario usuarioLogeado = usuarioService.findOne(getUserId());
		Long idJuzgado = usuarioLogeado.getJuzgado().getId();
		Juzgado juzgado = juzgadoService.findOne(idJuzgado);
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		//ProcesoUsuario procesoUsuarioEdit = new ProcesoUsuario();
		Usuario usu = new Usuario();
		
		if(usuario != null)
		{
			Long id = Long.parseLong(usuario);
			
			usu = usuarioService.findOne(id);
		}
		
		/* si al validar los campos, estos contienen errores se envia al formulario de
		 * crear proceso de nuevo para que se corrijan los errores*/
		/* por eso se agrega otro parametro al metodo guardar llamado BindingResult, este
		 * parametro siempre va junto al objeto que esta mapeado al formulario, en este
		 * caso, el objeto proceso */
		if (result.hasErrors())
		{
			/* se debe pasar nuevamente el titulo a la vista*/
			model.addAttribute("titulo", "Crear Proceso");
			model.addAttribute("usuarios", usuarios);
			model.addAttribute("tipoProcesos", tipoProcesoDao.findAll());
			
			/* se retorna el formulario con los errores*/
			return "formProceso";
		}
		
		System.out.println("el id del proceso a guardar es: " + proceso.getId());
		String mensajeFlash = (proceso.getId() != null) ? "Proceso editado con exito" : "Proceso creado con exito";
		boolean bandera = false;
		
		if(proceso.getId() == null)
		{
			bandera = true;
		}
		/*else if(proceso.getId() != null)
		{
			
			procesoUsuarioEdit = procesoUsuarioService.findOne(proceso.getId());
		}*/
		
		

		proceso.setJuzgado(juzgado);
		//procesoUsuarioEdit.setUsuario(usu);
		
		
		/*System.out.println("el id del usuario es " + id);
		System.out.println("el nombre del usuario es" + usu.getNombre());
		System.out.println("el radicado del proceso es: " + proceso.getRadicado());*/
		
		procesoService.save(proceso);
		//procesoUsuarioService.save(procesoUsuarioEdit);
		
		if(bandera == true)
		{
			ProcesoUsuario procesoUsuario = new ProcesoUsuario();
			
			List<ProcesoUsuario> listProcesosUsuarios = new ArrayList<ProcesoUsuario>();
			listProcesosUsuarios.add(procesoUsuario);
			proceso.setProcesosUsuarios(listProcesosUsuarios);
			procesoUsuario.setProceso(proceso);
			procesoUsuario.setUsuario(usu);
			
			procesoUsuarioService.save(procesoUsuario);
		}
		
		/* despues de guardar el objeto, se termina la sesion */
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listarProcesos";
	}
	
	@RequestMapping(value ="/eliminarProceso/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash)
	{
		if(id > 0)
		{
			procesoService.delete(id);
			flash.addFlashAttribute("success", "El proceso se ha eliminado con èxito");
		}
		
		return "redirect:/listarProcesos";
	}
}