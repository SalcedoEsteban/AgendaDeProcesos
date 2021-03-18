package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "usu_id")
	private Long id;

	@Column(name = "usu_username", unique = true, length = 40)
	@NotBlank
	@Email
	private String username;

	@Column(name = "usu_password", length = 60)
	@NotBlank
	@Size(min = 8)
	private String password;

	@Column(name = "usu_enabled")
	private boolean enabled;

	@Column(name = "usu_nombre")
	@NotBlank
	private String nombre;

	@Column(name = "usu_apellido")
	@NotBlank
	private String apellido;
	
	@Column(name="usu_cargo")
	@NotBlank
	private String cargo;

	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	/* llave foranea de la tabla rol */
	// @JoinColumn(name = "usu_id_rol")
	private List<Rol> roles;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "juz_id_usu")//se quito el nullable = false para poder dejar en null cuando se haga la salida del usuario
	//@NotNull
	private Juzgado juzgado;

	@Temporal(TemporalType.DATE)
	@Column(name="usu_create_at")
	@DateTimeFormat(pattern = "MM/dd/yyyy")
	private Date createAt;

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<ProcesoUsuario> listProcesosUsuarios = new ArrayList<>();

	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	private List<HistorialUsuario> historialUsuarios;
	
	private static final long serialVersionUID = 1L;
	
	/*
	 * @OneToMany ( mappedBy = "usuario", orphanRemoval = true, cascade =
	 * CascadeType.ALL ) private List<ProcesoUsuario> listProcesosUsuarios = new
	 * ArrayList<>();
	 */

	/* este metodo se invoca justo antes de insertar en la base de datos */
	@PrePersist
	public void prePersistEnabled() {
		
		/*se establece en true el estado del usuario para poder iniciar sesion*/
		this.enabled = true;
		Calendar calendar = Calendar.getInstance();
		//calendar.set(2019, 4, 12);
		this.createAt = calendar.getTime();
	}

	// de esta forma se puede guardar la fecha en el sistema
	/*
	 * @PrePersist public void prePersistFecha() { //createAt = new Date(); }
	 */

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public List<Rol> getRoles() {
		return roles;
	}

	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	public Juzgado getJuzgado() {
		return juzgado;
	}

	public void setJuzgado(Juzgado juzgado) {
		this.juzgado = juzgado;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public List<ProcesoUsuario> getListProcesosUsuarios() {
		return listProcesosUsuarios;
	}

	public void setListProcesosUsuarios(List<ProcesoUsuario> listProcesosUsuarios) {
		this.listProcesosUsuarios = listProcesosUsuarios;
	}
	public List<HistorialUsuario> getHistorialUsuarios() {
		return historialUsuarios;
	}

	public void setHistorialUsuarios(List<HistorialUsuario> historialUsuarios) {
		this.historialUsuarios = historialUsuarios;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apellido == null) ? 0 : apellido.hashCode());
		result = prime * result + (enabled ? 1231 : 1237);
		result = prime * result + ((juzgado == null) ? 0 : juzgado.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((roles == null) ? 0 : roles.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (apellido == null) {
			if (other.apellido != null)
				return false;
		} else if (!apellido.equals(other.apellido))
			return false;
		if (enabled != other.enabled)
			return false;
		if (juzgado == null) {
			if (other.juzgado != null)
				return false;
		} else if (!juzgado.equals(other.juzgado))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (roles == null) {
			if (other.roles != null)
				return false;
		} else if (!roles.equals(other.roles))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	/*
	 * public List<Proceso> getProcesos() { return procesos; }
	 * 
	 * public void setProcesos(List<Proceso> procesos) { this.procesos = procesos; }
	 */

}
