package com.usco.esteban.agenda_procesos.app.models.dao;




import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;

public interface IProcesoDao extends PagingAndSortingRepository<Proceso, Long> 
{
	/* esta consulta se hace a nivel de entity (entidad) y no a nivel de tabla */
	//@Query("select p from Proceso p where p.radicado like %?1%")
	//public List<Proceso> findByRadicado(String radicado);
	
	Page<Proceso> findByRadicado(String radicado, Pageable pageable);
}
