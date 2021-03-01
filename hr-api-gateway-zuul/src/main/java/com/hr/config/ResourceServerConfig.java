package com.hr.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/*
 * Configura o projeto como um Resource Server, que irá receber as credenciais e despachar a requisição se tiver autorizado.
 * 
 * Localmente, os endpoints continuam podendo ser acessados diretamente, sem passar pelo Resource Server. 
 * Num ambiente de produção/testes isso é resolvido colocando os recursos (microserviços) isolados numa rede (VPC da aws, por exemplo)
 * e expondo somente o gateway, que é o resource server, para que todas as requisições passem por ele e consequentemente 
 * aplicando as regras do Spring Security.
 * Em resumo é fechar as redes com algum recurso e expor apenas o gateway.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

	private static final String[] PUBLIC_ROUTES = {"/hr-oauth/oauth/token"};
	private static final String[] OPERATOR_ROUTES = {"/hr-worker/**"};
	private static final String[] ADMIN_ROUTES = {"/hr-payroll/**", "/hr-user/**", "/actuator/**", "/hr-worker/actuator/**", "/hr-oauth/actuator/**"};
	
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
		
		http.cors().configurationSource(corsConfigurationSource());
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Arrays.asList("*"));
		corsConfig.setAllowedMethods(Arrays.asList("POST, GET, PUT, DELETE, PATCH"));
		corsConfig.setAllowCredentials(true);
		corsConfig.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);
		
		return source;
	}
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(corsConfigurationSource()));
		bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		
		return bean;
	}
}