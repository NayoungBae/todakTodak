package java142.todak.board.controller;

import java.util.Enumeration;
import java.util.List;

import java142.todak.board.service.BoardService;
import java142.todak.board.vo.NoCheckVO;
import java142.todak.board.vo.NoticeVO;
import java142.todak.board.vo.SuLikeVO;
import java142.todak.board.vo.SuReplyVO;
import java142.todak.board.vo.SuggestionVO;
import java142.todak.common.ChaebunUtils;
import java142.todak.common.FileUploadUtil;
import java142.todak.common.Paging;
import java142.todak.common.VOPrintUtil;
import java142.todak.etc.utils.LoginSession;
import java142.todak.human.vo.MemberVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/board")
public class BoardController {
   
   
   final static String FILEPATH1 =  "//home//ec2-user//tomcatt//webapps//todakProject//upload//board//notice";
   final static String FILEPATH2 =  "//home//ec2-user//tomcatt//webapps//todakProject//upload//board//suggestion";
   final static String DOWNLOADPATH1 = "upload/board/notice/";
   final static String DOWNLOADPATH2 = "upload/board/suggestion/";
   final static String NOTICE_GUBUN = "N";
   final static String SUGGESTION_GUBUN = "S";
   final static String SUREPLY_GUBUN = "R";
   final static String SULIKE_GUBUN = "L";
   final static String CHECK_GUBUN = "C";
   
   Logger logger = Logger.getLogger(BoardController.class);

   
   @Autowired
   private BoardService boardService;


   //??????????????????
   @RequestMapping(value="/countSuLike", method = RequestMethod.POST, produces = "application/json")
   @ResponseBody
   public ResponseEntity<String> countSuLike(@RequestBody SuLikeVO slvo){
      //logger.info("(log)BoardController.countSuLike ??????"); 
      
      ResponseEntity<String> entity = null;
      String result = null;
      String bs_num = slvo.getBs_num();
      //logger.info("????????? bs_num >>> " + bs_num);
      
      try{
         List<SuLikeVO> countSuLike = boardService.countSuLike(slvo);
         result = countSuLike.get(0).getBsl_likeYN();
         
         entity = new ResponseEntity<String>(result, HttpStatus.OK);
         
      }catch(Exception e){
         e.printStackTrace();
         entity = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
      }
      //logger.info("(log)BoardController.countSuLike ??????"); 
      
      return entity;
   }//end of selectSuReply
   
   //??????????????? ??????
     @ResponseBody
     @RequestMapping(value="/checkLike", method = RequestMethod.POST, produces = "application/json")
      public ResponseEntity<String> checkLike(@RequestBody SuLikeVO slvo){
         //logger.info("(log)BoardController.checkLike ??????"); 

         List<SuLikeVO> list = boardService.chaebunSuLike();
         
         if(list.size() == 1){
            String bsl_num = ChaebunUtils.cNum(list.get(0).getBsl_num(), SUREPLY_GUBUN);
            slvo.setBsl_num(bsl_num);
         }
         
         String bs_num = slvo.getBs_num();
         
         //logger.info("????????? >>> " + slvo.getBsl_num());
         //logger.info("????????? bs_num >>> " + bs_num);
         
         ResponseEntity<String> entity = null;
        
         int result = 0;
         
         try{
               result = boardService.checkSuLike(slvo);
               if(result == 1){
                   entity = new ResponseEntity<String>("1", HttpStatus.OK);
               }else{
                  boardService.unCheckSuLike(slvo);
                  entity = new ResponseEntity<String>("0", HttpStatus.OK);
               }
            
         }catch(Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
         }
         //logger.info("(log)BoardController.checkLike ??????"); 
         return entity;
      }

      //???????????????
      @RequestMapping(value="/countSuDislike", method = RequestMethod.POST, produces = "application/json")
      @ResponseBody
   public ResponseEntity<String> countSuDislike(@RequestBody SuLikeVO slvo){
      //logger.info("(log)BoardController.countSuDislike ??????"); 
      
      ResponseEntity<String> entity = null;
      String result = null;
      String bs_num = slvo.getBs_num();
      //logger.info("????????? bs_num >>> " + bs_num);
      
      try{
         List<SuLikeVO> countSuDislike = boardService.countSuDislike(slvo);
         result = countSuDislike.get(0).getBsl_dislikeYN();
         
         entity = new ResponseEntity<String>(result, HttpStatus.OK);
         
      }catch(Exception e){
         e.printStackTrace();
         entity = new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
      }
      //logger.info("(log)BoardController.countSuDislike ??????"); 
      
      return entity;
   }//end of selectSuReply

   //?????? ?????? ??????
     @ResponseBody
     @RequestMapping(value="/checkSuDislike", method = RequestMethod.POST, produces = "application/json")
      public ResponseEntity<String> checkSuDislike(@RequestBody SuLikeVO slvo){
         //logger.info("(log)BoardController.checkSuDislike ??????"); 

         List<SuLikeVO> list = boardService.chaebunSuLike();
         
         if(list.size() == 1){
            String bsl_num = ChaebunUtils.cNum(list.get(0).getBsl_num(), SUREPLY_GUBUN);
            slvo.setBsl_num(bsl_num);
         }
         
         String bs_num = slvo.getBs_num();
         
         //logger.info("????????? >>> " + slvo.getBsl_num());
         //logger.info("????????? bs_num >>> " + bs_num);
         
         ResponseEntity<String> entity = null;
        
         int result = 0;
         
         try{
               result = boardService.checkSuDislike(slvo);
               if(result == 1){
                   entity = new ResponseEntity<String>("1", HttpStatus.OK);
               }else{
                  boardService.unCheckSuDislike(slvo);
                  entity = new ResponseEntity<String>("0", HttpStatus.OK);
               }
            
         }catch(Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
         }
         //logger.info("(log)BoardController.checkSuDislike ??????"); 
         return entity;
      }

     
        
   //?????? ??????
   @RequestMapping(value="/all/{bs_num}.td")
   @ResponseBody
   public ResponseEntity<List<SuReplyVO>> selectSuReply(@PathVariable("bs_num") String  bs_num, SuReplyVO srvo){
      //logger.info("(log)ReplyController.selectSuReply ??????"); 
      
      ResponseEntity<List<SuReplyVO>> entity = null;
      
      srvo.setBs_num(bs_num);
      
      //logger.info("bs_num >>> " + srvo.getBs_num());
      try{
         
         entity = new ResponseEntity<>(boardService.selectSuReply(srvo), HttpStatus.OK);
         
      }catch(Exception e){
         e.printStackTrace();
         entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }
      //logger.info("(log)ReplyController.selectSuReply ??????"); 
      
      return entity;
   }//end of selectSuReply 
   
   
   //??????????????????
     @RequestMapping(value="/replyInsert")
      public ResponseEntity<String> insertSuReply(@RequestBody SuReplyVO srvo){
         //logger.info("(log)ReplyController.insertSuReply ??????"); 

         List<SuReplyVO> list = boardService.chaebunSuReply();
         if(list.size() == 1){
            String bsr_num = ChaebunUtils.cNum(list.get(0).getBsr_num(), SUREPLY_GUBUN);
            srvo.setBsr_num(bsr_num);
         }
         
         String bs_num = srvo.getBs_num();
         
         //logger.info("???????????? >>> " + srvo.getBsr_num());
         //logger.info("????????? bs_num >>> " + bs_num);
         
         ResponseEntity<String> entity = null;
         int result;
         
         try{
               result = boardService.insertSuReply(srvo);
               if(result == 1){
                  entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
               }
            
            
         }catch(Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
         }
         //logger.info("(log)ReplyController.insertSuReply ??????"); 
         return entity;
      }

     //?????? ??????
      @RequestMapping(value="/update/{bsr_num}.td", method = {RequestMethod.PUT, RequestMethod.PATCH})
      @ResponseBody
      public ResponseEntity<String> updateSuReply(@PathVariable("bsr_num") String bsr_num, @RequestBody SuReplyVO srvo){
         //logger.info("(log)ReplyController.replyUpdate ??????"); 
         ResponseEntity<String> entity = null;
         
         try{
            srvo.setBsr_num(bsr_num);
            int result = boardService.updateSuReply(srvo);
            if(result == 1){
               entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
            }else{
               entity = new ResponseEntity<String>("FAIL", HttpStatus.OK);
            }
            
         }catch(Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
      
         }
         //logger.info("(log)ReplyController.replyUpdate ??????"); 
         return entity;
      }
      
      //???????????? ?????? ????????????
      @RequestMapping(value="/delete/{bsr_num}.td", method = {RequestMethod.PUT, RequestMethod.PATCH})
      @ResponseBody
      public ResponseEntity<String> deleteSuReply(@PathVariable("bsr_num") String bsr_num, @RequestBody SuReplyVO srvo){
         //logger.info("(log)ReplyController.replyDelete ??????"); 
         ResponseEntity<String> entity = null;
         
         try{
            srvo.setBsr_num(bsr_num);
            int result = boardService.deleteSuReply(srvo);
            if(result == 1){
               entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
            }else{
               entity = new ResponseEntity<String>("FAIL", HttpStatus.OK);
            }         
            
         }catch(Exception e){
            e.printStackTrace();
            entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
      
         }
         //logger.info("(log)ReplyController.replyDelete ??????"); 
         return entity;
      }
    /*--------------------------------------------*/
   
   //??????????????? ?????????????????? ????????????
   @RequestMapping("/moveSelectNotice")
   public String moveSelectNotice(@RequestParam("hm_empnum") String hm_empnum, Model model){
      
      model.addAttribute("hm_empnum",hm_empnum);
      return "redirect:/board/selectNotice.td";
   }
   
   
   
   //???????????? ?????? ????????????
   @RequestMapping("/selectSuggestion")
   public String selectSuggestion(@ModelAttribute SuggestionVO svo, MemberVO mvo, Model model,HttpServletRequest request){
      
      //logger.info("(log)BoardController.selectSuggestion ?????? >>> ");
      
      LoginSession sManager = LoginSession.getInstance();
      String sessionId = request.getSession().getId();
      String hm_empnum = sManager.getUserID(sessionId);
      //logger.info("hm_empnum >>>> : " + hm_empnum); 
      
      
      int totalCnt = 0;
      String cPage = request.getParameter("curPage");
      String pageCtrl=request.getParameter("pageCtrl");
      
      //logger.info("cPage >>>> " + cPage);
      
      if(svo.getFindIndex() == null){
         String key = request.getParameter("key");//????????? ????????? ????????? ????????????????????? ?????? ???????????????.
         svo.setKeyword(key);
         String index = request.getParameter("index");//????????? ????????? ????????? ????????????????????? ?????? ???????????????.
         svo.setFindIndex(index);
      }
      
      
      String findIndex = svo.getFindIndex();
      //logger.info("FindIndex == " + findIndex);
      if(findIndex != null && findIndex.equals("hm_empnum")) {
         svo.setKeyword(hm_empnum);
         String e = "hm_empnum";
         model.addAttribute("findIndex",e);
      }

      //logger.info("FindIndex == " + svo.getFindIndex());
      //logger.info("keyword == " + svo.getKeyword());
      
      Paging.setPage(svo,cPage,pageCtrl);//???????????? ????????? Paging???????????? ???????????????
      
      
      mvo.setHm_empnum(hm_empnum);
   
      List<SuggestionVO> suggestionList = null;
      suggestionList = boardService.selectSuggestion(svo);
      if(suggestionList.size() != 0){
         totalCnt=suggestionList.get(0).getTotalCount();//?????? ????????? ???????????? 0??? ???????????? ?????? totalCount?????? ????????? 
         svo.setTotalCount(totalCnt);//vo??? ???????????????.
      }
      svo.setN_hm_empnum(hm_empnum);
      
      model.addAttribute("suggestionList",suggestionList);
      model.addAttribute("i_svo",svo);
      
      String url = "board/selectSuggestion";
      
      //logger.info("(log)BoardController.selectSuggestion ??????>>> ");
      
      return url;
      
   }
   
   //???????????? ?????? ????????????
   @RequestMapping("/searchSuggestion")
   public String searchSuggestion(@ModelAttribute SuggestionVO svo, Model model, SuLikeVO slvo, @RequestParam("hm_empnum") String hm_empnum){
      
      //logger.info("(log)BoardController.searchSuggestion ?????? >>> ");

      List<SuggestionVO> suggestionDetail = null;
      suggestionDetail = boardService.searchSuggestion(svo);
      int iFlag = 0;
      iFlag = boardService.updateSuggestionHit(svo);
      
      //logger.info("(log)BoardController.searchSuggestion ????????? ?????? >>> " + iFlag);

      model.addAttribute("suggestionDetail",suggestionDetail);
      
      
      //??????????????? ???????????? ????????????
      String bs_num = svo.getBs_num();
      
      //logger.info("hm_empnum >>>> : " + hm_empnum );
      
      slvo.setBs_num(bs_num);
      slvo.setHm_empnum(hm_empnum);
      
      
      List<SuLikeVO> beforeLike = null;
      beforeLike = boardService.beforeSuLike(slvo);
      String bsl_likeYN = "";
      bsl_likeYN = beforeLike.get(0).getBsl_likeYN();
      
      model.addAttribute("bsl_likeYN",bsl_likeYN);
      
      
      //?????? ?????? ???????????? ????????????
      List<SuLikeVO> beforeSuDislike = null;
      beforeSuDislike = boardService.beforeSuDislike(slvo);
      String bsl_dislikeYN = "";
      bsl_dislikeYN = beforeSuDislike.get(0).getBsl_dislikeYN();
      
      model.addAttribute("bsl_dislikeYN",bsl_dislikeYN);
      
      String url = "board/searchSuggestion";
      
      //logger.info("(log)BoardController.searchSuggestion ?????? >>> ");
      
      return url;
      
   }
   
   //???????????? ????????? ????????????
   @RequestMapping(value="/deleteSuggestion")
   public String deleteSuggestion(@ModelAttribute SuggestionVO svo, Model model){
      
      int result = 0;
      
      String hm_empnum = svo.getHm_empnum();
      
      result = boardService.deleteSuggestion(svo);
      
      if(result == 1){
         //logger.info("????????????");
      }else{
         //logger.info("????????????");
      }
      
      model.addAttribute("hm_empnum",hm_empnum);
      
      return "redirect:" + "/board/selectSuggestion.td";
   }
   
   //???????????? ????????? ?????????????????? ??????
   @RequestMapping(value="/moveUpdateSuggestion")
   public String moveUpdateSuggestion(@ModelAttribute SuggestionVO svo, Model model){
      //logger.info("(log)BoardController.moveUpdateSuggestion ?????? >>> ");
      
      List<SuggestionVO> updateList = null;
      String url = null;
      String bs_num = svo.getBs_num();
      //logger.info("bs_num >>> : " + bs_num);
      updateList = boardService.searchSuggestion(svo);
      
      model.addAttribute("updateList",updateList);
      
      
      if(updateList.size() == 1){
         url = "board/updateSuggestion";
      }else{
         model.addAttribute("hm_empnum",svo.getHm_empnum());
         url = "redirect:" + "/board/selectSuggestion.td";
      }
      
      //logger.info("(log)BoardController.updateSearchSuggestion ?????? >>> ");
      return  url;
   }
   
   
   //???????????? ????????? ????????????
   @RequestMapping(value="/updateSuggestion", method=RequestMethod.POST)
   public String updateSuggestion(@ModelAttribute SuggestionVO svo, Model model, HttpServletRequest request){
      //logger.info("(log)BoardController.updateSuggestion ?????? >>> ");
      int result = 0;
      String hm_empnum = null;
      
      FileUploadUtil fuu = new FileUploadUtil();
      boolean bFlag = false;
      bFlag = fuu.fileUpload(request, FILEPATH2);
      
      //logger.info("bFlag >>> : " + bFlag );
         
      if(bFlag){
         
         Enumeration<String> en = fuu.getFileNames();
         
         String fileName = en.nextElement();
         
         if(fileName != null){
            fileName = fileName.replaceAll(DOWNLOADPATH2, "");
         }

         
         if(fileName != null){
            String bs_image = DOWNLOADPATH2 + fileName;
            svo.setBs_image(bs_image);
         }else{
            svo.setBs_image("");
         }
         
         String bs_image = svo.getBs_image();
   
         //logger.info("??????????????? >>>> " + bs_image);
         
         String bs_title = fuu.getParameter("bs_title");
         String bs_content = fuu.getParameter("bs_content");
         String bs_num = fuu.getParameter("bs_num");
         hm_empnum = fuu.getParameter("hm_empnum");
         
         
         
         //logger.info(bs_num + "" + bs_title + "" +bs_content + "" + hm_empnum);
         
         svo.setBs_num(bs_num);
         svo.setBs_title(bs_title);
         svo.setBs_content(bs_content);
         svo.setBs_image(bs_image);
         svo.setHm_empnum(hm_empnum);
      

         VOPrintUtil.suggestionVOPrint(svo);
      
      }else{
         //logger.info("multipart ?????? ??????");
      }
   
      result = boardService.updateSuggestion(svo);
      
      if(result == 1){
         //logger.info("????????????");
      }else{
         //logger.info("????????????");
      }
      
      String bs_num = svo.getBs_num();
      model.addAttribute("hm_empnum",hm_empnum);
      
      
      return "redirect:" + "/board/searchSuggestion.td?bs_num=" + bs_num;
   }
   
   //???????????? ?????????????????? ??????
   @RequestMapping(value="/downloadSuggestion")
   public String downloadSuggestion(@ModelAttribute SuggestionVO svo, Model model){
      //logger.info("(log)BoardController.downloadSuggestion ?????? >>> ");

      String bs_image = svo.getBs_image();
      
      bs_image = bs_image.replace(DOWNLOADPATH2, "");
      
      //logger.info("bs_image >>> " + bs_image);
      
      model.addAttribute("fileName" ,bs_image);
      model.addAttribute("FilePath", FILEPATH2 );
      
      //logger.info("(log)BoardController.downloadSuggestion ?????? >>> ");
      return "board/fileDownload";
   }

   //???????????? ???????????? ????????????
   @RequestMapping("/moveWriteSuggestion")
   public String moveWriteSuggestion(){
      //logger.info("(log)BoardController.moveWriteSuggestion ?????? >>> ");

      return "board/insertSuggestion";
   }
   
   //???????????? ??????
   @RequestMapping("/insertSuggestion")
   public String insertSuggestion(@ModelAttribute SuggestionVO svo, Model model,HttpServletRequest request){
      //logger.info("(log)BoardController.insetSuggestion ?????? >>> ");
      //logger.info("(log)BoardController.cheabunSuggestion ?????? >>> ");
      
      String bs_num = "";
      List<SuggestionVO> list = null;
      list = boardService.cheabunSuggestion();
      bs_num = list.get(0).getBs_num();
      
      svo.setBs_num(ChaebunUtils.cNum2(bs_num, SUGGESTION_GUBUN));
      //logger.info("(log)BoardController.cheabunSuggestion ??????  : bs_num >>> " + ChaebunUtils.cNum2(bs_num, SUGGESTION_GUBUN));

      
      String url =  null;
      
      
      FileUploadUtil fuu = new FileUploadUtil();
      boolean bFlag = false;
      bFlag = fuu.fileUpload(request, FILEPATH2);
      //logger.info("bFlag >>> : " + bFlag );
      
      if(bFlag){
         
         Enumeration<String> en = fuu.getFileNames();
         
         String fileName = en.nextElement();
         
         if(fileName != null){
            String bs_image = DOWNLOADPATH2 + fileName;
            svo.setBs_image(bs_image);
         }else{
            svo.setBs_image("");
         }
         
         String    hm_empnum = fuu.getParameter("hm_empnum");
         String    bs_title   =  fuu.getParameter("bs_title");
         String    bs_content = fuu.getParameter("bs_content");
         
         svo.setHm_empnum(hm_empnum);
         svo.setBs_title(bs_title);
         svo.setBs_content(bs_content);
         
         int result = 0;
         result = boardService.insertSuggestion(svo);
         
         if(result == 1){
            
            //logger.info("?????? ??????!");
            url = "redirect:/board/selectSuggestion.td";
            model.addAttribute("hm_empnum",hm_empnum);
         }else{
            //logger.info("?????? ??????!");
            url = "redirect:/board/writeSuggestion.td";
         }

      }else{
         //logger.info("?????????????????????");
         url = "redirect:/board/writeSuggestion.td";
      }
      
      
      //logger.info("(log)BoardController.insetSuggestion ?????? >>> ");
      return url;
   }
   
   //???????????? ????????? ??????  ??? ??????
   @RequestMapping("/selectNotice") 
   public String selectBoardNotice(@ModelAttribute NoticeVO nvo, MemberVO mvo, Model model, HttpServletRequest request ) {
      
      //logger.info("(log)BoardController.selectNotice ?????? >>> ");
      LoginSession sManager = LoginSession.getInstance();
      String sessionId = request.getSession().getId();
      String hm_empnum = sManager.getUserID(sessionId);
      //logger.info(hm_empnum);
      int totalCnt = 0;
      String cPage = request.getParameter("curPage");
      String pageCtrl=request.getParameter("pageCtrl");
      
      if(nvo.getFindIndex() == null){
         String key=request.getParameter("key");//????????? ????????? ????????? ????????????????????? ?????? ???????????????.
         nvo.setKeyword(key);
         String index=request.getParameter("index");//????????? ????????? ????????? ????????????????????? ?????? ???????????????.
         nvo.setFindIndex(index);
      }
      
      
      String findIndex = nvo.getFindIndex();
      //logger.info("FindIndex == " + findIndex);
      if(findIndex != null && findIndex.equals("hm_empnum")) {
         nvo.setKeyword(hm_empnum);
         String e = "hm_empnum";
         model.addAttribute("findIndex",e);
      }

      //logger.info("FindIndex == " + nvo.getFindIndex());
      //logger.info("keyword == " + nvo.getKeyword());
      
      MemberVO i_mvo = new MemberVO();
      
      //???????????? ?????? ?????? ????????????
      mvo.setHm_empnum(hm_empnum);
      
      if(mvo.getHm_empnum().isEmpty()){
         mvo.setHm_empnum(nvo.getHm_empnum());
      }
      
      String[] check_hm_empnum = mvo.getHm_empnum().split(",");
      
      i_mvo.setHm_empnum(check_hm_empnum[0]);
      List<MemberVO> writerQualified = null;
      writerQualified =  boardService.selectWrite(i_mvo);
      
      MemberVO _mvo = writerQualified.get(0);
      String check_empnum = _mvo.getHm_empnum();
      nvo.setCheck_empnum(check_empnum);
      
      String c_hm_deptnum = _mvo.getHm_deptnum();
      //logger.info("???????????? ????????????" + c_hm_deptnum);
      String check_divnum = c_hm_deptnum.substring(0, 2);
      String check_deptnum = "";
      
      if(c_hm_deptnum.length() > 2) check_deptnum = c_hm_deptnum.substring(2, 4);
      else check_deptnum = "98";
      
      //logger.info("check_divnum >>> : " + check_divnum);
      //logger.info("check_deptnum >>> : " + check_deptnum);
   
      //?????? ??? ???????????????????
      if(findIndex == null) nvo =  new NoticeVO();
   
      //admin????????? ????????? ????????????
      if(hm_empnum.equals("H000000000000")) {
         nvo.setFindIndex("admin");
         //logger.info("FindIndex == " + nvo.getFindIndex());
      }else{
         nvo.setCheck_divnum(check_divnum);
         nvo.setCheck_deptnum(check_deptnum);
         
      }
      
      //logger.info("???????????? ????????? ?????? ?????? ????????????  >>> : " + nvo.getCheck_divnum() +  " " + nvo.getCheck_deptnum());
      
       //logger.info("cPage >>>> " + cPage);
         
       Paging.setPage(nvo,cPage,pageCtrl);//???????????? ????????? Paging???????????? ???????????????
       
      //System.out.println("null...>>> : " + nvo.getCurPage() + " : " + nvo.getPageSize());
      
      List<NoticeVO> noticeSelectList = null;
      noticeSelectList = boardService.selectNotice(nvo);
      
         if(noticeSelectList.size() != 0){
             totalCnt=noticeSelectList.get(0).getTotalCount();//?????? ????????? ???????????? 0??? ???????????? ?????? totalCount?????? ????????? 
             nvo.setTotalCount(totalCnt);//vo??? ???????????????.
          }
         nvo.setN_hm_empnum(hm_empnum);
          
      
      model.addAttribute("noticeList", noticeSelectList);
      model.addAttribute("writerQualified", writerQualified);
       model.addAttribute("i_nvo",nvo);
      
      //logger.info("(log)BoardController.listNotice ??????");
      return "board/selectNotice";
   }
   
   
   //???????????? ????????? ??????
   @RequestMapping("/insertNotice")
   public String insertNotice(@ModelAttribute NoticeVO nvo, MemberVO mvo, 
                                       Model model, HttpServletRequest request){
      //logger.info("(log)BoardController.insertNotice ?????? >>> ");
      //logger.info("request.getContentType() >>> : " + request.getContentType());
      //logger.info("(log)BoardController.chaebunNotice ?????? >>> ");

      String bn_num = "";
      List<NoticeVO> list = null;
      NoticeVO nvo_Bn_num = null;
      
      list = boardService.chaebunNotice();
      nvo_Bn_num = list.get(0);
      bn_num = nvo_Bn_num.getBn_num();
      bn_num = ChaebunUtils.cNum2(bn_num, NOTICE_GUBUN);
      //logger.info(" bn_num : " + bn_num);

      String url = "";
      
      FileUploadUtil fuu = new FileUploadUtil();
      boolean bFlag = false;
      bFlag = fuu.fileUpload(request, FILEPATH1);
      //logger.info("bFlag >>> : " + bFlag );
      if(bFlag){
         
         Enumeration<String> en = fuu.getFileNames();
         
         String firstFile =  en.nextElement();
         String secondFile =  en.nextElement();
         
         //logger.info("firstFile >>> : " + firstFile);
         //logger.info("secondFile >>> : " + secondFile);

         
         if(secondFile == null && firstFile != null){
            
            nvo.setBn_image(DOWNLOADPATH2 + firstFile);
            nvo.setBn_file("");
            //logger.info("1.????????? >>> : " +  nvo.getBn_file());
            //logger.info("1.???????????? >>> : " +  nvo.getBn_image());
      
         }else if(firstFile == null && secondFile != null){
            nvo.setBn_file(secondFile);
            nvo.setBn_image("");
            //logger.info("2.????????? >>> : " +  nvo.getBn_file());
            //logger.info("2.???????????? >>> : " +  nvo.getBn_image());
      
         }else if(firstFile == null && secondFile == null){
            nvo.setBn_file("");
            nvo.setBn_image("");
            //logger.info("3.????????? >>> : " +  nvo.getBn_file());
            //logger.info("3.???????????? >>> : " +  nvo.getBn_image());
         
         }else{
            nvo.setBn_file(secondFile);
            nvo.setBn_image(DOWNLOADPATH2 + firstFile);
            //logger.info("4.????????? >>> : " +  nvo.getBn_file());
            //logger.info("4.???????????? >>> : " +  nvo.getBn_image());
         }

         String bn_file = nvo.getBn_file();
         String bn_image = nvo.getBn_image();
   
         //logger.info("???????????? >>>> " + bn_file);
         //logger.info("??????????????? >>>> " + bn_image);
         
         String bn_title = fuu.getParameter("bn_title");
         String hm_name = fuu.getParameter("hm_name");
         String hm_empnum = fuu.getParameter("hm_empnum");
         
         String bn_content = fuu.getParameter("bn_content");
         String hm_duty = fuu.getParameter("hm_duty");
         
         String bn_divnum   = fuu.getParameter("bn_divnum");
         String bn_deptnum = fuu.getParameter("bn_deptnum");
         
         //logger.info(hm_empnum + "" + bn_num + "" + bn_title + "" + hm_name + "" +bn_content + "" + bn_deptnum +"" + hm_duty);
         
         nvo.setHm_empnum(hm_empnum);
         nvo.setBn_num(bn_num);
         nvo.setBn_title(bn_title);
         nvo.setHm_name(hm_name);
         nvo.setBn_content(bn_content);
         nvo.setBn_image(bn_image);
         nvo.setBn_file(bn_file);
         nvo.setBn_deptnum(bn_deptnum);
         
         if(bn_deptnum == null) nvo.setBn_divnum("");
         else nvo.setBn_divnum(bn_divnum);
         
         nvo.setHm_duty(hm_duty);
         
         model.addAttribute("hm_empnum",hm_empnum);
         
         VOPrintUtil.noticeVOPrint(nvo);
      
      }else{
         //logger.info("multipart ?????? ??????");
         url = "/board/writeNotice.td";
         return "redirect:" + url;
      }
      
      int result = boardService.insertNotice(nvo);
   
      //System.out.println("iFlag >>> " + result );
      if(result > 0){
         //logger.info("??? ?????? ??????!");
         
         url = "/board/selectNotice.td";
      }else{ 
         //logger.info("??? ?????? ?????? ?????? !"); 
      }


      //logger.info("(log)BoardController.insertNotice ?????? ");
      return "redirect:" + url;
   }
   
   //???????????? ????????? ????????? ????????????
   @RequestMapping(value="/moveWriteNotice")
   public String moveWriteNotice(@ModelAttribute MemberVO mvo, Model model){
      //logger.info("(log)BoardController.moveWriteNotice ?????? >>> ");
      List<MemberVO> mList = null;
      mList = boardService.selectWrite(mvo);

      model.addAttribute("mList", mList);


      return "board/insertNotice";
   }
   
   //???????????? ????????? ????????????
   @RequestMapping(value="/searchNotice")
   public String searchNotice(@ModelAttribute NoticeVO nvo, Model model){
      //logger.info("(log)BoardController.searchNotice ?????? >>> ");
      
      List<NoticeVO> noticeSearchList = null;
      String url = null;
      String bn_num = nvo.getBn_num();
      //logger.info("bn_num >>> : " + bn_num);
      noticeSearchList =    boardService.searchNotice(nvo);
      int iFlag = boardService.updateNoticeHit(nvo);
      //logger.info("(log)BoardController.searchNotice ????????? ??????>>> " + iFlag);
      
      VOPrintUtil.noticeVOPrint(nvo);
      
      model.addAttribute("noticeSearchList",noticeSearchList);
      
      if(noticeSearchList.size() == 1){
         url = "/board/searchNotice";
      }else{
         url = "redirect:" + "/board/selectNotice.td";
      }
      
      return  url;
   }

   //?????????????????? ??????
   @RequestMapping(value="/downloadNotice")
   public String downloadNotice(@ModelAttribute NoticeVO nvo, Model model){
      //logger.info("(log)BoardController.downloadNotice ?????? >>> ");

      String bn_file = nvo.getBn_file();
      
      //logger.info("bn_file >>> " + bn_file);
      
      model.addAttribute("fileName" ,bn_file);
      model.addAttribute("FilePath", FILEPATH1 );
      
      //logger.info("(log)BoardController.downloadNotice ?????? >>> ");
      return "board/fileDownload";
   }

   //???????????? ????????? ?????????????????? ??????
   @RequestMapping(value="/moveUpdateNotice")
   public String moveUpdateNotice(@ModelAttribute NoticeVO nvo, Model model){
      //logger.info("(log)BoardController.updateSearchNotice ?????? >>> ");
      
      List<NoticeVO> updateList = null;
      String url = null;
      String bn_num = nvo.getBn_num();
      //logger.info("bn_num >>> : " + bn_num);
      updateList = boardService.searchNotice(nvo);
      
      model.addAttribute("updateList",updateList);
      
      
      if(updateList.size() == 1){
         url = "board/updateNotice";
      }else{
         url = "redirect:" + "/board/selectNotice.td";
      }
      
      //logger.info("(log)BoardController.updateSearchNotice ?????? >>> ");
      return  url;
   }
   
   //???????????? ????????? ????????????
      @RequestMapping(value="/updateNotice", method=RequestMethod.POST)
      public String updateNotice(@ModelAttribute NoticeVO nvo, Model model, HttpServletRequest request){
         //logger.info("(log)BoardController.updateNotice ?????? >>> ");
         int result = 0;
         
         FileUploadUtil fuu = new FileUploadUtil();
         boolean bFlag = false;
         bFlag = fuu.fileUpload(request, FILEPATH1);
         
         //logger.info("bFlag >>> : " + bFlag );
         
         if(bFlag){
            
            Enumeration<String> en = fuu.getFileNames();
            
            String firstFile =  en.nextElement();
            String secondFile =  en.nextElement();
            
            //logger.info("firstFile >>> : " + firstFile);
            //logger.info("secondFile >>> : " + secondFile);
            
            if(firstFile != null){
               firstFile = firstFile.replaceAll(DOWNLOADPATH1, "");
            }

            
            if(secondFile == null && firstFile != null){
               
               nvo.setBn_image(DOWNLOADPATH1 + firstFile);
               nvo.setBn_file("");
               //logger.info("1.????????? >>> : " +  nvo.getBn_file());
               //logger.info("1.???????????? >>> : " +  nvo.getBn_image());
         
            }else if(firstFile == null && secondFile != null){
               nvo.setBn_file(secondFile);
               nvo.setBn_image("");
               //logger.info("2.????????? >>> : " +  nvo.getBn_file());
               //logger.info("2.???????????? >>> : " +  nvo.getBn_image());
         
            }else if(firstFile == null && secondFile == null){
               nvo.setBn_file("");
               nvo.setBn_image("");
               //logger.info("3.????????? >>> : " +  nvo.getBn_file());
               //logger.info("3.???????????? >>> : " +  nvo.getBn_image());
            
            }else{
               nvo.setBn_file(secondFile);
               nvo.setBn_image(DOWNLOADPATH1 + firstFile);
               //logger.info("4.????????? >>> : " +  nvo.getBn_file());
               //logger.info("4.???????????? >>> : " +  nvo.getBn_image());
            }

            String bn_file = nvo.getBn_file();
            String bn_image = nvo.getBn_image();
      
            //logger.info("???????????? >>>> " + bn_file);
            //logger.info("??????????????? >>>> " + bn_image);
            
            String bn_title = fuu.getParameter("bn_title");
            String bn_content = fuu.getParameter("bn_content");
            String bn_num = fuu.getParameter("bn_num");
            
            //logger.info(bn_num + "" + bn_title + "" +bn_content + "");
            
            nvo.setBn_num(bn_num);
            nvo.setBn_title(bn_title);
            nvo.setBn_content(bn_content);
            nvo.setBn_image(bn_image);
            nvo.setBn_file(bn_file);
         

            VOPrintUtil.noticeVOPrint(nvo);
         
         }else{
            //logger.info("multipart ?????? ??????");
         }
      
         result = boardService.updateNotice(nvo);
         
         if(result == 1){
            //logger.info("????????????");
         }else{
            //logger.info("????????????");
         }
         
         String bn_num = nvo.getBn_num();
         
         return "redirect:" + "/board/searchNotice.td?bn_num=" + bn_num;
      }

      
   //???????????? ????????? ????????????
   @RequestMapping(value="/deleteNotice")
   public String deleteNotice(@ModelAttribute NoticeVO nvo, Model model){
      
      int result = 0;
      
      String hm_empnum = nvo.getHm_empnum();
      
      result = boardService.deleteNotice(nvo);
      
      if(result == 1){
         //logger.info("????????????");
      }else{
         //logger.info("????????????");
      }
      
      model.addAttribute("hm_empnum",hm_empnum);
      
      return "redirect:" + "/board/selectNotice.td";
   }

   
   //???????????? ?????? ????????? ????????????
   @RequestMapping(value="/moveCheckNotice")
   public String moveCheckNotice(@ModelAttribute MemberVO mvo, NoticeVO nvo, Model model){
      //logger.info("(log)BoardController.moveWriteNotice ?????? >>> ");
      List<MemberVO> mList = null;
      mList = boardService.selectWrite(mvo);
      
      List<NoticeVO> checkList = null;
      String bn_num = nvo.getBn_num();
      //logger.info("bn_num >>> : " + bn_num);
      
      model.addAttribute("mList", mList);
      model.addAttribute("bn_num",bn_num);
      
   
      
      return "board/checkNotice";
   }
   
   
   //???????????? ????????? ??????
   @RequestMapping("/checkNotice")
   public String checkNotice(@ModelAttribute NoCheckVO ncvo, Model model){
      //logger.info("(log)BoardController.checkNotice ?????? >>> ");
      //logger.info("(log)BoardController.chaebunNoCheck ?????? >>> ");

      
      
      String bn_checknum = "";
      String message = "";
      List<NoCheckVO> list = null;
      
      list = boardService.chaebunNoCheck();
      
      NoCheckVO _ncvo = list.get(0);
      bn_checknum = _ncvo.getBn_checknum();
      bn_checknum = ChaebunUtils.cNum(bn_checknum, CHECK_GUBUN);
      //logger.info(" bn_checknum >>> : " + bn_checknum);

      String bn_num   = ncvo.getBn_num();
      String hm_empnum = ncvo.getHm_empnum();
      String hm_name   = ncvo.getHm_name();
      String hm_deptnum = ncvo.getHm_deptnum();
      
      ncvo.setBn_num(bn_num);
      ncvo.setBn_checknum(bn_checknum);
      ncvo.setHm_deptnum(hm_deptnum);
      ncvo.setHm_name(hm_name);
      ncvo.setHm_empnum(hm_empnum);
      
      VOPrintUtil.noCheckVOPrint(ncvo);
      
      int iFlag = boardService.checkNotice(ncvo);
      
      if(iFlag == 1){
         
         message = hm_name + "??? ?????? ???????????? ?????? ??????";
         
      }else if(iFlag == 0 ){
         message = hm_name + "?????? ?????? ??????????????? ?????? ?????????????????????.";
      }else{
         message = "???????????? ?????? ?????? ??????";
      }
   
      model.addAttribute("message",message);
      //System.out.println("iFlag >>> " + iFlag );

      //logger.info("(log)BoardController.insertNotice ?????? ");
      
      return "board/checkNotice";
   }
   
   //???????????? ????????? ????????? ????????????
   @RequestMapping(value="/checkList")
   public String checkList(@ModelAttribute NoCheckVO ncvo, Model model){
      //logger.info("(log)BoardController.moveCheckList ??????  >>>");
      
      //System.out.println(ncvo.getBn_num());
      
      List<NoCheckVO> cList = null;
      cList = boardService.checkList(ncvo);
      
      model.addAttribute("cList", cList);
      
      //logger.info("(log)BoardController.moveCheckList ??????  >>>");
      return "board/checkListNotice";
   }
   
}//end of class