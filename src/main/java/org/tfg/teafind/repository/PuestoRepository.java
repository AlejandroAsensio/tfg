package org.tfg.teafind.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.tfg.teafind.entities.Puesto;

@Repository
public interface PuestoRepository extends JpaRepository<Puesto, Long>{
	public List<Puesto> findByOcupanteId(Long id);
}
