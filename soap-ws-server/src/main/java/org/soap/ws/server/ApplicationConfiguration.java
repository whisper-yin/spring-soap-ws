package org.soap.ws.server;

import javax.xml.ws.Endpoint;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.soap.ws.server.service.AuthorService;
import org.soap.ws.server.service.AuthorServiceImpl;
import org.soap.ws.server.utils.LoggingInMessageInterceptor;
import org.soap.ws.server.utils.LoggingOutMessageInterceptor;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

	@Bean(name = "cxfServletRegistration")
	public ServletRegistrationBean dispatcherServlet() {
		return new ServletRegistrationBean(new CXFServlet(), "/ws/*");
	}

	@Bean
	public AuthorService authorService() {
		return new AuthorServiceImpl();
	}

	@Bean(name = Bus.DEFAULT_BUS_ID)
	public SpringBus springBus() {
		SpringBus springBus = new SpringBus();
		return springBus;
	}

	@Bean
	public Endpoint endpoint(AuthorService authorService) {
		EndpointImpl endpoint = new EndpointImpl(springBus(), authorService);
		endpoint.getInInterceptors().add(new LoggingInMessageInterceptor());
		endpoint.getOutInterceptors().add(new LoggingOutMessageInterceptor());
		endpoint.publish("/author");// 发布地址
		return endpoint;
	}
}
