package com.jaehan.portal.domain;

public class BoardFile {
	private int seq;
	private String board_id;
	private int board_seq;
	private String org_filename;
	private String save_path;
	private String save_filename;
	private long file_size;
	private int state;
	

	public int getSeq() {
		return seq;
	}


	public void setSeq(int seq) {
		this.seq = seq;
	}


	public String getBoard_id() {
		return board_id;
	}


	public void setBoard_id(String board_id) {
		this.board_id = board_id;
	}


	public int getBoard_seq() {
		return board_seq;
	}


	public void setBoard_seq(int board_seq) {
		this.board_seq = board_seq;
	}


	public String getOrg_filename() {
		return org_filename;
	}


	public void setOrg_filename(String org_filename) {
		this.org_filename = org_filename;
	}


	public String getSave_path() {
		return save_path;
	}


	public void setSave_path(String save_path) {
		this.save_path = save_path;
	}


	public String getSave_filename() {
		return save_filename;
	}


	public void setSave_filename(String save_filename) {
		this.save_filename = save_filename;
	}


	public long getFile_size() {
		return file_size;
	}


	public void setFile_size(long file_size) {
		this.file_size = file_size;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}

	
}
