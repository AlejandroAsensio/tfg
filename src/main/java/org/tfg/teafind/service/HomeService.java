package org.tfg.teafind.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.teafind.entities.Proyecto;
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.helper.H;
import org.tfg.teafind.repository.ProyectoRepository;
import org.tfg.teafind.repository.UsuarioRepository;

@Service
public class HomeService {

    @Autowired
	private ProyectoRepository proyectoRepository;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
    
	public String index(ModelMap m, @RequestParam(value="nombre",required=false) String nombre) {
		List<Proyecto> proyectos = null;
		
		if (nombre != null) {
			proyectos = proyectoRepository.findByNombreContainingIgnoreCase(nombre);
		} else {
			proyectos = proyectoRepository.findAll();	
		}
		
		m.put("proyectos", proyectos);
		m.put("view", "home/rHome");
		return "_t/frame";
	}

	public String login(
			ModelMap m,
			HttpSession s) throws DangerException {
		H.isRolOK("anon", s);
		m.put("view", "home/login");
		return "_t/frame";
	}

}
