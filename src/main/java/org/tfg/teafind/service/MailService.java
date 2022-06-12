package org.tfg.teafind.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {
	
	@Autowired
    private JavaMailSender javaMailSender;

	/**
	 * Manda un correo electrónico cuando el usuario se registra correctamente
	 * desde la dirección establecida a la dirección que llega "toEmail"
	 * con el cuerpo y el asunto personalizados a los del usuario.
	 * 
	 * @param toEmail 	Dirección de correo que recibirá el mensaje.
	 * @param nick		Nick del usuario que recibe el mensaje. Aparecerá en el asunto del mensaje.
	 * @param nombre	Nombre del usuario que recibe el mensaje. Aparecerá en el cuerpo del mensaje.
	 * @throws MessagingException
	 */
    public void enviarEmailBienvenida(String toEmail, String nick, String nombre) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom("teafindes@gmail.com");
        helper.setTo(toEmail);
        helper.setText(cuerpoMailBienvenida(nombre), true);
        helper.setSubject(nick + ", ¡bienvenido a teafind!");
        javaMailSender.send(mimeMessage);
        System.out.println("Email enviado");
    }

	/**
	 * Manda un correo electrónico cuando el usuario se registra correctamente
	 * desde la dirección establecida a la dirección que llega "toEmail"
	 * con el cuerpo y el asunto personalizados a los del usuario.
	 * 
	 * @param toEmail 			Dirección de correo que recibirá el mensaje.
	 * @param numVerificacion	Número que aparecerá en el cuerpo del mensaje.
	 * @throws MessagingException
	 */
	public void enviarEmailVerificacion(String toEmail, int numVerificacion) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

        helper.setFrom("teafindes@gmail.com");
        helper.setTo(toEmail);
        helper.setText(cuerpoMailVerificacion(numVerificacion), true);
        helper.setSubject("Activa tu cuenta de Teafind");
        javaMailSender.send(mimeMessage);
        System.out.println("Email enviado");
    }
    
    public String cuerpoMailBienvenida(String nombre) {
    	return "<!DOCTYPE html>\r\n"
    			+ "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\r\n"
    			+ "<head>\r\n"
    			+ "  <meta charset=\"utf-8\">\r\n"
    			+ "  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\r\n"
    			+ "  <meta name=\"x-apple-disable-message-reformatting\">\r\n"
    			+ "  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\r\n"
    			+ "	<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\r\n"
    			+ "	<link\r\n"
    			+ "		href=\"https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,100;0,300;0,400;0,700;0,900;1,100;1,300;1,400;1,700;1,900&display=swap\"\r\n"
    			+ "		rel=\"stylesheet\">\r\n"
    			+ "  <title></title>\r\n"
    			+ "  <!--[if mso]>\r\n"
    			+ "  <style>\r\n"
    			+ "    table {border-collapse:collapse;border-spacing:0;border:none;margin:0;}\r\n"
    			+ "    div, td {padding:0;}\r\n"
    			+ "    div {margin:0 !important;}\r\n"
    			+ "  </style>\r\n"
    			+ "  <noscript>\r\n"
    			+ "    <xml>\r\n"
    			+ "      <o:OfficeDocumentSettings>\r\n"
    			+ "        <o:PixelsPerInch>96</o:PixelsPerInch>\r\n"
    			+ "      </o:OfficeDocumentSettings>\r\n"
    			+ "    </xml>\r\n"
    			+ "  </noscript>\r\n"
    			+ "  <![endif]-->\r\n"
    			+ "  <style>\r\n"
    			+ "    table, td, div, h1, p {\r\n"
    			+ "        font-family: 'Lato', sans-serif;\r\n"
    			+ "    }\r\n"
    			+ "    @media screen and (max-width: 530px) {\r\n"
    			+ "      .unsub {\r\n"
    			+ "        display: block;\r\n"
    			+ "        padding: 8px;\r\n"
    			+ "        margin-top: 14px;\r\n"
    			+ "        border-radius: 6px;\r\n"
    			+ "        background-color: #555555;\r\n"
    			+ "        text-decoration: none !important;\r\n"
    			+ "        font-weight: bold;\r\n"
    			+ "      }\r\n"
    			+ "      .col-lge {\r\n"
    			+ "        max-width: 100% !important;\r\n"
    			+ "      }\r\n"
    			+ "    }\r\n"
    			+ "    @media screen and (min-width: 531px) {\r\n"
    			+ "      .col-sml {\r\n"
    			+ "        max-width: 27% !important;\r\n"
    			+ "      }\r\n"
    			+ "      .col-lge {\r\n"
    			+ "        max-width: 73% !important;\r\n"
    			+ "      }\r\n"
    			+ "    }\r\n"
    			+ "  </style>\r\n"
    			+ "</head>\r\n"
    			+ "<body style=\"margin:0;padding:0;word-spacing:normal;background-color:#939297;\">\r\n"
    			+ "  <div role=\"article\" aria-roledescription=\"email\" lang=\"en\" style=\"text-size-adjust:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;background-color:#939297;\">\r\n"
    			+ "    <table role=\"presentation\" style=\"width:100%;border:none;border-spacing:0;\">\r\n"
    			+ "      <tr>\r\n"
    			+ "        <td align=\"center\" style=\"padding:0;\">\r\n"
    			+ "          <!--[if mso]>\r\n"
    			+ "          <table role=\"presentation\" align=\"center\" style=\"width:600px;\">\r\n"
    			+ "          <tr>\r\n"
    			+ "          <td>\r\n"
    			+ "          <![endif]-->\r\n"
    			+ "          <table role=\"presentation\" style=\"width:94%;max-width:600px;border:none;border-spacing:0;text-align:left;font-family:'Lato',sans-serif;font-size:16px;line-height:22px;color:#363636;\">\r\n"
    			+ "            <tr>\r\n"
    			+ "                <td style=\"padding:40px 30px 30px 30px;padding:30px;\">\r\n"
    			+ "                </td>\r\n"
    			+ "              </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:40px 30px 30px 30px;text-align:center;font-size:24px;font-weight:bold;padding:30px;background-color:#ffffff;\">\r\n"
    			+ "                <a href=\"http://www.example.com/\" style=\"text-decoration:none;\"><img src=\"https://i.imgur.com/z7B9ZTR.png\" width=\"165\" alt=\"Logo\" style=\"width:165px;max-width:80%;height:auto;border:none;text-decoration:none;color:#ffffff;\"></a>\r\n"
    			+ "                <h1 style=\"margin-top:0;margin-bottom:16px;font-size:26px;line-height:32px;font-weight:bold;letter-spacing:-0.02em;\">¡Bienvenido a Teafind!</h1>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:30px;background-color:#ffffff;\">\r\n"
    			+ "                <p style=\"margin:0;\">\r\n"
    			+ "                    " + nombre + ", ¡muchas gracias por registrarte en Teafind!. \r\n"
    			+ "                    <br/>Antes de nada, debes saber que no vamos a molestarte con mails. Esta es tu bienvenida porque te la mereces, pero te prometemos no volver a aparecer por aquí, a no ser que nos lo pidas, claro 😁.\r\n"
    			+ "                </p>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:0;font-size:24px;line-height:28px;font-weight:bold;\">\r\n"
    			+ "                <a href=\"http://www.example.com/\" style=\"text-decoration:none;\"><img src=\"https://i.imgur.com/xjOFVyh.jpg\" width=\"600\" alt=\"Piña\" style=\"width:100%;height:auto;display:block;border:none;text-decoration:none;color:#363636;\"></a>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:35px 30px 11px 30px;font-size:0;background-color:#ffffff;border-bottom:1px solid #f0f0f5;border-color:rgba(201,201,207,.35);\">\r\n"
    			+ "                <!--[if mso]>\r\n"
    			+ "                <table role=\"presentation\" width=\"100%\">\r\n"
    			+ "                <tr>\r\n"
    			+ "                <td style=\"width:145px;\" align=\"left\" valign=\"top\">\r\n"
    			+ "                <![endif]-->\r\n"
    			+ "                <div class=\"col-sml\" style=\"display:inline-block;width:100%;max-width:145px;vertical-align:top;text-align:left;font-family:'Lato',sans-serif;font-size:14px;color:#363636;\">\r\n"
    			+ "                  <img src=\"https://i.imgur.com/cw5Kjnf.png\" width=\"115\" alt=\"Smile\" style=\"width:115px;max-width:80%;margin-bottom:20px;\">\r\n"
    			+ "                </div>\r\n"
    			+ "                <!--[if mso]>\r\n"
    			+ "                </td>\r\n"
    			+ "                <td style=\"width:395px;padding-bottom:20px;\" valign=\"top\">\r\n"
    			+ "                <![endif]-->\r\n"
    			+ "                <div class=\"col-lge\" style=\"display:inline-block;width:100%;max-width:395px;vertical-align:top;padding-bottom:20px;font-family:'Lato',sans-serif;font-size:16px;line-height:22px;color:#363636;\">\r\n"
    			+ "                    <p style=\"margin-top:0;margin-bottom:12px;\">\r\n"
    			+ "                        Ahora que formas parte de esta increíble comunidad, date una vuelta por los proyectos que otras personas han publicado y si te convence algún puesto, ¡únete a él! \r\n"
    			+ "                        <br/>\r\n"
    			+ "                        Estamos completamente seguros de que tienes madera para eso ¡y MUCHO MÁS!\r\n"
    			+ "                    </p>\r\n"
    			+ "                    <p style=\"margin-top:0;margin-bottom:18px;\">\r\n"
    			+ "                        Por eso, desde Teafind también te animamos a que publiques tu primer proyecto si tienes una idea brillante que sacar adelante.\r\n"
    			+ "                        <br/>\r\n"
    			+ "                        ¡Acabarás reuniendo un gran equipo con el que desarrollar algo grandioso!.\r\n"
    			+ "                    </p>\r\n"
    			+ "                    <p style=ºmargin:0;\"><a href=\"https://teafind.es\" style=\"background: #ff3884; text-decoration: none; padding: 10px 25px; color: #97dce4; border-radius: 4px; display:inline-block; mso-padding-alt:0;text-underline-color:#ff3884\"><!--[if mso]><i style=\"letter-spacing: 25px;mso-font-width:-100%;mso-text-raise:20pt\">&nbsp;</i><![endif]--><span style=\"mso-text-raise:10pt;font-weight:bold;\">Entra ahora en Teafind</span><!--[if mso]><i style=\"letter-spacing: 25px;mso-font-width:-100%\">&nbsp;</i><![endif]--></a></p>\r\n"
    			+ "                </div>\r\n"
    			+ "                <!--[if mso]>\r\n"
    			+ "                </td>\r\n"
    			+ "                </tr>\r\n"
    			+ "                </table>\r\n"
    			+ "                <![endif]-->\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:30px;font-size:24px;line-height:28px;font-weight:bold;background-color:#ffffff;border-bottom:1px solid #f0f0f5;border-color:rgba(201,201,207,.35);\">\r\n"
    			+ "                <a href=\"http://www.example.com/\" style=\"text-decoration:none;\"><img src=\"https://i.imgur.com/55BH4bP.jpg\" width=\"540\" alt=\"\" style=\"width:100%;height:auto;border:none;text-decoration:none;color:#363636;\"></a>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:30px;background-color:#ffffff;\">\r\n"
    			+ "                <p style=\"margin:0;\">Teafind es un proyecto de fin de grado. Si crees que has recibido este correo por error, por favor, ignóralo o contacta con nosotros a teafindes@gmail.com.</p>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:30px;text-align:center;font-size:12px;background-color:#404040;color:#cccccc;\">\r\n"
    			+ "                <p style=\"margin:0 0 8px 0;\"><a href=\"https://bit.ly/38kd14F\" style=\"text-decoration:none;\"><img src=\"https://assets.codepen.io/210284/twitter_1.png\" width=\"40\" height=\"40\" alt=\"t\" style=\"display:inline-block;color:#cccccc;\"></a></p>\r\n"
    			+ "                <p style=\"margin:0;font-size:14px;line-height:20px;\">Teafind, España 2022<br></p>\r\n"
    			+ "                <p style=\"margin:0 0 8px 0;\"><a href=\"https://creativecommons.org/licenses/by-nc-nd/4.0/\" style=\"text-decoration:none;\"><img src=\"https://licensebuttons.net/l/by-nc-nd/4.0/80x15.png\" alt=\"f\" style=\"display:inline-block;color:#cccccc;\"></a></p>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "          </table>\r\n"
    			+ "          <!--[if mso]>\r\n"
    			+ "          </td>\r\n"
    			+ "          </tr>\r\n"
    			+ "          </table>\r\n"
    			+ "          <![endif]-->\r\n"
    			+ "        </td>\r\n"
    			+ "      </tr>\r\n"
    			+ "    </table>\r\n"
    			+ "  </div>\r\n"
    			+ "</body>\r\n"
    			+ "</html>";
    }

	public String cuerpoMailVerificacion(int numVerificacion) {
    	return "<!DOCTYPE html>\r\n"
    			+ "<html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\">\r\n"
    			+ "<head>\r\n"
    			+ "  <meta charset=\"utf-8\">\r\n"
    			+ "  <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\">\r\n"
    			+ "  <meta name=\"x-apple-disable-message-reformatting\">\r\n"
    			+ "  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\r\n"
    			+ "	<link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\r\n"
    			+ "	<link\r\n"
    			+ "		href=\"https://fonts.googleapis.com/css2?family=Lato:ital,wght@0,100;0,300;0,400;0,700;0,900;1,100;1,300;1,400;1,700;1,900&display=swap\"\r\n"
    			+ "		rel=\"stylesheet\">\r\n"
    			+ "  <title></title>\r\n"
    			+ "  <!--[if mso]>\r\n"
    			+ "  <style>\r\n"
    			+ "    table {border-collapse:collapse;border-spacing:0;border:none;margin:0;}\r\n"
    			+ "    div, td {padding:0;}\r\n"
    			+ "    div {margin:0 !important;}\r\n"
    			+ "  </style>\r\n"
    			+ "  <noscript>\r\n"
    			+ "    <xml>\r\n"
    			+ "      <o:OfficeDocumentSettings>\r\n"
    			+ "        <o:PixelsPerInch>96</o:PixelsPerInch>\r\n"
    			+ "      </o:OfficeDocumentSettings>\r\n"
    			+ "    </xml>\r\n"
    			+ "  </noscript>\r\n"
    			+ "  <![endif]-->\r\n"
    			+ "  <style>\r\n"
    			+ "    table, td, div, h1, p {\r\n"
    			+ "        font-family: 'Lato', sans-serif;\r\n"
    			+ "    }\r\n"
    			+ "    @media screen and (max-width: 530px) {\r\n"
    			+ "      .unsub {\r\n"
    			+ "        display: block;\r\n"
    			+ "        padding: 8px;\r\n"
    			+ "        margin-top: 14px;\r\n"
    			+ "        border-radius: 6px;\r\n"
    			+ "        background-color: #555555;\r\n"
    			+ "        text-decoration: none !important;\r\n"
    			+ "        font-weight: bold;\r\n"
    			+ "      }\r\n"
    			+ "      .col-lge {\r\n"
    			+ "        max-width: 100% !important;\r\n"
    			+ "      }\r\n"
    			+ "    }\r\n"
    			+ "    @media screen and (min-width: 531px) {\r\n"
    			+ "      .col-sml {\r\n"
    			+ "        max-width: 27% !important;\r\n"
    			+ "      }\r\n"
    			+ "      .col-lge {\r\n"
    			+ "        max-width: 73% !important;\r\n"
    			+ "      }\r\n"
    			+ "    }\r\n"
    			+ "  </style>\r\n"
    			+ "</head>\r\n"
    			+ "<body style=\"margin:0;padding:0;word-spacing:normal;background-color:#939297;\">\r\n"
    			+ "  <div role=\"article\" aria-roledescription=\"email\" lang=\"en\" style=\"text-size-adjust:100%;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;background-color:#939297;\">\r\n"
    			+ "    <table role=\"presentation\" style=\"width:100%;border:none;border-spacing:0;\">\r\n"
    			+ "      <tr>\r\n"
    			+ "        <td align=\"center\" style=\"padding:0;\">\r\n"
    			+ "          <!--[if mso]>\r\n"
    			+ "          <table role=\"presentation\" align=\"center\" style=\"width:600px;\">\r\n"
    			+ "          <tr>\r\n"
    			+ "          <td>\r\n"
    			+ "          <![endif]-->\r\n"
    			+ "          <table role=\"presentation\" style=\"width:94%;max-width:600px;border:none;border-spacing:0;text-align:left;font-family:'Lato',sans-serif;font-size:16px;line-height:22px;color:#363636;\">\r\n"
    			+ "            <tr>\r\n"
    			+ "                <td style=\"padding:40px 30px 30px 30px;padding:30px;\">\r\n"
    			+ "                </td>\r\n"
    			+ "              </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding-top:30px;text-align:center;font-size:24px;font-weight:bold;background-color:#ffffff;\">\r\n"
    			+ "                <a href=\"http://www.example.com/\" style=\"text-decoration:none;\"><img src=\"https://i.imgur.com/z7B9ZTR.png\" width=\"165\" alt=\"Logo\" style=\"width:165px;max-width:80%;height:auto;border:none;text-decoration:none;color:#ffffff;\"></a>\r\n"
    			+ "                <h1 style=\"margin-top:0;margin-bottom:16px;font-size:26px;line-height:32px;font-weight:bold;letter-spacing:-0.02em;\">Teafind</h1>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:30px;background-color:#ffffff;\">\r\n"
    			+ "                <p style=\"margin:0;margin-bottom:10px;\">Tu código de verificación es:</p>\r\n"
    			+ "                  <fieldset style=\"border-radius:8px;\">\r\n"
    			+ "                    <p style=\"font-weight:bold;background-color:#ffffff;color:#00AFC1;font-size:larger;text-align:center;letter-spacing:4px;\">" + numVerificacion + "</p>\r\n"
    			+ "                  </fieldset>\r\n"
    			+ "                <p>Cópialo en la web para activar tu cuenta y poder desbloquear todas las funciones gratuitas de Teafind.</p>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:30px;padding-top:0px;background-color:#ffffff;\">\r\n"
    			+ "                <p style=\"margin:0;\">Teafind es un proyecto de fin de grado. Si crees que has recibido este correo por error, por favor, ignóralo o contacta con nosotros a teafindes@gmail.com.</p>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "            <tr>\r\n"
    			+ "              <td style=\"padding:30px;text-align:center;font-size:12px;background-color:#404040;color:#cccccc;\">\r\n"
    			+ "                <p style=\"margin:0;font-size:14px;line-height:20px;\">Teafind, España 2022<br></p>\r\n"
    			+ "                <p style=\"margin:0 0 8px 0;\"><a href=\"https://creativecommons.org/licenses/by-nc-nd/4.0/\" style=\"text-decoration:none;\"><img src=\"https://licensebuttons.net/l/by-nc-nd/4.0/80x15.png\" alt=\"f\" style=\"display:inline-block;color:#cccccc;\"></a></p>\r\n"
    			+ "              </td>\r\n"
    			+ "            </tr>\r\n"
    			+ "          </table>\r\n"
    			+ "          <!--[if mso]>\r\n"
    			+ "          </td>\r\n"
    			+ "          </tr>\r\n"
    			+ "          </table>\r\n"
    			+ "          <![endif]-->\r\n"
    			+ "        </td>\r\n"
    			+ "      </tr>\r\n"
    			+ "    </table>\r\n"
    			+ "  </div>\r\n"
    			+ "</body>\r\n"
    			+ "</html>";
    }
}
