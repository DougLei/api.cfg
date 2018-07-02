package com.king.tooth.web.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

/**
 * 上传文件servlet
 * @author DougLei
 */
@SuppressWarnings("serial")
public class UploadFileServlet extends HttpServlet{

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload servletUpload = new ServletFileUpload(factory);
		try {
			List<FileItem> fileItemList = servletUpload.parseRequest(request);
			for (FileItem file : fileItemList) {
				System.out.println(file.getName());
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
	}
}
