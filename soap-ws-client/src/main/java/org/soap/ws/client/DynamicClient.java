package org.soap.ws.client;

import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.soap.ws.client.utils.LoggingInMessageInterceptor;
import org.soap.ws.client.utils.LoggingOutMessageInterceptor;

public class DynamicClient {
	private static final Logger logger = LoggerFactory.getLogger(DynamicClient.class);

	public static void main(String[] args) {

		// Map<String, String> nameSpaceMap = new HashMap<>();

		// 创建动态客户端
		JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();
		Client client = jaxWsDynamicClientFactory.createClient("http://localhost:8080/ws/author?wsdl");
		// 设置命名空间
		// nameSpaceMap.put("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
		// nameSpaceMap.put("sch", "http://producer.ws.soap.org/service");
		// client.getRequestContext().put("soap.env.ns.map", nameSpaceMap);
		// client.getRequestContext().put("disable.outputstream,optimization", "true");
		// 设置日志拦截器
		client.getInInterceptors().add(new LoggingInMessageInterceptor());
		client.getOutInterceptors().add(new LoggingOutMessageInterceptor());

		try {
			// 发送请求
			Object[] objects = client.invoke("getAuthorByName", "jason");

			// 获取服务接口方法
			Method[] methods = objects[0].getClass().getMethods();

			// 遍历返回结果
			for (Method method : methods) {
				if (method.getName().startsWith("get")) {
					logger.info("{} - {}", method.getName(), method.invoke(objects[0]));
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
