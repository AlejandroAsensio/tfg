package org.tfg.teafind.entities;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Entity
public class Usuario {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
//	private String nick;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String telefono;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	private boolean admin;
	
	@ManyToMany
	private Collection<Habilidad> sabe;
	
	@OneToMany(mappedBy = "leader")
	private Collection<Proyecto> creados;
	
	//======================================================
	public Usuario() {
		this.sabe = new ArrayList<Habilidad>();
	}
	public Usuario(String nombre, String apellido1, String apellido2, String telefono, String email, String password,
			boolean admin) {
//		this.nick = nick;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.telefono = telefono;
		this.email = email;
		this.password = encriptar(password);
		this.admin = admin;
		this.sabe = new ArrayList<Habilidad>();
	}
	
	//======================================================
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
//	public String getNick() {
//		return nick;
//	}
//	public void setNick(String nick) {
//		this.nick = nick;
//	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public Collection<Habilidad> getSabe() {
		return sabe;
	}
	public void setSabe(Collection<Habilidad> sabe) {
		this.sabe = sabe;
	}
	public Collection<Proyecto> getCreados() {
		return creados;
	}
	public void setCreados(Collection<Proyecto> creados) {
		this.creados = creados;
	}
	
	private String encriptar(String password) {
		return (new BCryptPasswordEncoder()).encode(password);
		
	}
	 public void addSabe(Habilidad habilidad) {
		 this.sabe.add(habilidad);
		 habilidad.getConocida().add(this);
	 }
	
	/*TODO
	 
	 * y añadir o modificar mis habilidades
	*/
	
}
