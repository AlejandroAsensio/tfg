package org.tfg.teafind.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Habilidad {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String nombre;
	
	private String descripcion;
	
	@ManyToMany(mappedBy = "sabe")
	private Collection<Usuario> conocida;
	
	//======================================================

	public Habilidad() {
	this.conocida = new ArrayList<Usuario>();
	}

	public Habilidad(String nombre, String descripcion) {
		
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.conocida = new ArrayList<Usuario>();
	}
	//======================================================

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

	public Collection<Usuario> getConocida() {
		return conocida;
	}

	public void setConocida(Collection<Usuario> conocida) {
		this.conocida = conocida;
	}
	
	//======================================================
	
}
