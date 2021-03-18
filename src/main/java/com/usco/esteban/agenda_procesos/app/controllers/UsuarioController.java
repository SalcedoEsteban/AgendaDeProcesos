package com.usco.esteban.agenda_procesos.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

import com.usco.esteban.agenda_procesos.app.editors.JuzgadoPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.HistorialUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;

import com.usco.esteban.agenda_procesos.app.models.service.IEspecialidadService;
import com.usco.esteban.agenda_procesos.app.models.service.IHistorialUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;
import com.usco.esteban.agenda_procesos.app.models.service.IRolService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;
import com.usco.esteban.agenda_procesos.app.util.paginator.PageRender;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

	@Autowired
	private IJuzgadoService juzgadoService;

	@Autowired
	private JuzgadoPropertyEditor juzgadoEditor;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IHistorialUsuarioService historialUsuarioService;
	
	@Autowired
	private IRolService rolService;
	
	@Autowired
	private IEspecialidadService especialidadService;

	//private Juzgado juzgado;
	
	private Usuario usuario;
	
	private boolean crearUsuario = false;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Juzgado.class, "juzgado", juzgadoEditor);
	}

	@ModelAttribute("numeroRoles")
	public int obtenerNumeroRoles()
	{	
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = context.getAuthentication();
		Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
		
		return authorities.size();
	}
	
	
	@RequestMapping(value="/buscarUsuarioPorNombre")
	public String buscarUsuarioPorNombre(@RequestParam(value="nombreUsuario") String nombreUsuario, 
			@RequestParam(value="page", defaultValue = "0") int page, Model model)
	{
		Pageable pageRequest = PageRequest.of(page, 5);
		
		/*Se obtiene el usuario logeado*/
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		List<Rol> roles = rolService.findByUsuario(usuario);
		
		/* Se obtiene el juzgado del usuario logueado*/
		Juzgado juzgado = usuario.getJuzgado();
		
		System.out.println("la lista roles es vacia: " + roles.isEmpty());
		String nombre = null;
		
		Page<Usuario> usuarios = null; 
		
		if(roles.size() == 2)
		{
			nombre = "ROLE_ADMIN";
		}
		else if(roles.size() == 3)
		{
			nombre = "ROLE_SUPER_ADMIN";
		}
		
		System.out.println("el rol es: " + nombre);
		
		if(nombre.contentEquals("ROLE_SUPER_ADMIN"))
		{
			System.out.println("se hace la consulta de todos los usuarios");
			usuarios = usuarioService.findByNombre(nombreUsuario, pageRequest);
		}
		else if(nombre.contentEquals("ROLE_ADMIN"))
		{
			System.out.println("se hace la consulta de usuario por juzgado");
			usuarios = usuarioService.findByNombreAndJuzgado(juzgado, pageRequest, nombreUsuario);
		}
		
//		usuarios = usuarioService.findByNombreAndJuzgado(juzgado, pageRequest,  nombreUsuario);
		PageRender<Usuario> pageRender = new PageRender<>("/buscarUsuarioPorNombre", usuarios);
		
		System.out.println(usuarios.isEmpty());
		
		model.addAttribute("page", pageRender);
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("titulo", "Listado de Usuario(s)");
		
		return "usuario/listarUsuarios";
	}
	
	@RequestMapping(value = "/listarUsuarios")
	public String listarUsuarios(@RequestParam(name = "page", defaultValue = "0") int page, Map<String, Object> model) {
		
		Pageable pageRequest = PageRequest.of(page, 15);
		
		/*Se obtiene el usuario logeado*/
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		/* Se obtiene el juzgado del usuario logueado*/
		Juzgado juzgado = usuario.getJuzgado();
		
		/*se obtienen los roles del usuario*/
		List<Rol> roles = rolService.findByUsuario(usuario);
		
		System.out.println("la lista roles es vacia: " + roles.isEmpty());
		
		String nombre = null;
		Page<Usuario> usuarios = null; 
		
		if(roles.size() == 2)
		{
			nombre = "ROLE_ADMIN";
		}
		else if(roles.size() == 3)
		{
			nombre = "ROLE_SUPER_ADMIN";
		}
		
		System.out.println("el rol es: " + nombre);
		
		if(nombre.contentEquals("ROLE_SUPER_ADMIN"))
		{
			System.out.println("se hace la consulta de todos los usuarios");
			usuarios = usuarioService.findAll(pageRequest);
		}
		else if(nombre.contentEquals("ROLE_ADMIN"))
		{
			System.out.println("se hace la consulta de usuario por juzgado");
			usuarios = usuarioService.findByJuzgadoPageable(juzgado, pageRequest);
		}
		
		PageRender<Usuario> pageRender = new PageRender<>("/listarUsuarios", usuarios);
		
		model.put("page", pageRender);
		model.put("titulo", "Listado de Usuarios");
		model.put("usuarios", usuarios);

		return "usuario/listarUsuarios";
	}

	@RequestMapping(value = "/formUsuario")
	public String crearUsuario(Map<String, Object> model)
	{
		Usuario usuario = new Usuario();
		//List<Rol> roles = rolService.findAll();
		//boolean actualizarPassword = false;
		this.crearUsuario = true;
		//model.put("roles", roles);
		model.put("usuario", usuario);
		//model.put("actualizarPassword", actualizarPassword);
		model.put("especialidades", especialidadService.findAll());
		//model.put("juzgados", juzgadoService.findAll());
		model.put("titulo", "Crear Usuario");

		return "usuario/formUsuario";
	}

	@RequestMapping(value = "/editarUsuario/{id}")
	public String editarUsuario(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Usuario usuario = null;
		//boolean actualizarPassword = false;
		if (id > 0) {
			usuario = usuarioService.findOne(id);

			if (usuario == null) {
				flash.addFlashAttribute("error", "El usuario no existe");
				return "redirect:/listarUsuarios";
			}
		} else {
			flash.addFlashAttribute("error", "El id del usuario no puede ser cero");
			return "redirect:/listarUsuarios";
		}

		this.crearUsuario = false;
		
		/*System.out.println("el juzgado desde editar es: ".concat(this.juzgado.getNombre()));*/
		
		/*bandera para no mostrar el campo de contraseña al editar un usuario*/
		//actualizarPassword = true;

		model.addAttribute("usuario", usuario);
		model.addAttribute("especialidades", especialidadService.findAll());
		//model.addAttribute("actualizarPassword", actualizarPassword);
		model.addAttribute("titulo", "Editar Usuario");
		model.addAttribute("juzgados", juzgadoService.findAll());
		
		//actualizarPassword = true;

		return "usuario/formUsuario";
	}

	//Usuario usuario1 = null;
	boolean bandera = false;

	@RequestMapping(value = "/administrarUsuario/{id}")
	public String adminUsuario(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Usuario usuario = null;

		if (id > 0) {
			usuario = usuarioService.findOne(id);

			if (usuario == null) {
				flash.addFlashAttribute("error", "El usuario no existe");
				return "redirect:usuario/listarUsuarios";
			}
		} else {
			flash.addFlashAttribute("error", "El id del usuario no puede ser cero");
			return "redirect:usuario/listarUsuarios";
		}

		model.addAttribute("usuario", usuario);
		
		return "usuario/adminUsuario";
	}
	
//	private Date fechaIngreso = null;
//	private HistorialUsuario historial = null;
	
	@RequestMapping(value="/salidaUsuario/{id}")
	public String salidaUsuario(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Usuario usuario = null;
		
		if(id > 0)
		{
			usuario = usuarioService.findOne(id);
			
			if(usuario == null)
			{
				flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
				return "redirect:/listarUsuarios";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID no puede ser Cero");
			return "redirect:/listarUsuarios";
		}
		
		Date fechaIngreso = usuario.getCreateAt();
		//Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		
		
		model.put("usuario", usuario);
		model.put("fechaIngreso", fechaIngreso);
		model.put("titulo", "Salida Usuario");
		
		
		return "usuario/salidaUsuario";
	}
	
	@RequestMapping(value="/entradaUsuario/{id}")
	public String entradaUsuario(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Usuario usuario = null;
		
		if(id > 0)
		{
			usuario = usuarioService.findOne(id);
			
			if(usuario == null)
			{
				flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
				return "redirect:/listarUsuarios";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID no puede ser Cero");
			return "redirect:/listarUsuarios";
		}
		
		//Date fechaIngreso = usuario.getCreateAt();
		//Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		
		
		model.put("usuario", usuario);
		//model.put("fechaIngreso", fechaIngreso);
		model.put("juzgados", juzgadoService.findAll());
		model.put("especialidades", especialidadService.findAll());
		model.put("titulo", "Entrada Usuario");
		
		
		return "usuario/entradaUsuario";
	}
	
	@RequestMapping(value="/guardarSalidaUsuario/{id}", method = RequestMethod.POST)
	public String guardarSalidaHistorial(@PathVariable(value="id") Long id, @RequestParam(name="fechaSalida") String fechaSalida, Model model, RedirectAttributes flash)
	{
		Usuario usuario = usuarioService.findOne(id);
		HistorialUsuario historialUsuario = new HistorialUsuario();
		//Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		List<ProcesoUsuario> procesos = usuario.getListProcesosUsuarios();
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fechaSalida);
        } 
        catch (ParseException ex) 
        {
            System.out.println(ex);
        }
        
        System.out.println("Para la salida del usuario, los procesos asignados son: " + procesos.size());
        
       if(!procesos.isEmpty())
        {
        	flash.addFlashAttribute("error", "El usuario ".concat(usuario.getNombre().concat(" ")
        			.concat(usuario.getApellido().concat(" cuenta con procesos asignados, se deben reasignar antes de realizar la salida del juzgado y/o especialidad"))));
        	return "redirect:/listarUsuarios";
        } 
       	else
       	{
       		usuario.setJuzgado(null);
    		usuarioService.save(usuario);
    		historialUsuario.setFecha(fechaDate);
    		historialUsuario.setTipo("SALIDA");
    		historialUsuario.setDescripcion("Se establece la salida del usuario, no pertenece a ninguna especialidad");
    		historialUsuario.setEspecialidad(null);
    		historialUsuario.setUsuario(usuario);
    		historialUsuarioService.save(historialUsuario);
    		
    		
    		System.out.println("la fecha salida es: " + fechaSalida);
    		//System.out.println("El id del historial es: " + historial.getId());
    		
    		
    		bandera = true;
    		
    		flash.addFlashAttribute("success", "Se estableció la salida del usuario: ".concat(usuario.getNombre().concat(" ").concat(usuario.getApellido())).concat(" de la especialidad y el juzgado al que pertenecía."));
    		
    		return "redirect:/listarUsuarios";
       	}
		
	}
	
	@RequestMapping(value="/guardarEntradaUsuario/{id}", method = RequestMethod.POST )
	public String guardarEntradaHistorial(@PathVariable(value="id")Long id, @RequestParam(name="fechaIngreso") String fechaIngreso, @RequestParam(name="idJuzgado") Long idJuzgado, Model model, RedirectAttributes flash)
	{
		Usuario usuario = usuarioService.findOne(id);
		Juzgado juzgado = juzgadoService.findOne(idJuzgado);
		Especialidad especialidad = juzgado.getEspecialidad();
		
		System.out.println("la especialidad es: " + especialidad.getNombre());
		String nombreJuzgado = juzgado.getNombre();
		
		System.out.println("el juzgado es: "+ nombreJuzgado);
		
		String descripcion = "Usuario Trasladado el: " + fechaIngreso + ", pertenece a la especialidad "
				+ especialidad.getNombre() + " y al juzgado: " + nombreJuzgado;
		
		//HistorialUsuario historial = historialUsuarioService.findByUsuarioAndEspecialidadAndFechaIngreso(usuario, especialidad, fechaIngreso);
		//this.fechaIngreso = fechaIngreso;
		
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaDate = null;
        try {
            fechaDate = formato.parse(fechaIngreso);
        } 
        catch (ParseException ex) 
        {
            System.out.println(ex);
        }
		
		
		HistorialUsuario historialUsuario = new HistorialUsuario();
		usuario.setJuzgado(juzgado);
		usuarioService.save(usuario);
		historialUsuario.setFecha(fechaDate);
		historialUsuario.setTipo("ENTRADA");
		historialUsuario.setUsuario(usuario);
		historialUsuario.setDescripcion(descripcion);
		historialUsuario.setEspecialidad(especialidad);
		//historial.setFechaIngreso(fechaIngreso);
		historialUsuarioService.save(historialUsuario);
		
		
		
		bandera = false;
		
		//model.addAttribute("fechaIngreso", this.fechaIngreso);
		flash.addFlashAttribute("success", "El usuario: ".concat(usuario.getNombre().concat(" ").concat(usuario.getApellido())).concat(" fue trasladado de especialidad con éxito, ahora pertenece a: ".concat(especialidad.getNombre())));
		
		
		return "redirect:/listarUsuarios";
	}
	
	@RequestMapping(value="/verHistorialUsuario/{id}")
	public String verHistorialUsuario(@PathVariable(value="id") Long id, Model model, RedirectAttributes flash)
	{
		Usuario usuario = null;
		
		if( id > 0)
		{
			usuario = usuarioService.findOne(id);;
			
			if(usuario == null)
			{
				flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
				return "redirect:/listarUsuarios";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID no puede ser cero");
			return "redirect:/listarUsuarios";
		}
		
		List<HistorialUsuario> historial = historialUsuarioService.findByUsuario(usuario);
		
		model.addAttribute("titulo", "Historial de Registro del Usuario: ".concat(usuario.getNombre().concat(" ").concat(usuario.getApellido())));
		model.addAttribute("historial", historial);
		model.addAttribute("usuario", usuario);
		
		return "historial/historialUsuario";
	}
	
	@RequestMapping(value = "/guardarUsuario")
	public String guardarUsuario(@Valid Usuario usuario, BindingResult result, SessionStatus status, RedirectAttributes flash, Model model)
	{
		
		if(result.hasErrors()) 
		{
			model.addAttribute("especialidades", especialidadService.findAll());
			model.addAttribute("juzgados", juzgadoService.findAll());
			model.addAttribute("titulo", "Crear Usuario");
			return "usuario/formUsuario";
		}
		
		if(this.crearUsuario == true)
		{
			/*Usuario que se busca para validar si hay uno con el mismo nombre de usuario*/
			Usuario usuarioUsername = usuarioService.findByUsername(usuario.getUsername());
			
			if(usuarioUsername != null)
			{
				//flash.addFlashAttribute("error", "Ya existe una persona con el nombre de usuario dado");
				model.addAttribute("especialidades", especialidadService.findAll());
				//model.addAttribute("juzgados", juzgadoService.findAll());
				model.addAttribute("error", "Ya existe una persona con el nombre de usuario (email) dado");
				model.addAttribute("titulo", "Crear Usuario");
				return "usuario/formUsuario";
			}
		}
		
		this.crearUsuario = false;
		
		String ps = usuario.getPassword();
		String bycryptPassword = passwordEncoder.encode(ps);
		usuario.setPassword(bycryptPassword);
		System.out.println("La contraseña encriptada es: ".concat(bycryptPassword));

		// se guardar el usuario
		usuarioService.save(usuario);
		flash.addFlashAttribute("success", "El usuario fue guardado con éxito");
		
		/*List<Rol> roles = usuario.getRoles();
		//System.out.println("¿ROLES ES VACIO? " + roles.size());
		
		for (Rol rol : roles)
		{
			
			System.out.println("los roles son: "+ rol);
			Rol nuevoRol = new Rol();
			nuevoRol.setRol(rol);
			nuevoRol.setUsuario(usuario);
			rolService.save(nuevoRol);
		}*/
		
		/* ======================================================================= */
		/* Bloque de codigo para crear HistorialUsuario */
		List<HistorialUsuario> historiales = usuario.getHistorialUsuarios();
		
		if(historiales == null)
		{

			HistorialUsuario historialUsuario = new HistorialUsuario();

			String descripcion = "Usuario creado el: " + usuario.getCreateAt() + ", pertenece a la especialidad "
					+ usuario.getJuzgado().getEspecialidad().getNombre() + " y al juzgado: "
					+ usuario.getJuzgado().getNombre();

					
					/*Calendar calendar = Calendar.getInstance();
					calendar.set(2018, 11, 31);
					Date fechaIngreso = calendar.getTime();*/
			Date fechaIngreso = usuario.getCreateAt();
			System.out.println("la fecha ingreso es: "+ fechaIngreso);
					// Date fechaIngreso = StringToDate("10-Jan-2016");
			Especialidad especialidad = usuario.getJuzgado().getEspecialidad();

			historialUsuario.setDescripcion(descripcion);
			historialUsuario.setFecha(fechaIngreso);
			historialUsuario.setTipo("CREADO");
			historialUsuario.setEspecialidad(especialidad);
			historialUsuario.setUsuario(usuario);

			historialUsuarioService.save(historialUsuario);
		}
		//this.fechaIngreso = historialUsuario.getFechaIngreso();
		//this.fechaIngreso = null;
		//this.juzgado = usuario.getJuzgado();

		/* ======================================================================= */

		// usuarioService.save(usuario);
		status.setComplete();

		return "redirect:/listarUsuarios";
	}

	
	@RequestMapping(value="/desactiActiUsuario/{id}")
	public String desactivarUsuario(@PathVariable(value ="id") Long id, RedirectAttributes flash)
	{
		Usuario usuario = null;
		HistorialUsuario historial = new HistorialUsuario();
		Calendar calendar = Calendar.getInstance();
		
		if(id > 0)
		{
			usuario = usuarioService.findOne(id);
			
			if(usuario == null)
			{
				flash.addFlashAttribute("warning", "El usuario no existe");
				return "redirect:/listarUsuarios";
			}
			
			if(usuario.isEnabled())
			{
				historial.setDescripcion("Se Desactivó el usuario el: " + calendar.getTime());
				historial.setFecha(calendar.getTime());
				historial.setEspecialidad(usuario.getJuzgado().getEspecialidad());
				historial.setTipo("DESACTIVADO");
				historial.setUsuario(usuario);
				historialUsuarioService.save(historial);
				
				usuario.setEnabled(false);
				usuarioService.save(usuario);
				flash.addFlashAttribute("success", "El usuario ha sido desactivado con exito");
				//return "redirect:/listarUsuarios";
			}
			else if(!usuario.isEnabled())
			{
				historial.setDescripcion("Se Activó el usuario el: " + calendar.getTime());
				historial.setFecha(calendar.getTime());
				historial.setEspecialidad(usuario.getJuzgado().getEspecialidad());
				historial.setTipo("ACTIVADO");
				historial.setUsuario(usuario);
				historialUsuarioService.save(historial);
				
				usuario.setEnabled(true);
				usuarioService.save(usuario);
				flash.addFlashAttribute("success", "El usuario ha sido Activado con exito");
				//return "redirect:/listarUsuarios";
			}	
		}
		else
		{
			flash.addFlashAttribute("error", "El ID del usuario no puede ser cero");
			return "redirect:/listarUsuarios";
		}
		
		return "redirect:/listarUsuarios";
	}
	
	@RequestMapping(value="/eliminarUsuario/{id}")
	public String eliminarUsuario(@PathVariable Long id, RedirectAttributes flash)
	{
		Usuario usuario = null;
		List<ProcesoUsuario> procesos = new ArrayList<>();
		HistorialUsuario historialEliminado = new HistorialUsuario();
		Calendar calendar = Calendar.getInstance();
		
		if(id > 0)
		{
			usuario = usuarioService.findOne(id);
			
			if(usuario == null)
			{
				flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
				return "redirect:/listarUsuarios";
			}
		}
		
		/*se obtienen los proceso del usuario que se va eliminar*/
		procesos = usuario.getListProcesosUsuarios();
		
		/*se valida que los procesos no estén vacios*/
		if(!procesos.isEmpty())
		{
			flash.addFlashAttribute("warning", "El usuario tiene procesos asignados, se deben reasignar para poder realizar la eliminación");
			return "redirect:/listarUsuarios";
		}
		
		historialEliminado.setDescripcion("Se elimina el usuario el: " + calendar.getTime());
		historialEliminado.setFecha(calendar.getTime());
		historialEliminado.setEspecialidad(null);
		historialEliminado.setTipo("ELIMINADO");
		historialEliminado.setUsuario(usuario);
		
		usuarioService.delete(id);
		
		flash.addFlashAttribute("success", "El usuario se ha eliminado con exito");
		return "redirect:/listarUsuarios";
	}
}
