package org.tfg.teafind.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.teafind.entities.Proyecto;
import org.tfg.teafind.entities.Usuario;
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.exception.PRG;
import org.tfg.teafind.helper.H;
import org.tfg.teafind.repository.ProyectoRepository;
import org.tfg.teafind.repository.UsuarioRepository;

@Controller
public class HomeController {
	
	@Autowired
	private ProyectoRepository proyectoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@GetMapping("/")
	public String index(ModelMap m) {
		List<Proyecto> proyectos = proyectoRepository.findAll();
		List<Proyecto> activos = new ArrayList<Proyecto>();
		List<Proyecto> listaRandom = new ArrayList<Proyecto>();

		/**
		 * Añadimos a una lista nueva los proyectos activos
		 */
		for (Proyecto proyecto : proyectos) {
			if (!proyecto.isEnded()) {
				activos.add(proyecto);
			}
		}

		for (int i = 0; i < 3; i++) {
			Proyecto prnd = getRandomProyectoFromList(activos);
			activos.remove(prnd);
			listaRandom.add(prnd);
		}

		m.put("listaProyectosRandom", listaRandom);
		m.put("proyectos", proyectos);
		m.put("view", "home/rHome");
		return "_t/frame";
	}

	@GetMapping("/search")
	public String search(ModelMap m, @RequestParam(value="nombre",required=false) String nombre) {
		List<Proyecto> proyectos = new ArrayList<Proyecto>();
		List<Proyecto> activos = new ArrayList<Proyecto>();
		
		/**
		 * Busqueda de proyecto por nombre
		 */
		if (nombre != null) {
			proyectos = proyectoRepository.findByNombreContainingIgnoreCase(nombre);
		} else {
			proyectos = proyectoRepository.findAll();	
		}

		/**
		 * Añadimos a una lista nueva los proyectos activos
		 */
		for (Proyecto proyecto : proyectos) {
			if (!proyecto.isEnded()) {
				activos.add(proyecto);
			}
		}

		m.put("busqueda", nombre);
		m.put("proyectos", proyectos);
		m.put("view", "home/search");
		return "_t/frame";
	}

	@GetMapping("/info")
	public String info(
			HttpSession s,
			ModelMap m
			) {
		String mensaje = s.getAttribute("_mensaje") != null ? (String) s.getAttribute("_mensaje")
				: "Pulsa para volver a home";
		String severity = s.getAttribute("_severity") != null ? (String) s.getAttribute("_severity") : "info";
		String link = s.getAttribute("_link") != null ? (String) s.getAttribute("_link") : "/";

		s.removeAttribute("_mensaje");
		s.removeAttribute("_severity");
		s.removeAttribute("_link");

		m.put("mensaje", mensaje);
		m.put("severity", severity);
		m.put("link", link);

		m.put("view", "_t/info");
		return "_t/frame";
	}
	
	@GetMapping("/login")
	public String login(
			ModelMap m,
			HttpSession s) throws DangerException {
		H.isRolOK("anon", s);
		
		if (s.getAttribute("usuario") != null) {
			PRG.error("Ya has iniciado sesión." ,"/");
		}

		m.put("view", "home/login");
		return "_t/frame";
	}

	@PostMapping("/login")
	public String loginPost(@RequestParam("email") String email, @RequestParam("pwd") String pwd, HttpSession s) throws DangerException {
		
		try {
			Usuario usuario= usuarioRepository.getByEmail(email);
			if (new BCryptPasswordEncoder().matches(pwd, usuario.getPassword())) {
				s.setAttribute("usuario", usuario);
				
			} else {
				PRG.error("Usuario/Contraseña incorrectos.","/login");
			}
		} catch (Exception e) {
			PRG.error("Usuario/Contraseña incorrectos.","/login");
		}
		return "redirect:/";
	}
	
	@GetMapping("/logout")
	public String logout(
			HttpSession s) {
		s.invalidate();
		return "redirect:/";
	}

	@GetMapping("/team")
	public String team(ModelMap m) {
		
		m.put("view", "home/team");
		return "_t/frame";
	}
	
	@GetMapping("/about")
	public String about(ModelMap m) {
		
		m.put("view", "home/about");
		return "_t/frame";
	}


	/**
	 * Devuelve un proyecto aleatorio de una lista de proyectos dada.
	 * @param listaProyectos	Lista de Proyecto.
	 * @return					Proyecto aleatorio de listaProyectos.
	 */
	public Proyecto getRandomProyectoFromList(List<Proyecto> listaProyectos) {
		Random rnd = new Random();
		return listaProyectos.get(rnd.nextInt((listaProyectos.size())));
	}
	
}
