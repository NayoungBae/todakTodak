package java142.todak.etc.controller;

import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import java142.todak.common.ChaebunUtils;
import java142.todak.common.FileUploadUtil;
import java142.todak.etc.service.EtcService;
import java142.todak.etc.utils.LoginSession;
import java142.todak.etc.utils.MimeMailSender;
import java142.todak.human.service.HumanService;
import java142.todak.human.vo.ApprVO;
import java142.todak.human.vo.MemberVO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value="/etc")
public class EtcController {
	
	Logger logger = Logger.getLogger(EtcController.class);
	
	@Autowired
	private HumanService humanService;
	
	@Autowired
	private EtcService etcService;
	
	@RequestMapping(value="/login")
	public String login(@ModelAttribute MemberVO hmvo,
						HttpServletRequest request,
						Model model){
		//logger.info("(log) login entered");
		//logger.info(" id >>> " + hmvo.getHm_id());
		//logger.info(" pass >>> " + hmvo.getHm_pw());
		
		// μνΈν
//		BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
//		//logger.info(" encoded pass >>> " + bcpe.encode(hmvo.getHm_pw()));
//		String encodedPass = bcpe.encode(hmvo.getHm_pw());
//		hmvo.setHm_pw(encodedPass);
		
		List<MemberVO> aList = etcService.login(hmvo);
		int nCnt = aList.size();
		//logger.info(" nCnt >>> " + nCnt);
		
		String message = "";
		String url = "../../index";
		LoginSession sManager = LoginSession.getInstance();
		
		if (nCnt == 1){
			MemberVO hmvo2 = aList.get(0);
			String hm_empnum = hmvo2.getHm_empnum();
			if (!sManager.isUsing(hm_empnum)){
				sManager.setSession(request.getSession(), hm_empnum);
				//url = "redirect:/sponsor/selectCharity.td";
				model.addAttribute("hm_empnum",hm_empnum);
				url = "scheduler/selectSchedule";
				model.addAttribute("selectFunc", "login");
			}else{
				message = "μ΄λ―Έ μ¬μ©μ€μΈ μμ΄λμλλ€.";
			}
		}else{ 
			message = "μμ΄λ λλ ν¨μ€μλκ° μλͺ» λμμ΅λλ€.";
		}

		model.addAttribute("message", message);
		//logger.info("(log) login left");
		return url;
	}
	
	@RequestMapping(value="logout")
	public String logout(HttpServletRequest request,
						 Model model){
		//logger.info("(log) logout entered");
		
		request.getSession().invalidate();
		String message = "λ‘κ·Έμμ νμ΅λλ€.";
		model.addAttribute("message", message);
		String url = "../../index";
		
		//logger.info("(log) logout left");
		return url;
	}
	
	@RequestMapping(value="moveSession")
	public String moveSession(){
		return "commons/bindSession";
	}

	@RequestMapping(value="setMainSession")
	public void setMainSession(HttpServletRequest request, @RequestParam("main") String main){
//		//System.out.println(" main IN ETC CONTROLLER >>> " + main);
		LoginSession sManager = (LoginSession)request.getSession().getAttribute("login");
		
		sManager.setMain(main, request.getSession().getId());
	}
	
	@RequestMapping(value="moveId")
	public String moveId(){
		return "etc/idPopup";
	}

	@RequestMapping(value="movePw")
	public String movePw(){
		return "etc/pwPopup";
	}
	@RequestMapping(value="/insertMember", method = RequestMethod.POST)
	public String registerMember(@ModelAttribute ApprVO param, HttpServletRequest request){
		//logger.info("/c μ»¨νΈλ‘€λ¬ >>>>>>>>>>");
		String filePath = "//home//ec2-user//tomcatt//webapps//todakProject//upload//human";
		//String filePath=request.getSession().getServletContext().getRealPath("/upload/human");
	
		boolean flag=false;
		param=humanService.chaebunMemberAppr();
		//System.out.println("+++++++"+param.getHmp_empnum());
		String cnum=param.getHmp_empnum();
		cnum=ChaebunUtils.cNum(cnum, "H");
		
		try{
			FileUploadUtil fuu = new FileUploadUtil();
			boolean bFlag = false;
			bFlag = fuu.fileUpload(request, filePath);
			//logger.info("bFlag μ¬μ§ μΈμνΈ :  >>> : " + bFlag );
			
				Enumeration<String> en = fuu.getFileNames();
//				dbFileName="../"+"upload"+"/"+fileName;
				String hmp_picture =  en.nextElement();
				//logger.info("picture >>> : " + hmp_picture);
				String test=fuu.getParameter("hmp_picture");
				//System.out.println("test>>>>>>>>>>>"+test);
				
				
				String hmp_name=fuu.getParameter("hmp_name");
				String hmp_birth=fuu.getParameter("hmp_birth");
				String hmp_residentnum=fuu.getParameter("hmp_residentnum");
				String hmp_hpnum=fuu.getParameter("hmp_hpnum");
				String hmp_email=fuu.getParameter("hmp_email");
				String detailAddr=fuu.getParameter("cadress");
				String hmp_addr=fuu.getParameter("hmp_addr")+" "+detailAddr;
				String hmp_postcode=fuu.getParameter("hmp_postcode");
				String hmp_depositors=fuu.getParameter("hmp_depositors");
				String hmp_accountnum=fuu.getParameter("hmp_accountnum");
				String hmp_education=fuu.getParameter("hmp_education");
				String hmp_educontents=fuu.getParameter("hmp_educontents");
				String hmp_workexperience=fuu.getParameter("hmp_workexperience");
				String hmp_workcontents=fuu.getParameter("hmp_workcontents");
				String hmp_id=fuu.getParameter("hmp_id");
				String hmp_pw=fuu.getParameter("hmp_pw");

				BCryptPasswordEncoder bcpe = new BCryptPasswordEncoder();
				logger.info(" encoded pw >>> " + bcpe.encode(hmp_pw));
				String encodedPass = bcpe.encode(hmp_pw);
				
				
				param.setHmp_empnum(cnum);
				param.setHmp_name(hmp_name);
				param.setHmp_birth(hmp_birth);
				param.setHmp_residentnum(hmp_residentnum);
				param.setHmp_hpnum(hmp_hpnum);
				param.setHmp_email(hmp_email);
				param.setHmp_addr(hmp_addr);
				param.setHmp_postcode(hmp_postcode);
				param.setHmp_picture(hmp_picture);
				param.setHmp_bank("38");
				param.setHmp_depositors(hmp_depositors);
				param.setHmp_accountnum(hmp_accountnum);
				param.setHmp_education(hmp_education);
				param.setHmp_educontents(hmp_educontents);
				param.setHmp_workexperience(hmp_workexperience);
				param.setHmp_workcontents(hmp_workcontents);
				param.setHmp_id(hmp_id);
				param.setHmp_pw(encodedPass);
				
			
				//System.out.println("Hmp_empnum()"+param.getHmp_name());
				//System.out.println("Hmp_empnum()"+param.getHmp_birth());
				//System.out.println("Hmp_empnum()"+param.getHmp_residentnum());
				//System.out.println("Hmp_empnum()"+param.getHmp_hpnum());
				//System.out.println("Hmp_empnum()"+param.getHmp_email());
				//System.out.println("Hmp_empnum()"+param.getHmp_addr());
				//System.out.println("Hmp_empnum()"+param.getHmp_postcode());
				//System.out.println("Hmp_empnum()"+param.getHmp_picture());
				//System.out.println("Hmp_empnum()"+param.getHmp_bank());
				//System.out.println("Hmp_empnum()"+param.getHmp_depositors());
				//System.out.println("Hmp_empnum()"+param.getHmp_accountnum());
				//System.out.println("Hmp_empnum()"+param.getHmp_education());
				//System.out.println("Hmp_empnum()"+param.getHmp_educontents());
				//System.out.println("Hmp_empnum()"+param.getHmp_workexperience());
				//System.out.println("Hmp_empnum()"+param.getHmp_workcontents());
				
				
				
				
				flag=humanService.insertMemberAppr(param);
			}catch(Exception e){
			//System.out.println("μλ¬λ°μ!"+e);
		}
		return "/human/result";
	}
	
	@RequestMapping(value = "/moveSignup", method = RequestMethod.GET)
	public String movePage(){
		//logger.info("moveSignup νμ΄μ§ μ΄λ>>>>>>>>>>");
		
		return "/human/signup";
	}
	
	@RequestMapping(value="emailAuth")
	public String emailAuth(@RequestParam("selectFunc") String selectFunc,
							@ModelAttribute MemberVO hmvo,
							Model model){
		//logger.info("(log) emailAuth entered");
		
		//logger.info(" selectFunc >>> " + selectFunc);
		boolean emailConfirm = false;
		String url = "etc/popupOK";

		ApplicationContext ctx = new ClassPathXmlApplicationContext( new String[] {"config/applicationContext.xml"});
		MimeMailSender sender = (MimeMailSender) ctx.getBean("mimeMailSender");
		String toEmail = "";
		String fromEmail = "developerQuo@gmail.com";
		String titleEmail = "μλλ€.";
		String contentEmail = "μλνμΈμ. \n ";
		String message = "";
		String checker = "1";
		
		// νμκ°μ
		if(selectFunc.equals("0")){
			//logger.info("μ΄λ¦: "+hmvo.getHm_name());
			//logger.info("μ΄λ©μΌ μ£Όμ:"+hmvo.getHm_email());
//			  StringBuffer temp =new StringBuffer();
//              Random rnd = new Random();
//              for(int i=0;i<10;i++)
//              {
//                  int rIndex = rnd.nextInt(3);
//                  switch (rIndex) {
//                  case 0:
//                      // a-z
//                      temp.append((char) ((int) (rnd.nextInt(26)) + 97));
//                      break;
//                  case 1:
//                      // A-Z
//                      temp.append((char) ((int) (rnd.nextInt(26)) + 65));
//                      break;
//                  case 2:
//                      // 0-9
//                      temp.append((rnd.nextInt(10)));
//                      break;
//                  }
//              }
//              String AuthenticationKey = temp.toString();
//  			
//  			toEmail=hmvo.getHm_email();
//  			titleEmail ="ν λ₯ν λ₯ νμκ°μ μΈμ¦ λ©μΌ μλλ€.";
//  			contentEmail="μλνμΈμ. λ°κ°μ΅λλ€.<br> "+hmvo.getHm_name()+"μ¬μλμ λ©μΌ μΈμ¦ μ½λλ <br><p><font size='14' color='#ED6B5E'>'<strong>"+AuthenticationKey+"</strong>'</font></p>μλλ€.<br><br>κ°μ¬ν©λλ€. νλ³΅ν νλ£¨ λμΈμ!";
//  			
//  			String result=AuthenticationKey;
//  			model.addAttribute("result",result);
//  			
//  			emailConfirm = true;
//  			url= "/human/emailCheck";
		}
		// μμ΄λ
		if(selectFunc.equals("1")){
			//logger.info(" id μ§μ ");
			
			List<MemberVO> hList = etcService.idEmailAuth(hmvo);
			//logger.info(" κΈΈμ΄>>>>>>>>>>" + hList.size());
			if (hList.size() > 0){
				MemberVO hmvo2 = hList.get(0);
				
				toEmail = hmvo2.getHm_email();
				titleEmail = "μμ΄λ μ°ΎκΈ° κ²°κ³Ό" + titleEmail;
				contentEmail += "λΉμ μ μμ΄λλ '" + hmvo2.getHm_id() + "'μλλ€.";
				message = "μμ΄λκ° μ μ‘λμμ΅λλ€.";
				checker = "0";
				emailConfirm = true;
			}else{
				message = "μ¬μλ²νΈκ° μ‘΄μ¬νμ§ μμ΅λλ€.";
			}
		}
		
		// λΉλ²μ°ΎκΈ°
		if(selectFunc.equals("2")){
			
			List<MemberVO> hList = etcService.pwEmailAuth(hmvo);
			//logger.info(" κΈΈμ΄>>>>>>>>>>" + hList.size());
			if (hList.size() > 0){
				MemberVO hmvo2 = hList.get(0);
				
				//οΏ½οΏ½οΏ½οΏ½ οΏ½οΏ½Θ£ οΏ½οΏ½οΏ½
                StringBuffer temp =new StringBuffer();
                Random rnd = new Random();
                for(int i=0;i<10;i++)
                {
                    int rIndex = rnd.nextInt(3);
                    switch (rIndex) {
                    case 0:
                        // a-z
                        temp.append((char) ((int) (rnd.nextInt(26)) + 97));
                        break;
                    case 1:
                        // A-Z
                        temp.append((char) ((int) (rnd.nextInt(26)) + 65));
                        break;
                    case 2:
                        // 0-9
                        temp.append((rnd.nextInt(10)));
                        break;
                    }
                }
                String AuthenticationKey = temp.toString();
                hmvo2.setHm_pw(AuthenticationKey);
//                //logger.info(" μ λΉλ² >>> " + hmvo2.getHm_pw());
//                //logger.info(" μ¬λ²>>> " + hmvo2.getHm_empnum());
                //μλ‘μ΄ λΉλ°λ²νΈ μ μ₯
                boolean bool = etcService.setNewPw(hmvo2);

        		//logger.info(" bool >>> " + bool);
                if(bool){
    				toEmail = hmvo.getHm_email();
    				titleEmail = "λΉλ°λ²νΈ μ°ΎκΈ° κ²°κ³Ό" + titleEmail;
    				contentEmail += "λΉμ μ μλ‘μ΄ λΉλ°λ²νΈλ'" + AuthenticationKey + "'μλλ€.";
    				message = "μλ‘μ΄ λΉλ°λ²νΈκ° μ μ‘λμμ΅λλ€.";
    				checker = "0";
    				emailConfirm = true;
                }else{
    				message = "μ€λ₯κ° λ°μνμ΅λλ€. μ μν λ€μ μλνμΈμ";
                }
                
			}else{
				message = "μ¬μλ²νΈ λλ λΉλ°λ²νΈκ° νλ Έμ΅λλ€.";
			}
			
		}

		//logger.info(" emailConfirm >>> " + emailConfirm);
		// λ©μΌ λ³΄λ΄κΈ°
		if (emailConfirm){
			try {
				sender.sendMail(toEmail, fromEmail, titleEmail, contentEmail);
			} catch (MessagingException e) {
			 // TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		model.addAttribute("message", message);
		model.addAttribute("checker", checker);
		
		//logger.info("(log) emailAuth left");
		return url;
	}
}