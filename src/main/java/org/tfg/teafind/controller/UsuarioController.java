package org.tfg.teafind.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
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
import org.tfg.teafind.service.MailService;
import org.tfg.teafind.utils.NombreImagenUtils;

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

	@Autowired
	private MailService mailService;

	Logger logger = Logger.getLogger(UsuarioController.class);

	@GetMapping("r")
	public String r(ModelMap m, HttpSession s) throws DangerException {
		Usuario u = null;
		if (s.getAttribute("usuario") != null) {
			u = (Usuario) s.getAttribute("usuario");
		}
		if (s.getAttribute("usuario") == null || !u.isAdmin()) {
			PRG.error("No tienes permiso para acceder", "/");
		}
		List<Usuario> usuarios = usuarioRepository.findAll();

		m.put("usuarios", usuarios);
		m.put("view", "/usuario/r");
		return "_t/frame";
	}

	@GetMapping("c")
	public String c(ModelMap m, HttpSession s) throws DangerException {
		if (s.getAttribute("usuario") != null) {
			PRG.error("No puedes registrar un usuario nuevo con la sesión iniciada", "/");
		}
		List<Habilidad> habilidades = habilidadRepository.findAll();
		m.put("habilidades", habilidades);
		m.put("view", "/usuario/c");
		return "_t/frame";
	}

	/*
	 * Incorporado el atributo admin provisionalmente a cada usuario nuevo que se
	 * crea Más adelante se habrá de tener en cuenta si ya hay un admin o no para
	 * crear o no con un nuevo usuario
	 */
	@PostMapping("c")
	public String cPost(@RequestParam("nick") String nick, @RequestParam("nombre") String nombre,
			@RequestParam("apellido1") String apellido1, @RequestParam("apellido2") String apellido2,
			@RequestParam("telefono") String telefono, @RequestParam("email") String email,
			@RequestParam("imagen") MultipartFile imagen, @RequestParam("password") String password,
			@RequestParam(value = "idsHabilidadesSabe[]", required = false) List<Long> idsHabilidadesSabe

	) throws DangerException, InfoException {
		Usuario usuario;
		String nombreImagen = "default.png";

		try {
			usuario = new Usuario(nick, nombre, apellido1, apellido2, telefono, email, password, false, false);

			// Ruta relativa de almacenamiento
			Path directorioImagenes = Paths.get("src//main//resources//static//img//profile//");
			File f = new File(directorioImagenes.toString());
			f.mkdir();

			if (!imagen.isEmpty()) {
				nombreImagen = nick + "-" + NombreImagenUtils.getFileName(imagen.getOriginalFilename());

				// Ruta absoluta
				String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();

				// Bytes de la imagen
				byte[] bytesImg = imagen.getBytes();

				// Ruta completa que ocupará la imagen, con su nombre
				Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreImagen);

				// Escritura del fichero
				Files.write(rutaCompleta, bytesImg);

				usuario.setImagen(nombreImagen);
			} else {
				usuario.setImagen(nombreImagen);
			}

			if (idsHabilidadesSabe != null) {
				for (Long idHabilidadSabe : idsHabilidadesSabe) {
					usuario.addSabe(habilidadRepository.getById(idHabilidadSabe));
				}
			}

			usuarioRepository.save(usuario);
			mailService.enviarEmailBienvenida(email, nick, nombre);
		} catch (Exception e) {
			PRG.error("El número de móvil/email ya están registrados.", "/usuario/c");
		}
//		PRG.info(nombre + " creado correctamente.", "/usuario/r");
		return "redirect:/";
	}

	@PostMapping("d")
	public String dPost(@RequestParam("idUsuario") Long idUsuario) {
		Usuario usuario = usuarioRepository.getById(idUsuario);

		for (Proyecto p : usuario.getCreados()) {
			Proyecto pr = proyectoRepository.getById(p.getId());
			pr.quitarLeader(null);
			pr.setFin(LocalDate.now());
			String desc = "El lider del proyecto, "+usuario.getNick()+" ("+usuario.getNombre()
					+") ha sido eliminado, por lo que el proyecto pasa a estar finalizado.\n"+pr.getDescripcion();
			pr.setDescripcion(desc);
			proyectoRepository.saveAndFlush(pr);
		}
		
		usuario.setCreados(null);

		for (Puesto p : usuario.getOcupa()) {
			Puesto pu = puestoRepository.getById(p.getId());
			pu.setOcupante(null);
			puestoRepository.saveAndFlush(pu);
		}
		usuario.setOcupa(null);
		
		for(Habilidad h : usuario.getSabe()) {
			Habilidad hab = habilidadRepository.getById(h.getId());
			h.getConocida().remove(usuario);
		}
		
		usuario.setSabe(null);
		
		usuarioRepository.saveAndFlush(usuario);
		usuarioRepository.deleteById(idUsuario);

		return "redirect:/usuario/r";
	}

	@GetMapping("verificar")
	public String verificar(ModelMap m, HttpSession s) throws DangerException {

		if (s.getAttribute("usuario") == null) {
			PRG.error("Por favor, inicia sesión para acceder aquí.", "/login");
		}
		Usuario u = (Usuario) s.getAttribute("usuario");
		if (u.isVerified()) {
			PRG.error("Tu cuenta ya está verificada.", "/");
		}

		m.put("view", "/usuario/verificar");
		return "_t/frame";
	}

	@PostMapping("sendEmail")
	public String enviarEmail(HttpSession s) throws DangerException, MessagingException {

		Usuario usuario = (Usuario) s.getAttribute("usuario");

		if (s.getAttribute("usuario") == null) {
			PRG.error("Por favor, inicia sesión para acceder aquí.", "/login");
		}

		if (usuario.isVerified()) {
			PRG.error("Tu cuenta ya está verificada.", "/login");
		}

		int nToken = (int) (Math.random() * 90000 + 10000);
		s.setAttribute("nToken", nToken);

		mailService.enviarEmailVerificacion(usuario.getEmail(), nToken);
		return "redirect:/usuario/verificar";
	}

	@PostMapping("verificar")
	public String verificarPost(@RequestParam("numero") int numero, HttpSession s) throws DangerException {
		if (s.getAttribute("usuario") == null) {
			PRG.error("Por favor, inicia sesión para acceder aquí.", "/login");
		}

		Usuario u = (Usuario) s.getAttribute("usuario");
		int nToken = (int) s.getAttribute("nToken");
		String redirect = null;

		if (u.isVerified()) {
			PRG.error("Tu cuenta ya está verificada.", "/");
		}

		if (numero == nToken) {
			u.setVerified(true);
			usuarioRepository.save(u);
			s.invalidate();
			redirect = "redirect:/login";
		} else {
			redirect = "redirect:/usuario/verificar";
		}
		return redirect;
	}

	@GetMapping("u")
	public String perfil(ModelMap m, HttpSession s) throws DangerException, InfoException {
		Usuario usuario = (Usuario) s.getAttribute("usuario");
		if (usuario == null) {
			PRG.error("Por favor, inicia sesión para acceder a tu perfil.", "/login");
		}
		Usuario u = usuarioRepository.getById(usuario.getId());
		List<Habilidad> habilidades = habilidadRepository.findAll();
		m.put("usuario", u);
		m.put("habilidades", habilidades);
		m.put("view", "/usuario/perfil");

		return "_t/frame";
	}

	@PostMapping("u")
	public String perfilPost(@RequestParam("nick") String nick, @RequestParam("nombre") String nombre,
			@RequestParam("apellido1") String apellido1, @RequestParam("apellido2") String apellido2,
			@RequestParam("telefono") String telefono, @RequestParam("email") String email,
			@RequestParam(value = "descripcion", required = false) String descripcion,
			@RequestParam(value = "imagen", required = false) MultipartFile imagen,
			@RequestParam(value = "idsHabilidades[]", required = false) List<Long> idsHabilidades,
			@RequestParam(value = "password", required = false) String password,
			@RequestParam(value = "newPassword", required = false) String newPassword,
			@RequestParam(value = "passwordConfirm", required = false) String passwordConfirm, HttpSession s)
			throws DangerException, IOException {
		Usuario u = (Usuario) s.getAttribute("usuario");
		Usuario usuario = usuarioRepository.getById(u.getId());
		ArrayList<Habilidad> nuevasHabilidades = new ArrayList<Habilidad>();

		if (!nick.isBlank()) {
			usuario.setNick(nick);
		}
		if (!nombre.isBlank()) {
			usuario.setNombre(nombre);
		}
		if (!apellido1.isBlank()) {
			usuario.setApellido1(apellido1);
		}
		if (!apellido2.isBlank()) {
			usuario.setApellido2(apellido2);
		}
		if (!email.isBlank()) {
			usuario.setEmail(email);
			usuario.setVerified(false);
		}
		if (!telefono.isBlank()) {
			usuario.setTelefono(telefono);
		}
		if (!descripcion.isBlank()) {
			if (usuario.getDescripcion().compareTo(descripcion) != 0) {
				usuario.setDescripcion(descripcion);
			}
		} else {
			usuario.setDescripcion("Acerca de " + nick);
		}

		// Ruta relativa de almacenamiento
		Path directorioImagenes = Paths.get("src//main//resources//static//img//profile//");
		File f = new File(directorioImagenes.toString());
		f.mkdir();

		String nombreImagen;

		if (!imagen.isEmpty()) {
			// Si el usuario tiene la imagen default, se cogerá el nombre de la nueva subida
			if (usuario.getImagen().compareTo("default.png") == 0) {
				nombreImagen = nick + "-" + NombreImagenUtils.getFileName(imagen.getOriginalFilename());
			} else if (usuario.getImagen() == null || usuario.getImagen().isEmpty()
					|| usuario.getImagen().compareTo("null") == 0) {
				nombreImagen = nick + "-" + NombreImagenUtils.getFileName(imagen.getOriginalFilename());
			} else {
				nombreImagen = usuario.getImagen();
			}

			// Ruta absoluta
			String rutaAbsoluta = directorioImagenes.toFile().getAbsolutePath();

			// Bytes de la imagen
			byte[] bytesImg = imagen.getBytes();

			// Ruta completa que ocupará la imagen, con su nombre
			Path rutaCompleta = Paths.get(rutaAbsoluta + "/" + nombreImagen);

			// Escritura del fichero
			Files.write(rutaCompleta, bytesImg);

			usuario.setImagen(nombreImagen);
		}

		if (idsHabilidades != null) {
			for (Long idHabilidad : idsHabilidades) {
				nuevasHabilidades.add(habilidadRepository.getById(idHabilidad));
			}
		}
		usuario.setSabe(nuevasHabilidades);
		if (password == null || password.compareTo("") != 0) {
			if (new BCryptPasswordEncoder().matches(password, usuario.getPassword())) {
				if (newPassword.equals(passwordConfirm)) {
					usuario.setPassword(newPassword);
				} else {
					PRG.error("Las contraseñas no coinciden", "/usuario/u");
				}
			} else {
				PRG.error("Contraseña incorrecta", "/usuario/u");
			}
		}

		usuarioRepository.save(usuario);
		return "redirect:/usuario/u";
	}

	@PostMapping("u/mail")
	public String uMail(HttpSession s, @RequestParam("email") String email) throws DangerException {
		Usuario u = (Usuario) s.getAttribute("usuario");
		Usuario usuario = usuarioRepository.getById(u.getId());

		if (!email.isBlank()) {
			if (email.compareTo(usuario.getEmail()) != 0) {
				usuario.setEmail(email);
				usuario.setVerified(false);
			} else {
				PRG.error("El email introducido es el mismo que el actual.", "/usuario/u");
			}
		} else {
			PRG.error("Por favor, introduce un email válido.", "/usuario/u");
		}
		usuarioRepository.save(usuario);
		return "redirect:/usuario/u";
	}

	/**
	 * ReadOwnProjects Vista para cada usuario de los proyectos de los que es líder
	 * y en los que ocupa un puesto
	 * 
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
	public String unir(ModelMap m, HttpSession s, @RequestParam("idPuesto") Long idPuesto,
			@RequestParam("idProyecto") Long idProyecto) throws DangerException, InfoException {
		Puesto puesto = puestoRepository.getById(idPuesto);

		Usuario u = (Usuario) s.getAttribute("usuario");
		Usuario usuario = usuarioRepository.getById(u.getId());

		if (usuario.getOcupa().contains(puesto)) {
			PRG.error("Ya ocupas el puesto " + puesto.getNombre() + ".", "javascript:history.back()");
		}

		if (!usuario.isVerified()) {
			PRG.info("Debes verificar tu cuenta para ocupar este puesto.", "/usuario/verificar");
		}

		for (Habilidad h : puesto.getRequiere()) {
			if (!usuario.getSabe().contains(h)) {
				PRG.error("No tienes los conocimientos necesarios para poder unirte a este puesto.",
						"javascript:history.back()");
			}
		}

		puesto.setOcupante(usuario);
		puestoRepository.save(puesto);
		return "redirect:/proyecto/verProyecto?idProyecto=" + puesto.getProyecto().getId();
	}

	@PostMapping("salir")
	public String salir(ModelMap m, HttpSession s, @RequestParam("idPuesto") Long idPuesto) {

		Puesto puesto = puestoRepository.getById(idPuesto);

		puesto.setOcupante(null);
		puestoRepository.save(puesto);
		return "redirect:/proyecto/verProyecto?idProyecto=" + puesto.getProyecto().getId();
	}

}