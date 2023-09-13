package org.sp.springapp.controller.client;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.sp.springapp.domain.Gallery;
import org.sp.springapp.domain.GalleryImg;
import org.sp.springapp.exception.FileException;
import org.sp.springapp.exception.GalleryException;
import org.sp.springapp.exception.GalleryImgException;
import org.sp.springapp.model.gallery.GalleryService;
import org.sp.springapp.util.FileManager;
import org.sp.springapp.util.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

//갤러리와 관련된 요청을 처리하는 하위 컨트롤러
@Controller
public class GalleryController {
	
	//컨트롤러가 직접 DAO를 다루게 되면, 트랜잭션 처리까지 부담한다거나
	//Model 파트의 업무를 너무 전문적으로 처리하게 된다.
	//→ 컨트롤러와 모델의 업무 경계가 모호해지므로, 즉 코드의 분리가 안되므로
	//추후 비슷한 업무를 수행할 때 코드를 분리해놓지 않았기 때문에 코드의 재사용성이 떨어진다.
	@Autowired
	private GalleryService galleryService;
	
	@Autowired
	private FileManager fileManager;
	
	@Autowired
	private Pager pager;
	
	//게시판 목록 요청  처리
	@RequestMapping(value="/gallery/list",method=RequestMethod.GET)
	public ModelAndView getList(HttpServletRequest request) {
		//3단계 : 일 시키기
		List galleryList = galleryService.selectAll();
		
		//4단계 : 목록 저장
		pager.init(galleryList, request);
		
		ModelAndView mav = new ModelAndView("gallery/list");
		mav.addObject("galleryList", galleryList); //요청 객체에 galleryList 저장
		mav.addObject("pager", pager); //요청 객체에 pager 저장
		//저장했다는 것은 포워딩이 필요하다는 것. 요청 유지한 채 저장 데이터를 전달해야 한다. redirect 아님
		
		return mav;
	}

	//글쓰기 폼 요청
	@RequestMapping(value="/gallery/registform", method=RequestMethod.GET)
	public String getForm() {
		return "gallery/regist";
		
	}
	
	//글쓰기 요청 처리
	@RequestMapping(value="/gallery/regist", method=RequestMethod.POST)
	public String regist(Gallery gallery, HttpServletRequest request) {
		//3단계 : 오라클에 글등록 + 파일 업로드 + 
		
		MultipartFile[] photo = gallery.getPhoto();
		System.out.println("넘겨받은 파일의 수는 "+gallery.getPhoto().length);
		
		//jsp의 application 내장객체는 서블릿 api에서 ServletContext 이다.
		//따라서 이 객체를 얻기 위해 HttpSession을 얻어야 한다.
		ServletContext context=request.getSession().getServletContext();
		String path=context.getRealPath("/resources/data/");
		System.out.println("파일이 저장될 풀 경로는 "+path);
		
		List<GalleryImg> ImgList=new ArrayList<GalleryImg>(); //새롭게 생성한 파일명이 누적될 곳
		
		for(int i=0;i<photo.length; i++) {
			if(photo[i].isEmpty()==false) { //비어있지 않다면, 즉 업로드가 된 경우만. 업로드된 파일에 한해서만 반복문을 돌린다.
				String filename = photo[i].getOriginalFilename();
				String name=fileManager.save(path, filename, photo[i]); //Sun사에서 강요하는 예외는 try-catch문을 쓰지 않으려면 따로 예외를 가공해야 함
				// → 강요되지 않는 예외인 RuntimeException을 상속받아 해당 예외 (file) 처리를 따로 만든다
				// → RuntimeException을 상속받아 예외 객체를 만들면 강요되는 예외를 강요받지 않는 예외 객체로 만들 수 있음!
				
				GalleryImg galleryImg = new GalleryImg(); //비어있는 dto 생성
				galleryImg.setGallery(gallery); //이 시점의 gallery dto에는 아직 gallery_idx는 0인 상태
				galleryImg.setFilename(name); //새롭게 바뀐 이름으로 대체
				
				ImgList.add(galleryImg); //galleryImg dto가 List에 모인다.
			}
		}
		
		//Gallery dto에 GalleryImg 들을 생성하여 List로 넣어두기
		//Service가 넘겨 받은 Gallery로 GalleryImgDAO에게 일 시키기 위해서 
		//상위 컨트롤러가 Gallery에 GalleryImg를 채워놓는 것
		gallery.setGalleryImgList(ImgList);
		
		//서비스에서 예외가 발생했을 떈, 스프링의 컨트롤러는 예외를 감지하는 이벤트가 발생한다
		//이때 이 이벤트를 처리할 수 있는 메서드를 정의해놓고 개발자가 알맞는 에러 페이지 및 메시지를 구성한다
		galleryService.regist(gallery); //글 등록 요청
				
		return "redirect:/gallery/list"; //형님인 DispatcherServlet이 ViewResolver를 이용하여, 이 몸뚱아리를 해석
	}
	
	//상세보기 요청 처리
	@RequestMapping(value="/gallery/content", method=RequestMethod.GET)
	public ModelAndView getContent(int gallery_idx) {
		//3단계: 데이터 가져오기
		Gallery gallery = galleryService.select(gallery_idx);
		
		//4단계: 가져온 레코드 저장(jsp로 가져가기 위해서)
		ModelAndView mav = new ModelAndView();
		mav.addObject("gallery", gallery); //request.setAttribute() 와 같은 결과의 로직
		mav.setViewName("gallery/content"); //접두어, 접미어 뺸 경로 입력
		
		return mav;
	}
	
	//삭제요청 처리
	@RequestMapping(value="/gallery/delete", method=RequestMethod.POST)
	public String del(int gallery_idx, String[] filename, HttpServletRequest request) {
		//3단게: 삭제
		ServletContext context = request.getSession().getServletContext();
		
		for(String str : filename) {
			System.out.println("파일명 배열은 "+str);
			
			//삭제될 파일의 풀 경로 얻기
			String path = context.getRealPath("/resources/data/"+str);
			
			fileManager.remove(path);
		}
		
		galleryService.delete(gallery_idx); //db 삭제
		
		//4단계: 리스트를 재요청 들어오게 할 것이므로, jsp로 가져갈 것이 없다 -> redirect
		return "redirect:/gallery/list";
	}
	
	//어떠한 예외가 발생했을 때, 어떤 처리는 할지 아래 메서드에서 로직을 작성
	@ExceptionHandler(FileException.class)
	public ModelAndView handle(FileException e) { //e에는 각 DAO에서 처리한 galleryException에 반응한 메시지가 들어있다
		//jsp에서 에러 메시지를 보여줘야 하므로, 요청은 유지돼야 한다.
		//즉 저장할 것이 있고, 가져갈 것이 있다 → ModelAndView 객체 이용
		ModelAndView mav = new ModelAndView();
		mav.addObject("e", e); //에러 객체 저장
		mav.setViewName("error/result");
		
		return mav;
	}
	
	//어떠한 예외가 발생했을 때, 어떤 처리는 할지 아래 메서드에서 로직을 작성
	@ExceptionHandler(GalleryException.class)
	public ModelAndView handle(GalleryException e) { //e에는 각 DAO에서 처리한 galleryException에 반응한 메시지가 들어있다
		//jsp에서 에러 메시지를 보여줘야 하므로, 요청은 유지돼야 한다.
		//즉 저장할 것이 있고, 가져갈 것이 있다 → ModelAndView 객체 이용
		ModelAndView mav = new ModelAndView();
		mav.addObject("e", e); //에러 객체 저장
		mav.setViewName("error/result");
		
		return mav;
	}
	
	@ExceptionHandler(GalleryImgException.class)
	public ModelAndView handle(GalleryImgException e) { //e에는 각 DAO에서 처리한 galleryException에 반응한 메시지가 들어있다
		//jsp에서 에러 메시지를 보여줘야 하므로, 요청은 유지돼야 한다.
		//즉 저장할 것이 있고, 가져갈 것이 있다 → ModelAndView 객체 이용
		ModelAndView mav = new ModelAndView();
		mav.addObject("e", e); //에러 객체 저장
		mav.setViewName("error/result");
		
		return mav;
	}

}
