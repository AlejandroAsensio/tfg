package org.tfg.teafind.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.teafind.entities.Habilidad;
import org.tfg.teafind.entities.Puesto;
import org.tfg.teafind.entities.Usuario;
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.exception.InfoException;
import org.tfg.teafind.exception.PRG;
import org.tfg.teafind.repository.HabilidadRepository;
import org.tfg.teafind.repository.PuestoRepository;
import org.tfg.teafind.repository.UsuarioRepository;

@Controller
public class HabilidadController {
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PuestoRepository puestoRepository;
	
	@GetMapping("admin/habilities")
	public String r(
			ModelMap m,
			HttpSession s
			) throws DangerException {
		Usuario u = null;
		if(s.getAttribute("usuario")!=null) {
			u = (Usuario) s.getAttribute("usuario");
		}
		if(s.getAttribute("usuario")==null || !u.isAdmin()) {
			PRG.error("No tienes permiso para acceder","/");
		}
		List<Habilidad> habilidades = habilidadRepository.findAll();
		
		m.put("habilidades", habilidades);
		m.put("view", "habilidad/r");
		return "_t/frame";
	}
	
	// @GetMapping("c")
	// public String c(ModelMap m,HttpSession s) throws DangerException {
	// 	Usuario u = null;
	// 	if(s.getAttribute("usuario")!=null) {
	// 		u = (Usuario) s.getAttribute("usuario");
	// 	}
	// 	if(s.getAttribute("usuario")==null || !u.isAdmin()) {
	// 		PRG.error("No tienes permiso para acceder","/");
	// 	}
	// 	m.put("view", "/habilidad/c");
	// 	return "_t/frame";
	// }
	
	
	@PostMapping("hability/new")
	public void cPost(
			@RequestParam("nombre") String nombre, 
			@RequestParam("descripcion") String descripcion
			) throws DangerException, InfoException {
		try {
			if(nombre==null || nombre.equals("") || descripcion == null || descripcion.equals("")) {
				PRG.error("Error al crear la habilidad.", "admin/habilities");
			}
			else {
				
				habilidadRepository.save(new Habilidad(nombre, descripcion));	
			}
				
		} catch (Exception e) {
			PRG.error("La habilidad " + nombre + " ya existe o tiene campos vacios", "admin/habilities");
		}
		PRG.info(nombre + " creado correctamente.", "admin/habilities");
//		return "redirect:r";
	}

	// @GetMapping("u")
	// public String u(
	// 		@RequestParam("idHabilidad") Long idHabilidad,
	// 		ModelMap m,HttpSession s
	// 		) throws DangerException {
	// 	Usuario u = null;
	// 	if(s.getAttribute("usuario")!=null) {
	// 		u = (Usuario) s.getAttribute("usuario");
	// 	}
	// 	if(s.getAttribute("usuario")==null || !u.isAdmin()) {
	// 		PRG.error("No tienes permiso para acceder","/");
	// 	}
	// 	m.put("habilidad", habilidadRepository.getById(idHabilidad));
	// 	m.put("view", "habilidad/u");
	// 	return "_t/frame";
	// }

	@PostMapping("hability/edit")
	public String uPost(
			@RequestParam("idHabilidad") Long idHabilidad,
			@RequestParam("nombre") String nombre,
			@RequestParam("descripcion") String descripcion
			) throws DangerException {
		if(nombre == null || nombre.equals("") || descripcion == null || descripcion.equals("")) {
			PRG.error("La habilidad ya existe o tiene campos vacíos","admin/habilities");
		}
		try {
			
			Habilidad habilidad= habilidadRepository.getById(idHabilidad);
			habilidad.setNombre(nombre);
			habilidad.setDescripcion(descripcion);
			habilidadRepository.save(habilidad);
		} catch (Exception e) {
			PRG.error("La habilidad "+nombre+" ya existe", "admin/habilities");
		}
		return "redirect:/admin/habilities";
	}

	@PostMapping("hability/delete")
	public String dPost(
			@RequestParam("idHabilidad") Long idHabilidad
			) {
		Habilidad h = habilidadRepository.getById(idHabilidad);
//		h.removeHabilidad();
		
		for(Usuario u: h.getConocida()) {
			Usuario us = usuarioRepository.getById(u.getId());
			us.getSabe().remove(h);
			usuarioRepository.saveAndFlush(us);
		}
		for(Puesto p: h.getRequerida()) {
			Puesto pu = puestoRepository.getById(p.getId());
			pu.getRequiere().remove(h);
			puestoRepository.saveAndFlush(pu);
		}
		
		habilidadRepository.saveAndFlush(h);
		habilidadRepository.deleteById(idHabilidad);

		return "redirect:/admin/habilities";
	}
	
}
