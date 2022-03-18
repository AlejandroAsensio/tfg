package org.tfg.teafind.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.teafind.entities.Habilidad;
import org.tfg.teafind.entities.Proyecto;
import org.tfg.teafind.entities.Puesto;
import org.tfg.teafind.entities.Usuario;
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.exception.InfoException;
import org.tfg.teafind.exception.PRG;
import org.tfg.teafind.repository.HabilidadRepository;
import org.tfg.teafind.repository.ProyectoRepository;
import org.tfg.teafind.repository.PuestoRepository;
import org.tfg.teafind.repository.UsuarioRepository;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@Autowired
	private ProyectoRepository proyectoRepository;
	
	@Autowired
	private PuestoRepository puestoRepository;
	
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
	
	
	/**
	 * Vista para cada usuario de los proyectos de los que es líder y en los que ocupa un puesto
	 */
	@GetMapping("mis_proyectos")
	public String rOP(ModelMap m, HttpSession s) {
		Usuario usuario = (Usuario) s.getAttribute("usuario");
		List<Proyecto> creados = proyectoRepository.findByLeaderId(usuario.getId());
		List<Puesto> ocupa = puestoRepository.findByOcupanteId(usuario.getId());
		
		m.put("usuario", usuario);
		m.put("proyectosCreados", creados);
		m.put("proyectosPertenece", ocupa);
		
		m.put("view", "/usuario/ownProjects");
		return "_t/frame";
	}
	@PostMapping("unir")
	public String unir(ModelMap m,HttpSession s,
			@RequestParam("idPuesto") Long idPuesto
			) {
		Puesto puesto = puestoRepository.getById(idPuesto);
		Usuario usuario = (Usuario) s.getAttribute("usuario");
		puesto.setOcupante(usuario);
		puestoRepository.save(puesto);
		return "redirect:/proyecto/verProyecto?idProyecto="+puesto.getProyecto().getId();
	}
}
