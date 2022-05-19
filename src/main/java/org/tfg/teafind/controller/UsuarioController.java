package org.tfg.teafind.controller;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private HabilidadRepository habilidadRepository;
	
	@Autowired
	private ProyectoRepository proyectoRepository;
	
	@Autowired
	private PuestoRepository puestoRepository;
	
	@GetMapping("r")
	public String r(
			ModelMap m
			) {
		List<Usuario> usuarios = usuarioRepository.findAll();
		
		m.put("usuarios", usuarios);
		m.put("view", "/usuario/r");
		return "_t/frame";
	}
	
	@GetMapping("c")
	public String c(ModelMap m) {
		List<Habilidad> habilidades = habilidadRepository.findAll();
		m.put("habilidades", habilidades);
		m.put("view", "/usuario/c");
		return "_t/frame";
	}
	
	/*
	 * Incorporado el atributo admin provisionalmente a cada usuario nuevo que se crea
	 * Más adelante se habrá de tener en cuenta si ya hay un admin o no para crear o no con un nuevo usuario
	 */
	@PostMapping("c")
	public String cPost(
			@RequestParam("nick") String nick, 
			@RequestParam("nombre") String nombre, 
			@RequestParam("apellido1") String apellido1,
			@RequestParam("apellido2") String apellido2,
			@RequestParam("telefono") String telefono,
			@RequestParam("email") String email,
			@RequestParam("imagen") MultipartFile imagen,
			@RequestParam("password") String password,
			@RequestParam(value="idsHabilidadesSabe[]",required=false) List<Long> idsHabilidadesSabe

			) throws DangerException, InfoException {
		String nombreImagen = "default.png";
		Usuario usuario;
		
		try {
			usuario= new Usuario(
					nick,
					nombre, 
					apellido1,
					apellido2, 
					telefono, 
					email, 
					password, 
					false
					);
			if(!imagen.isEmpty() ) {
				//Ruta relativa de almacenamiento
				Path directorioImagenes = Paths.get("src//main//resources//static//img//profile");
				//Ruta absoluta
				String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
				
				//Bytes de la imagen
				byte[] bytesImg = imagen.getBytes();
				//Ruta completa que ocupará la imagen, con su nombre
//				Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nick 
//								+ imagen.getOriginalFilename().substring(imagen.getOriginalFilename().length() - 4));
				Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + imagen.getOriginalFilename());
				//Escritura del fichero
				Files.write(rutaCompleta, bytesImg);
				
				usuario.setImagen(imagen.getOriginalFilename());
			} else {
				usuario.setImagen(nombreImagen);
			}
			if(idsHabilidadesSabe!=null) {
				for(Long idHabilidadSabe: idsHabilidadesSabe) {
					usuario.addSabe(habilidadRepository.getById(idHabilidadSabe));
				}
			}
			usuarioRepository.save(usuario);	
		} catch (Exception e) {
			PRG.error("El número de móvil/email ya están registrados.", "/usuario/c");
		}
//		PRG.info(nombre + " creado correctamente.", "/usuario/r");
		return "redirect:r";
	}
	
	
	/**
	 * ReadOwnProjects
	 * Vista para cada usuario de los proyectos de los que es líder y en los que ocupa un puesto
	 * @throws DangerException 
	 */
	@GetMapping("mis_proyectos")
	public String rOP(ModelMap m, HttpSession s) throws DangerException {
		Usuario usuario = (Usuario) s.getAttribute("usuario");
		if (usuario == null) {
			PRG.error("Por favor, inicia sesión para acceder a tus proyectos.", "/");
		}
		
		List<Proyecto> creados = proyectoRepository.findByLeaderId(usuario.getId());
		List<Puesto> ocupa = puestoRepository.findByOcupanteId(usuario.getId());
		
		m.put("usuario", usuario);
		m.put("proyectosCreados", creados);
		m.put("proyectosPertenece", ocupa);
		
		m.put("view", "/usuario/ownProjects");
		return "_t/frame";
	}
	
	@PostMapping("unir")
	public String unir(ModelMap m,HttpSession s,
			@RequestParam("idPuesto") Long idPuesto,
			@RequestParam("idProyecto") Long idProyecto
			) throws DangerException {
		Puesto puesto = puestoRepository.getById(idPuesto);
		
		Usuario u = (Usuario) s.getAttribute("usuario");
		
		Usuario usuario = usuarioRepository.getById(u.getId()); 
		var ok = true;
		
		for(Habilidad h: puesto.getRequiere()) {
			if(!usuario.getSabe().contains(h)) {
				PRG.error("No tienes los conocimientos necesarios para poder unirte a este puesto","/proyecto/verProyecto?idProyecto="+idProyecto);
			}
		}
		
		
		puesto.setOcupante(usuario);
		puestoRepository.save(puesto);
		return "redirect:/proyecto/verProyecto?idProyecto="+puesto.getProyecto().getId();
	}
	
	@PostMapping("salir")
	public String salir(ModelMap m,HttpSession s,
			@RequestParam("idPuesto") Long idPuesto
			) {
		
		Puesto puesto = puestoRepository.getById(idPuesto);
		
		puesto.setOcupante(null);
		puestoRepository.save(puesto);
		return "redirect:/proyecto/verProyecto?idProyecto="+puesto.getProyecto().getId();
	}
	
	@GetMapping("u")
	public String perfil(ModelMap m, HttpSession s) throws DangerException {
		Usuario usuario = (Usuario) s.getAttribute("usuario");
		if (usuario == null) {
			PRG.error("Por favor, inicia sesión para acceder a tu perfil.", "/");
		}
		Usuario u = usuarioRepository.getById(usuario.getId());
		List<Habilidad> habilidades = habilidadRepository.findAll();
		m.put("usuario", u);
		m.put("habilidades", habilidades);
		m.put("view", "/usuario/perfil");
		
		return "_t/frame";
	}
	@PostMapping("u")
	public String perfilPost(
			@RequestParam("nombre") String nick, 
			@RequestParam("nombre") String nombre, 
			@RequestParam("apellido1") String apellido1,
			@RequestParam("apellido2") String apellido2,
			@RequestParam("telefono") String telefono,
			@RequestParam("email") String email,
			@RequestParam("descripcion") String descripcion,
			@RequestParam("imagen") MultipartFile imagen,
			@RequestParam(value="idsHabilidades[]",required=false) List<Long> idsHabilidades,
			@RequestParam(value="password",required=false) String password,
			@RequestParam(value="newPassword",required=false) String newPassword,
			@RequestParam(value="passwordConfirm",required=false) String passwordConfirm,
			HttpSession s
			) throws DangerException, IOException {
		Usuario u = (Usuario) s.getAttribute("usuario");
		Usuario usuario = usuarioRepository.getById(u.getId());
		ArrayList<Habilidad> nuevasHabilidades = new ArrayList<Habilidad>();
		
		usuario.setNick(nick);
		usuario.setNombre(nombre);
		usuario.setApellido1(apellido1);
		usuario.setApellido2(apellido2);
		usuario.setEmail(email);
		usuario.setTelefono(telefono);
		usuario.setDescripcion(descripcion);
		
		if(!imagen.isEmpty() ) {
			//Ruta relativa de almacenamiento
			Path directorioImagenes = Paths.get("src//main//resources//static//img//profile");
			//Ruta absoluta
			String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();
			
			//Bytes de la imagen
			byte[] bytesImg = imagen.getBytes();
			//Ruta completa que ocupará la imagen, con su nombre
			Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + imagen.getOriginalFilename());
			//Escritura del fichero
			Files.write(rutaCompleta, bytesImg);
			
			usuario.setImagen(imagen.getOriginalFilename());
		}
		
		if (idsHabilidades!=null) {
			for (Long idHabilidad:idsHabilidades) {
				nuevasHabilidades.add(habilidadRepository.getById(idHabilidad));
			}
		}
		usuario.setSabe(nuevasHabilidades);
//		if(password != null || password.compareTo("")!=0) {
//			
//		}
		if(password == null || password.compareTo("")==0) {
			
		}
		else {
			if (new BCryptPasswordEncoder().matches(password, usuario.getPassword())) {
				if(newPassword.equals(passwordConfirm)) {
					usuario.setPassword(newPassword);
				}
				else {
					PRG.error("Las contraseñas no coinciden","/usuario/u");	
				}
			}
			else {
				PRG.error("Contraseña incorrecta","/usuario/u");
			}
		}
		
		usuarioRepository.save(usuario);
		
		return "redirect:/usuario/u";
	}

}
