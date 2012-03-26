package com.jaehan.portal.persistence;

import java.util.List;

import com.jaehan.portal.domain.Board;
import com.jaehan.portal.domain.BoardFile;

public interface BoardMapper {
	List<Board> getBoardList(Board board);
	Board getBoardDetail(Board board);
	void insertBoard(Board board);
	void updateHitCount(Board board);
	int getBoardLastInsertSeq();
	int insertBoardFile(BoardFile boardFile);
	List<BoardFile> getBoardFileList(Board board);
}
