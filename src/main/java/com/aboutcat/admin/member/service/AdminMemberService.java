package com.aboutcat.admin.member.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.aboutcat.member.vo.MemberVO;

public interface AdminMemberService {

	

	public ArrayList<MemberVO> listMember(HashMap<String, Object> condMap);

	public MemberVO memberDetail(String member_id);

}
