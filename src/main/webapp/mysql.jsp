<%@page import="java.sql.DriverManager"%>
<%@page import="java.sql.Connection"%>
<%@page import="db.DBUtil"%>
<%@page language="java" contentType="text/html; charset=EUC-KR" pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>MySQL ���� ����̹� �׽�Ʈ</title>
</head>
<body>
	<h1>MySQL ���� ����̹� �׽�Ʈ</h1>
	<%
		
		try
		{
			DBUtil dbUtil = new DBUtil();
			Connection conn = dbUtil.open();
			if (conn != null) {
				out.println("MySQL ���� ����");
			}
			else {
				out.println("MySQL ���� ����");
			}
			
		}
		catch (Exception ex)
		{
			out.println("���� �����Դϴ�. ���� �޽��� : " + ex.getMessage());
		}
	%>
</body>
</html>