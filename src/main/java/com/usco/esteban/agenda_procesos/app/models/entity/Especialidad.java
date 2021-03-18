package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name ="especialidad")
public class Especialidad implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy =GenerationType.IDENTITY)
	@Column(name ="esp_id")
	private Long id;
	
	@Column(name="esp_nombre")
	@NotBlank
	private String nombre;
	
	@JsonIgnore
	@OneToMany(mappedBy = "especialidad", fetch = FetchType.LAZY)
	private List<Juzgado> juzgados;
	
	@OneToMany(mappedBy ="especialidad", fetch = FetchType.LAZY)
	private List<Termino> terminos;
	
	@JsonIgnore
	@OneToMany(mappedBy = "especialidad", fetch = FetchType.LAZY)
	private List<HistorialUsuario> historialUsuarios;
	
	@JsonIgnore
	@OneToMany(mappedBy = "especialidad", fetch = FetchType.LAZY)
	private List<TipoProceso> tiposProceso;

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

	public List<Juzgado> getJuzgados() {
		return juzgados;
	}

	public void setJuzgados(List<Juzgado> juzgados) {
		this.juzgados = juzgados;
	}

	public List<Termino> getTerminos() {
		return terminos;
	}

	public void setTerminos(List<Termino> terminos) {
		this.terminos = terminos;
	}
	

	public List<HistorialUsuario> getHistorialUsuarios() {
		return historialUsuarios;
	}

	public void setHistorialUsuarios(List<HistorialUsuario> historialUsuarios) {
		this.historialUsuarios = historialUsuarios;
	}

	public List<TipoProceso> getTiposProceso() {
		return tiposProceso;
	}

	public void setTiposProceso(List<TipoProceso> tiposProceso) {
		this.tiposProceso = tiposProceso;
	}
	
	
}
