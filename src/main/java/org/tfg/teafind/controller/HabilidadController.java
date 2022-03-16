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
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.exception.InfoException;
import org.tfg.teafind.exception.PRG;
import org.tfg.teafind.repository.HabilidadRepository;

@Controller
@RequestMapping("/habilidad")
public class HabilidadController {
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
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
	public String cPost(
			@RequestParam("nombre") String nombre, 
			@RequestParam("descripcion") String descripcion
			) throws DangerException, InfoException {
		try {
			habilidadRepository.save(new Habilidad(nombre, descripcion));	
		} catch (Exception e) {
			PRG.error("La habilidad " + nombre + " ya existe.", "/habilidad/c");
		}
//		PRG.info(nombre + " creado correctamente.", "/habilidad/r");
		return "redirect:r";
	}
	
}
