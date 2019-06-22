package org.soap.ws.server.client;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soap.ws.server.bean.Author;
import org.soap.ws.server.service.AuthorService;
import org.soap.ws.server.utils.LoggingInMessageInterceptor;
import org.soap.ws.server.utils.LoggingOutMessageInterceptor;

public class StaticClient {

	private static final Logger logger = LoggerFactory.getLogger(StaticClient.class);

	public static void main(String[] args) {
		JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();

		jaxWsProxyFactoryBean.setAddress("http://localhost:8080/ws/author");
		jaxWsProxyFactoryBean.setServiceClass(AuthorService.class);

		// 设置日志拦截
		jaxWsProxyFactoryBean.getInInterceptors().add(new LoggingInMessageInterceptor());
		jaxWsProxyFactoryBean.getOutInterceptors().add(new LoggingOutMessageInterceptor());

		// 创建会发起一个http请求
		AuthorService authorService = (AuthorService) jaxWsProxyFactoryBean.create();

		// 调用方法，获得数据
		Author author = authorService.getAuthor("jason");

		logger.info("[{}] ", author.getName());

	}
}
