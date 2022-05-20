package org.tfg.teafind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.teafind.entities.Proyecto;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {
	public Proyecto getByNombre(String nombre);
	
	public List<Proyecto> findByLeaderId(Long id);
	
	public List<Proyecto> findByNombreContainingIgnoreCase(String nombre);
}
