package org.tfg.teafind.controller;

import java.time.LocalDate;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/proyecto")
public class ProyectoController {

	@Autowired
	private ProyectoRepository proyectoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@Autowired
	private PuestoRepository puestoRepository;

//	@GetMapping("/")
//	public String r(ModelMap m) {
//		List<Proyecto> proyectos = proyectoRepository.findAll();
//
//		m.put("proyectos", proyectos);
//		m.put("view", "/proyecto/rHome");
//		return "_t/frame";
//	}

	@GetMapping("c")
	public String c(ModelMap m,HttpSession s) throws DangerException, InfoException {
		Usuario usuario = (Usuario) s.getAttribute("usuario");
		if(usuario == null) {
			PRG.error("Debes iniciar sesion para poder crear proyectos.","/login");
		}
		if (!usuario.isVerified()) {
			PRG.info("Debes verificar tu cuenta para poder crear proyectos.", "/usuario/verificar");
		}
		m.put("view", "/proyecto/c");
		return "_t/frame";
	}

	@PostMapping("c")
	public String cPost(@RequestParam("nombre") String nombre, @RequestParam("descripcion") String descripcion,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("fIni") LocalDate fIni,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("fFin") LocalDate fFin,
			@RequestParam("idUsuario") Long idUsuario
	// falta el tipo de dato usuario

	) throws DangerException, InfoException {

		try {
			Usuario usuario = usuarioRepository.getById(idUsuario);
			proyectoRepository.save(new Proyecto(nombre, descripcion, fIni, fFin, usuario));
		} catch (Exception e) {
			PRG.error("El proyecto " + nombre + " ya existe.", "/proyecto/c");
		}
		// PRG.info( "El proyecto "+ nombre + "se ha creado correctamente.",
		// "/proyecto/r");
		return "redirect:/puesto/c?nombreProyecto=" + nombre;

	}

	@GetMapping("verProyecto")
	public String verProyecto(ModelMap m, @RequestParam("idProyecto") Long idProyecto, HttpSession s) {
		Proyecto proyecto = proyectoRepository.getById(idProyecto);
		Usuario usuario = (Usuario) s.getAttribute("usuario");
		boolean pertenece = false;
		int puestosOcupados = 0;
		boolean ocupadoUnicoLibre = false;
		
		//Para saber si el usuario pertenece al proyecto
		if (usuario != null) {
			for (Puesto p : proyecto.getPuestos()) {
				if (p.getOcupante() != null) {
					if (p.getOcupante().getId() == usuario.getId()) {
						pertenece = true;
					}
				}
			}
		}
		
		//Para saber el número de puestos OCUPADOS del proyecto
		for (Puesto p : proyecto.getPuestos()) {
			if (p.getOcupante() != null) {
				puestosOcupados++;
			}
		}

		//Habiendo un solo puesto, averiguar si el usuario activo es el que lo ocupa
		ocupadoUnicoLibre = (puestosOcupados == 1 && pertenece) ? true : false;



		
		
		m.put("pertenece", pertenece);
		m.put("proyecto", proyecto);
		m.put("ocupados", puestosOcupados);
		//Número de puestos libres del proyecto
		m.put("libres", proyecto.getPuestos().size() - puestosOcupados);
		m.put("ocupadoUnicoLibre", ocupadoUnicoLibre);
		m.put("view", "/proyecto/verProyecto");
		return "_t/frame";
	}

	@GetMapping("tuProyecto")
	public String uProyectoLiderado(ModelMap m, HttpSession s, @RequestParam("idProyecto") Long idProyecto)
			throws DangerException {

		Proyecto proyecto = proyectoRepository.getById(idProyecto);
		Usuario leader = (Usuario) s.getAttribute("usuario");

		if (proyecto.getLeader() == null) {
			PRG.error("No se puede acceder a la gestión de este proyecto.", "/");
		}

		//Se comprueba si el usuario es el lider o no para permitir el acceso
		if ((leader == null) || (!leader.getId().equals(proyecto.getLeader().getId()))) {
			PRG.error("No tienes permiso para gestionar este proyecto", "/");
		}
		
		//Para saber el número de puestos OCUPADOS del proyecto
		int puestosOcupados = 0;
		for (Puesto p : proyecto.getPuestos()) {
			if (p.getOcupante() != null) {
				puestosOcupados++;
			}
		}
		
		//Obtiene la lista de habilidades para la modificación de puestos
		List<Habilidad> habilidades = habilidadRepository.findAll();
		
		m.put("habilidades", habilidades);
		m.put("proyecto", proyecto);
		m.put("ocupados", puestosOcupados);
		//Número de puestos libres del proyecto
		m.put("libres", proyecto.getPuestos().size() - puestosOcupados);
		m.put("view", "/proyecto/gestionarProyectoLiderado");
		return "_t/frame";
	}

	@PostMapping("tuProyecto")
	public String uProyectoLideradoPost(@RequestParam("nombre") String nombre,
			@RequestParam("descripcion") String descripcion,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("fIni") LocalDate fIni,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("fFin") LocalDate fFin,
			@RequestParam("idProyecto") Long idProyecto, HttpSession s) throws DangerException {
		Proyecto proyecto = proyectoRepository.getById(idProyecto);
		Usuario leader = (Usuario) s.getAttribute("usuario");

		if ((leader == null) || (!leader.getId().equals(proyecto.getLeader().getId()))) {
			PRG.error("No tienes permiso para gestionar este proyecto", "/");
		}

		proyecto.setNombre(nombre);
		proyecto.setDescripcion(descripcion);
		proyecto.setInicio(fIni);
		proyecto.setFin(fFin);

		proyectoRepository.save(proyecto);

		return "redirect:/proyecto/tuProyecto?idProyecto=" + idProyecto;
	}
	@PostMapping("despedir")
	public String despedir(
			@RequestParam("idPuesto") Long idPuesto 
			) {
		Puesto puesto = puestoRepository.getById(idPuesto);
		
		puesto.setOcupante(null);
		puestoRepository.save(puesto);
		return "redirect:/proyecto/verProyecto?idProyecto="+puesto.getProyecto().getId();
	}
}
