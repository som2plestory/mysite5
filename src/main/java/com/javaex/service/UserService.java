package com.javaex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javaex.dao.UserDao;
import com.javaex.vo.UserVo;

@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	/*
	// 아이디 중복 확인
	public boolean idCheck(String id) {
		System.out.println("UserService > idCheck");
		
		if(userDao.idCheck(id) == false) return false;
		else return true;
	}
	*/
	
	/************************************* 회원가입폼 ***************************************/
	public int userInsert(UserVo userVo) {
		System.out.println("UserService > userInsert()");
		
		System.out.println(userVo);
		String id = userVo.getId();
		boolean idOk = userDao.idCheck(id);
		
		int count;
		
		//아이디 중복
		if(idOk == false) {
			System.out.println("회원가입 실패: 아이디가 중복입니다.");
			
			count = -1;
					
		//입력한 아이디가 중복이 아니지만 
		//아이디/비밀번호/이름 중에 하나라도 입력하지 않았을 때	
		}else if(userVo.getId().equals("") 
				|| userVo.getPassword().equals("") 
				|| userVo.getName().equals("")) {
			System.out.println("회원가입 실패: 아이디/비밀번호/이름 중 한 가지 이상 입력되지 않았습니다.");
		
			count = 0;
			
		//아이디 미중복 /비밀번호/이름 모두 입력 > 회원가입
		}else {
			System.out.println("회원가입 성공");
			
			userDao.userInsert(userVo);
			count = 1;
		}
		
		return count;
	}
	
	
	/************************************* 로그인 정보 ***************************************/
	public UserVo loginUser(UserVo userVo) {
		System.out.println("UserService > loginUser()");
		
		UserVo loginUser = userDao.loginUser(userVo);
		
		return loginUser;
	}
	
	
	
	/************************************** 회원 정보 ****************************************/
	public UserVo getUser(int no) {
		System.out.println("UserService > getUser()");
		
		UserVo userVo = userDao.getUser(no);
		
		return userVo;
	}
	 
	/************************************ 회원 정보 수정**************************************/
	public UserVo userUpdate(UserVo userVo) {
		System.out.println("UserService > userUpdate");
		
		UserVo userVo1 = null;
		System.out.println(userVo);
		
		if(userVo.getGender() == null) {
			userVo.setGender("");
		}
		
		//비밀번호 또는 이름이 입력되지 않았을 때
		if(userVo.getPassword().equals("") || userVo.getName().equals("")) {
			System.out.println("회원정보 수정 실패: 비밀번호 또는 이름이 입력되지 않았습니다.");
			
			userVo1 = userDao.getUser(userVo.getNo());
		
		//모두 입력됨 > 회원정보 수정
		}else {
			System.out.println("회원정보 수정 성공");
			
			userDao.userUpdate(userVo);
			userVo1 = userDao.loginUser(userVo);
		}

		return userVo1;
	}
}
