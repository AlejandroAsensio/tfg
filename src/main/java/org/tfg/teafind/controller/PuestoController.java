package org.tfg.teafind.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
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


@Controller
public class PuestoController {

	@Autowired
	private PuestoRepository puestoRepository;
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@Autowired
	private ProyectoRepository proyectoRepository;
	
	
	
	@GetMapping("puesto/r")
	public String r(
			ModelMap m
			) {
		
		m.put("view", "/puesto/r");
		return "_t/frame";
	}
	
	@GetMapping("project/work/new")
	public String c(
			ModelMap m,
			HttpSession s,
			@RequestParam (value="nombreProyecto") String nombreProyecto
			) throws DangerException {
		if (nombreProyecto == null || nombreProyecto.equals("")) {
			PRG.error("Se ha producido un error.","/");
		}
		Proyecto proyecto = proyectoRepository.getByNombre(nombreProyecto);
		Usuario leader = (Usuario) s.getAttribute("usuario");
		
		if ((leader == null) || (!leader.getId().equals(proyecto.getLeader().getId()))) {
			PRG.error("No tienes permiso para gestionar este proyecto.", "/");
		}
		
		List<Habilidad> habilidades = habilidadRepository.findAll();
		m.put("habilidades", habilidades);
		m.put("proyecto", proyecto);
		m.put("view", "puesto/c");
		return "_t/frame";
	}
	
	
	@PostMapping("project/work/new")
	public String cPost(
			HttpSession s,
			@RequestParam(value="nombre",required=false) String nombre,
			@RequestParam(value="descripcion",required=false) String descripcion,
			@RequestParam(value="idsHabilidadesRequire[]",required=false)List<Long> idsHabilidadesRequire,
			@RequestParam(value="idProyecto",required=false) Long idProyecto,
			@RequestParam(value="destino",required=false) boolean seguir,
			@RequestParam(value="nombreProyecto",required=false) String nombreProyecto
			) throws DangerException, InfoException {
		
		if(nombre == null || descripcion == null || idsHabilidadesRequire == null || idProyecto == null || nombreProyecto == null) {
			PRG.error("Error al crear puesto.","javascript:history.back()");
		}
		String ruta = "";
		List<Habilidad> habilidadesRequeridas = new ArrayList<Habilidad>();
		Proyecto proyecto = proyectoRepository.getById(idProyecto);
		Usuario leader = (Usuario) s.getAttribute("usuario");
		
		for (Long id: idsHabilidadesRequire) {
			habilidadesRequeridas.add(habilidadRepository.getById(id));
		}
		
		if ((leader == null) || (!leader.getId().equals(proyecto.getLeader().getId()))) {
			PRG.error("No tienes permiso para gestionar este proyecto.", "/");
		}

		try {
			puestoRepository.save(new Puesto(nombre, descripcion, proyecto, habilidadesRequeridas));
		} catch (Exception e) {
			PRG.error("La habilidad " + nombre + " ya existe.", "/user/profile");
		}

		if (seguir) {
			ruta = "redirect:/project/work/new?nombreProyecto=" + nombreProyecto;
		} else {
			ruta = "redirect:/project/manage?idProyecto=" + idProyecto;
		}
//		PRG.info(nombre + " creado correctamente.", "/usuario/r");
		return ruta;
	}
	
	@PostMapping("project/work/edit")
	public String uPost(
			@RequestParam(value="idProyecto",required=false) Long idProyecto,
			@RequestParam(value="idPuesto",required=false) Long idPuesto,
			@RequestParam(value="nombre",required=false) String nombre,
			@RequestParam(value="descripcion",required=false) String descripcion,
			@RequestParam(value="idsHabilidadesRequiere[]", required=false) List<Long> idsHabilidades
			) throws DangerException {
		try {
			ArrayList<Habilidad> nuevasHabilidades = new ArrayList<Habilidad>();
			Puesto puesto= puestoRepository.getById(idPuesto);
			if (idsHabilidades!=null) {
				for (Long idHabilidad:idsHabilidades) {
					nuevasHabilidades.add(habilidadRepository.getById(idHabilidad));
				}
				puesto.setRequiere(nuevasHabilidades);
			}
			
			puesto.setNombre(nombre);
			puesto.setDescripcion(descripcion);
			puestoRepository.save(puesto);
		} catch (Exception e) {
			PRG.error("El puesto "+nombre+" ya existe", "/project/manage?idProyecto="+idProyecto);
		}
		return "redirect:/project/manage?idProyecto="+idProyecto;
	}
	
	@PostMapping("project/work/delete")
	public String dPost(
			@RequestParam(value="idPuesto",required=false) Long idPuesto,
			@RequestParam(value="idProyecto",required=false) Long idProyecto
			) throws DangerException {
		try {
		puestoRepository.deleteById(idPuesto);
		}
		catch (Exception e) {
			PRG.error("Error al eliminar puesto.","javascript:history.back()");
		}
		return "redirect:/project/manage?idProyecto=" + idProyecto;
	}
}
