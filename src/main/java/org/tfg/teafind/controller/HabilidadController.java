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
import org.tfg.teafind.entities.Puesto;
import org.tfg.teafind.entities.Usuario;
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.exception.InfoException;
import org.tfg.teafind.exception.PRG;
import org.tfg.teafind.repository.HabilidadRepository;
import org.tfg.teafind.repository.PuestoRepository;
import org.tfg.teafind.repository.UsuarioRepository;

@Controller
@RequestMapping("/habilidad")
public class HabilidadController {
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private PuestoRepository puestoRepository;
	
	@GetMapping("r")
	public String r(
			ModelMap m
			) {
		List<Habilidad> habilidades = habilidadRepository.findAll();
		
		m.put("habilidades", habilidades);
		m.put("view", "/habilidad/r");
		return "_t/frame";
	}
	
	@GetMapping("c")
	public String c(ModelMap m) {
		m.put("view", "/habilidad/c");
		return "_t/frame";
	}
	
	
	@PostMapping("c")
	public void cPost(
			@RequestParam("nombre") String nombre, 
			@RequestParam("descripcion") String descripcion
			) throws DangerException, InfoException {
		try {
			habilidadRepository.save(new Habilidad(nombre, descripcion));	
		} catch (Exception e) {
			PRG.error("La habilidad " + nombre + " ya existe.", "/habilidad/c");
		}
		PRG.info(nombre + " creado correctamente.", "/habilidad/r");
//		return "redirect:r";
	}
	@GetMapping("u")
	public String u(
			@RequestParam("idHabilidad") Long idHabilidad,
			ModelMap m
			) {
		m.put("habilidad", habilidadRepository.getById(idHabilidad));
		m.put("view", "habilidad/u");
		return "_t/frame";
	}

	@PostMapping("u")
	public String uPost(
			@RequestParam("idHabilidad") Long idHabilidad,
			@RequestParam("nombre") String nombre,
			@RequestParam("descripcion") String descripcion
			) throws DangerException {
		try {
			Habilidad habilidad= habilidadRepository.getById(idHabilidad);
			habilidad.setNombre(nombre);
			habilidad.setDescripcion(descripcion);
			habilidadRepository.save(habilidad);
		} catch (Exception e) {
			PRG.error("La habilidad "+nombre+" ya existe", "/habilidad/r");
		}
		return "redirect:/habilidad/r";
	}
	@PostMapping("d")
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

		return "redirect:/habilidad/r";
	}
	
}
