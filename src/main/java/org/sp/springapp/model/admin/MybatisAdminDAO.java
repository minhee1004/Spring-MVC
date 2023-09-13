package org.sp.springapp.model.admin;

import org.apache.ibatis.session.SqlSession;
import org.sp.springapp.domain.Admin;
import org.sp.springapp.mybatis.MybatisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MybatisAdminDAO implements AdminDAO {

	@Autowired
	private MybatisConfig mybatisConfig;
	
	public Admin login(Admin admin) {
		SqlSession sqlSession = mybatisConfig.getSqlSession();
		Admin dto=sqlSession.selectOne("Admin.login", admin);
		mybatisConfig.release(sqlSession);
		
		return dto;
	}
	
}
