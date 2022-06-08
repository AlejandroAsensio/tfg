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
	
	@Column(unique = true)
	private String nick;
	private String nombre;
	private String apellido1;
	private String apellido2;
	
	@Column(unique = true)
	private String telefono;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	private String descripcion;
	
	@Column(nullable = true, length = 64)
	private String imagen;
	private boolean admin;
	
	private boolean verified;
	
	@ManyToMany
	private Collection<Habilidad> sabe;
	
	@OneToMany(mappedBy = "leader")
	private Collection<Proyecto> creados;
	
	@OneToMany(mappedBy = "ocupante")
	private Collection<Puesto> ocupa;
	
	//======================================================
	public Usuario() {
		this.sabe = new ArrayList<Habilidad>();
		this.ocupa = new ArrayList<Puesto>();
	}
	public Usuario(String nick, String nombre, String apellido1, String apellido2, String telefono, String email, String password, boolean admin, boolean verified) {
		this.nick = nick;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.telefono = telefono;
		this.email = email;
		this.password = encriptar(password);
		this.admin = admin;
		this.verified = verified;
		this.sabe = new ArrayList<Habilidad>();
		this.ocupa = new ArrayList<Puesto>();
	}
	
	public Usuario(String nick, String nombre, String apellido1, String apellido2, String telefono, String email, String password, String imagen,
			boolean admin) {
		this.nick = nick;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.telefono = telefono;
		this.email = email;
		this.password = encriptar(password);
		this.imagen = "default.jpg";
		this.admin = admin;
		this.sabe = new ArrayList<Habilidad>();
		this.ocupa = new ArrayList<Puesto>();
	}
	
	//======================================================
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNick() {
		return nick;
	}
	public void setNick(String nick) {
		this.nick = nick;
	}
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
		this.password = encriptar(password);
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getImagen() {
		return imagen;
	}
	public void setImagen(String imagen) {
		this.imagen = imagen;
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
	
	public Collection<Puesto> getOcupa() {
		return ocupa;
	}
	public void setOcupa(Collection<Puesto> ocupa) {
		this.ocupa = ocupa;
	}
	public boolean isVerified() {
		return verified;
	}
	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	//=======================================0
	
	
	private String encriptar(String password) {
		return (new BCryptPasswordEncoder()).encode(password);
		
	}
	public void addSabe(Habilidad habilidad) {
		this.sabe.add(habilidad);
		habilidad.getConocida().add(this);
	}
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", nick=" + nick + ", nombre=" + nombre + ", apellido1=" + apellido1
				+ ", apellido2=" + apellido2 + ", telefono=" + telefono + ", email=" + email + ", password=" + password
				+ ", descripcion=" + descripcion + ", imagen=" + imagen + ", admin=" + admin + ", sabe=" + sabe
				+ ", creados=" + creados + ", ocupa=" + ocupa + "]";
	}
	 
	
	
	/*TODO
	 * y añadir o modificar mis habilidades
	*/
	
}
