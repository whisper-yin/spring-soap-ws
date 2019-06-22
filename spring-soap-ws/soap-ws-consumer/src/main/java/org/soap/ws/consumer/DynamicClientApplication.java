package org.soap.ws.consumer;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicClientApplication {
	private static final Logger logger = LoggerFactory.getLogger(DynamicClientApplication.class);

	public static void main(String[] args) {

		Map<String, String> nameSpaceMap = new HashMap<>();

		JaxWsDynamicClientFactory jaxWsDynamicClientFactory = JaxWsDynamicClientFactory.newInstance();

		Client client = jaxWsDynamicClientFactory.createClient("http://localhost:8089/ws/countries.wsdl");
		nameSpaceMap.put("SOAP-ENV", "http://schemas.xmlsoap.org/soap/envelope/");
//		nameSpaceMap.put("sch", "http://producer.ws.soap.org/service");
		client.getRequestContext().put("soap.env.ns.map", nameSpaceMap);
		client.getRequestContext().put("disable.outputstream,optimization", "true");
		client.getInInterceptors().add(new LoggingInInterceptor());
		client.getOutInterceptors().add(new LoggingOutInterceptor());

		try {
			Object order = Thread.currentThread().getContextClassLoader()
					.loadClass("org.soap.ws.producer.service.GetCountryRequest").newInstance();
			Method m = order.getClass().getMethod("setName", String.class);
			m.invoke(order, "Spain");
			Object[] objects = client.invoke("getCountry", order);
			logger.info("country info is:[{}]", objects[0].toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
