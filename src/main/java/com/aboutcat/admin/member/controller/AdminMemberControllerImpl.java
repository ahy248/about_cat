package com.aboutcat.admin.member.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.aboutcat.admin.member.service.AdminMemberService;
import com.aboutcat.common.base.BaseController;
import com.aboutcat.member.vo.MemberVO;

@Controller
@RequestMapping("/admin/member")
public class AdminMemberControllerImpl extends BaseController implements AdminMemberController {
	@Autowired
	AdminMemberService adminMemberService;

	@Override
	@RequestMapping(value = "/adminMemberMain.do")
	public String adminMemberMain(@RequestParam Map<String, String> dateMap, HttpServletRequest request,
			HttpServletResponse response, Model model) {
//		HttpSession session = request.getSession();
//		String uid = (String) session.getAttribute("user_id");
//		if (!uid.equals("admin")) {
//			request.setAttribute("alert_notAdmin", "������ ������ �����ϴ�.");
//			// ���������� header���� alert_notAdmin�� null�� �ƴѰ�� �ش� ������ alert���� �߰Բ� �ڵ��ϴ°� �����ϰ� § �ڵ�.
//			return "main/main.do";
//		}

//		session.setAttribute("menu_mode", "admin_mode");
		String section = dateMap.get("section");
		String pageNum = dateMap.get("pageNum");
		// �������� ����¡�� �°� ������� �̾ƿ��� ���� �ѱ� ��

		String searchPeriod = dateMap.get("searchPeriod");
		String beginDate = null, endDate = null;

		String tempDate[] = calcSearchPeriod(searchPeriod).split(",");
		beginDate = tempDate[0];
		endDate = tempDate[1];

		dateMap.put("beginDate", beginDate);
		dateMap.put("endDate", endDate);

		HashMap<String, Object> condMap = new HashMap<String, Object>();

		if (section == null) {
			section = "1";
		}
		condMap.put("section", section);

		if (pageNum == null) {
			pageNum = "1";
		}
		condMap.put("pageNum", pageNum);
		// ���ʿ�û�� = Ŭ���̾�Ʈ���� 1�������� ��
		
		condMap.put("beginDate", beginDate);
		condMap.put("endDate", endDate);
		//��䵥��Ʈ : 2023-07-23(����Ʈ4������) ���嵥��Ʈ : 2023-11-23(����)
		String tempBeginDate[] = beginDate.split("-");
		String tempEndDate[] = endDate.split("-");

		model.addAttribute("beginYear",tempBeginDate[0]);
		model.addAttribute("beginMonth",tempBeginDate[1]);
		model.addAttribute("beginDay",tempBeginDate[2]);
		model.addAttribute("endYear",tempEndDate[0]);
		model.addAttribute("endMonth",tempEndDate[1]);
		model.addAttribute("endDay",tempEndDate[2]);
		
		ArrayList<MemberVO> membersList = adminMemberService.listMember(condMap);
		
		System.out.println(membersList);
		
		model.addAttribute("section",section);
		model.addAttribute("pageNum",pageNum);
		model.addAttribute("membersList", membersList);

		
		return "/admin/member/adminMemberMain";
	}

	@Override
	@RequestMapping(value = "/memberDetail.do")
	public String memberDetail(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		String viewName = (String)request.getAttribute("viewName");
		String member_id = request.getParameter("member_id");
		MemberVO member_info = adminMemberService.memberDetail(member_id);
		model.addAttribute("member_info",member_info);
		System.out.println(member_info);
		return viewName;
	}

	@Override
	@RequestMapping(value="/modifyMemberInfo.do", method={RequestMethod.POST,RequestMethod.GET})
	public void modifyMemberInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String,String> memberMap=new HashMap<String,String>();
		String val[]=null;
		PrintWriter pw=response.getWriter();
		String member_id=request.getParameter("member_id");
		String mod_type=request.getParameter("mod_type");
		String value =request.getParameter("value");
		if(mod_type.equals("member_birth")){
			val=value.split(",");
			memberMap.put("birth_year",val[0]);
			memberMap.put("birth_month",val[1]);
			memberMap.put("birth_day",val[2]);
			memberMap.put("birth_day_yinyang",val[3]);
		}else if(mod_type.equals("hp")){
			val=value.split(",");
			memberMap.put("phone",val[0]);
			memberMap.put("smssts_yn", val[1]);
		}else if(mod_type.equals("email")){
			val=value.split(",");
			memberMap.put("email1",val[0]);
			memberMap.put("email2",val[1]);
			memberMap.put("email_valid_check", val[2]);
		}else if(mod_type.equals("address")){
			val=value.split(",");
			memberMap.put("postcode",val[0]);
			memberMap.put("address1_new",val[1]);
			memberMap.put("address1_old", val[2]);
			memberMap.put("address2", val[3]);
		}
		
		memberMap.put("member_id", member_id);
		
//		adminMemberService.modifyMemberInfo(memberMap);
		pw.print("mod_success");
		pw.close();	
	}

	@Override
	public void deleteMember(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		
	}
	
	

}
