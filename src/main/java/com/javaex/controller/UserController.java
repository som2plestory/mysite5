package com.javaex.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.javaex.service.UserService;
import com.javaex.vo.UserVo;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	
	
	/***************************************** 회원가입폼 ******************************************/
	@RequestMapping(value = "/user/joinForm")
	public String joinForm() {
		System.out.println("UserController > joinForm()");
		
		return "user/joinForm";
	}
	
	
	/****************************************** 회원가입 *******************************************/
	@RequestMapping(value = "/user/join", method = { RequestMethod.GET, RequestMethod.POST})
	public String join(Model model, @ModelAttribute UserVo userVo) {
		System.out.println("UserContoller > join()");
		
		//약관동의
		
		int count = userService.userInsert(userVo);
		
		//회원가입 성공
		if(count == 1) {
			
			return "redirect:/user/joinOk";
		
		}else {
			model.addAttribute("userVo", userVo);
			
			//아이디 중복 
			if(count == -1) {
				model.addAttribute("idCheck", "Fail");
			}
			
			return "user/joinForm";
		}
	}
	
	
	/*************************************** 회원가입 성공폼 ****************************************/
	@RequestMapping(value = "/user/joinOk") public String joinOk() {
		System.out.println("UserController > joinOk()");

		return "/user/joinOk"; 
	}
	
	
	/****************************************** 로그인폼 *******************************************/
	@RequestMapping(value = "/user/loginForm")
	public String loginForm() {
		System.out.println("UserController > loginForm()");
		
		return "user/loginForm";
	}
	
	
	/******************************************* 로그인 ********************************************/
	@RequestMapping(value = "/user/login", method = { RequestMethod.GET, RequestMethod.POST })
	public String login(HttpSession session, UserVo userVo) {
		System.out.println("UserController > login()");

		UserVo authUser = userService.loginUser(userVo);
		
		if(authUser != null) {
			System.out.println("로그인 성공");
			session.setAttribute("authUser", authUser);

			return "redirect:/main";
			
		}else {
			System.out.println("로그인 실패");
			
			return "redirect:/user/loginForm?result=fail";
		}
		
	}
	
	
	/****************************************** 로그아웃 *******************************************/
	@RequestMapping(value = "/user/logout")
	public String logout(HttpSession session) {
		System.out.println("UserController > login()");
		
		session.removeAttribute("authUser");
		session.invalidate();
		
		return "redirect:/main";
	}
	
	
	/*************************************** 회원 정보 수정폼 ****************************************/
	@RequestMapping(value = "/user/modifyForm")
	public String modifyForm(Model model, HttpSession session) {
		System.out.println("UserController > modifyForm()");
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		//세션에서 불러온 정보가 없을 때
		if(authUser == null) {
			System.out.println("회원정보 수정불가: 로그인이 되어있지않음");
			
			return "redirect:/user/loginForm";
			
		//로그인이 되어있을 때
		}else {
			model.addAttribute("userVo", userService.getUser(authUser.getNo()));
			
			return "user/modifyForm";
		}
		
	}
	
	
	/**************************************** 회원 정보 수정 *****************************************/
	@RequestMapping(value = "/user/modify")
	public String modify(Model model, HttpSession session, @ModelAttribute UserVo userVo) {
		System.out.println("UserController > modify()");
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		//로그인이 풀리면 or 주소로 타인의 아이디에 접근할 때
		if(authUser == null) {
			System.out.println("회원정보 수정불가: 로그인 확인");
			
			return "redirect:/user/loginForm";
			
		//로그인 상태
		}else {
			userVo.setNo(authUser.getNo());
			
			userVo = userService.userUpdate(userVo);	
			
			//회원정보 수정 실패
			if(userVo.getId() == null) {
				return "redirect:/user/modifyForm?result=fail";
			
			//회원정보 수정 성공
			}else {
				session.setAttribute("authUser", userVo);
				
				return "redirect:/user/modifyOk";
			}
		}
	}
	
	
	// 회원 정보 수정 성공폼
	@RequestMapping(value = "/user/modifyOk")
	public String modifyOk() {
		System.out.println("UserController > modifyOk()");
		
		return "user/modifyOk";
	}
}
