package com.mingi.springframe.sqlservice.notuse;

import java.io.InputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.mingi.springframe.dao.UserDao;
import com.mingi.springframe.sqlservice.SqlReader;
import com.mingi.springframe.sqlservice.SqlRegistry;
import com.mingi.springframe.sqlservice.json.SQLMap;
import com.mingi.springframe.sqlservice.json.SQLQuery;

public class JaxbXmlSqlReader implements SqlReader {

	private final String DEFAULT_SQLMAP_FILE = "sqlmap.xml";
	private String sqlmapFile = DEFAULT_SQLMAP_FILE;

	public void setSqlmapFile(String sqlmapFile) { this.sqlmapFile = sqlmapFile; }

	public void read(SqlRegistry sqlRegistry) {
		String contextPath = SQLMap.class.getPackage().getName();
		try {
			JAXBContext context = JAXBContext.newInstance(contextPath);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			InputStream is = UserDao.class.getResourceAsStream(sqlmapFile);
			
			SQLMap sqlmap = (SQLMap)unmarshaller.unmarshal(is);
			for(SQLQuery sql : sqlmap.getSqlmap()) {
				sqlRegistry.registerSql(sql.getKey(), sql.getQuery());
			}
		} 
		catch (JAXBException e) { throw new RuntimeException(e); } 		
	}
}