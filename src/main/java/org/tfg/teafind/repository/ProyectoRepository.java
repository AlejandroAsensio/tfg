package org.tfg.teafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.teafind.entities.Proyecto;

@Repository
public interface ProyectoRepository extends JpaRepository<Proyecto, Long>{

}
