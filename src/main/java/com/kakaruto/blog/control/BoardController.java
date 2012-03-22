package com.kakaruto.blog.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.kakaruto.blog.base.APIBaseHandler;
import com.kakaruto.blog.common.BoardDefine;
import com.kakaruto.blog.persistence.Board;
import com.kakaruto.blog.vo.BoardFileVO;
import com.kakaruto.blog.vo.BoardVO;

@Controller
@RequestMapping("/board")
public class BoardController {

	Logger logger = Logger.getLogger(this.getClass());

	private APIBaseHandler apiBaseHandler = null;
	private Board board;

	@Autowired
	public void init(APIBaseHandler apiBaseHandler,  Board board) {
		this.apiBaseHandler = apiBaseHandler;
		this.board = board;
	}

	@RequestMapping(value={"/boardWriteForm.html"} , method=RequestMethod.GET)
	public String boardWriteForm(ModelMap model) throws Exception{
		model.addAttribute(new BoardVO());
		return "board/boardWriteForm";
	}

	@RequestMapping(value={"/insertBoard.action"} , method=RequestMethod.POST)
	public String insertBoard(@ModelAttribute @Valid BoardVO boardVO,BindingResult bindingResult)throws Exception{
		if (bindingResult.hasErrors()) return "board/boardWriteForm";

		//boardVO 다듬기 -> 한글인코딩, 파라미터 추가 
		String file_exist = (!boardVO.getFile1().isEmpty() || !boardVO.getFile2().isEmpty()) ? "Y" : "N";	// file1 or file2 하나라도 있다면 Y 아니면 N
		boardVO.setFile_exist(file_exist);

		//		mybatis selectKey가 안된다..젠장...ㅠㅠ 그래서 걍 주석처리 
		//		int board_seq = sqlSession.insert("board.insertBoard",boardVO);	// board 테이블 등록
		board.insertBoard(boardVO);

		int board_seq = board.getBoardLastInsertSeq();//(Integer)sqlSession.selectOne("board.getBoardLastInsertSeq");

		if(!boardVO.getFile1().isEmpty()){
			String org_file_name = boardVO.getFile1().getOriginalFilename();	// 파일명 
			String save_file_name = this.getSaveFileName();

			BoardFileVO fileVO1 = new BoardFileVO();
			fileVO1.setSeq(1);
			fileVO1.setBoard_id(boardVO.getId());
			fileVO1.setBoard_seq(board_seq);
			fileVO1.setOrg_filename(org_file_name);
			fileVO1.setSave_path(BoardDefine.getSaveFilePath());
			fileVO1.setSave_filename(save_file_name);
			fileVO1.setFile_size(boardVO.getFile1().getSize());
			fileVO1.setState(1);

			board.insertBoardFile(fileVO1);//sqlSession.insert("board.insertBoardFile",fileVO1);	// file1 테이블 등록
			this.uploadFile(boardVO.getFile1() , save_file_name);		//물리적 파일 서버에 등록
		}

		if(!boardVO.getFile2().isEmpty()){
			String org_file_name = boardVO.getFile2().getOriginalFilename();	 
			String save_file_name = this.getSaveFileName();

			BoardFileVO fileVO2 = new BoardFileVO();
			fileVO2.setSeq(2);
			fileVO2.setBoard_id(boardVO.getId());
			fileVO2.setBoard_seq(board_seq);
			fileVO2.setOrg_filename(org_file_name);
			fileVO2.setSave_path(BoardDefine.getSaveFilePath());
			fileVO2.setSave_filename(save_file_name);
			fileVO2.setFile_size(boardVO.getFile2().getSize());
			fileVO2.setState(1);

			board.insertBoardFile(fileVO2);//sqlSession.insert("board.insertBoardFile",fileVO2);
			this.uploadFile(boardVO.getFile2(),save_file_name);
		}
		return "redirect:/board/getBoardList.html";
	}


	@RequestMapping(value={"/getBoardList.html"} , method=RequestMethod.GET)
	public String selectBoardList(BoardVO boardVO , Model model) throws Exception{
		List<BoardVO> list = board.getBoardList(boardVO);
		model.addAttribute("boardList", list);
		return "board/boardList";
	}

	@RequestMapping(value={"/getBoardDetail.html"} , method=RequestMethod.GET)
	public String selectBoardDetail(BoardVO boardVO , Model model) throws Exception{
		BoardVO vo = board.getBoardDetail(boardVO);//(BoardVO)sqlSession.selectOne("board.getBoardDetail",boardVO);
		model.addAttribute("boardVO", vo);

		if(vo.getFile_exist().equals("Y")){ // 첨부 파일이 있을경우 리스트 구해오기 
			List<BoardFileVO> boardFileList = board.getBoardFileList(vo);//sqlSession.selectList("board.getBoardFileList",vo);
			model.addAttribute("boardFileList", boardFileList );
		}
		// 조회수 증가 
		board.updateHitCount(boardVO);//sqlSession.update("board.updateHitCount",boardVO);
		return "board/boardDetail";
	}

	@RequestMapping(value={"/downloadFile.html"} , method=RequestMethod.GET)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response , BoardFileVO boardFileVO) throws Exception{

		// 다운로드시 파일명 깨짐방지 
		String org_filename = URLEncoder.encode(boardFileVO.getOrg_filename(), "UTF8").replaceAll("\\+", " ");

		File file = new File(BoardDefine.getSaveFilePath() + boardFileVO.getSave_filename());

		response.setContentLength((int)file.length());
		response.setHeader("Content-Transfer-Encoding", "binary");
		response.setHeader("Content-Disposition", 
				"attachment;fileName=\""+org_filename+"\";");

		OutputStream out = response.getOutputStream();
		FileInputStream fis = null;
		try
		{
			fis = new FileInputStream(file);
			FileCopyUtils.copy(fis,out);
		}
		catch(java.io.IOException ioe)
		{
			ioe.printStackTrace();
		}
		finally
		{
			if(fis != null) fis.close();
		}
	}


	/**
	 * 물리적 공간에 파일저장
	 * @param file
	 * @param save_file_name
	 * @throws Exception
	 */
	private void uploadFile(CommonsMultipartFile file,String save_file_name)throws Exception{

		byte[] bytes = file.getBytes();
		File lOutFile = new File(BoardDefine.getSaveFilePath() + save_file_name);
		FileOutputStream lFileOutputStream = new FileOutputStream(lOutFile);
		lFileOutputStream.write(bytes);
		lFileOutputStream.close();
	}

	/**
	 * 서버에 저장될 첨부파일 이름 만들기 현재시간 + UUIC 
	 * @return String 서버에저장될파일이름
	 */
	private String getSaveFileName() {
		long now = System.currentTimeMillis();
		SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_now = sdfNow.format(new Date(now));
		String save_file_name = str_now+"-"+UUID.randomUUID().toString();	// 임시키 만들기 = 시간+UUID
		return save_file_name;
	}

	/**
	 * 기본 API 핸들러를 이용하여 json 타입 API 의 Exception 처리 결과를 처리 한다.
	 */
	@ExceptionHandler(Exception.class)
	public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) 
	{
		apiBaseHandler.handleException(response, e);
	}
}
