package org.tfg.teafind.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.teafind.entities.Habilidad;
import org.tfg.teafind.entities.Usuario;
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.exception.InfoException;
import org.tfg.teafind.exception.PRG;
import org.tfg.teafind.repository.HabilidadRepository;
import org.tfg.teafind.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@GetMapping("r")
	public String r(
			ModelMap m
			) {
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		m.put("usuarios", usuarios);
		m.put("view", "/usuario/r");
		return "_t/frame";
	}
	
	@GetMapping("c")
	public String c(ModelMap m) {
		List<Habilidad> habilidades = habilidadRepository.findAll();
		m.put("habilidades", habilidades);
		m.put("view", "/usuario/c");
		return "_t/frame";
	}
	
	/*
	 * Incorporado el atributo admin provisionalmente a cada usuario nuevo que se crea
	 * Más adelante se habrá de tener en cuenta si ya hay un admin o no para crear o no con un nuevo usuario
	 */
	@PostMapping("c")
	public String cPost(
			@RequestParam("nombre") String nombre, 
			@RequestParam("apellido1") String apellido1,
			@RequestParam("apellido2") String apellido2,
			@RequestParam("telefono") String telefono,
			@RequestParam("email") String email,
			@RequestParam("password") String password,
			@RequestParam("idsHabilidadesSabe[]") List<Long> idsHabilidadesSabe,
			@RequestParam("admin") boolean admin
			) throws DangerException, InfoException {
		try {
			Usuario usuario= new Usuario(
					nombre, 
					apellido1,
					apellido2, 
					telefono, 
					email, 
					password, 
					admin
					);
			if(idsHabilidadesSabe!=null) {
				for(Long idHabilidadSabe: idsHabilidadesSabe) {
					usuario.addSabe(habilidadRepository.getById(idHabilidadSabe));
				}
			}
			usuarioRepository.save(usuario);	
		} catch (Exception e) {
			PRG.error("La habilidad " + nombre + " ya existe.", "/usuario/c");
		}
//		PRG.info(nombre + " creado correctamente.", "/usuario/r");
		return "redirect:r";
	}
}
