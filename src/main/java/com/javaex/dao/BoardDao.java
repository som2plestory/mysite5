package com.javaex.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.javaex.vo.BoardVo;

@Repository
public class BoardDao {

	@Autowired
	private SqlSession sqlSession;
	
	
	// 게시판 리스트 (+ 제목 검색)
	public List<BoardVo> boardList(String keyword){
		System.out.println("BoardDao > boardList()");
		
		List<BoardVo> boardList = sqlSession.selectList("board.boardList", keyword);
		
		return boardList;
	}
	
	// 게시글 정보(글 한 개 읽기)
	public BoardVo getBoard(int no){
		System.out.println("BoardDao > getBoard()");
		
		BoardVo boardVo = sqlSession.selectOne("board.getBoard", no);
		
		return boardVo;
	}
	
	// 다른 사람의 글을 읽을 때: 조회수 1 상승
	public void hitUp(int no) {
		System.out.println("BoardDao > hitUp()");
	
		sqlSession.insert("board.hitUp", no);
	}
	
	// 게시글 삭제
	public int boardDelete(BoardVo boardVo) {
		System.out.println("BoardDao > boardDelete()");
		
		int count = sqlSession.delete("board.boardDelete", boardVo);
		
		return count;
	}
	
	// 게시글 작성
	public void boardWrite(BoardVo boardVo) {
		System.out.println("BoardDao > boardWrite()");
		
		sqlSession.insert("board.boardWrite", boardVo);
	}
	
	// 게시글 수정폼
	public BoardVo modifyForm(BoardVo writeVo) {
		System.out.println("BoardDao > modifyForm()");	
		
		BoardVo boardVo = sqlSession.selectOne("board.modifyForm", writeVo);
		
		return boardVo;
	}
	
	// 게시글 수정
	public int boardModify(BoardVo writeVo) {
		System.out.println("BoardDao > boardModify()");
		
		int count = sqlSession.update("board.boardModify", writeVo);
		
		return count;
	}
}
