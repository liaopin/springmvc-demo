package com.lp.mybatis.test;

import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import com.lp.springmvc.demo.entity.MyUser;
import com.lp.springmvc.demo.mapper.MyUserMapper;
/**
 * 
 * @author Administrator
 * mybatis框架测试
 *
 */
public class MybatisTest {
	
	@Test
	public void queryMyUser() throws Exception{
		String resource = "mybatis-config.xml";
		InputStream inputStream = Resources.getResourceAsStream(resource);
		SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
		
		SqlSession session = sqlSessionFactory.openSession();
		try {
			MyUserMapper mapper = session.getMapper(MyUserMapper.class);
			List<MyUser> myUsers =mapper.findAll();
			System.out.println("数据库记录:" + myUsers.size());
			
		}catch(Exception e){
			throw e;
		}finally{
		  session.close();
		}
	}

}
