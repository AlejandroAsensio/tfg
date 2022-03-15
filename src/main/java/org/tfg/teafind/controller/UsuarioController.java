package org.tfg.teafind.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.teafind.entities.Usuario;
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.exception.InfoException;
import org.tfg.teafind.exception.PRG;
import org.tfg.teafind.repository.UsuarioRepository;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/usuario/r")
	public String r(
			ModelMap m
			) {
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		m.put("usuarios", usuarios);
		m.put("view", "/usuario/r");
		return "_t/frame";
	}
	
	@GetMapping("/usuario/c")
	public String c(ModelMap m) {
		m.put("view", "/usuario/c");
		return "_t/frame";
	}
	
	/*
	 * Incorporado el atributo admin provisionalmente a cada usuario nuevo que se crea
	 * Más adelante se habrá de tener en cuenta si ya hay un admin o no para crear o no con un nuevo usuario
	 */
	@PostMapping("/usuario/c")
	public void cPost(
			@RequestParam("nombre") String nombre, 
			@RequestParam("apellido1") String apellido1,
			@RequestParam("apellido2") String apellido2,
			@RequestParam("telefono") String telefono,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("admin") boolean admin
			) throws DangerException, InfoException {
		try {
			usuarioRepository.save(new Usuario(nombre, apellido1, apellido2, telefono, email, password, admin));	
		} catch (Exception e) {
			PRG.error("La habilidad " + nombre + " ya existe.", "/usuario/c");
		}
		PRG.info(nombre + " creado correctamente.", "/usuario/r");
	}
}
