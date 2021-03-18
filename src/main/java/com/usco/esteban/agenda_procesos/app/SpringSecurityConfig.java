package com.usco.esteban.agenda_procesos.app;

//import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import com.usco.esteban.agenda_procesos.app.auth.handler.LoginSuccessHandler;
import com.usco.esteban.agenda_procesos.app.models.service.JpaUsuarioDetailsService;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter
{
	/* inyeccion de dependencias*/
	@Autowired
	private LoginSuccessHandler successHandler;
	
	/* como el metodo de BCcrypt está en un componente, lo podemos inyectar a la clase con @Autowired*/
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	/* inyectamos el data source para la conexion a la base de datos mediante JDBC*/
	/* @Autowired
	private DataSource dataSource; */
	
	@Autowired
	private JpaUsuarioDetailsService userDetailsService;
	
	/*se debe implementar un metodo para poder registrar y configurar los usuarios se guardaran
	 *  los usuarios en memoria*/
	/* se anota con @autowired para poder inyectar el objeto de srping AuthenticationManagerBuilder*/
	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder builder) throws Exception
	{
		/* se usa el componente invocando al metodo */
		//PasswordEncoder encoder = this.passwordEncoder;
		
		/* se configura la forma en la que se va a encriptar la contraseña*/
		//UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		
		/* builder.inMemoryAuthentication()
		.withUser(users.username("admin").password("12345").roles("ADMIN", "USER"))
		.withUser(users.username("esteban").password("12345").roles("USER")); */
		
		/* ===================================================================
		 *  Ahora vamos a implementar y configurar la autenticacion mediante JDBC */
		/*builder.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("select usu_username, usu_password, usu_enabled from usuario where usu_username = ?")
		.authoritiesByUsernameQuery("select u.usu_username, r.rol_nombre from rol r inner join usuario u on (r.usu_id_rol = u.usu_id) where u.usu_username = ?");
		*/
		
		//===========================================================================
		/* Implementamos la autenticacion con JPA Y Spring security*/
		builder.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
		
	}


	/* autorizaciones http en las rutas */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/css/**", "/js/**", "/icons/**").permitAll() //rutas publicas
		.antMatchers("/").hasAnyRole("USER")
		.antMatchers("/listar").hasAnyRole("USER")
		.antMatchers("/listarProcesos").hasAnyRole("USER")
		.antMatchers("/verDetalleTerminosProceso/**").hasAnyRole("USER")
		.antMatchers("/verDetalleProceso/**").hasAnyRole("USER")
		.antMatchers("/crearDetalleTermino/**").hasAnyRole("USER")
		.antMatchers("/verTerminos/**").hasAnyRole("USER")
		.antMatchers("/formAgregarDiasHabiles").hasAnyRole("ADMIN")
		.antMatchers("/agregarDiasHabiles").hasAnyRole("ADMIN")
		.antMatchers("/formAgregarDiasCalendario").hasAnyRole("ADMIN")
		.antMatchers("/agregarDiasCalendario").hasAnyRole("ADMIN")
		.antMatchers("/agregarProrroga/**").hasAnyRole("USER")
		.antMatchers("/dictarSentencia/**").hasAnyRole("USER")
		.antMatchers("/listarProcesosPorUsuario").hasAnyRole("ADMIN")
		.antMatchers("/formAsignarProcesos").hasAnyRole("ADMIN")
		.antMatchers("/asignarProcesos").hasAnyRole("ADMIN")
		.antMatchers("/subsanarDemanda/**").hasAnyRole("USER")
		.antMatchers("/adminEstadoProceso/**").hasAnyRole("ADMIN")
		.antMatchers("/verProcesosPrioritarios").hasAnyRole("USER")
		.antMatchers("/verProcesos").hasAnyRole("ADMIN")
		.antMatchers("/verProcesosAAdmitir").hasAnyRole("USER")
		.antMatchers("/verProcesosASubsanarDemanda").hasAnyRole("USER")
		.antMatchers("/admitirProceso/**").hasAnyRole("USER")
		.antMatchers("/inadmitirProceso/**").hasAnyRole("USER")
		.antMatchers("/verAlarmasProceso/**").hasAnyRole("USER")
		.antMatchers("/formProceso").hasAnyRole("ADMIN")
		.antMatchers("/formTipoProceso/**").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/listarTiposProceso").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/eliminarTipoProceso/**").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/verTerminosTipoProceso/**").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/editarProceso/**").hasAnyRole("ADMIN")
		.antMatchers("/eliminarProceso/**").hasAnyRole("ADMIN")
		.antMatchers("/formTermino/**").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/listarUsuarios").hasAnyRole("ADMIN")
		.antMatchers("/formUsuario").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/administrarUsuario/**").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/salidaUsuario/**").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/crearRol/**").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/guardarRol").hasAnyRole("ADMIN")
		.antMatchers("/editarUsuario/**").hasAnyRole("ADMIN")
		.antMatchers("/especialidad/**").hasAnyRole("SUPER_ADMIN")
		.antMatchers("/juzgado/**").hasAnyRole("SUPER_ADMIN")
		.anyRequest().authenticated()
		.and()
			.formLogin()
				.loginPage("/login")
				.successHandler(successHandler)
			.permitAll()
		.and()
		.logout().permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
	}
}
