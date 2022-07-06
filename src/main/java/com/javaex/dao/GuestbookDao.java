package com.javaex.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.GuestbookVo;

@Repository
public class GuestbookDao {

	@Autowired
	private SqlSession sqlSession;
	
	
	 /******************************** 방명록 저장(ajax) *********************************/
		public void insertGuest(GuestbookVo guestbookVo) {
			System.out.println("GuestbookDao > insertGuest()");
			
			sqlSession.insert("guestbook.insertSelectKey", guestbookVo);
		}
	
	
	/*********************************** 방명록 리스트 ************************************/
	public List<GuestbookVo> getGuestList() {
		System.out.println("GuestbookDao > getGuestList()");
		
		List<GuestbookVo> guestList = sqlSession.selectList("guestbook.selectList");
		
		return guestList;
	}

	
	/************************************ 방명록 등록 ************************************/
	public void guestbookInsert(GuestbookVo guestbookVo) {
		System.out.println("GuestbookDao > guestbookInsert()");
		
		sqlSession.insert("guestbook.guestbookInsert", guestbookVo);
	}

	
	/******************************* 방명록 삭제폼(1개 정보) *******************************/
	public GuestbookVo getGuest(int no) {
		System.out.println("GuestbookDao > getGuest()");
		
		GuestbookVo guestbookVo = sqlSession.selectOne("guestbook.getGuest", no);
		
		return guestbookVo;
	}
	

	/************************************ 방명록 삭제 ************************************/
	public int guestbookDelete(GuestbookVo guestbookVo) {
		System.out.println("GuestbookDao > guestbookDelete()");
		
		int count = sqlSession.delete("guestbook.guestbookDelete", guestbookVo);
		
		return count;
	}

	
}