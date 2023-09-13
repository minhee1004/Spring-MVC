package org.sp.springapp.util;

import java.io.File;
import java.io.IOException;

import org.sp.springapp.exception.FileException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

//scan에 의해 자동 인스턴스 생성
@Component
public class FileManager {

	// 확장자 구하기 432432432.jpg
	public static String getExt(String path) {
		int index = path.lastIndexOf(".");
		return path.substring(index + 1, path.length());// exclusive
	}

	// 최종적인 파일명 만들기 789788.jpg
	public static String createFilename(String filename) {
		long time = System.currentTimeMillis();

		return time + "." + getExt(filename);
	}

	// 파일 저장
	public String save(String path, String filename, MultipartFile mf) throws FileException{
		//util 패키지의 메서드로 파일 한 건을 저장하는 기능 수행

		// 파일명 만들기
		String newName = FileManager.createFilename(filename);
		
		File file = new File(path+newName);

		try {
			mf.transferTo(file);
		} catch (IllegalStateException e) {
			throw new FileException("파일 저장 실패", e);
		} catch (IOException e) {
			throw new FileException("파일 저장 실패", e);	
		}
		
		return newName;
	}
	
	//파일 삭제
	public void remove(String path) throws FileException{
		File file = new File(path);
		boolean result = file.delete(); //파일 삭제 메서드
		if(result==false) {
			throw new FileException("파일 삭제를 실패하였습니다.");
		}
	}
}
