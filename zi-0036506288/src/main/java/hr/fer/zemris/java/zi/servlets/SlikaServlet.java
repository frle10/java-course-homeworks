package hr.fer.zemris.java.zi.servlets;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import hr.fer.zemris.java.hw17.jvdraw.Util;
import hr.fer.zemris.java.hw17.jvdraw.interfaces.DrawingModel;
import hr.fer.zemris.java.hw17.jvdraw.models.JVDrawingModel;

@WebServlet(name="slikaaa", urlPatterns={"/dobiSliku"})
public class SlikaServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("image/png");
		String path = req.getParameter("path");
		
		Path imagesPath = Paths.get(req.getServletContext().getRealPath("/WEB-INF/images/" + path));
		
		DrawingModel model = new JVDrawingModel();
		try {
			Util.openFile(imagesPath, model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		BufferedImage image = null;
		try {
			image = Util.export2(model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(image, "png", baos);
		baos.flush();
		byte[] imageInByte = baos.toByteArray();
		baos.close();
		resp.getOutputStream().write(imageInByte);
	}
	
}
