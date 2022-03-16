package org.tfg.teafind.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.teafind.entities.Proyecto;
import org.tfg.teafind.entities.Usuario;
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.exception.InfoException;
import org.tfg.teafind.exception.PRG;
import org.tfg.teafind.repository.ProyectoRepository;
import org.tfg.teafind.repository.UsuarioRepository;

@Controller
public class ProyectoController {
	
	@Autowired
	private ProyectoRepository proyectoRepository;
	
	@GetMapping("/proyecto/r")
	public String r(
			ModelMap m
			) {
		List<Proyecto> proyectos = proyectoRepository.findAll();
		
		m.put("proyectos", proyectos);
		m.put("view", "/proyecto/r");
		return "_t/frame";
	}
	
	@GetMapping("/proyecto/c")
	public String c(ModelMap m) {
		m.put("view", "/proyecto/c");
		return "_t/frame";
	}

	@PostMapping("/proyecto/c")
	public String cPost(
			@RequestParam("nombre") String nombre, 
			@RequestParam("descripcion") String descripcion,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@RequestParam("fIni") LocalDate fIni,
			@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
			@RequestParam("fFin") LocalDate fFin
			//falta el tipo de dato usuario
			
			) throws DangerException, InfoException {
		try {
			proyectoRepository.save(new Proyecto(nombre, descripcion, fIni, fFin, null));	
		} catch (Exception e) {
			PRG.error("El proyecto " + nombre + " ya existe.", "/proyecto/c");
		}
		//PRG.info( "El proyecto "+ nombre + "se ha creado correctamente.", "/proyecto/r");
		return "redirect:/proyecto/r";
	
	}
}
