package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
//import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name ="DetalleTermino")
public class DetalleTermino implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="detTer_id")
	private Long id;
	
	/*@Column(name ="detTer_nombre")
	private String nombre;*/
	
	@Column(name ="detTer_dias_habiles")
	private boolean diasHabiles;
	
	@Column(name="detTer_fecha_incial")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	
	private Calendar fechaInicial;
	
	@Column(name ="detTer_fecha_final")
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@NotNull
	
	private Calendar fechaFinal;
	
	@Column(name="det_ter_termino")
	private String ter;
	
	@Column(name="det_ter_observacion")
	private String observacion;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ter_id_det_ter", nullable = false)
	private Termino termino;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="pro_id_det_ter", updatable = false, nullable = false)
	private Proceso proceso;
	
	/*@Column(name="ter_numero_dias")
	private Integer numeroDias;¨*/
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}*/

	public boolean isDiasHabiles() {
		return diasHabiles;
	}

	public void setDiasHabiles(boolean diasHabiles) {
		this.diasHabiles = diasHabiles;
	}

	public Calendar getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(Calendar fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public Calendar getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(Calendar fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/*public Integer getNumeroDias() {
		return numeroDias;
	}

	public void setNumeroDias(Integer numeroDias) {
		this.numeroDias = numeroDias;
	}*/

	public Proceso getProceso() {
		return proceso;
	}

	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}

	public String getTer() {
		return ter;
	}

	public void setTer(String ter) {
		this.ter = ter;
	}
	
	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Termino getTermino() {
		return termino;
	}

	public void setTermino(Termino termino) {
		this.termino = termino;
	}

	
}
