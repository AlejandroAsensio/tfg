package org.tfg.teafind.utils;

import java.util.UUID;

/**
 * Genera y devuelve una cadena de texto compuesta por caracteres numéricos aleatorios.
 */
public class UUIDUtils {
	 public static String getUUID(){
         return UUID.randomUUID().toString().replace("-", "");
     }
}
