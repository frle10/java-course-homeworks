package hr.fer.zemris.java.hw13.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

/**
 * This servlet generates and writes to client an Excel file containing
 * the voting results.
 * 
 * @author Ivan Skorupan
 */
@WebServlet(name="votingXls", urlPatterns= {"/glasanje-xls"})
public class VotingXlsServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Cell style that will be used when creating new cells.
	 */
	private HSSFCellStyle style;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Map<Integer, BandEntry> bands = Utility.getVotingResults(req);
		
		resp.setContentType("application/ms-excel");
		resp.setHeader("Content-Disposition", "attachment; filename=rezultati-glasanja.xls");
		
		
		HSSFWorkbook wb = createWorkbook();
		HSSFRow row = wb.getSheetAt(0).createRow(0);
		HSSFCell bandNameCell = row.createCell(0);
		HSSFCell votesCell = row.createCell(1);
		bandNameCell.setCellStyle(style);
		votesCell.setCellStyle(style);
		bandNameCell.setCellValue("Band Name");
		votesCell.setCellValue("Votes");
		
		int i = 1;
		List<Entry<Integer, BandEntry>> entryList = bands.entrySet().stream()
				.sorted((e1, e2) -> e2.getValue().getNumberOfVotes().compareTo(e1.getValue().getNumberOfVotes()))
				.collect(Collectors.toList());
		for(Entry<Integer, BandEntry> entry : entryList) {
			HSSFRow iRow = wb.getSheetAt(0).createRow(i++);
			HSSFCell bandName = iRow.createCell(0);
			HSSFCell votes = iRow.createCell(1);
			bandName.setCellStyle(style);
			votes.setCellStyle(style);
			bandName.setCellValue(entry.getValue().getBandName());
			votes.setCellValue(entry.getValue().getNumberOfVotes());
		}
		
		wb.write(resp.getOutputStream());
		wb.close();
	}
	
	/**
	 * Creates and returns a new Excel workbook with its properties appropriately set.
	 * 
	 * @return a new Excel workbook as a {@link HSSFWorkbook} object
	 */
	private HSSFWorkbook createWorkbook() {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Voting Results");
		sheet.setColumnWidth(0, 5120);
		sheet.setColumnWidth(1, 1792);
		
		style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.GREY_25_PERCENT.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		
		return wb;
	}
	
}
