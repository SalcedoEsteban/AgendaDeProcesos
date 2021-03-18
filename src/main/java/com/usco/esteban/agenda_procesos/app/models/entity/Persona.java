package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

//es una clase POJO con atributos setter y getter que esta mapeado a una tabla y es una entidad
//de JPA
@Entity
@Table(name = "persona")
public class Persona implements Serializable {

	private static final long serialVersionUID = -2343243243242432341L;
	
	/* anotacion que indica que este campo es la llave primaria */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "per_id")
	private long id;

	/*@NotEmpty
	@Column(name = "per_identificacion")
	private String identificacion;*/

	@NotEmpty
	@Column(name = "per_nombre")
	private String nombre;

	@NotEmpty
	@Column(name = "per_apellidos")
	private String apellidos;

	@NotEmpty
	@Column(name = "per_telefono")
	private String telefono;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/*public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}*/

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
