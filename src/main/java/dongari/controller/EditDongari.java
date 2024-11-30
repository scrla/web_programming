package dongari.controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import dongari.DongariDto;
import dongari.DongariService;

/**
 * Servlet implementation class EditDongari
 */
@WebServlet("/edit")
public class EditDongari extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditDongari() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int id = Integer.parseInt(request.getParameter("id")); //���⼭ id�� ���Ƹ� id
		
		HttpSession session = request.getSession();
		if (session == null) {
			response.sendRedirect("login");
			return; 
		}
		int user_id = (int) session.getAttribute("user_id");
		
		DongariService dongariService = new DongariService();
		DongariDto dongari = dongariService.findById(id);
		if (user_id != dongari.getUser_id()) {
			response.sendRedirect("./");
			return;
		}
		
		request.setAttribute("dongari", dongari); // �˻��� ����� ��� (jsp���� �� �� �ְ�)
		request.setAttribute("type", "edit");
		
		RequestDispatcher dis = request.getRequestDispatcher("dongariForm.jsp"); //form ������ ����� �߰�
		dis.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setCharacterEncoding("UTF-8");
	    response.setContentType("text/html;charset=UTF-8");

	    HttpSession session = request.getSession();
	    if (session == null || session.getAttribute("user_id") == null) {
	        response.sendRedirect("login");
	        return;
	    }

	    int user_id = (int) session.getAttribute("user_id");

	    // ���� ���ε� ��� ����
	    String uploadPath = getServletContext().getRealPath("/uploads");
	    File uploadDir = new File(uploadPath);
	    if (!uploadDir.exists()) {
	        uploadDir.mkdir();
	    }

	    // ���� ���ε� ó��
	    int maxSize = 1024 * 1024 * 10; // 10MB
	    MultipartRequest multi = new MultipartRequest(
	        request,
	        uploadPath,
	        maxSize,
	        "UTF-8",
	        new DefaultFileRenamePolicy()
	    );

	    // �Ķ���� ��������
	    int id = Integer.parseInt(multi.getParameter("id")); // ���Ƹ� ID
	    String title = multi.getParameter("title");
	    String summary = multi.getParameter("summary");
	    String body = multi.getParameter("body");
	    int member_num = Integer.parseInt(multi.getParameter("member_num"));
	    int apply_status = Integer.parseInt(multi.getParameter("apply_status")); // 1 - ���� ��, 2 - ��� ����, 3 - ���� ����
	    String apply_link = multi.getParameter("apply_link");
	    String sns_link = multi.getParameter("sns_link");
	    String location = multi.getParameter("location");
	    int category_id = Integer.parseInt(multi.getParameter("category_id"));

	    // ���� �Ⱓ ó��
	    String apply_start = null;
	    String apply_end = null;
	    if (apply_status == 1) { // ���� ��
	        apply_start = multi.getParameter("apply_start");
	        apply_end = multi.getParameter("apply_end");
	    } else if (apply_status == 3) { // ���� ����
	        apply_start = "9999";
	        apply_end = "9999";
	    }

	    // �̹��� ó��
	    String existingImage = multi.getParameter("existingImage"); // ���� �̹���
	    String newImage = null;

	    // ���� ���ε�
	    Enumeration<?> files = multi.getFileNames();
	    if (files.hasMoreElements()) {
	        String fileInputName = (String) files.nextElement();
	        String savedFileName = multi.getFilesystemName(fileInputName);
	        if (savedFileName != null) {
	            newImage = "/uploads/" + savedFileName; // �� �̹��� ��� ����
	        }
	    }

	    // ���� �̹��� ��� ����
	    String finalImage = (newImage != null) ? newImage : existingImage;

	    // ���Ƹ� ���� ����
	    DongariService dongariService = new DongariService();
	    dongariService.editDongari(new DongariDto(
	        id, user_id, category_id, member_num, finalImage,
	        title, summary, body, apply_start, apply_end,
	        apply_link, sns_link, location, ""
	    ));

	    response.sendRedirect("./");
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		
		HttpSession session = request.getSession();
		if (session == null) {
			response.sendRedirect("login");
			return; 
		}
		
		int id = Integer.parseInt(request.getParameter("id")); // url �ּҷ� ���� num�� �о���� 
		
		DongariService dongariService = new DongariService();
		dongariService.deleteDongari(id);
		
		response.sendRedirect("./");
		return; 
	}
}
