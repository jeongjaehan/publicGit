package com.paran.bizportal.domain;

import java.sql.Timestamp;
import java.util.Arrays;

import javax.validation.constraints.Size;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class Board {
	private int seq;
	private String id;
	@Size(min=1, max=4)
	private String title;
	private byte[] bContent;
	private String content;
	private String writer;
	private Timestamp regdate;
	private Timestamp moddate;
	private int state;
	private int type;
	private int hitcount;
	private CommonsMultipartFile file1;
	private CommonsMultipartFile file2;
	private String file_exist;
	@Size(min=1, max=20)
	private String password;
	
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	//mybatis 에서 사용하는 set,get
	public byte[] getbContent() {
		return bContent;
	}
	public void setbContent(byte[] bContent) {
		this.bContent = bContent;
	}
	public String getContent() {
		return this.content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getContentStringValue()throws Exception {
		return new String(bContent,"UTF-8");
	}
	public String getWriter() {
		return writer;
	}
	public void setWriter(String writer) {
		this.writer = writer;
	}
	public Timestamp getRegdate() {
		return regdate;
	}
	public void setRegdate(Timestamp regdate) {
		this.regdate = regdate;
	}
	public Timestamp getModdate() {
		return moddate;
	}
	public void setModdate(Timestamp moddate) {
		this.moddate = moddate;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getHitcount() {
		return hitcount;
	}
	public void setHitcount(int hitcount) {
		this.hitcount = hitcount;
	}
	public CommonsMultipartFile getFile1() {
		return file1;
	}
	public void setFile1(CommonsMultipartFile file1) {
		this.file1 = file1;
	}
	public CommonsMultipartFile getFile2() {
		return file2;
	}
	public void setFile2(CommonsMultipartFile file2) {
		this.file2 = file2;
	}
	public String getFile_exist() {
		return file_exist;
	}
	public void setFile_exist(String file_exist) {
		this.file_exist = file_exist;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BoardVO [seq=");
		builder.append(seq);
		builder.append(", id=");
		builder.append(id);
		builder.append(", title=");
		builder.append(title);
		builder.append(", bContent=");
		builder.append(Arrays.toString(bContent));
		builder.append(", Content=");
		builder.append(content);
		builder.append(", writer=");
		builder.append(writer);
		builder.append(", regdate=");
		builder.append(regdate);
		builder.append(", moddate=");
		builder.append(moddate);
		builder.append(", state=");
		builder.append(state);
		builder.append(", type=");
		builder.append(type);
		builder.append(", hitcount=");
		builder.append(hitcount);
		builder.append(", file1=");
		builder.append(file1);
		builder.append(", file2=");
		builder.append(file2);
		builder.append(", file_exist=");
		builder.append(file_exist);
		builder.append(", password=");
		builder.append(password);
		builder.append("]");
		return builder.toString();
	}

}
