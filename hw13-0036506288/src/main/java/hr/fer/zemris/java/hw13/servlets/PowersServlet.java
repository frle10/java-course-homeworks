package hr.fer.zemris.java.hw13.servlets;

import static java.lang.Math.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * This servlet parses the parameters given by user in order to create an Excel
 * file which contains n (n being one of the parameters) tabs where each tab
 * contains a table of i-th powers of values a to b (a and b also being parameters)
 * where i = 1,...,n. 
 * 
 * @author Ivan Skorupan
 */
public class PowersServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		int a = 0, b = 0, n = 0;
		
		try {
			a = Integer.parseInt(req.getParameter("a"));
			if(a < -100 || a > 100) throw new Exception();
		} catch(Exception ex) {
			proccessError("a", req, resp);
			return;
		}
		
		try {
			b = Integer.parseInt(req.getParameter("b"));
			if(b < -100 || b > 100) throw new Exception();
		} catch(Exception ex) {
			proccessError("b", req, resp);
			return;
		}
		
		try {
			n = Integer.parseInt(req.getParameter("n"));
			if(n < 1 || n > 5) throw new Exception();
		} catch(Exception ex) {
			proccessError("n", req, resp);
			return;
		}
		
		resp.setContentType("application/ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=tablica.xls");
		
		HSSFWorkbook wb = new HSSFWorkbook();
		for(int i = 1; i <= n; i++) {
			HSSFSheet sheet = wb.createSheet(i + "-th Powers");
			for(int j = a; j <= b; j++) {
				HSSFRow row = sheet.createRow(j - a);
				HSSFCell valueCell = row.createCell(0);
				HSSFCell powerCell = row.createCell(1);
				
				valueCell.setCellValue(String.valueOf(j));
				powerCell.setCellValue(pow(j, i));
			}
		}
		
		wb.write(resp.getOutputStream());
		wb.close();
	}
	
	/**
	 * Helper method that gets called in case there is a parameter parsing error in the doGet() method.
	 * 
	 * @param parameter - parameter that is invalid
	 * @param req - request object
	 * @param resp - response object
	 * @throws IOException if there was an IO error while reading the parameter
	 * @throws ServletException if there was a problem with the servlet
	 */
	private void proccessError(String parameter, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
		req.setAttribute("errorMessage", "Parameter " + parameter + " cannot be: " + req.getParameter(parameter) + "!");
		req.getRequestDispatcher("/WEB-INF/pages/powersError.jsp").forward(req, resp);
	}
	
}
