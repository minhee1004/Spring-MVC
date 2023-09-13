package org.sp.springapp.model.gallery;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.sp.springapp.domain.GalleryImg;
import org.sp.springapp.exception.GalleryImgException;
import org.sp.springapp.mybatis.MybatisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository //메모리에 자동으로 올리기
public class MybatisGalleryImgDAO implements GalleryImgDAO{
	
	@Autowired
	private MybatisConfig mybatisConfig;
	
	@Override
	public void insert(GalleryImg galleryImg) throws GalleryImgException{
		SqlSession sqlSession=mybatisConfig.getSqlSession();
	
		int result=sqlSession.insert("GalleryImg.insert",galleryImg);
		sqlSession.commit();
		mybatisConfig.release(sqlSession);
		
		if(result==0) {
			throw new GalleryImgException("이미지 등록에 실패하였습니다");
		}
	}

	@Override
	public List selectAll() {
		return null;
	}

	@Override
	public GalleryImgDAO select(int gallery_im_idx) {
		return null;
	}

	@Override
	public void update(GalleryImgDAO galleryImg) {
		
	}

	@Override
	public void delete(int gallery_img_idx) {
		
	}
	
	@Override
	public void deleteByGalleryIdx(int gallery_idx) throws GalleryImgException{
		SqlSession sqlSession = mybatisConfig.getSqlSession();
		int result = sqlSession.delete("GalleryImg.deleteByGalleryIdx", gallery_idx);
		sqlSession.commit();
		mybatisConfig.release(sqlSession);
		
		if(result<1) {
			throw new GalleryImgException("이미지 레코드 삭제 실패");
		}
	}

}
