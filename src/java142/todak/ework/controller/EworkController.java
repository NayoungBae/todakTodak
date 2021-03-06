package java142.todak.ework.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java142.todak.common.ChaebunUtils;
import java142.todak.common.FileUploadUtil;
import java142.todak.common.VOPrintUtil;
import java142.todak.ework.service.EworkFormService;
import java142.todak.ework.service.EworkService;
import java142.todak.ework.vo.ApprovalVO;
import java142.todak.ework.vo.AuthPersonVO;
import java142.todak.ework.vo.AuthVO;
import java142.todak.ework.vo.AuthorizationVO;
import java142.todak.ework.vo.EanumVO;
import java142.todak.ework.vo.HolidayVO;
import java142.todak.ework.vo.LineHistoryVO;
import java142.todak.ework.vo.LineVO;
import java142.todak.human.vo.MemberVO;
import java142.todak.ework.vo.ProposalVO;
import java142.todak.ework.vo.SelectAuthBoxVO;
import java142.todak.ework.vo.SignStampVO;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ework")
public class EworkController {

	Logger logger = Logger.getLogger(EworkController.class);
	
	public final static String PROPOSAL_GUNBUN = "P";
	public final static String APPROVAL_GUNBUN = "D";
	public final static String HOLIDAY_GUNBUN = "H";
	public final static String LINE_GUNBUN = "L";
	public final static String AUTH_GUNBUN = "A";
	public final static String AUTHPERSON_GUNBUN = "S";
	public final static String AUTHBOX_GUNBUN = "B";
	public final static String LINEHISTORY_GUNBUN = "I";
	public final static String SIGNSTAMP_GUNBUN = "S";
	
	public final static String UPLOAD_ABSTRACT_PATH = "//home//ec2-user//tomcatt//webapps//todakProject//upload//ework//sign";
	public final static String UPLOAD_RELATIVE_PATH = "upload//ework//sign";
	
	
	@Autowired
	private EworkService eworkService;
	@Autowired
	private EworkFormService eworkFormService;
	
	//????????????
	@Autowired
	private PlatformTransactionManager ptm;
	DefaultTransactionDefinition dtd = null;
	TransactionStatus ts = null;
	
	@RequestMapping(value="/selectAuthBox", method={RequestMethod.GET, RequestMethod.POST})
	public String moveSelectAuthBox(@ModelAttribute SelectAuthBoxVO sabvo, Model model) {
		//logger.info("(EworkController)public String moveSelectAuthBox() ?????? >>> ");
		//logger.info("  ???????????? sabvo : " + sabvo);
		VOPrintUtil.printVO(sabvo);
		
		List<SelectAuthBoxVO> list = null;
		list = eworkService.selectAuthBox(sabvo);
		//logger.info("  list : " + list);
		
		model.addAttribute("list", list);
		//logger.info("  model : " + model);
		
		//logger.info("(EworkController)public String moveSelectAuthBox() ??? >>> ");
		return "ework/selectAuth";
	}
	
	@RequestMapping("/authDetail")
	public String authDetail(@ModelAttribute SelectAuthBoxVO sabvo, Model model) {
		//logger.info("(EworkController)public String authDetail(@ModelAttribute SelectAuthBoxVO sabvo) ?????? >>> ");
		//logger.info("  ???????????? sabvo : " + sabvo);
		VOPrintUtil.printVO(sabvo);
		
		String url = "";
		
		String eab_group = sabvo.getEab_group();
		//logger.info("  eab_group : " + eab_group);
		
		if(eab_group.equals("23")) { //?????????
			//logger.info("  if(eab_group.equals('23')) ?????? >>> ");
			
			List<ProposalVO> list_proposal = null;
			list_proposal = eworkService.searchProposal(sabvo);
			//logger.info("  list_proposal : " + list_proposal);
			
			List<AuthPersonVO> list_person = null;
			list_person = eworkService.searchAuthPerson(sabvo); //???????????? ??????
			//logger.info("  list_person : " + list_person);
			
			
			if(list_proposal!= null && list_proposal.size() > 0 &&
										list_person!=null && list_person.size() > 0) {
				//logger.info("  if(list_proposal!= null && list_proposal.size() > 0 && "
//											+ "list_person!=null && list_person.size() > 0) ?????? >>> ");
				//???????????????
				List<MemberVO> list_names = null;
				list_names = new ArrayList<MemberVO>();
				
				//?????????
				List<MemberVO> list_subname = null;
				list_subname = new ArrayList<MemberVO>();
				
				//????????? ?????? ?????????
				for(int i=0; i<list_person.size(); i++) {
					//logger.info("  ============ for??? " + (i+1) + " ============  ");
					
					AuthPersonVO apvo = list_person.get(i);
					//logger.info("  " + apvo.getEai_num());
					//logger.info("  " + apvo.getEa_num());
					//logger.info("  " + apvo.getEai_recentnum());
					//logger.info("  " + apvo.getEai_position());
					//logger.info("  " + apvo.getEai_auth());
					//logger.info("  " + apvo.getEai_filedir());
					//logger.info("  " + apvo.getEai_substituteYN());
					//logger.info("  " + apvo.getEai_substitutenum());
					//logger.info("  " + apvo.getEai_sequence());
					
					//???????????????
					MemberVO _mvo = null;
					_mvo = new MemberVO();
					_mvo.setHm_empnum(apvo.getEai_recentnum());
					
					List<MemberVO> list = eworkFormService.selectPerson(_mvo);
					//logger.info("  list : " + list);
					
					if(list!=null) {
						//logger.info("  if(list!=null) ?????? >>> ");
						
						MemberVO mvo = list.get(0);
						//logger.info("  mvo : " + mvo); 
						//logger.info("  mvo.getHm_empnum() : " + mvo.getHm_empnum()); 
						//logger.info("  mvo.getHm_name() : " + mvo.getHm_name()); 
						
						list_names.add(mvo);
						//logger.info("  list_names : " + list_names);
						
					}
					
					//?????????
					MemberVO _submvo = null;
					_submvo = new MemberVO();
					_submvo.setHm_empnum(apvo.getEai_substitutenum());
					
					if(_submvo.getHm_empnum()!=null) {
						//logger.info("  if(_submvo.getHm_empnum()!=null) ?????? >>> ");
						
						List<MemberVO> list_sub = eworkFormService.selectPerson(_submvo);
						//logger.info("  list : " + list);
						
						if(list_sub!=null) {
							//logger.info("  if(list_sub!=null) ?????? >>> ");
							
							MemberVO mvo = list.get(0);
							//logger.info("  mvo : " + mvo); 
							//logger.info("  mvo.getHm_empnum() : " + mvo.getHm_empnum()); 
							//logger.info("  mvo.getHm_name() : " + mvo.getHm_name()); 
							
							list_subname.add(mvo);
							//logger.info("  list_subname : " + list_subname);
						}
					}
					
					
				} //end of for???
				
				
				List<String> list_presentnum = null;
				list_presentnum = new ArrayList<String>();
				list_presentnum.add(sabvo.getEa_presentnum());
				model.addAttribute("list_presentnum", list_presentnum);
				
				model.addAttribute("list_proposal", list_proposal);
				model.addAttribute("list_person", list_person);
				model.addAttribute("list_names", list_names);
				model.addAttribute("list_subname", list_subname);
				
				url = "ework/searchProposal";
				
			} else {
				//logger.info("  if(list_proposal!= null && list_proposal.size() > 0 && "
//										+ "list_person!=null && list_person.size() > 0)-else ?????? >>> ");
			}
			
		} else if(eab_group.equals("22")) { //?????????
			//logger.info("  else if(eab_group.equals('22')) ?????? >>> ");
			
			List<ApprovalVO> list_approval = null;
			list_approval = eworkService.searchApproval(sabvo);
			//logger.info("  list_approval : " + list_approval);
			
			List<AuthPersonVO> list_person = null;
			list_person = eworkService.searchAuthPerson(sabvo); //???????????? ??????
			//logger.info("  list_person : " + list_person);
			
			
			if(list_approval!= null && list_approval.size() > 0 &&
										list_person!=null && list_person.size() > 0) {
				//logger.info("  if(list_approval!= null && list_approval.size() > 0 && "
//											+ "list_person!=null && list_person.size() > 0) ?????? >>> ");
				//???????????????
				List<MemberVO> list_names = null;
				list_names = new ArrayList<MemberVO>();
				
				//????????? ?????? ?????????
				for(int i=0; i<list_person.size(); i++) {
					//logger.info("  ============ for??? " + (i+1) + " ============  ");
					
					AuthPersonVO apvo = list_person.get(i);
					
					//???????????????
					MemberVO _mvo = null;
					_mvo = new MemberVO();
					_mvo.setHm_empnum(apvo.getEai_recentnum());
					
					List<MemberVO> list = eworkFormService.selectPerson(_mvo);
					//logger.info("  list : " + list);
					
					if(list!=null) {
						//logger.info("  if(list!=null) ?????? >>> ");
						
						MemberVO mvo = list.get(0);
						//logger.info("  mvo : " + mvo); 
						//logger.info("  mvo.getHm_empnum() : " + mvo.getHm_empnum()); 
						//logger.info("  mvo.getHm_name() : " + mvo.getHm_name()); 
						
						list_names.add(mvo);
						//logger.info("  list_names : " + list_names);
						
					}	
					
				} //end of for???
				
				List<String> list_presentnum = null;
				list_presentnum = new ArrayList<String>();
				list_presentnum.add(sabvo.getEa_presentnum());
				model.addAttribute("list_presentnum", list_presentnum);
				
				model.addAttribute("list_approval", list_approval);
				model.addAttribute("list_person", list_person);
				model.addAttribute("list_names", list_names);
//				model.addAttribute("list_subname", list_subname);
				url = "ework/searchApproval";
				
			} else {
				//logger.info("  if(list_approval!= null && list_approval.size() > 0 && "
//										+ "list_person!=null && list_person.size() > 0)-else ?????? >>> ");
			}
			
			
			
		} else if(eab_group.equals("21")) { //???????????????
			//logger.info("  else if(eab_group.equals('21')) ?????? >>> ");
			
			List<HolidayVO> list_holiday = null;
			list_holiday = eworkService.searchHoliday(sabvo);
			//logger.info("  list_holiday : " + list_holiday);
			
			List<AuthPersonVO> list_person = null;
			list_person = eworkService.searchAuthPerson(sabvo); //???????????? ??????
			//logger.info("  list_person : " + list_person);
			
			if(list_holiday!= null && list_holiday.size() > 0 &&
											list_person!=null && list_person.size() > 0) {
				//logger.info("  if(list_holiday!= null && list_holiday.size() > 0 && "
//												+ "list_person!=null && list_person.size() > 0) ?????? >>> ");
				//???????????????
				List<MemberVO> list_names = null;
				list_names = new ArrayList<MemberVO>();
				
				//?????????
				List<MemberVO> list_subname = null;
				list_subname = new ArrayList<MemberVO>();
				
				//????????? ?????? ?????????
				for(int i=0; i<list_person.size(); i++) {
					//logger.info("  ============ for??? " + (i+1) + " ============  ");
					
					AuthPersonVO apvo = list_person.get(i);
					
					//???????????????
					MemberVO _mvo = null;
					_mvo = new MemberVO();
					_mvo.setHm_empnum(apvo.getEai_recentnum());
					
					List<MemberVO> list = eworkFormService.selectPerson(_mvo);
					//logger.info("  list : " + list);
					
					if(list!=null) {
						//logger.info("  if(list!=null) ?????? >>> ");
						
						MemberVO mvo = list.get(0);
						//logger.info("  mvo : " + mvo); 
						//logger.info("  mvo.getHm_empnum() : " + mvo.getHm_empnum()); 
						//logger.info("  mvo.getHm_name() : " + mvo.getHm_name()); 
						
						list_names.add(mvo);
						//logger.info("  list_names : " + list_names);
						
					}
					
					//?????????
					MemberVO _submvo = null;
					_submvo = new MemberVO();
					_submvo.setHm_empnum(apvo.getEai_substitutenum());
					
					if(_submvo.getHm_empnum()!=null) {
						//logger.info("  if(_submvo.getHm_empnum()!=null) ?????? >>> ");
						
						List<MemberVO> list_sub = eworkFormService.selectPerson(_submvo);
						//logger.info("  list : " + list);
						
						if(list_sub!=null) {
							//logger.info("  if(list_sub!=null) ?????? >>> ");
							
							MemberVO mvo = list.get(0);
							//logger.info("  mvo : " + mvo); 
							//logger.info("  mvo.getHm_empnum() : " + mvo.getHm_empnum()); 
							//logger.info("  mvo.getHm_name() : " + mvo.getHm_name()); 
							
							list_subname.add(mvo);
							//logger.info("  list_subname : " + list_subname);
						}
					}
					
					
				} //end of for???
				
				List<String> list_presentnum = null;
				list_presentnum = new ArrayList<String>();
				list_presentnum.add(sabvo.getEa_presentnum());
				model.addAttribute("list_presentnum", list_presentnum);
				
				model.addAttribute("list_holiday", list_holiday);
				model.addAttribute("list_person", list_person);
				model.addAttribute("list_names", list_names);
				model.addAttribute("list_subname", list_subname);
				url = "ework/searchHoliday";
				
				} else {
					//logger.info("  if(list_holiday!= null && list_holiday.size() > 0 && "
//														+ "list_person!=null && list_person.size() > 0)-else ?????? >>> ");
				}
			
		}
		
		//logger.info("(EworkController)public String authDetail(@ModelAttribute SelectAuthBoxVO sabvo) ??? >>> ");
		return url;
	} //end of authDetail method
	
	@RequestMapping("/approval")
	public String approval(@ModelAttribute AuthorizationVO authvo, Model model) { //??????
		//logger.info("(EworkController)public String approval(@ModelAttribute ProposalVO pvo, Model model) ?????? >>> ");
		//logger.info("  ???????????? authvo : " + authvo);
		//logger.info("  getEa_num() : " + authvo.getEa_num());
		//logger.info("  getHm_empnum() : " + authvo.getHm_empnum());
		
		String eai_num = "";
		boolean bFlag = false;
		
		//???????????? ??????
		dtd = new DefaultTransactionDefinition();
		dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = ptm.getTransaction(dtd);
		
		try {
			
			// 1) ?????? -------------
			List<AuthPersonVO> list_Eai_num = eworkFormService.chaebunAuthPerson();
			//logger.info("  list_Eai_num : " + list_Eai_num);
			AuthPersonVO apvo_Eai_num = list_Eai_num.get(0);
			//logger.info("  apvo_Eai_num : " + apvo_Eai_num);
			eai_num = apvo_Eai_num.getEai_num();
			//logger.info("  eai_num : " + eai_num);
			eai_num = ChaebunUtils.cNum(eai_num, AUTHPERSON_GUNBUN);
			//logger.info("  eai_num : " + eai_num);
			
			//??????????????? ????????? ????????????
			SignStampVO _ssvo = null;
			_ssvo = new SignStampVO();
			//logger.info("  _ssvo : " + _ssvo);
			_ssvo.setHm_empnum(authvo.getHm_empnum());
			
			List<SignStampVO> list_signstamp = eworkFormService.selectSignStamp(_ssvo);
			//logger.info("  list_signstamp : " + list_signstamp);
			SignStampVO ssvo = null;
			ssvo = list_signstamp.get(0);
			//logger.info("  ssvo : " + ssvo);
			String es_filedir = ssvo.getEs_filedir();
			
			//?????????????????? & ????????? ??? ??????
			EanumVO _nvo = null;
			_nvo = new EanumVO();
			_nvo.setEa_num(authvo.getEa_num());
			
			//????????? ????????????
			List<AuthPersonVO> list_sequence = null;
			list_sequence = eworkService.plusSequence(_nvo);
			//logger.info("  list_sequence : " + list_sequence);
			AuthPersonVO apvo_sequence = null;
			apvo_sequence = list_sequence.get(0);
			//logger.info("  apvo_sequence : " + apvo_sequence);
			String eai_sequence = "";
			eai_sequence = apvo_sequence.getEai_sequence();
			
			//?????? ?????? ?????? ?????? ????????????
			MemberVO _mvo = null;
			_mvo = new MemberVO();
			//logger.info("  _mvo : " + _mvo);
			_mvo.setHm_empnum(authvo.getHm_empnum());
			List<MemberVO> list_person = null;
			list_person = eworkFormService.selectPerson(_mvo);
			MemberVO mvo_person = list_person.get(0);
			//logger.info("  mvo_person : " + mvo_person);
			//logger.info("  getHm_empnum() : " + mvo_person.getHm_empnum());
			//logger.info("  getHm_position() : " + mvo_person.getHm_position());
			
			AuthPersonVO _apvo = null;
			_apvo = new AuthPersonVO();
			_apvo.setEai_num(eai_num);
			_apvo.setEa_num(authvo.getEa_num());
			_apvo.setEai_recentnum(authvo.getHm_empnum()); //?????? ????????? ?????? ???????????? ???????????????!!!!!!!!!!!!!!!!!
			_apvo.setEai_position(mvo_person.getHm_position());
			_apvo.setEai_auth("61"); //????????????
			_apvo.setEai_filedir(es_filedir);
			_apvo.setEai_substituteYN("N"); //????????? ??????
			_apvo.setEai_substitutenum(null); //????????? ??????
			_apvo.setEai_sequence(eai_sequence);
			
			// 1)
			bFlag = eworkFormService.insertAuthPerson(_apvo);
			//logger.info("  (insertAuthPerson)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
			
			
			// 2) ?????? -------------
			//?????? ????????????
			List<LineVO> list_line = eworkService.searchLine(_nvo);
			//logger.info("  list_line : " + list_line);
			LineVO lvo = list_line.get(0);
			//logger.info("  lvo : " + lvo);
			String el_line = lvo.getEl_line();
			//logger.info("  el_line : " + el_line);
			String[] arr_line = el_line.split("-");
			
			String split1 = "";
			String split2 = "";
			String split3 = ""; 
			String split4 = "";
			String split5 = ""; 
			
			/* ?????????????????? NULL??? ???????????? '' ??? ?????? NULL ??? ?????? ?????????. ????????? id ??? 1, 2 ??? ?????? name??? ?????? NULL ??? ???????????????. */
			
			if(arr_line.length == 2) { //???????????????
				split1 = arr_line[0];
				//logger.info("  split1 : " + split1);
				split2 = arr_line[1];
				//logger.info("  split2 : " + split2);
			}
			
			if(arr_line.length == 4) { //??????
				split1 = arr_line[0];
				//logger.info("  split1 : " + split1);
				split2 = arr_line[1];
				//logger.info("  split2 : " + split2);
				split3 = arr_line[2];
				//logger.info("  split3 : " + split3);
				split4 = arr_line[3];
				//logger.info("  split4 : " + split4);
			}
			
			if(arr_line.length == 5) { //???????????? ????????? ?????????????????? ??????
				split1 = arr_line[0];
				//logger.info("  split1 : " + split1);
				split2 = arr_line[1];
				//logger.info("  split2 : " + split2);
				split3 = arr_line[2];
				//logger.info("  split3 : " + split3);
				split4 = arr_line[3];
				//logger.info("  split4 : " + split4);
				split5 = arr_line[4];
				//logger.info("  split5 : " + split5);
			}
			
			String recent = authvo.getHm_empnum(); //?????? ?????????
			//logger.info("  recent : " + recent);
			
			// 2)??? ?????? VO ?????? ?????? ----------
			AuthVO _auvo = null;
			_auvo = new AuthVO();
			
			String next = ""; //??????????????? ????????????
			
			//???????????? ???????????????
			if(recent.equals(split5)) {
				//logger.info("  if(recent.equals(split5)) ?????? >>> ");
				
				next = "";
				//logger.info("  next : " + next);
				
			} else if(recent.equals(split4)) {
				//logger.info("  if(recent.equals(split4)) ?????? >>> ");
				
				next = split5;
				//logger.info("  next : " + next);
				
			} else if(recent.equals(split3)) {
				//logger.info("  if(recent.equals(split3)) ?????? >>> ");
				
				next = split4;
				//logger.info("  next : " + next);
				
			} else if(recent.equals(split2)) {
				//logger.info("  if(recent.equals(split2)) ?????? >>> ");
				
				next = split3;
				//logger.info("  next : " + next);
				
			}else if(recent.equals(split1)) {
				//logger.info("  if(recent.equals(split1)) ?????? >>> ");
				
				next = split2;
				//logger.info("  next : " + next);
				
			}
			
			_auvo.setEa_presentnum(next);
			
			if(!next.equals("")) {
				//logger.info("  if(!next.equals('')) ?????? >>>  ");
			
				//???????????? ?????? ????????????
				_mvo = null; //?????? ???????????? ?????????
				_mvo = new MemberVO();
				//logger.info("  _mvo : " + _mvo);
				_mvo.setHm_empnum(next);
				list_person = null; //?????? ???????????? ?????????
				list_person = eworkFormService.selectPerson(_mvo);
				mvo_person = null; //?????? ???????????? ?????????
				mvo_person = list_person.get(0);
				//logger.info("  mvo_person : " + mvo_person);
				
				_auvo.setEa_position(mvo_person.getHm_position()); //???????????? //?????? ?????? ?????? ????????????. ????????? ?????? ??????????????? ?????? ???????????? ???????????????
				_auvo.setEa_num(authvo.getEa_num());
				
			} else if(next.equals("")) {
				//logger.info("  else if(next.equals('') ?????? >>>  ");
				
				_auvo.setEa_position(""); //???????????? //?????? ?????? ?????? ????????????. ????????? ?????? ??????????????? ?????? ???????????? ???????????????
				_auvo.setEa_num(authvo.getEa_num());
				
			}
			// 2)??? ?????? VO ?????? ???..! ----------
			
			// 2)
			bFlag = eworkService.updateAuth(_auvo);
			//logger.info("  (updateAuth)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
		
			ptm.commit(ts);
			
			
			if(bFlag) {
				//logger.info("  if(bFlag) ?????? >>> ");
				
				return "redirect:/ework/selectAuthBox.td";
				
			}
			
			
		} catch(Exception e) {
			//logger.info("?????? : " + e);
			ptm.rollback(ts);
		}
		
		//logger.info("(EworkController)public String approval(@ModelAttribute ProposalVO pvo, Model model) ??? >>> ");
		return "#";
	} //end of approval method
	
	@RequestMapping("/arbitrary") //??????
	public String arbitrary(@ModelAttribute AuthorizationVO authvo, Model model) {
		//logger.info("(EworkController)public String arbitrary(@ModelAttribute AuthorizationVO authvo, Model model) ?????? >>> ");
		//logger.info("  ???????????? authvo : " + authvo);
		//logger.info("  getEa_num() : " + authvo.getEa_num());
		//logger.info("  getHm_empnum() : " + authvo.getHm_empnum());
		
		String eai_num = "";
		boolean bFlag = false;
		
		//???????????? ??????
		dtd = new DefaultTransactionDefinition();
		dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = ptm.getTransaction(dtd);
		
		try {
			
			// 1) ?????? -------------
			List<AuthPersonVO> list_Eai_num = eworkFormService.chaebunAuthPerson();
			//logger.info("  list_Eai_num : " + list_Eai_num);
			AuthPersonVO apvo_Eai_num = list_Eai_num.get(0);
			//logger.info("  apvo_Eai_num : " + apvo_Eai_num);
			eai_num = apvo_Eai_num.getEai_num();
			//logger.info("  eai_num : " + eai_num);
			eai_num = ChaebunUtils.cNum(eai_num, AUTHPERSON_GUNBUN);
			//logger.info("  eai_num : " + eai_num);
			
			//??????????????? ????????? ????????????
			SignStampVO _ssvo = null;
			_ssvo = new SignStampVO();
			//logger.info("  _ssvo : " + _ssvo);
			_ssvo.setHm_empnum(authvo.getHm_empnum());
			
			List<SignStampVO> list_signstamp = eworkFormService.selectSignStamp(_ssvo);
			//logger.info("  list_signstamp : " + list_signstamp);
			SignStampVO ssvo = null;
			ssvo = list_signstamp.get(0);
			//logger.info("  ssvo : " + ssvo);
			String es_filedir = ssvo.getEs_filedir();
			
			//?????????????????? & ????????? ??? ??????
			EanumVO _nvo = null;
			_nvo = new EanumVO();
			_nvo.setEa_num(authvo.getEa_num());
			
			//????????? ????????????
			List<AuthPersonVO> list_sequence = null;
			list_sequence = eworkService.plusSequence(_nvo);
			//logger.info("  list_sequence : " + list_sequence);
			AuthPersonVO apvo_sequence = null;
			apvo_sequence = list_sequence.get(0);
			//logger.info("  apvo_sequence : " + apvo_sequence);
			String eai_sequence = "";
			eai_sequence = apvo_sequence.getEai_sequence();
			
			//?????? ?????? ?????? ?????? ????????????
			MemberVO _mvo = null;
			_mvo = new MemberVO();
			//logger.info("  _mvo : " + _mvo);
			_mvo.setHm_empnum(authvo.getHm_empnum());
			List<MemberVO> list_person = null;
			list_person = eworkFormService.selectPerson(_mvo);
			MemberVO mvo_person = list_person.get(0);
			//logger.info("  mvo_person : " + mvo_person);
			//logger.info("  getHm_empnum() : " + mvo_person.getHm_empnum());
			//logger.info("  getHm_position() : " + mvo_person.getHm_position());
			
			AuthPersonVO _apvo = null;
			_apvo = new AuthPersonVO();
			_apvo.setEai_num(eai_num);
			_apvo.setEa_num(authvo.getEa_num());
			_apvo.setEai_recentnum(authvo.getHm_empnum());
			_apvo.setEai_position(mvo_person.getHm_position());
			_apvo.setEai_auth("64"); //????????????
			_apvo.setEai_filedir(es_filedir);
			_apvo.setEai_substituteYN("N"); //????????? ??????
			_apvo.setEai_substitutenum(null); //????????? ??????
			_apvo.setEai_sequence(eai_sequence);
			
			// 1)
			bFlag = eworkFormService.insertAuthPerson(_apvo);
			//logger.info("  (insertAuthPerson)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
			
			
			// 2) ?????? -------------
			
			// 2)??? ?????? VO ?????? ?????? ----------
			AuthVO _auvo = null;
			_auvo = new AuthVO();
			
			_auvo.setEa_presentnum("");
			_auvo.setEa_position("");
			_auvo.setEa_num(authvo.getEa_num());
			// 2)??? ?????? VO ?????? ???..! ----------
			
			// 2)
			bFlag = eworkService.updateAuth(_auvo);
			//logger.info("  (updateAuth)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
		
			ptm.commit(ts);
			
			
			if(bFlag) {
				//logger.info("  if(bFlag) ?????? >>> ");
				
				return "redirect:/ework/selectAuthBox.td";
				
			}
			
			
		} catch(Exception e) {
			//logger.info("?????? : " + e);
			ptm.rollback(ts);
		}
		
		//logger.info("(EworkController)public String returnMethod(@ModelAttribute AuthorizationVO authvo, Model model) ??? >>> ");
		return "#";
	} //end of 'returnMethod' method
	
	@RequestMapping("/substitute")
	public String substitute(@ModelAttribute AuthorizationVO authvo, Model model) {
		//logger.info("(EworkController)public String substitute(@ModelAttribute AuthorizationVO authvo, Model model) ?????? >>> ");
		//logger.info("  ???????????? authvo : " + authvo);
		//logger.info("  getEa_num() : " + authvo.getEa_num());
		//logger.info("  getHm_empnum() : " + authvo.getHm_empnum());
		
		String eai_num = "";
		boolean bFlag = false;
		
		//???????????? ??????
		dtd = new DefaultTransactionDefinition();
		dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = ptm.getTransaction(dtd);
		
		try {
			// 1) ?????? -------------
			List<LineHistoryVO> list_elh_num = null;
			list_elh_num = eworkService.chaebunLineHistory();
			//logger.info("  list_elh_num : " + list_elh_num);
			LineHistoryVO lhvo_elh_num = list_elh_num.get(0);
			//logger.info("  lhvo_elh_num : " + lhvo_elh_num);
			String elh_num = lhvo_elh_num.getElh_num();
			//logger.info("  elh_num : " + elh_num);
			elh_num = ChaebunUtils.cNum(elh_num, LINEHISTORY_GUNBUN);
			//logger.info("  elh_num : " + elh_num);
			
			//???????????? ????????? ????????? ?????? VO ??????
			EanumVO _nvo = null;
			_nvo = new EanumVO();
			_nvo.setEa_num(authvo.getEa_num());
			
			//?????? ????????????
			List<LineVO> list_line = eworkService.searchLine(_nvo);
			//logger.info("  list_line : " + list_line);
			LineVO lvo = list_line.get(0);
			//logger.info("  lvo : " + lvo);
			
			String el_line = lvo.getEl_line();
			//logger.info("  el_line : " + el_line);
			
			//???????????????????????? ???????????? INSERT??? ?????? VO??????
			LineHistoryVO _lhvo = null;
			_lhvo = new LineHistoryVO();
			_lhvo.setElh_num(elh_num);
			_lhvo.setEa_num(lvo.getEa_num());
			_lhvo.setElh_line(lvo.getEl_line());
			
			bFlag = eworkService.insertLineHistory(_lhvo); // 1) ??????
			//logger.info("  (eworkService)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
			
			// 2) ???????????? ???????????? ?????????
			//???????????? ??????????????? ?????? ????????????
			MemberVO _mvo = null;
			_mvo = new MemberVO();
			_mvo.setHm_empnum(authvo.getHm_empnum());
			
			List<MemberVO> list_person = eworkFormService.selectPerson(_mvo);
			//logger.info("  list_person : " + list_person);
			MemberVO mvo_person = list_person.get(0);
			//logger.info("  mvo_person : " + mvo_person);
			
			String person_deptnum = mvo_person.getHm_deptnum();
			//logger.info("  person_deptnum : " + person_deptnum);
			String person_dept = person_deptnum.substring(0, 2); //?????????
			//logger.info("  person_dept : " + person_dept);
			String person_position = mvo_person.getHm_position(); //?????? ?????????
			//logger.info("  person_position : " + person_position);
			int position_int = Integer.parseInt(person_position); //?????? ?????? : 14
			//logger.info("  position_int : " + position_int);
			position_int = position_int + 1; //?????? ?????? : 15 (14 + 1)
			//logger.info("  position_int : " + position_int);
			person_position = String.valueOf(position_int);
			//logger.info("  person_position : " + person_position);
			
			//?????????
			_mvo = null;
			_mvo = new MemberVO();
			_mvo.setHm_deptnum(person_dept);//????????????????????? ??????(01)
			_mvo.setHm_position(person_position); //?????? ??????(15)
			//logger.info("  _mvo : " + _mvo);
			//logger.info("  person_dept : " + person_dept);
			//logger.info("  person_position : " + person_position);
			
			//?????? ?????? ????????????
			List<MemberVO> list_dm = eworkService.selectLowerPositionPerson(_mvo); //_mvo ??????. dm:department_manager(??????)
			//logger.info("  list_dm : " + list_dm);
			MemberVO mvo_dm = list_dm.get(0);
			//logger.info("  mvo_dm : " + mvo_dm);
			String empnum_dm = mvo_dm.getHm_empnum(); //????????? ????????????
			//logger.info("  empnum_dm : " + empnum_dm);
			
			//?????? ????????????
			el_line = lvo.getEl_line();
			//logger.info("  el_line : " + el_line);
			
			//????????? ????????? ???????????? ??????????????? set ?????????
			String replace = authvo.getHm_empnum() + "-" + empnum_dm;
			//logger.info("  replace : " + replace);
			
			el_line = el_line.replace(authvo.getHm_empnum(), replace);
			//logger.info("  el_line : " + el_line);
			
			//???????????? ???????????????
			LineVO _lvo = null;
			_lvo = new LineVO();
			_lvo.setEl_line(el_line);
			_lvo.setEa_num(authvo.getEa_num());
			//logger.info("  _lvo : " + _lvo);
			
			bFlag = eworkService.updateLine(_lvo); // 2) ??????
			//logger.info("  (updateLine)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
			
			// 3) ?????? -------------
			//??????????????? ????????? ??????
			List<AuthPersonVO> list_Eai_num = eworkFormService.chaebunAuthPerson();
			//logger.info("  list_Eai_num : " + list_Eai_num);
			AuthPersonVO apvo_Eai_num = list_Eai_num.get(0);
			//logger.info("  apvo_Eai_num : " + apvo_Eai_num);
			eai_num = apvo_Eai_num.getEai_num();
			//logger.info("  eai_num : " + eai_num);
			eai_num = ChaebunUtils.cNum(eai_num, AUTHPERSON_GUNBUN);
			//logger.info("  eai_num : " + eai_num);
			
			//??????????????? ????????? ????????????
//			SignStampVO _ssvo = null;
//			_ssvo = new SignStampVO();
//			//logger.info("  _ssvo : " + _ssvo);
//			_ssvo.setHm_empnum(authvo.getHm_empnum());
//			
//			List<SignStampVO> list_signstamp = eworkFormService.selectSignStamp(_ssvo);
//			//logger.info("  list_signstamp : " + list_signstamp);
//			SignStampVO ssvo = null;
//			ssvo = list_signstamp.get(0);
//			//logger.info("  ssvo : " + ssvo);
//			String es_filedir = ssvo.getEs_filedir();
			
			//?????????????????? & ????????? ??? ??????
			//?????????
			_nvo = null;
			_nvo = new EanumVO();
			_nvo.setEa_num(authvo.getEa_num());
			
			//????????? ????????????
//			List<AuthPersonVO> list_sequence = null;
//			list_sequence = eworkService.plusSequence(_nvo);
//			//logger.info("  list_sequence : " + list_sequence);
//			AuthPersonVO apvo_sequence = null;
//			apvo_sequence = list_sequence.get(0);
//			//logger.info("  apvo_sequence : " + apvo_sequence);
//			String eai_sequence = "";
//			eai_sequence = apvo_sequence.getEai_sequence();
//			//logger.info("  eai_sequence : " + eai_sequence);
			
			
			//?????? ?????? ?????? ?????? ????????????
			//?????????
			_mvo = null;
			_mvo = new MemberVO();
			//logger.info("  _mvo : " + _mvo);
			_mvo.setHm_empnum(authvo.getHm_empnum());
			list_person = null; //?????????
			list_person = eworkFormService.selectPerson(_mvo);
			mvo_person = list_person.get(0); //?????????
			//logger.info("  mvo_person : " + mvo_person);
			//logger.info("  getHm_empnum() : " + mvo_person.getHm_empnum());
			//logger.info("  getHm_position() : " + mvo_person.getHm_position());
			
			AuthPersonVO _apvo = null;
			_apvo = new AuthPersonVO();
			_apvo.setEai_num(eai_num);
			_apvo.setEa_num(authvo.getEa_num());
			_apvo.setEai_recentnum(authvo.getHm_empnum());
			_apvo.setEai_position(mvo_person.getHm_position());
			_apvo.setEai_auth("63"); //?????? ??????
			_apvo.setEai_filedir(""); //null???
			_apvo.setEai_substituteYN("Y"); //????????? ??????
			_apvo.setEai_substitutenum(empnum_dm); //?????? ????????????
			_apvo.setEai_sequence(""); //???????????? null??? ??????
			
			// 3)
			bFlag = eworkFormService.insertAuthPerson(_apvo); // 3) ??????
			//logger.info("  (insertAuthPerson)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
			
			
			// 4) ?????? -------------
			//?????? ????????????
			list_line = null; //?????????
			list_line = eworkService.searchLine(_nvo); //_nvo??? ?????? ??????
			//logger.info("  list_line : " + list_line);
			lvo = null; //?????????
			lvo = list_line.get(0);
			//logger.info("  lvo : " + lvo);
			
			el_line = ""; //?????????
			el_line = lvo.getEl_line(); //UPDATE??? ??? ????????? ???????????????
			//logger.info("  el_line : " + el_line);
			String[] arr_line = el_line.split("-");
			
			String split1 = "";
			String split2 = "";
			String split3 = ""; 
			String split4 = "";
			String split5 = ""; 
			
			/* ?????????????????? NULL??? ???????????? '' ??? ?????? NULL ??? ?????? ?????????. ????????? id ??? 1, 2 ??? ?????? name??? ?????? NULL ??? ???????????????. */
			
			if(arr_line.length == 2) { //???????????????
				split1 = arr_line[0];
				//logger.info("  split1 : " + split1);
				split2 = arr_line[1];
				//logger.info("  split2 : " + split2);
			}
			
			if(arr_line.length == 4) { //??????
				split1 = arr_line[0];
				//logger.info("  split1 : " + split1);
				split2 = arr_line[1];
				//logger.info("  split2 : " + split2);
				split3 = arr_line[2];
				//logger.info("  split3 : " + split3);
				split4 = arr_line[3];
				//logger.info("  split4 : " + split4);
			}
			
			if(arr_line.length == 5) { //???????????? ????????? ?????????????????? ??????
				split1 = arr_line[0];
				//logger.info("  split1 : " + split1);
				split2 = arr_line[1];
				//logger.info("  split2 : " + split2);
				split3 = arr_line[2];
				//logger.info("  split3 : " + split3);
				split4 = arr_line[3];
				//logger.info("  split4 : " + split4);
				split5 = arr_line[4];
				//logger.info("  split5 : " + split5);
			}
			
			String recent = authvo.getHm_empnum(); //?????? ?????????
			//logger.info("  recent : " + recent);
			
			// 4)??? ?????? VO ?????? ?????? ----------
			AuthVO _auvo = null;
			_auvo = new AuthVO();
			
			String next = ""; //??????????????? ????????????
			
			//???????????? ???????????????
			if(recent.equals(split5)) {
				//logger.info("  if(recent.equals(split5)) ?????? >>> ");
				
				next = "";
				//logger.info("  next : " + next);
				
			} else if(recent.equals(split4)) {
				//logger.info("  if(recent.equals(split4)) ?????? >>> ");
				
				next = split5;
				//logger.info("  next : " + next);
				
			} else if(recent.equals(split3)) {
				//logger.info("  if(recent.equals(split3)) ?????? >>> ");
				
				next = split4;
				//logger.info("  next : " + next);
				
			} else if(recent.equals(split2)) {
				//logger.info("  if(recent.equals(split2)) ?????? >>> ");
				
				next = split3;
				//logger.info("  next : " + next);
				
			}else if(recent.equals(split1)) {
				//logger.info("  if(recent.equals(split1)) ?????? >>> ");
				
				next = split2;
				//logger.info("  next : " + next);
				
			}
			
			_auvo.setEa_presentnum(next);
			
			if(!next.equals("")) {
				//logger.info("  if(!next.equals('')) ?????? >>>  ");
			
				//???????????? ?????? ????????????
				_mvo = null; //?????? ???????????? ?????????
				_mvo = new MemberVO();
				//logger.info("  _mvo : " + _mvo);
				_mvo.setHm_empnum(next);
				list_person = null; //?????? ???????????? ?????????
				list_person = eworkFormService.selectPerson(_mvo);
				mvo_person = null; //?????? ???????????? ?????????
				mvo_person = list_person.get(0);
				//logger.info("  mvo_person : " + mvo_person);
				
				_auvo.setEa_position(mvo_person.getHm_position());
				_auvo.setEa_num(authvo.getEa_num());
				
			} else if(next.equals("")) {
				//logger.info("  else if(next.equals('') ?????? >>>  ");
				
				_auvo.setEa_position(""); 
				_auvo.setEa_num(authvo.getEa_num());
				
			}
			// 4)??? ?????? VO ?????? ???..! ----------
			
			// 4) ??????
			bFlag = eworkService.updateAuth(_auvo);
			//logger.info("  (updateAuth)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
		
			ptm.commit(ts);
			
			
			if(bFlag) {
				//logger.info("  if(bFlag) ?????? >>> ");
				
				return "redirect:/ework/selectAuthBox.td";
				
			}
			
		} catch(Exception e) {
			//logger.info("?????? : " + e);
			ptm.rollback(ts);
		}
		
		return "#";
	} //end of substitute method
	
	
	@RequestMapping("/return")
	public String returnMethod(@ModelAttribute AuthorizationVO authvo, Model model) {
		//logger.info("(EworkController)public String returnMethod(@ModelAttribute AuthorizationVO authvo, Model model) ?????? >>> ");
		//logger.info("  ???????????? authvo : " + authvo);
		//logger.info("  getEa_num() : " + authvo.getEa_num());
		//logger.info("  getHm_empnum() : " + authvo.getHm_empnum());
	
		String eai_num = "";
		boolean bFlag = false;
		
		//???????????? ??????
		dtd = new DefaultTransactionDefinition();
		dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = ptm.getTransaction(dtd);
		
		try {
			
			// 1) ?????? -------------
			List<AuthPersonVO> list_Eai_num = eworkFormService.chaebunAuthPerson();
			//logger.info("  list_Eai_num : " + list_Eai_num);
			AuthPersonVO apvo_Eai_num = list_Eai_num.get(0);
			//logger.info("  apvo_Eai_num : " + apvo_Eai_num);
			eai_num = apvo_Eai_num.getEai_num();
			//logger.info("  eai_num : " + eai_num);
			eai_num = ChaebunUtils.cNum(eai_num, AUTHPERSON_GUNBUN);
			//logger.info("  eai_num : " + eai_num);
			
			//??????????????? ????????? ????????????
//			SignStampVO _ssvo = null;
//			_ssvo = new SignStampVO();
//			//logger.info("  _ssvo : " + _ssvo);
//			_ssvo.setHm_empnum(authvo.getHm_empnum());
//			
//			List<SignStampVO> list_signstamp = eworkFormService.selectSignStamp(_ssvo);
//			//logger.info("  list_signstamp : " + list_signstamp);
//			SignStampVO ssvo = null;
//			ssvo = list_signstamp.get(0);
//			//logger.info("  ssvo : " + ssvo);
//			String es_filedir = ssvo.getEs_filedir();
			
			//?????????????????? & ????????? ??? ??????
			EanumVO _nvo = null;
			_nvo = new EanumVO();
			_nvo.setEa_num(authvo.getEa_num());
			
			//????????? ????????????
			List<AuthPersonVO> list_sequence = null;
			list_sequence = eworkService.plusSequence(_nvo);
			//logger.info("  list_sequence : " + list_sequence);
			AuthPersonVO apvo_sequence = null;
			apvo_sequence = list_sequence.get(0);
			//logger.info("  apvo_sequence : " + apvo_sequence);
			String eai_sequence = "";
			eai_sequence = apvo_sequence.getEai_sequence();
			
			//?????? ?????? ?????? ?????? ????????????
			MemberVO _mvo = null;
			_mvo = new MemberVO();
			//logger.info("  _mvo : " + _mvo);
			_mvo.setHm_empnum(authvo.getHm_empnum());
			List<MemberVO> list_person = null;
			list_person = eworkFormService.selectPerson(_mvo);
			MemberVO mvo_person = list_person.get(0);
			//logger.info("  mvo_person : " + mvo_person);
			//logger.info("  getHm_empnum() : " + mvo_person.getHm_empnum());
			//logger.info("  getHm_position() : " + mvo_person.getHm_position());
			
			AuthPersonVO _apvo = null;
			_apvo = new AuthPersonVO();
			_apvo.setEai_num(eai_num);
			_apvo.setEa_num(authvo.getEa_num());
			_apvo.setEai_recentnum(authvo.getHm_empnum());
			_apvo.setEai_position(mvo_person.getHm_position());
			_apvo.setEai_auth("62"); //????????????
			_apvo.setEai_filedir(""); //???????????? ???????????? null
			_apvo.setEai_substituteYN("N"); //????????? ??????
			_apvo.setEai_substitutenum(null); //????????? ??????
			_apvo.setEai_sequence(eai_sequence);
			
			// 1)
			bFlag = eworkFormService.insertAuthPerson(_apvo);
			//logger.info("  (insertAuthPerson)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
			
			
			// 2) ?????? -------------
			//?????? ????????????
//			List<LineVO> list_line = eworkService.searchLine(_nvo);
//			//logger.info("  list_line : " + list_line);
//			LineVO lvo = list_line.get(0);
//			//logger.info("  lvo : " + lvo);
//			String el_line = lvo.getEl_line();
//			//logger.info("  el_line : " + el_line);
//			String[] arr_line = el_line.split("-");
//			
//			String split1 = "";
//			String split2 = "";
//			String split3 = ""; 
//			String split4 = "";
//			String split5 = ""; 
//			
//			/* ?????????????????? NULL??? ???????????? '' ??? ?????? NULL ??? ?????? ?????????. ????????? id ??? 1, 2 ??? ?????? name??? ?????? NULL ??? ???????????????. */
//			
//			if(arr_line.length == 2) { //???????????????
//				split1 = arr_line[0];
//				//logger.info("  split1 : " + split1);
//				split2 = arr_line[1];
//				//logger.info("  split2 : " + split2);
//			}
//			
//			if(arr_line.length == 4) { //??????
//				split1 = arr_line[0];
//				//logger.info("  split1 : " + split1);
//				split2 = arr_line[1];
//				//logger.info("  split2 : " + split2);
//				split3 = arr_line[2];
//				//logger.info("  split3 : " + split3);
//				split4 = arr_line[3];
//				//logger.info("  split4 : " + split4);
//			}
//			
//			if(arr_line.length == 5) { //???????????? ????????? ?????????????????? ??????
//				split1 = arr_line[0];
//				//logger.info("  split1 : " + split1);
//				split2 = arr_line[1];
//				//logger.info("  split2 : " + split2);
//				split3 = arr_line[2];
//				//logger.info("  split3 : " + split3);
//				split4 = arr_line[3];
//				//logger.info("  split4 : " + split4);
//				split5 = arr_line[4];
//				//logger.info("  split5 : " + split5);
//			}
//			
//			String recent = authvo.getHm_empnum(); //?????? ?????????
//			//logger.info("  recent : " + recent);
			
			// 2)??? ?????? VO ?????? ?????? ----------
			AuthVO _auvo = null;
			_auvo = new AuthVO();
			
//			String next = ""; //??????????????? ????????????
//			
//			//???????????? ???????????????
//			if(recent.equals(split5)) {
//				//logger.info("  if(recent.equals(split5)) ?????? >>> ");
//				
//				next = "";
//				//logger.info("  next : " + next);
//				
//			} else if(recent.equals(split4)) {
//				//logger.info("  if(recent.equals(split4)) ?????? >>> ");
//				
//				next = split5;
//				//logger.info("  next : " + next);
//				
//			} else if(recent.equals(split3)) {
//				//logger.info("  if(recent.equals(split3)) ?????? >>> ");
//				
//				next = split4;
//				//logger.info("  next : " + next);
//				
//			} else if(recent.equals(split2)) {
//				//logger.info("  if(recent.equals(split2)) ?????? >>> ");
//				
//				next = split3;
//				//logger.info("  next : " + next);
//				
//			}else if(recent.equals(split1)) {
//				//logger.info("  if(recent.equals(split1)) ?????? >>> ");
//				
//				next = split2;
//				//logger.info("  next : " + next);
//				
//			}
			
//			_auvo.setEa_presentnum(next);
			_auvo.setEa_presentnum("return"); //"??????" ????????? ??????
			
//			if(!next.equals("")) {
//				//logger.info("  if(!next.equals('')) ?????? >>>  ");
//			
//				//???????????? ?????? ????????????
//				_mvo = null; //?????? ???????????? ?????????
//				_mvo = new MemberVO();
//				//logger.info("  _mvo : " + _mvo);
//				_mvo.setHm_empnum(next);
//				list_person = null; //?????? ???????????? ?????????
//				list_person = eworkFormService.selectPerson(_mvo);
//				mvo_person = null; //?????? ???????????? ?????????
//				mvo_person = list_person.get(0);
//				//logger.info("  mvo_person : " + mvo_person);
//				
//				_auvo.setEa_position(mvo_person.getHm_position()); //???????????? //?????? ?????? ?????? ????????????. ????????? ?????? ??????????????? ?????? ???????????? ???????????????
//				_auvo.setEa_num(authvo.getEa_num());
//				
//			} else if(next.equals("")) {
//				//logger.info("  else if(next.equals('') ?????? >>>  ");
//				
//				_auvo.setEa_position(""); //???????????? //?????? ?????? ?????? ????????????. ????????? ?????? ??????????????? ?????? ???????????? ???????????????
//				_auvo.setEa_num(authvo.getEa_num());
//				
//			}
			
			_auvo.setEa_position(""); //????????? ?????? ???????????? ??????
			_auvo.setEa_num(authvo.getEa_num());
			
			// 2)??? ?????? VO ?????? ???..! ----------
			
			// 2)
			bFlag = eworkService.updateAuth(_auvo);
			//logger.info("  (updateAuth)bFlag : " + bFlag);
			
			if(!bFlag) {
				//logger.info("  if(!bFlag) ?????? >>> ");
				//logger.info("  ??????");
			}
		
			ptm.commit(ts);
			
			
			if(bFlag) {
				//logger.info("  if(bFlag) ?????? >>> ");
				
				return "redirect:/ework/selectAuthBox.td";
				
			}
			
			
		} catch(Exception e) {
			//logger.info("?????? : " + e);
			ptm.rollback(ts);
		}
		
		//logger.info("(EworkController)public String returnMethod(@ModelAttribute AuthorizationVO authvo, Model model) ??? >>> ");
		return "#";
	} //end of returnMethod method
	
	@RequestMapping("/moveInsertSignStamp")
	public String moveInsertSignStamp() {
		//logger.info("(EworkController)public String moveInsertSignStamp() ?????? >>> ");
		//logger.info("(EworkController)public String moveInsertSignStamp() ??? >>> ");
		return "ework/insertSignStamp";
	} //end of moveInsertSignStamp method
	
	@RequestMapping(value="/insertSignStamp", method=RequestMethod.POST)
	public String insertSignStamp(HttpServletRequest request, HttpServletResponse response
																					, Model model) {
		//logger.info("(EworkController)public String insertSignStamp(HttpServletRequest request, HttpServletResponse response, Model model) ?????? >>> ");

		//???????????? ??????
		dtd = new DefaultTransactionDefinition();
		dtd.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
		ts = ptm.getTransaction(dtd);
		
		boolean bFlag = false;
		boolean uploadFlag = false;
		
		try {
			
			//??????????????? ??????
			List<SignStampVO> list_chaebun = eworkFormService.chaebunSignStamp();
			//logger.info("  list_chaebun : " + list_chaebun);
			SignStampVO svo_chaebun = list_chaebun.get(0);
			//logger.info("  svo_chaebun : " + svo_chaebun);
			String es_num = svo_chaebun.getEs_num();
			es_num = ChaebunUtils.cNum(es_num, SIGNSTAMP_GUNBUN);
			//logger.info("  es_num : " + es_num);
			
			FileUploadUtil fuu = new FileUploadUtil();
			//logger.info("  fuu : " + fuu);
			
			uploadFlag = fuu.fileUpload(request, UPLOAD_ABSTRACT_PATH);
			//logger.info("  uploadFlag : " + uploadFlag);
			
			SignStampVO _ssvo = null;
			_ssvo = new SignStampVO();
			
			_ssvo.setEs_num(es_num);
			
			if(uploadFlag) {
				//logger.info("  if(uploadFlag) ?????? >>> " );
				
				String hm_empnum = fuu.getParameter("hm_empnum");
				//logger.info("  hm_empnum : " + hm_empnum);
				String image = fuu.getParameter("image");
				//logger.info("  image : " + image);
				
				Enumeration<String> files = fuu.getFileNames();
				//logger.info("  files : " + files);
				String file = files.nextElement();
				//logger.info("  file : " + file);
				
				String fileDirectory = UPLOAD_RELATIVE_PATH + "//" + file;
				//logger.info("  fileDirectory : " + fileDirectory);
				
				_ssvo.setHm_empnum(hm_empnum);
				_ssvo.setEs_filedir(fileDirectory);
				
				if(image != null) {
					//logger.info("  if(image != null) ?????? >>> ");
					
					if(image.equals("")) {
						//logger.info("  if(image.equals('')) ?????? >>> ");
						
						//?????????
						bFlag = eworkService.insertSignStamp(_ssvo);
						
						if(!bFlag) {
							//logger.info("  if(!bFlag) ?????? >>> ");
							//logger.info("  ??????");
						} //end of if(!bFlag)
						
					} else {
						//logger.info("  if(image.equals(''))-else ?????? >>> ");
						
						//????????????
						bFlag = eworkService.updateSignStamp(_ssvo);
						
						if(!bFlag) {
							//logger.info("  if(!bFlag) ?????? >>> ");
							//logger.info("  ??????");
						} //end of if(!bFlag)
						
					}
					
				} else {
					//System.out.println("  if(image != null)-else ?????? >>> ");
				
					//?????????
					bFlag = eworkService.insertSignStamp(_ssvo);
					
					if(!bFlag) {
						//logger.info("  if(!bFlag) ?????? >>> ");
						//logger.info("  ??????");
					} //end of if(!bFlag)
				
				} //end of else
				
				ptm.commit(ts);
				
			} //end of if(uploadFlag)
			
			
			if(bFlag) {
				//logger.info("  if(bFlag) ?????? >>> ");
				
				model.addAttribute("bFlag", bFlag);
				
				return "ework/signUploadResult";
				
			}
			
			
		} catch(Exception e) {
			//logger.info("?????? : " + e);
			ptm.rollback(ts);
		}
		
		
		//logger.info("(EworkController)public String insertSignStamp(HttpServletRequest request, HttpServletResponse response, Model model) ??? >>> ");
		return "#";
	}
}
