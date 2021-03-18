package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;


public interface IProcesoUsuarioService {
	
	public Page<ProcesoUsuario> findAllById(Long id, Pageable pageable, Juzgado juzgado);
	//public Page<ProcesoUsuario> findAllByUsuario(Usuario usuario, Pageable pageable);
	
	public List<ProcesoUsuario> findAllById(Long id, Juzgado juzgado);
	
	public void save(ProcesoUsuario procesoUsuario);
	
	public void delete(Long id);
	
	public Page<ProcesoUsuario> findByIdAndRadicado(Long id, Pageable pageable, String radicado);
	
	public Page<ProcesoUsuario> findByIdAndRadicadoAndEstadoFalse(Long id, Pageable pageable, String radicado);
	
	public Page<ProcesoUsuario> findByUsuarioAndEstadoAndJuzgadoAndPrioritario(Long id, Pageable pageable, Juzgado juzgado);
	
	public Page<ProcesoUsuario> findAllBy(Long id, Pageable pageable, Juzgado juzgado);
	
	public List<ProcesoUsuario> findAll(Juzgado juzgado);
	
	public Page<ProcesoUsuario> findAllByPrioritario(Juzgado juzgado, Pageable pageable);
	
	public Page<ProcesoUsuario> findAllByJuzgado(Juzgado juzgado, Pageable pageable);
	
	public Page<ProcesoUsuario> findAllBySuperAdmin(Pageable pageable);
	
	public ProcesoUsuario findOne(Long id);
	
	public Page<ProcesoUsuario> findAllByAdmisionToJuez(Juzgado juzgado, Pageable pageable);
	
	public Page<ProcesoUsuario> findAllByAdmision(Long id, Juzgado juzgado, Pageable pageable);
	
	public Page<ProcesoUsuario> findAllBySubsanarDemandaToJuez(Juzgado juzgado, Pageable pageable);
	
	public Page<ProcesoUsuario> findAllBySubsanarDemandaToUsuario(Long id, Juzgado juzgado, Pageable pageable);
	
}
