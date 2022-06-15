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
public class ProyectoController {

	@Autowired
	private ProyectoRepository proyectoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@Autowired
	private PuestoRepository puestoRepository;


	@GetMapping("admin/projects")
	public String r(ModelMap m, HttpSession s) throws DangerException {
		Usuario u = null;
		if (s.getAttribute("usuario") != null) {
			u = (Usuario) s.getAttribute("usuario");
		}
		if (s.getAttribute("usuario") == null || !u.isAdmin()) {
			PRG.error("No tienes permiso para acceder.", "/");
		}
		List<Proyecto> proyectos = proyectoRepository.findAll();

		m.put("proyectos", proyectos);
		m.put("view", "proyecto/r");
		return "_t/frame";
	}

	@GetMapping("project/new")
	public String c(ModelMap m,HttpSession s) throws DangerException, InfoException {
		Usuario usuario = (Usuario) s.getAttribute("usuario");
		if(usuario == null) {
			PRG.error("Debes iniciar sesion para poder crear proyectos.","/login");
		}
		if (!usuario.isVerified()) {
			PRG.info("Debes verificar tu cuenta para poder crear proyectos.", "/verify");
		}
		m.put("view", "proyecto/c");
		return "_t/frame";
	}

	@PostMapping("project/new")
	public String cPost(@RequestParam(value="nombre",required=false) String nombre, @RequestParam(value="descripcion",required=false) String descripcion,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value="fIni",required=false) LocalDate fIni,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value="fFin",required=false) LocalDate fFin,
			@RequestParam(value="idUsuario",required=false) Long idUsuario

	) throws DangerException, InfoException {

		try {
			Usuario usuario = usuarioRepository.getById(idUsuario);
			proyectoRepository.save(new Proyecto(nombre, descripcion, fIni, fFin, usuario));
		} catch (Exception e) {
			PRG.error("El proyecto " + nombre + " ya existe.", "/project/new");
		}
		try {
			Thread.sleep(3 * 1000);
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
		return "redirect:/user/projects";
	}
	
	@PostMapping("project/delete")
	public String dPost(HttpSession s, @RequestParam(value="idProyecto",required=false) Long idProyecto) throws DangerException {
		
		if (idProyecto == null) {
			PRG.error("Error al borrar proyecto","/");
		} else {
			Proyecto proyecto = proyectoRepository.getById(idProyecto);
			
			proyecto.quitarLeader(null);
			
			for (Puesto p : proyecto.getPuestos()) {
				p.quitarProyecto(null);
				for(Habilidad h : p.getRequiere()) {
					h.getRequerida().remove(p);
				}
				p.setRequiere(null);
				p.setOcupante(null);
				puestoRepository.saveAndFlush(p);
				puestoRepository.deleteById(p.getId());
			}
			proyecto.setPuestos(null);
			
			proyectoRepository.saveAndFlush(proyecto);
			proyectoRepository.deleteById(idProyecto);
		}

		return "redirect:/";
	}
	
	@GetMapping("project/view")
	public String verProyecto(ModelMap m, @RequestParam(value="idProyecto",required=false) Long idProyecto, HttpSession s) throws DangerException {
		
		if(idProyecto==null) {
			PRG.error("Error al acceder al proyecto","/");
		}
		
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
		m.put("view", "proyecto/verProyecto");
		return "_t/frame";
	}

	@GetMapping("project/manage")
	public String uProyectoLiderado(ModelMap m, HttpSession s, @RequestParam("idProyecto") Long idProyecto)
			throws DangerException {

		Proyecto proyecto = proyectoRepository.getById(idProyecto);
		Usuario leader = (Usuario) s.getAttribute("usuario");

		if (proyecto.getLeader() == null) {
			PRG.error("No se puede acceder a la gestión de este proyecto.", "/");
		}

		//Se comprueba si el usuario es el lider o no para permitir el acceso
		if ((leader == null) || (!leader.getId().equals(proyecto.getLeader().getId()))) {
			PRG.error("No tienes permiso para gestionar este proyecto.", "/");
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
		m.put("view", "proyecto/gestionarProyectoLiderado");
		return "_t/frame";
	}

	@PostMapping("project/manage")
	public String uProyectoLideradoPost(@RequestParam(value="nombre",required=false) String nombre,
			@RequestParam(value="descripcion",required=false) String descripcion,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value="fIni",required=false) LocalDate fIni,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam(value="fFin",required=false) LocalDate fFin,
			@RequestParam(value="idProyecto",required=false) Long idProyecto, HttpSession s) throws DangerException {
		
		
		if(nombre == null || descripcion == null || fFin == null || fFin == null || idProyecto == null) {
			PRG.error("Error al gestionar proyecto","javascript:history.back()");
		}
		
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

		return "redirect:/project/manage?idProyecto=" + idProyecto;
	}

	@PostMapping("project/user/dismiss")
	public String despedir(
			@RequestParam(value="idPuesto",required=false) Long idPuesto 
			) throws DangerException {
		if(idPuesto == null) {
			PRG.error("Error al despedir al usuario");
		}
		Puesto puesto = puestoRepository.getById(idPuesto);
		
		puesto.setOcupante(null);
		puestoRepository.save(puesto);
		return "redirect:/project/view?idProyecto="+puesto.getProyecto().getId();
	}
}
