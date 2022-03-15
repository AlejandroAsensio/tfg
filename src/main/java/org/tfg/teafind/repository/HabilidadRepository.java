package org.tfg.teafind.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.tfg.teafind.entities.Habilidad;

@Repository
public interface HabilidadRepository extends JpaRepository<Habilidad, Long>{

}
