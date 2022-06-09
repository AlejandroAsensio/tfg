package org.tfg.teafind.entities;


import java.util.Collection;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Puesto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private String descripcion;
	
	@ManyToOne
	private Proyecto proyecto;
	
	@ManyToOne
	private Usuario ocupante;
	
	@ManyToMany
	private Collection<Habilidad> requiere;
	
	//==================================================
	public Puesto() {
		
	}


	public Puesto(String nombre, String descripcion,Proyecto proyecto, List<Habilidad> requiere) {
		
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.proyecto = proyecto;
		this.requiere = requiere;
	}


	
	//==================================================
	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	public Proyecto getProyecto() {
		return proyecto;
	}


	public void setProyecto(Proyecto proyecto) {
		this.proyecto = proyecto;
		this.proyecto.getPuestos().add(this);
	}


	public Usuario getOcupante() {
		return ocupante;
	}


	public void setOcupante(Usuario ocupante) {
		this.ocupante = ocupante;
//		this.ocupante.getOcupa().add(this);
	}


	public Collection<Habilidad> getRequiere() {
		return requiere;
	}


	public void setRequiere(Collection<Habilidad> requiere) {
		this.requiere = requiere;
	}	
	public void quitarProyecto(Puesto p) {
		this.proyecto = null;
	}
	
}