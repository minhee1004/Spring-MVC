package org.sp.springapp;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		//3단계 업무 : 일시킨다
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		//4단계: 현재날짜를 결과로 저장, 뷰 처리는 jsp가 담당하므로, 여기서 생성된 데이터를 jsp까지 죽지 않고 
		//가져가려면,  requset에 저장
		model.addAttribute("serverTime", formattedDate ); 
		
		//out.print("<script>"); //디자인을 담당하는 순간부터 뷰가 되어버린다 
		
		return "home";
	}
	
}
