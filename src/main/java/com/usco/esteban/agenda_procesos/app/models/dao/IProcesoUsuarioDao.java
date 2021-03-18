package com.usco.esteban.agenda_procesos.app.models.dao;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;


public interface IProcesoUsuarioDao extends PagingAndSortingRepository<ProcesoUsuario, Long>
{

	/*metodo que devuelve todos los procesos activos de un usuario y del juzgado al que pertenece ese usuario y los ordena de mayor a menor por medio del id del proceso
	 * para luego aplicar la paginacion en la vista*/
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.estado = true and pu.proceso.juzgado=?2 order by pu.proceso.id asc")
	Page<ProcesoUsuario> findAllById(Long id, Pageable pageable, Juzgado juzgado);
	
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.estado = true and pu.proceso.juzgado=?2")
	List<ProcesoUsuario> findAllById(Long id, Juzgado juzgado);
	
	@Query("select pu from ProcesoUsuario pu join pu.proceso p where p.estado = true and p.juzgado=?1")
	List<ProcesoUsuario> findAll(Juzgado juzgado);
	
	/*metodo para ver todos los procesos (activos y no activos) de un usuario y del juzgado al que pertence a ese usuario y que implementa
	 * la paginacion */
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.juzgado=?2 order by pu.proceso.id asc")
	Page<ProcesoUsuario> findAllBy(Long id, Pageable pageable, Juzgado juzgado);
	
	/* metodo que realiza la busqueda de un proceso no activo mediante el radicado y el id de un usuario para luego implementar la paginacion*/
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.estado = false and pu.proceso.radicado like %?2%")
	public Page<ProcesoUsuario> findByIdAndRadicadoAndEstadoFalse(Long id, Pageable pageable, String radicado);
	
	//@Query("select pu from ProcesoUsuario pu where pu.usuario = ?1")
	//Page<ProcesoUsuario> findAllByUsuario(Usuario usuario, Pageable pageable);
	
	/* metodo que realiza la busqueda de un proceso activo mediante el radicado y el id de un usuario para luego implementar la paginacion*/
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.radicado like %?2%")
	public Page<ProcesoUsuario> findByIdAndRadicado(Long id, Pageable pageable, String radicado);
	
	
	/*metodo que realiza la busqueda de un proceso mediante el id de un usuario, juzgado, estado activo y prioritario y que implementa la paginacion */
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.estado = true and pu.proceso.juzgado=?2 and pu.proceso.prioritario=true order by pu.proceso.id asc")
	Page<ProcesoUsuario> findByUsuarioAndEstadoAndJuzgadoAndPrioritario(Long id, Pageable pageable, Juzgado juzgado);
	
	/* query para ver todos los procesos activos y no activos de un juzgado para el admin*/
	@Query("select pu from ProcesoUsuario pu join pu.proceso p where p.juzgado=?1 order by p.id asc")
	Page<ProcesoUsuario> findAllByJuzgado(Juzgado juzgado, Pageable pageable);
	
	/* query para ver los todos los procesos prioritarios y activos de un juzgado y que implementa la paginación */
	@Query("select pu from ProcesoUsuario pu join pu.proceso p where p.estado = true and p.juzgado=?1 and p.prioritario = true order by p.id asc")
	Page<ProcesoUsuario> findAllByPrioritario(Juzgado juzgado, Pageable pageable);
	
	/* query para ver todos los procesos activos para el super_Admin */
	@Query("select pu from ProcesoUsuario pu join pu.proceso p where p.estado = true order by p.id asc")
	Page<ProcesoUsuario> findAllBySuperAdmin(Pageable pageable);
	
	/*query que retorna un solo poceso por medio del id de un usuario */
	@Query("select pu from ProcesoUsuario pu join pu.proceso p where p.id = ?1")
	ProcesoUsuario findOne(Long id);
	
	/*metodo que consulta procesos que esteb por admitir, por juzgado y que sean activos y que implementa la paginacion*/
	@Query("select pu from ProcesoUsuario pu join pu.proceso p where p.estado = true and p.admision = true and p.juzgado = ?1 order by p.id asc")
	Page<ProcesoUsuario> findAllByAdmisionToJuez(Juzgado juzgado, Pageable pageable);
	
	/*metodo que consulta procesos que esteb por admitir, por juzgado - y usuario y que sean activos y que implementa la paginacion*/
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.estado = true and pu.proceso.admision = true and pu.proceso.juzgado = ?2 order by pu.proceso.id asc")
	Page<ProcesoUsuario> findAllByAdmision(Long id, Juzgado juzgado, Pageable pageable);
	
	/*metodo que consulta procesos que esten por subsanar demanda, por juzgado y que sean activos y que implementa la paginacion*/
	@Query("select pu from ProcesoUsuario pu join pu.proceso p where p.estado = true and p.subsanarDemanda = true and p.juzgado = ?1 order by p.id asc")
	Page<ProcesoUsuario> findAllBySubsanarDemandaToJuez(Juzgado juzgado, Pageable pageable);
	
	/*metodo que consulta procesos que esten por subsanar demanda, por usuario y juzgado, que sean activos y que implementa la paginacion*/
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.estado = true and pu.proceso.subsanarDemanda = true and pu.proceso.juzgado = ?2 order by pu.proceso.id asc")
	Page<ProcesoUsuario> findAllBySubsanarDemandaToUsuario(Long id, Juzgado juzgado, Pageable pageable);
}
