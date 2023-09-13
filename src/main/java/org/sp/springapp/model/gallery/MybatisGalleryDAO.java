package org.sp.springapp.model.gallery;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.sp.springapp.domain.Gallery;
import org.sp.springapp.exception.GalleryException;
import org.sp.springapp.mybatis.MybatisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository //자동으로 메모리에 올려주는. 자바가 DAO를  repository 컴포넌트로 인식하기 때문에
public class MybatisGalleryDAO implements GalleryDAO{
	
	@Autowired //메모리에 올라온 객체 dao에 mybatisConfig을 자동으로 넣어주는 
	private MybatisConfig mybatisConfig;
	
	@Override
	public void insert(Gallery gallery) throws GalleryException{//비정상 종료를 막기 위해 이 메서드를 호출하는 상위 컨트롤러에게 예외처리를 전가한다
		SqlSession sqlSession=mybatisConfig.getSqlSession();
		
		int result=sqlSession.insert("Gallery.insert", gallery); //galleryMapper의 insert 태그 수행, 결과 데이터를 넘겨줄 객체 gallery
		sqlSession.commit(); //DML인 경우 커밋 후
		mybatisConfig.release(sqlSession); //반납
		
		//result=0; //error 테스트용
		
		if(result==0) {
			//개발자가 일부러 관련있는 에러를 일으키자 -> 발생한 예외는 
			throw new GalleryException("갤러리 등록을 실패하였습니다");
		}
	}

	@Override
	public List selectAll() {
		SqlSession sqlSession = mybatisConfig.getSqlSession();
		List list = sqlSession.selectList("Gallery.selectAll");
		mybatisConfig.release(sqlSession);
		
		return list;
	}

	@Override
	public Gallery select(int gallery_idx) {
		SqlSession sqlSession = mybatisConfig.getSqlSession();
		Gallery gallery=sqlSession.selectOne("Gallery.select", gallery_idx);
		mybatisConfig.release(sqlSession);
		
		return gallery;
	}

	@Override
	public void update(Gallery gallery) {
		
	}

	@Override
	public void delete(int gallery_idx) throws GalleryException{
		SqlSession sqlSession = mybatisConfig.getSqlSession();
		int result = sqlSession.delete("Gallery.delete", gallery_idx);
		sqlSession.commit();
		mybatisConfig.release(sqlSession);
		
		if(result<1) { //삭제 실패시
			throw new GalleryException("삭제 실패");
		}
	}

}
