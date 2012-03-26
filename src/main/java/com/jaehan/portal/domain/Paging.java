package com.jaehan.portal.domain;

public class Paging {
	private int startCount;     // 한 페이지에서 보여줄 게시글의 시작 번호
	private int endCount;     // 한 페이지에서 보여줄 게시글의 끝번호
	private int currentPage = 1; //현재페이지
	private int totalCount;      //총게시물의수
	private int blockCount = 10; //한페이지의 게시물의 수
	private int blockPage = 5;  //한화면에 보여줄 페이지의 수
	private StringBuffer pagingHtml;  //페이징을 구현한 HTMl

	public Paging() {
		// 전체 페이지 수 //Math.ceil 올림
		int totalPage = (int) Math.ceil((double) totalCount / blockCount);
		if (totalPage == 0) {
			totalPage = 1;
		}

		// 연산에 오류가 날수도 있기때문에 오류 보정
		// 현재 페이지가 전체 페이지 수보다 크면 전체 페이지 수로 설정
		if (currentPage > totalPage) {
			currentPage = totalPage;
		}

		// 현재 페이지의 처음과  마지막 글의 번호 가져오기
		startCount = (currentPage - 1) * blockCount + 1;
		endCount = currentPage * blockCount;

		// 시작페이지와 마지막페이지 값 구하기  
		int startPage = (int) ((currentPage - 1) / blockPage) * blockPage + 1;
		int endPage = startPage + blockPage - 1;

		// 연산에 오류가 날수도 있기때문에 오류 보정
		// 마지막페이지가 전체페이지수보다 크면 전체페이지 수로 설정하기
		if (endPage > totalPage) {
			endPage = totalPage;
		}

		// 실제 HTML을 만드는 부분
		// 이전 block 페이지
		pagingHtml = new StringBuffer();
		if (currentPage > blockPage) {
			pagingHtml.append("<a href=listAction.action?&currentPage="
					+ (startPage - 1) + ">");
			pagingHtml.append("이전");
			pagingHtml.append("</a>");
		}
		pagingHtml.append("&nbsp;|&nbsp;");

		// 페이지번호, 현재 페이지는 빨간색으로 강조하고 링크를 제거.
		for (int i = startPage; i <= endPage; i++) {
			if (i > totalPage) {
				break;
			}
			if (i == currentPage) {
				pagingHtml.append("&nbsp;<b> <font color='red'>");
				pagingHtml.append(i);
				pagingHtml.append("</font></b>");
			} else {
				pagingHtml
				.append("&nbsp;<a href='listAction.action?currentPage=");
				pagingHtml.append(i);
				pagingHtml.append("'>");
				pagingHtml.append(i);
				pagingHtml.append("</a>");
			}
			pagingHtml.append("&nbsp;");
		}
		pagingHtml.append("&nbsp;&nbsp;|&nbsp;&nbsp;");

		// 다음 block 페이지
		if (totalPage - startPage >= blockPage) {
			pagingHtml.append("<a href=listAction.action?currentPage="
					+ (endPage + 1) + ">");
			pagingHtml.append("다음");
			pagingHtml.append("</a>");
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getBlockCount() {
		return blockCount;
	}
	public void setBlockCount(int blockCount) {
		this.blockCount = blockCount;
	}
	public int getBlockPage() {
		return blockPage;
	}
	public void setBlockPage(int blockPage) {
		this.blockPage = blockPage;
	}
	public void setPagingHtml(StringBuffer pagingHtml) {
		this.pagingHtml = pagingHtml;
	}
	public StringBuffer getPagingHtml() {
		return pagingHtml;
	}
	public int getStartCount() {
		return startCount;
	}
	public int getEndCount() {
		return endCount;
	}    
	public void setStartCount(int startCount) {
		this.startCount = startCount;
	}
	public void setEndCount(int endCount) {
		this.endCount = endCount;
	}

}
