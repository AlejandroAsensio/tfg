package org.tfg.teafind.controller;

import java.util.ArrayList;
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
@RequestMapping("/puesto")
public class PuestoController {

	@Autowired
	private PuestoRepository puestoRepository;
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@Autowired
	private ProyectoRepository proyectoRepository;
	
	
	
	@GetMapping("r")
	public String r(
			ModelMap m
			) {
		
		m.put("view", "/puesto/r");
		return "_t/frame";
	}
	
	@GetMapping("c")
	public String c(
			ModelMap m,
			HttpSession s,
			@RequestParam ("nombreProyecto") String nombreProyecto
			) throws DangerException {
		Proyecto proyecto = proyectoRepository.getByNombre(nombreProyecto);
		Usuario leader = (Usuario) s.getAttribute("usuario");
		
		if ((leader == null) || (!leader.getId().equals(proyecto.getLeader().getId()))) {
			PRG.error("No tienes permiso para gestionar este proyecto", "/");
		}
		
		List<Habilidad> habilidades = habilidadRepository.findAll();
		m.put("habilidades", habilidades);
		m.put("proyecto", proyecto);
		m.put("view", "/puesto/c");
		return "_t/frame";
	}
	
	
	@PostMapping("c")
	public String cPost(
			HttpSession s,
			@RequestParam("nombre") String nombre,
			@RequestParam("descripcion") String descripcion,
			@RequestParam("idsHabilidadesRequire[]")List<Long> idsHabilidadesRequire,
			@RequestParam("idProyecto") Long idProyecto,
			@RequestParam("destino") boolean fin,
			@RequestParam("nombreProyecto") String nombreProyecto
			) throws DangerException, InfoException {
		String ruta = "";
		List<Habilidad> habilidadesRequeridas = new ArrayList<Habilidad>();
		for(Long id: idsHabilidadesRequire) {
			habilidadesRequeridas.add(habilidadRepository.getById(id));
		}
		
		Proyecto proyecto = proyectoRepository.getById(idProyecto);
		Usuario leader = (Usuario) s.getAttribute("usuario");
		try {
			if ((leader == null) || (!leader.getId().equals(proyecto.getLeader().getId()))) {
				PRG.error("No tienes permiso para gestionar este proyecto", "/");
			}
			puestoRepository.save(new Puesto(nombre,descripcion,proyecto,habilidadesRequeridas));
			
		} catch (Exception e) {
			PRG.error("La habilidad " + nombre + " ya existe ", "/usuario/c");
		}
		if(fin) {
			ruta="redirect:r";
		}
		else if(!fin) {
			ruta="redirect:c?nombreProyecto="+nombreProyecto;
		}
//		PRG.info(nombre + " creado correctamente.", "/usuario/r");
		return ruta;
	}
}
