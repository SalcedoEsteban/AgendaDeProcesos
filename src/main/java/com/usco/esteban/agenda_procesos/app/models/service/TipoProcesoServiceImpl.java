package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.usco.esteban.agenda_procesos.app.models.dao.ITipoProcesoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

/* una clase service es un unico punto de acceso a distintos DAO o repositorios
 * con esto se evita tener que acceder de forma directa desde el controlador a los DAOS*/
/* de igual forma se pueden interactuar desde un mismo metodo con diferentes DAOS y todo
 * dentro de una misma transaccion */
@Service	
public class TipoProcesoServiceImpl implements ITipoProcesoService {
	
	@Autowired
	private ITipoProcesoDao tipoProcesoDao; //aca podemos instanciar otros DAOS
	
	@Override
	@Transactional(readOnly = true)
	public List<TipoProceso> findAll() {
		
		return tipoProcesoDao.findAll();
	}

	@Override
	@Transactional
	public void save(TipoProceso tipoProceso) {
		
		tipoProcesoDao.save(tipoProceso);
		
	}

	@Override
	@Transactional(readOnly = true)
	public TipoProceso findOne(Long id) {
		
		return tipoProcesoDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		tipoProcesoDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<TipoProceso> findByEspecialidad(Especialidad especialidad) {
		
		return tipoProcesoDao.findByEspecialidad(especialidad);
	}

}
