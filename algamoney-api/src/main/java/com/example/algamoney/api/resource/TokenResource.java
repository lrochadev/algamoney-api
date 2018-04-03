package com.example.algamoney.api.resource;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;

@Profile("oauth-security")
@RestController
@RequestMapping("/tokens")
public class TokenResource {
	
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty; 

	/**
	 * Remove o REFRESH TOKEN do cookie com isso só o access token fica ativo por poucos segundos.
	 * @param request
	 * @param response
	 */
	@DeleteMapping("/revoke")
	public void revoke(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("refreshToken", null);
		cookie.setHttpOnly(true);
		cookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
		cookie.setPath(request.getContextPath() + "/oauth/token");
		cookie.setMaxAge(0);

		response.addCookie(cookie);
		response.setStatus(HttpStatus.NO_CONTENT.value());
	}
}