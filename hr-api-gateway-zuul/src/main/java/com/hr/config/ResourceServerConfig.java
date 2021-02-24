package com.hr.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

//Configura o projeto como um Resource Server, que irá receber as credenciais e despachar a requisição se tiver autorizado.
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String[] PUBLIC_ROUTES = {"/hr-oauth/oauth/token"};
	private static final String[] OPERATOR_ROUTES = {"/hr-worker/**"};
	private static final String[] ADMIN_ROUTES = {"/hr-payroll/**", "/hr-user/**"};
	
	@Autowired
	private JwtTokenStore tokenStore;
	
	//Configuração para poder ler o token
	@Override
	public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
		resources.tokenStore(tokenStore);
	}

	//Configura as autorizações
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
			.antMatchers(PUBLIC_ROUTES).permitAll()
			.antMatchers(HttpMethod.GET, OPERATOR_ROUTES).hasAnyRole("OPERATOR", "ADMIN")
			.antMatchers(ADMIN_ROUTES).hasRole("ADMIN")
			.anyRequest().authenticated();
	}
}