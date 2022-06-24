package com.javaex.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.dao.BoardDao;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@Repository
public class BoardService {

	@Autowired
	private BoardDao boardDao;
	
	
	/**************************************** 게시판 리스트(+ 제목 검색) *****************************************/
	public List<BoardVo> boardList(String keyword){
		System.out.println("BoardService > BoardList()");
		
		List<BoardVo> boardList = boardDao.boardList(keyword);
		
		return boardList;
	}
	
	
	/******************************************** 게시글 읽기(1개) *********************************************/
	public BoardVo read(int no, UserVo authUser){
		System.out.println("BoardService > read()");
		
		
		//no 글의 정보 불러오기
		BoardVo boardVo = boardDao.getBoard(no);
		
		//잘못된 주소 접근 : no가 없거나 해당하는 게시글이 존재하지 않음
		if(boardVo == null) {
			System.out.println("잘못된 요청: 게시글이 존재하지 않음");
		
		//번호에 해당하는 게시글 존재	
		}else {
			System.out.println("게시글 불러오기");
			
			//조회수
			//다른 사람의 글을 읽을 때 : 로그인 상태가 아니거나 글쓴이와 로그인한 유저가 다를 때 
			if(authUser == null ||  authUser.getNo() != boardVo.getUserNo()) {
				System.out.println("게시글 조회수 상승");
				boardDao.hitUp(no);
				
				//올라간 조회수로 다시 세팅해줌
				boardVo.setHit(boardVo.getHit()+1); 
	
			//내 글을 읽으면 조회수 그대로
			}else {
				System.out.println("게시글 조회수 그대로");
			}
		}
		
		return boardVo;
	}
	
	
	/********************************************** 게시글 삭제 ***********************************************/
	public void boardDelete(BoardVo boardVo) {
		System.out.println("BoardService > boardDelete()");
		
		int count = boardDao.boardDelete(boardVo);
		
		//주소로 다른 사람의 게시글 삭제에 접근했을 때
		if(count != 1) {
			System.out.println("게시글 삭제 불가: 접근한 게시글이 회원정보와 맞지 않습니다");
		
		//로그인한 사람이 작성한 글이 맞을 때
		}else {
			System.out.println("게시글 삭제 성공");
			
		}
	}
	
	
	/********************************************** 게시글 작성 ***********************************************/
	public boolean boardWrite(BoardVo boardVo) {
		System.out.println("BoardService > boardWrite()");
		
		if(boardVo.getTitle().equals("") || boardVo.getContent().equals("")) {
			System.out.println("게시글 작성 실패: 제목 또는 내용 미입력");
			
			return false;
			
		}else {
			System.out.println("게시글 작성 성공");
			
			boardDao.boardWrite(boardVo);

			return true;
		}
		
	}
	

	/********************************************* 게시글 수정폼 **********************************************/
	public BoardVo modifyForm(BoardVo writeVo) {
		System.out.println("BoardService > ModifyForm()");
		
		//no 글의 정보 불러오기
		BoardVo boardVo = boardDao.modifyForm(writeVo);
		
		return boardVo;
	}
	
	
	/********************************************** 게시글 작성 ***********************************************/
	public BoardVo boardModify(BoardVo writeVo) {
		System.out.println("BoardService > boardModify()");
		
		BoardVo boardVo = null;
		
		// 제목 또는 내용을 작성하지 않은 경우
		if (writeVo.getTitle().equals("") || writeVo.getContent().equals("")) {
			System.out.println("게시글 수정 실패: 제목 또는 내용 미입력");
			
			// 다시 수정폼 정보 불러오기
			// 작성자가 맞지 않으면 불러오지 않음
			boardVo = boardDao.modifyForm(writeVo);
		
		// 제목/내용 모두 작성 
		}else {
			
			int count = boardDao.boardModify(writeVo);
			
			//작성자가 맞지않으면 업데이트 안됨
			if(count != 1) {
				System.out.println("게시글 수정 불가: 접근한 게시글이 회원정보와 맞지 않습니다");

			// 제목/내용 모두 작성 > 게시글 수정	
			}else {
				System.out.println("게시글 수정 성공");
			}
			
		}
		
		return boardVo;
	}
}
