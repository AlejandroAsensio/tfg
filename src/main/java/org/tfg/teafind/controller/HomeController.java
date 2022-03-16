package org.tfg.teafind.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//import javax.servlet.http.HttpSession;


//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.teafind.entities.Usuario;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.tfg.teafind.exception.DangerException;
import org.tfg.teafind.helper.H;
import org.tfg.teafind.repository.UsuarioRepository;

@Controller
public class HomeController {
	@Autowired
	private UsuarioRepository usuarioRepository;
//	@Autowired
//	private PersonaRepository personaRepository;

//	@GetMapping("/info")
//	public String info(
//			HttpSession s,
//			ModelMap m
//			) {
//		String mensaje = s.getAttribute("_mensaje") != null ? (String) s.getAttribute("_mensaje")
//				: "Pulsa para volver a home";
//		String severity = s.getAttribute("_severity") != null ? (String) s.getAttribute("_severity") : "info";
//		String link = s.getAttribute("_link") != null ? (String) s.getAttribute("_link") : "/";
//
//		s.removeAttribute("_mensaje");
//		s.removeAttribute("_severity");
//		s.removeAttribute("_link");
//
//		m.put("mensaje", mensaje);
//		m.put("severity", severity);
//		m.put("link", link);
//
//		m.put("view", "/_t/info");
//		return "/_t/frame";
//	}
	
	@GetMapping("/login")
	public String login(
			ModelMap m,
			HttpSession s) throws DangerException {
		H.isRolOK("anon", s);
		m.put("view", "home/login");
		return "_t/frame";
	}

	@PostMapping("/login")
	public String loginPost(@RequestParam("email") String email, @RequestParam("pwd") String pwd, HttpSession s) {
		String returnLocation = "redirect:/";
		try {
			Usuario usuario= usuarioRepository.getByEmail(email);
			if (new BCryptPasswordEncoder().matches(pwd, usuario.getPassword())) {
				s.setAttribute("usuario", usuario);
			}
			else {
				returnLocation="redirect:/errorDisplay?msg=Password incorrecta";
			}
		} catch (Exception e) {
			returnLocation="redirect:/errorDisplay?msg=Usuario incorrecto";
		}
		return returnLocation;
	}
	
	@GetMapping("/logout")
	public String logout(
			HttpSession s) {
		s.invalidate();
		return "redirect:/";
	}

	@GetMapping("/")
	public String index(ModelMap m) {
		m.put("view", "home/index");
		return "_t/frame";
	}
}
