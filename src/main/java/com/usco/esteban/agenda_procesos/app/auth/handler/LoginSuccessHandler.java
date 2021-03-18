package com.usco.esteban.agenda_procesos.app.auth.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.support.SessionFlashMapManager;

/* */
@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler
{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		SessionFlashMapManager flashMapManager = new SessionFlashMapManager();
		
		/* un flash map es una lista de java en el cual se pueden almacenar varios mensajes
		 * flash */
		FlashMap flashMap = new FlashMap();
		
		
		flashMap.put("success", "Hola, "+ authentication.getName()  +", Has iniciado sesión con éxito.");
		
		/* validamos que no sea nulo el objeto (es decir, que se haya iniciado sesión.
		 * es poco problable ya que estamos en el success del login, pero de igual 
		 * forma se valida */
		if(authentication != null)
		{
			/* de esta forma se registra en nuestra 'bitacora' los usuarios que se vayan logeando */
			logger.info("El usuario '"+ authentication.getName() +"' ha iniciado sesión");
		}
		
		/* despues de crear el falshMap se tiene que registrar mediante el flashMapManager*/
		flashMapManager.saveOutputFlashMap(flashMap, request, response);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	
}
