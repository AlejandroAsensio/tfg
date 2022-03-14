package org.tfg.teafind.entities;

import java.time.LocalDate;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Proyecto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private String descripcion;
	
	private LocalDate inicio;
	
	private LocalDate fin;

	//==================================================
	public Proyecto() {
		// TODO Auto-generated constructor stub
	}
	public Proyecto(String nombre, String descripcion, LocalDate inicio, LocalDate fin) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.inicio = inicio;
		this.fin = fin;
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
	public LocalDate getInicio() {
		return inicio;
	}
	public void setInicio(LocalDate inicio) {
		this.inicio = inicio;
	}
	public LocalDate getFin() {
		return fin;
	}
	public void setFin(LocalDate fin) {
		this.fin = fin;
	}
	
 
}
