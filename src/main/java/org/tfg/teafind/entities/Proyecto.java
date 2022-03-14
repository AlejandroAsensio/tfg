package org.tfg.teafind.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Proyecto {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	private String nombre;
	
	private String descripcion;
	
	private LocalDate inicio;
	
	private LocalDate fin;
	
	@ManyToOne
	private Usuario leader;
	
	@OneToMany(mappedBy = "proyecto")
	private Collection<Puesto> puestos;
	
	

	//==================================================
	public Proyecto() {
		this.puestos = new ArrayList<Puesto>();
	}
	
	public Proyecto(String nombre, String descripcion, LocalDate inicio, LocalDate fin, Usuario leader) {
		
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.inicio = inicio;
		this.fin = fin;
		this.leader = leader;
		this.puestos = new ArrayList<Puesto>();
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
	public Usuario getLeader() {
		return leader;
	}
	public void setLeader(Usuario leader) {
		this.leader = leader;
	}

	public Collection<Puesto> getPuestos() {
		return puestos;
	}

	public void setPuestos(Collection<Puesto> puestos) {
		this.puestos = puestos;
	}
	
 
}
