package com.kakaruto.blog.persistence;

import java.util.List;
import com.kakaruto.blog.vo.BoardFileVO;
import com.kakaruto.blog.vo.BoardVO;

public interface Board {
	List<BoardVO> getBoardList(BoardVO boardVO);
	BoardVO getBoardDetail(BoardVO boardVO);
	void insertBoard(BoardVO boardVO);
	void updateHitCount(BoardVO boardVO);
	int getBoardLastInsertSeq();
	int insertBoardFile(BoardFileVO boardFileVO);
	List<BoardFileVO> getBoardFileList(BoardVO boardFileVO);
}
