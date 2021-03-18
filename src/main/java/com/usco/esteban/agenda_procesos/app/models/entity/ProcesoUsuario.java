package com.usco.esteban.agenda_procesos.app.models.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name ="procesoUsuario")
public class ProcesoUsuario implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="pro_usu_id")
	private Long id;
 
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name ="pro_id_pro_usu")
	private Proceso proceso;
 
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name ="usu_id_pro_usu", updatable = true)
	private Usuario usuario;
	
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd-MM-yyyy")
	@Column(name ="pro_usu_create_at")
	private Date creatAt;
	
	@PrePersist
	public void prePersist()
	{
		this.creatAt = new Date();
	}
	
	public ProcesoUsuario(Proceso proceso, Usuario usuario) {
		super();
		this.proceso = proceso;
		this.usuario = usuario;
	}
	
	public ProcesoUsuario() {
		
	}
 
	public Proceso getProceso() {
		return proceso;
	}
 
	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}
 
	public Usuario getUsuario() {
		return usuario;
	}
	
	public Date getCreatAt() {
		return creatAt;
	}
 
	public void setCreatAt(Date creatAt) {
		this.creatAt = creatAt;
	}
 
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
 
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
		{
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		
		ProcesoUsuario that = (ProcesoUsuario) obj;
		return Objects.equals(proceso, that.proceso) &&
				Objects.equals(usuario, that.usuario);
		
	}
 
	@Override
	public int hashCode()
	{
		return Objects.hash(proceso, usuario);
	}
}