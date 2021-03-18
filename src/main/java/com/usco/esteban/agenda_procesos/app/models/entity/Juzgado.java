package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;




@Entity
@Table(name ="juzgado")
public class Juzgado implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name="juz_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name ="juz_nombre")
	@NotBlank
	private String nombre;
	
	@JsonIgnore
	@OneToMany(mappedBy = "juzgado", fetch = FetchType.LAZY)
	private List<Proceso> procesos;

	/*@Column(name ="juz_esp")
	private String esp;*/
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="esp_id_juz", nullable = false)
	private Especialidad especialidad;
	
	@JsonIgnore
	@OneToMany(mappedBy = "juzgado", fetch = FetchType.LAZY)
	private List<Usuario> usuarios;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Proceso> getProcesos() {
		return procesos;
	}

	public void setProcesos(List<Proceso> procesos) {
		this.procesos = procesos;
	}
	
	/*public String getEsp() {
		return esp;
	}

	public void setEsp(String esp) {
		this.esp = esp;
	}*/

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	
	
}
