package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "termino")
public class Termino implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ter_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name ="ter_nombre")
	@NotBlank
	private String nombre;
	
	@Column(name ="ter_numero_dias")
	@Min(1)
	@Max(450)
	private int numeroDias;
	
	@Column(name = "ter_basico")
	private boolean basico;
	
	/* ============================================ */
	
	/*@Column(name ="ter_esp")
	private String esp;
	
	@Column(name = "ter_tip")
	private String tipPro;*/
	
	/* ============================================= */
	
	@JsonIgnore
	@OneToMany(mappedBy = "termino", fetch = FetchType.LAZY, cascade=CascadeType.ALL)
	private List<DetalleTermino> detalleTerminos;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="tip_pro_id_ter", nullable = false, updatable = true)
	@NotNull
	private TipoProceso tipoProceso;
	
	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name ="esp_id_ter", nullable = false, updatable = true)
	@NotNull
	private Especialidad especialidad;

	public Termino() {
		
	}

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

	public int getNumeroDias() {
		return numeroDias;
	}

	public void setNumeroDias(int numeroDias) {
		this.numeroDias = numeroDias;
	}

	public boolean isBasico() {
		return basico;
	}

	public void setBasico(boolean basico) {
		this.basico = basico;
	}

	
	
	public TipoProceso getTipoProceso() {
		return tipoProceso;
	}

	public void setTipoProceso(TipoProceso tipoProceso) {
		this.tipoProceso = tipoProceso;
	}

	/* ======================================*/
	
	/*public String getEsp() {
		return esp;
	}

	public void setEsp(String esp) {
		this.esp = esp;
	}

	public String getTipPro() {
		return tipPro;
	}

	public void setTipPro(String tipPro) {
		this.tipPro = tipPro;
	}*/
	
	/* =============================================*/

	public List<DetalleTermino> getDetalleTerminos() {
		return detalleTerminos;
	}

	public void setDetalleTerminos(List<DetalleTermino> detalleTerminos) {
		this.detalleTerminos = detalleTerminos;
	}

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

	
}
