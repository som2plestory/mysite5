package com.javaex.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaex.service.BoardService;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@Controller
public class BoardController {

	@Autowired
	private BoardService boardService;

	// 게시판 리스트(+ 제목 검색)
	@RequestMapping(value = "/board/list", method = { RequestMethod.GET, RequestMethod.POST })
	public String list(Model model, @RequestParam(name = "keyword", defaultValue = "") String keyword) {
		System.out.println("BoardController > list()");

		List<BoardVo> boardList = boardService.boardList(keyword);
		model.addAttribute("boardList", boardList);

		return "board/list";
	}

	// 게시글 읽기(1개)
	@RequestMapping(value = "/board/read", method = { RequestMethod.GET, RequestMethod.POST })
	public String read(Model model, HttpSession session, @RequestParam(name = "no", defaultValue = "-1") int no) {
		System.out.println("BoardController > read()");

		// no : 글 번호
		UserVo authUser = (UserVo) session.getAttribute("authUser");
		BoardVo boardVo = boardService.read(no, authUser);

		// 번호에 해당하는 글이 없다? (그 사이에 삭제 됐을 수도 있다 or 잘못된 주소 접근)
		if (boardVo == null) {
			System.out.println("글 불러오기 실패: 해당하는 글이 존재하지 않음");

			return "redirect:/board/list";

		// 번호에 해당하는 글이 있다.
		} else {
			// model에 boardVo 정보 저장
			model.addAttribute("boardVo", boardVo);

			return "board/read";
		}
	}

	// 게시글 삭제
	@RequestMapping(value = "/board/delete", method = { RequestMethod.GET, RequestMethod.POST })
	public String delete(HttpSession session, @RequestParam(name="no", defaultValue = "-1") int no) {
		System.out.println("BoardController > delete()");

		UserVo authUser = (UserVo) session.getAttribute("authUser");

		// 로그인 x : 페이지에 오래 머물러서 도중에 세션만료로 로그인이 풀렸을 수도 있잖아
		if (authUser == null) {
			System.out.println("삭제 실패 : 로그인 필요");
			
			return "redirect:user/loginForm";
			
		// 로그인 o :
		}else {
			BoardVo boardVo = new BoardVo();
			boardVo.setNo(no);
			boardVo.setUserNo(authUser.getNo());

			// 로그인한 사용자(userNo)가 작성한 글(no)이 맞을 때 삭제
			boardService.boardDelete(boardVo);
			
			return "redirect:/board/list";
		}
	}

	// 게시글 작성폼
	@RequestMapping(value = "/board/writeForm")
	public String writeForm(HttpSession session) {
		System.out.println("BoardController > writeForm()");

		UserVo authUser = (UserVo) session.getAttribute("authUser");

		// 주소 접근 : 로그인이 상태가 아닐 때
		if (authUser == null) {
			System.out.println("접근 불가: 로그인 상태가 아님");

			return "redirect:/user/loginForm";

		} else {
			return "board/writeForm";
		}
	}

	// 게시글 작성
	@RequestMapping(value = "/board/write", method = { RequestMethod.GET, RequestMethod.POST })
	public String write(Model model, HttpSession session, BoardVo boardVo) {
		System.out.println("BoardController > write()");

		UserVo authUser = (UserVo) session.getAttribute("authUser");

		// 로그인이 상태가 아닐 때(로그인이 풀린 경우)
		if (authUser == null) {
			System.out.println("접근 불가: 로그인 상태가 아님");

			return "redirect:/user/loginForm";

		// 로그인은 되어있어
		}else {
			
			boardVo.setUserNo(authUser.getNo());
			boolean writeOk = boardService.boardWrite(boardVo); 
			
			// 작성실패 : 제목 또는 내용을 작성하지 않은 경우
			if (!writeOk) {
				model.addAttribute("boardVo", boardVo);
				// 받은 그대로 boardVo에 저장해서 보냄
				model.addAttribute("write", "Fail");
	
				return "board/writeForm";

			// 제목/내용 모두 작성 > 게시글 작성
			}else {
			
				return "redirect:/board/list";
			}
		}
	}

	// 게시글 수정폼
	@RequestMapping(value = "/board/modifyForm", method = { RequestMethod.GET, RequestMethod.POST })
	public String modifyForm(Model model, HttpSession session, @RequestParam(defaultValue = "-1") int no) {
		System.out.println("BoardController > modifyForm()");

		UserVo authUser = (UserVo) session.getAttribute("authUser");

		// 주소 접근: 로그인이 되어있지 않을 때
		if (authUser == null) {
			System.out.println("게시글 수정 불가: 로그인 상태 확인");

			return "redirect:/user/loginForm";
		
		// 로그인 o
		} else {
			
			BoardVo writeVo = new BoardVo();
			writeVo.setNo(no);
			writeVo.setUserNo(authUser.getNo());
			
			BoardVo boardVo = boardService.modifyForm(writeVo);
			
			// 주소로 다른 사람 글 수정폼 접근
			if(boardVo == null) {
				System.out.println("게시글 수정 불가: 작성자 불일치");
			
				return "redirect:/board/list";
				
			// 글 작성자 일치
			}else {
				System.out.println("게시글 수정 가능: 수정폼 이동");
				
				model.addAttribute("boardVo", boardVo);
				
				return "board/modifyForm";
			}
		}
	}

	// 게시글 수정
	@RequestMapping(value = "board/modify", method = { RequestMethod.GET, RequestMethod.POST })
	public String modify(Model model, HttpSession session, BoardVo writeVo) {
		System.out.println("BoardController > modify()");

		UserVo authUser = (UserVo) session.getAttribute("authUser");

		// 주소 접근: 로그인이 되어있지 않을 때
		if (authUser == null) {
			System.out.println("게시글 수정 불가: 로그인 상태 확인");

			return "user/loginForm";
		
		// 로그인 o
		} else {
			
			// writeVo.getUserNo() = authUser.no 로 화면에서 받음
			BoardVo boardVo = boardService.boardModify(writeVo);
			
			// 작성자 = 로그인 회원 and 제목 또는 내용을 작성하지 않은 경우
			if(boardVo != null){
				
				model.addAttribute("boardVo", boardVo);
				model.addAttribute("writeVo", writeVo);
				model.addAttribute("modify", "Fail");
	
				return "board/modifyForm";
				
			// 제목/내용 모두 작성 > 게시글 수정 성공
			// 수정 불가 : 회원정보 불일치
			}else {
				
				return "redirect:/board/list";
			}
		}
	}
	
}
