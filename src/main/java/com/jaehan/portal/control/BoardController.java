package com.jaehan.portal.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Provider;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import com.jaehan.portal.common.BoardDefine;
import com.jaehan.portal.domain.Board;
import com.jaehan.portal.domain.BoardFile;
import com.jaehan.portal.domain.LoginInfo;
import com.jaehan.portal.domain.User;
import com.jaehan.portal.persistence.BoardMapper;



@Controller
@RequestMapping("/board")
public class BoardController {

	Logger logger = Logger.getLogger(this.getClass());

	@Autowired private BoardMapper boardMapper;
	
	@Autowired 	private Provider<LoginInfo> loginInfoProvider;

	@ModelAttribute("currentUser")
	public User currentUser() {
		return loginInfoProvider.get().currentUser();
	}

	@RequestMapping(value={"/boardWriteForm.html"} , method=RequestMethod.GET)
	public String boardWriteForm(ModelMap model) throws Exception{
		model.addAttribute(new Board());
		return "board/boardWriteForm";
	}

	@RequestMapping(value={"/insertBoard.action"} , method=RequestMethod.POST)
	public String insertBoard(@ModelAttribute @Valid Board boardVO,BindingResult bindingResult)throws Exception{
		if (bindingResult.hasErrors()) return "board/boardWriteForm";

		//boardVO �ٵ�� -> �ѱ����ڵ�, �Ķ���� �߰� 
		String file_exist = (!boardVO.getFile1().isEmpty() || !boardVO.getFile2().isEmpty()) ? "Y" : "N";	// file1 or file2 �ϳ��� �ִٸ� Y �ƴϸ� N
		boardVO.setFile_exist(file_exist);

		//		mybatis selectKey�� �ȵȴ�..����...�Ф� �׷��� �� �ּ�ó�� 
		//		int board_seq = sqlSession.insert("board.insertBoard",boardVO);	// board ���̺� ���
		boardMapper.insertBoard(boardVO);

		int board_seq = boardMapper.getBoardLastInsertSeq();//(Integer)sqlSession.selectOne("board.getBoardLastInsertSeq");

		if(!boardVO.getFile1().isEmpty()){
			String org_file_name = boardVO.getFile1().getOriginalFilename();	// ���ϸ� 
			String save_file_name = this.getSaveFileName();

			BoardFile fileVO1 = new BoardFile();
			fileVO1.setSeq(1);
			fileVO1.setBoard_id(boardVO.getId());
			fileVO1.setBoard_seq(board_seq);
			fileVO1.setOrg_filename(org_file_name);
			fileVO1.setSave_path(BoardDefine.getSaveFilePath());
			fileVO1.setSave_filename(save_file_name);
			fileVO1.setFile_size(boardVO.getFile1().getSize());
			fileVO1.setState(1);

			boardMapper.insertBoardFile(fileVO1);//sqlSession.insert("board.insertBoardFile",fileVO1);	// file1 ���̺� ���
			this.uploadFile(boardVO.getFile1() , save_file_name);		//������ ���� ������ ���
		}

		if(!boardVO.getFile2().isEmpty()){
			String org_file_name = boardVO.getFile2().getOriginalFilename();	 
			String save_file_name = this.getSaveFileName();

			BoardFile fileVO2 = new BoardFile();
			fileVO2.setSeq(2);
			fileVO2.setBoard_id(boardVO.getId());
			fileVO2.setBoard_seq(board_seq);
			fileVO2.setOrg_filename(org_file_name);
			fileVO2.setSave_path(BoardDefine.getSaveFilePath());
			fileVO2.setSave_filename(save_file_name);
			fileVO2.setFile_size(boardVO.getFile2().getSize());
			fileVO2.setState(1);

			boardMapper.insertBoardFile(fileVO2);//sqlSession.insert("board.insertBoardFile",fileVO2);
			this.uploadFile(boardVO.getFile2(),save_file_name);
		}
		return "redirect:/board/getBoardList.html";
	}


	@RequestMapping(value={"/getBoardList.html"} , method=RequestMethod.GET)
	public String selectBoardList(Board boardVO , Model model) throws Exception{
		List<Board> list = boardMapper.getBoardList(boardVO);
		model.addAttribute("boardList", list);
		return "board/boardList";
	}

	@RequestMapping(value={"/getBoardDetail.html"} , method=RequestMethod.GET)
	public String selectBoardDetail(Board boardVO , Model model) throws Exception{
		Board vo = boardMapper.getBoardDetail(boardVO);//(BoardVO)sqlSession.selectOne("board.getBoardDetail",boardVO);
		model.addAttribute("boardVO", vo);

		if(vo.getFile_exist().equals("Y")){ // ÷�� ������ ������� ����Ʈ ���ؿ��� 
			List<BoardFile> boardFileList = boardMapper.getBoardFileList(vo);//sqlSession.selectList("board.getBoardFileList",vo);
			model.addAttribute("boardFileList", boardFileList );
		}
		// ��ȸ�� ���� 
		boardMapper.updateHitCount(boardVO);//sqlSession.update("board.updateHitCount",boardVO);
		return "board/boardDetail";
	}

	@RequestMapping(value={"/downloadFile.html"} , method=RequestMethod.GET)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response , BoardFile boardFileVO) throws Exception{

		// �ٿ�ε�� ���ϸ� �������� 
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
	 * ������ ������ ��������
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
	 * ������ ����� ÷������ �̸� ����� ����ð� + UUIC 
	 * @return String ����������������̸�
	 */
	private String getSaveFileName() {
		long now = System.currentTimeMillis();
		SimpleDateFormat sdfNow = new SimpleDateFormat("yyyyMMddHHmmss");
		String str_now = sdfNow.format(new Date(now));
		String save_file_name = str_now+"-"+UUID.randomUUID().toString();	// �ӽ�Ű ����� = �ð�+UUID
		return save_file_name;
	}

}
