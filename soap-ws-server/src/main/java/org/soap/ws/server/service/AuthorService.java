package org.soap.ws.server.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

import org.soap.ws.server.bean.Author;

@WebService(name = "AuthorServiceType", // portType名称 客户端生成代码时 为接口名称
		targetNamespace = "http://soap.ws.org/service"// 服务目标空间
)
public interface AuthorService {

	/**
	 * 根据名称获取作者信息
	 * 
	 */
	@WebMethod(operationName = "getAuthorByName")
	Author getAuthor(@WebParam(name = "authorName") String name);

	/**
	 * 获取作者列表信息
	 * 
	 */
	@WebMethod
	List<Author> getAuthorList();

	/**
	 * 返回字符串测试
	 * 
	 */
	@WebResult(name="authorName")
	String getAuthorString(@WebParam(name = "authorName") String name);
}
