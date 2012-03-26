package com.jaehan.portal.domain;

public class Paging {
	private int startCount;     // �� ���������� ������ �Խñ��� ���� ��ȣ
	private int endCount;     // �� ���������� ������ �Խñ��� ����ȣ
	private int currentPage = 1; //����������
	private int totalCount;      //�ѰԽù��Ǽ�
	private int blockCount = 10; //���������� �Խù��� ��
	private int blockPage = 5;  //��ȭ�鿡 ������ �������� ��
	private StringBuffer pagingHtml;  //����¡�� ������ HTMl

	public Paging() {
		// ��ü ������ �� //Math.ceil �ø�
		int totalPage = (int) Math.ceil((double) totalCount / blockCount);
		if (totalPage == 0) {
			totalPage = 1;
		}

		// ���꿡 ������ ������ �ֱ⶧���� ���� ����
		// ���� �������� ��ü ������ ������ ũ�� ��ü ������ ���� ����
		if (currentPage > totalPage) {
			currentPage = totalPage;
		}

		// ���� �������� ó����  ������ ���� ��ȣ ��������
		startCount = (currentPage - 1) * blockCount + 1;
		endCount = currentPage * blockCount;

		// ������������ ������������ �� ���ϱ�  
		int startPage = (int) ((currentPage - 1) / blockPage) * blockPage + 1;
		int endPage = startPage + blockPage - 1;

		// ���꿡 ������ ������ �ֱ⶧���� ���� ����
		// �������������� ��ü������������ ũ�� ��ü������ ���� �����ϱ�
		if (endPage > totalPage) {
			endPage = totalPage;
		}

		// ���� HTML�� ����� �κ�
		// ���� block ������
		pagingHtml = new StringBuffer();
		if (currentPage > blockPage) {
			pagingHtml.append("<a href=listAction.action?&currentPage="
					+ (startPage - 1) + ">");
			pagingHtml.append("����");
			pagingHtml.append("</a>");
		}
		pagingHtml.append("&nbsp;|&nbsp;");

		// ��������ȣ, ���� �������� ���������� �����ϰ� ��ũ�� ����.
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

		// ���� block ������
		if (totalPage - startPage >= blockPage) {
			pagingHtml.append("<a href=listAction.action?currentPage="
					+ (endPage + 1) + ">");
			pagingHtml.append("����");
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
