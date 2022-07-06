package com.javaex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaex.dao.GuestbookDao;
import com.javaex.vo.GuestbookVo;

@Service
public class GuestbookService {

	@Autowired
	private GuestbookDao guestbookDao;
	

	/*********************** 방명록 저장(ajax) ************************/
	public GuestbookVo addGuest(GuestbookVo guestbookVo) {
		System.out.println("GuestbookService > addGuest()");
		
		//저장
		System.out.println("전-->" + guestbookVo);
		guestbookDao.insertGuest(guestbookVo);
		System.out.println("후-->" + guestbookVo);
		
		int no = guestbookVo.getNo();
		
		//방금 저장한 1개의 데이터를 가져온다
		GuestbookVo guestVo = guestbookDao.getGuest(no);
		
		return guestVo;
	}
	

	/**************************  방명록 삭제 ***************************/
	public String remove(GuestbookVo guestVo) {
		System.out.println("GuestbookService > remove()");
		String state;
		
		int count = guestbookDao.guestbookDelete(guestVo);
		
		if(count == 1) {
			state = "success";
		}else {
			state = "fail";
		}
		
		return state;
	}
	

	/************************** 방명록 리스트 ***************************/
	public List<GuestbookVo> getGuestList() {
		System.out.println("GuestbookService > getGuestList()");
		
		List<GuestbookVo> guestList = guestbookDao.getGuestList();

		return guestList;
	}

	
	/*************************** 방명록 등록 ***************************/
	public void guestbookInsert(GuestbookVo guestbookVo) {
		System.out.println("GuestbookService > getGuestInsert()");
		
		guestbookDao.guestbookInsert(guestbookVo);
	}

	
	/********************** 방명록 삭제폼(1개 정보) **********************/
	public GuestbookVo getGuest(int no) {
		System.out.println("GuestbookService > getGuest()");
		
		GuestbookVo guestbookVo = guestbookDao.getGuest(no);
		
		return guestbookVo;
	}

	
	/*************************** 방명록 삭제 ***************************/
	public GuestbookVo guestbookDelete(GuestbookVo guestbookVo) {
		System.out.println("GuestbookService > guestbookDelete()");
		
		GuestbookVo guestVo = null;
		
		int count = guestbookDao.guestbookDelete(guestbookVo);
		
		//삭제 실패 : 비밀번호 입력 자체를 안했거나 입력했는데 틀렸다
		if(count != 1) {
			System.out.println("방명록 삭제 실패: 비밀번호 오류");
			
			//다시 삭제폼 불러오기
			guestVo = getGuest(guestbookVo.getNo());
			
		//비밀번호 일치 > 삭제 성공
		}else {
			System.out.println("방명록 삭제 성공");
		}
		
		return guestVo;
		
	}

	
	
	
	
	
	
}