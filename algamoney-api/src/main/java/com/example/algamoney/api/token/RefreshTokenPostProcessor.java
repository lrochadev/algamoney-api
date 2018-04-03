package com.example.algamoney.api.token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.example.algamoney.api.config.property.AlgamoneyApiProperty;


/**
 * 
 * @ControllerAdvice - Interceptor
 * ResponseBodyAdvice - Monitora a resposta de uma requisição.
 * @author Leonardo
 *
 */
@Profile("oauth-security")
@ControllerAdvice
public class RefreshTokenPostProcessor implements ResponseBodyAdvice<OAuth2AccessToken> {
	
	@Autowired
	private AlgamoneyApiProperty algamoneyApiProperty;
	
	/**
	 * 
	 * Método responsável por filtrar a ida para o método beforeBodyWrite()
	 * somente na situação abaixo.
	 * 
	 * Quando é feito um post para -> localhost:8080/oauth/token
	 * Cai na classe TokenEndpoint, no método: postAccessToken
	 * retonando um ResponseEntity<OAuth2AccessToken> 
	 * 
	 */
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return "postAccessToken".equals(returnType.getMethod().getName());
	}
	
	@Override
	public OAuth2AccessToken beforeBodyWrite(OAuth2AccessToken body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		
		HttpServletRequest req = ((ServletServerHttpRequest)request).getServletRequest();
		HttpServletResponse resp = ((ServletServerHttpResponse)response).getServletResponse();
		DefaultOAuth2AccessToken token = (DefaultOAuth2AccessToken) body;
		
		String refreshToken = body.getRefreshToken().getValue();

		resp = adicionarRefreshTokenNoCookie(refreshToken, req, resp);
		
		removerRefreshTokenDoBody(token);
		
		return body;
	}

	/**
	 * 
	 * Remove o refresh token do body por referência, como boa pratica.
	 * @param token
	 * 
	 */
	private void removerRefreshTokenDoBody(DefaultOAuth2AccessToken token) {
		token.setRefreshToken(null);
	}

	private HttpServletResponse adicionarRefreshTokenNoCookie(String refreshToken, HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = new Cookie("refreshToken", refreshToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(algamoneyApiProperty.getSeguranca().isEnableHttps());
		cookie.setPath(request.getContextPath() + "/oauth/token");
		cookie.setMaxAge(2592000);
		
		response.addCookie(cookie);
		
		return response;
	}
}