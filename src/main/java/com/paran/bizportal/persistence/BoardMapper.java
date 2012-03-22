package com.paran.bizportal.persistence;

import java.util.List;

import com.paran.bizportal.domain.Board;
import com.paran.bizportal.domain.BoardFile;

public interface BoardMapper {
	List<Board> getBoardList(Board board);
	Board getBoardDetail(Board board);
	void insertBoard(Board board);
	void updateHitCount(Board board);
	int getBoardLastInsertSeq();
	int insertBoardFile(BoardFile boardFile);
	List<BoardFile> getBoardFileList(Board board);
}
