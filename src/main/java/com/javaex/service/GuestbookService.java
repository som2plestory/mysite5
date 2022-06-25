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