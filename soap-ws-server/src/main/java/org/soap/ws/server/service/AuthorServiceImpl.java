package org.soap.ws.server.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.jws.WebService;

import org.soap.ws.server.bean.Author;
import org.soap.ws.server.data.Sex;

@WebService(name = "AuthorServiceType", // wsdl port Type
		targetNamespace = "http://soap.ws.org/service", // 服务目标空间
		endpointInterface = "org.soap.ws.server.service.AuthorService", // portType名称 客户端生成代码时 为接口名称
		serviceName = "authorService", // 服务name名称
		portName = "authorPortName" // port名称
)
public class AuthorServiceImpl implements AuthorService {

	@Override
	public Author getAuthor(String name) {
		Author author = new Author();
		author.setBirthday("1990-01-23");
		author.setName("姓名：" + name);
		author.setSex(Sex.MALE);
		author.setHobby(Arrays.asList("电影", "旅游"));
		author.setDescription("现在时间：" + new Date().getTime());
		return author;
	}

	@Override
	public List<Author> getAuthorList() {
		List<Author> resultList = new ArrayList<>();
		Author author = new Author();
		author.setBirthday("1990-01-23");
		author.setName("姓名：jason");
		author.setSex(Sex.MALE);
		author.setHobby(Arrays.asList("电影", "旅游"));
		author.setDescription("现在时间：" + new Date().getTime());
		resultList.add(author);
		resultList.add(author);
		return resultList;
	}

	@Override
	public String getAuthorString(String name) {
		Author author = getAuthor(name);
		return author.getName();
	}

}
