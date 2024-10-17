package class_test;



import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PictureData;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFPictureData;
import org.apache.poi.xssf.usermodel.XSSFShape;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterClass;

import com.itextpdf.io.source.ByteArrayOutputStream;



public class Sampleclass_Excel_utility extends Sampleclass_DB_utility {


		private Workbook workbook;
	    private Sheet sheet;
	    private Sheet screenshotSheet;
	    private int rowNum = 5;
	    private int screenshotSheetRowNum;
	    private static final int IMAGE_ROW_OFFSET = 31;

	    public Sampleclass_Excel_utility(String filePath) throws IOException {
	    	super();
	    	File excelFile = new File(filePath);
	    	if (excelFile.exists()) {
	            try (FileInputStream inputStream = new FileInputStream(excelFile)) {
	                workbook = new XSSFWorkbook(inputStream);
	                sheet = workbook.getSheet("Test Results");
	                screenshotSheet = workbook.getSheet("Screenshots");
	                System.out.println("Excel workbook is exist");
	                if (sheet == null) {
	                    sheet = workbook.createSheet("Test Results");
	                    createHeader();
	                }
	                
	                if (screenshotSheet == null) {
	                    screenshotSheet = workbook.createSheet("Screenshots");
	                }

	                // Calculate the last used row in the Screenshots sheet
	                updateRowNumbers();
	                System.out.println("Calculated screenshotSheetRowNum: " + screenshotSheetRowNum);
	            }
	        } else {
	            workbook = new XSSFWorkbook();
	            sheet = workbook.createSheet("Test Results");
	            screenshotSheet = workbook.createSheet("Screenshots");
	            createHeader();
	            createFile(filePath);
	            screenshotSheetRowNum = 0; // Start from the first row for a new file
	        }
	    }

	    private void createHeader() {
	        
	    	System.out.println("Creating Header with:");
	        System.out.println("Company Name: " + compName);
	        System.out.println("Location Name: " + locName);
	        System.out.println("Server & DB: " + DBconnect);
	        
	        Row row1=sheet.createRow(0);
	        row1.createCell(0).setCellValue("Client:");
	        row1.createCell(1).setCellValue(compName);
	        Row row2=sheet.createRow(1);
	        row2.createCell(0).setCellValue("Location:");
	        row2.createCell(1).setCellValue(locName);
	        Row row3=sheet.createRow(2);
	        row3.createCell(0).setCellValue("Server & DB:");
	        row3.createCell(1).setCellValue(DBconnect);
	        Row row4=sheet.createRow(3);
	        row4.createCell(0).setCellValue("Link:");
	        row4.createCell(1).setCellValue(link);
	        Row row5=sheet.createRow(4);
	        row5.createCell(0).setCellValue("LinkUserId:");
	        row5.createCell(1).setCellValue(usr);
	        Row row6=sheet.createRow(5);
	        row6.createCell(0).setCellValue("LinkPassword:");
	        row6.createCell(1).setCellValue(pas);
	    	
	        Font headerFont = workbook.createFont();
	        headerFont.setBold(true);
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFont(headerFont);
	        
	    	Row headerRow = sheet.createRow(6);
	        Cell cell = headerRow.createCell(0);
	        cell.setCellValue("SCENARIO");
	        cell.setCellStyle(headerCellStyle);

	        cell = headerRow.createCell(1);
	        cell.setCellValue("CHECKBOX STATE");
	        cell.setCellStyle(headerCellStyle);

	        cell = headerRow.createCell(2);
	        cell.setCellValue("EXPECTED RESULT");
	        cell.setCellStyle(headerCellStyle);

	        cell = headerRow.createCell(3);
	        cell.setCellValue("ACTUAL RESULT");
	        cell.setCellStyle(headerCellStyle);

	        cell = headerRow.createCell(4);
	        cell.setCellValue("STATUS");
	        cell.setCellStyle(headerCellStyle);
	        
	        cell = headerRow.createCell(5);
	        cell.setCellValue("SCREENSHOTS");
	        cell.setCellStyle(headerCellStyle);
	    }

	    public void addResult(String scenario,String checkbox_state,String expectedresult, String actualresult, String status, Image screenshot) throws IOException {
	    	while(sheet.getRow(rowNum)!= null) {
	    		rowNum++;
	    	}
	    	Row row = sheet.createRow(rowNum);
	        Cell cell = row.createCell(0);
	        cell.setCellValue(scenario);

	        cell = row.createCell(1);
	        cell.setCellValue(checkbox_state);
	        
	        cell = row.createCell(2);
	        cell.setCellValue(expectedresult);

	        cell = row.createCell(3);
	        cell.setCellValue(actualresult);

	        cell = row.createCell(4);
	        cell.setCellValue(status);
	        if ("Fail".equalsIgnoreCase(status)) {
	        	CellStyle style = sheet.getWorkbook().createCellStyle();
	            style.setFillForegroundColor(IndexedColors.RED.getIndex());
	            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	            cell.setCellStyle(style);
	        }

	        if (screenshot != null) {
	            addScreenshot(row, screenshot);
	        }else {
	            screenshotSheetRowNum++;
	        }
	    }

	    private void addScreenshot(Row row, Image screenshot) throws IOException {
	        // Define the desired width and height (in pixels) for the screenshot in the Excel sheet
	        int desiredWidth = 1000; // Width in pixels
	        int desiredHeight = 500; // Height in pixels

	        // Convert Image to BufferedImage if it's not already a BufferedImage
	        BufferedImage bufferedImage;
	        if (screenshot instanceof BufferedImage) {
	            bufferedImage = (BufferedImage) screenshot;
	        } else {
	            bufferedImage = new BufferedImage(screenshot.getWidth(null), screenshot.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	            Graphics2D g2d = bufferedImage.createGraphics();
	            g2d.drawImage(screenshot, 0, 0, null);
	            g2d.dispose();  // Dispose the graphics object to release resources
	        }

	        // Resize the BufferedImage to desired dimensions
	        BufferedImage resizedImage = new BufferedImage(desiredWidth, desiredHeight, BufferedImage.TYPE_INT_ARGB);
	        Graphics2D g2d = resizedImage.createGraphics();
	        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
	        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	        g2d.drawImage(bufferedImage, 0, 0, desiredWidth, desiredHeight, null);
	        g2d.dispose();  // Dispose the graphics object to release resources

	        // Convert resized image to byte array
	        ByteArrayOutputStream bos = new ByteArrayOutputStream();
	        ImageIO.write(resizedImage, "png", bos);
	        byte[] imageBytes = bos.toByteArray();

	        // Add picture to screenshot sheet
	        int pictureIdx = workbook.addPicture(imageBytes, Workbook.PICTURE_TYPE_PNG);
	        Drawing<?> drawing = screenshotSheet.createDrawingPatriarch();
	        CreationHelper helper = workbook.getCreationHelper();

	        // Create an anchor point
	        ClientAnchor anchor = helper.createClientAnchor();
	        anchor.setCol1(0); // Starting column
	        anchor.setCol2(15); // Ending column (adjust based on desired width)
	        anchor.setRow1(screenshotSheetRowNum); // Starting row
	        anchor.setRow2(screenshotSheetRowNum + 30);
	        
	        // Insert the picture
	        Picture pict = drawing.createPicture(anchor, pictureIdx);
	        pict.resize();  // Resize the image to fit into the cell

	        // Get the address of the screenshot cell
	       
	        Row screenshotSheetRow = screenshotSheet.getRow(screenshotSheetRowNum);
	        if (screenshotSheetRow == null) {
	            screenshotSheetRow = screenshotSheet.createRow(screenshotSheetRowNum);
	        }

	        // Create a cell for the picture if it doesn't exist
	        Cell screenshotCell = screenshotSheetRow.createCell(0);

	        // Get the address of the screenshot cell
	        String screenshotCellAddress = screenshotCell.getAddress().formatAsString();
	        Hyperlink link = helper.createHyperlink(HyperlinkType.DOCUMENT);
	        link.setAddress("'Screenshots'!" + screenshotCellAddress);
	        Cell mainSheetScreenshotCell = row.createCell(5);
	        mainSheetScreenshotCell.setHyperlink(link);
	        mainSheetScreenshotCell.setCellValue("'Screenshots'!" + screenshotCellAddress);
	        
	        CellStyle hyperlinkStyle = workbook.createCellStyle();
	        Font hyperlinkFont = workbook.createFont();
	        hyperlinkFont.setUnderline(Font.U_SINGLE);
	        hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
	        hyperlinkStyle.setFont(hyperlinkFont);

	        mainSheetScreenshotCell.setCellStyle(hyperlinkStyle);

	        // Update the screenshot sheet row number for the next screenshot
	        screenshotSheetRowNum += IMAGE_ROW_OFFSET;

	       
	    }

	    public void save(String filePath) throws IOException {
	        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
	            workbook.write(fileOut);
	        }
	    }

	    private void createFile(String filePath) throws IOException {
	        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
	            workbook.write(fileOut);
	        }
	    }
	    private void updateRowNumbers() {
	    	int lastRowWithImages = -1;

	        // Ensure the sheet is an instance of XSSFSheet for XSSFWorkbook
	        if (screenshotSheet instanceof XSSFSheet) {
	            XSSFSheet xssfSheet = (XSSFSheet) screenshotSheet;
	            XSSFWorkbook workbook = (XSSFWorkbook) this.workbook;

	            // Get the drawing patriarch
	            Drawing<?> drawingPatriarch = xssfSheet.getDrawingPatriarch();
	            if (drawingPatriarch != null) {
	                // Iterate over all shapes in the drawing patriarch
	                for (XSSFShape shape : ((XSSFDrawing) drawingPatriarch).getShapes()) {
	                    if (shape instanceof Picture) {
	                        Picture picture = (Picture) shape;
	                        ClientAnchor anchor = picture.getClientAnchor();
	                        if (anchor != null) {
	                            int row = anchor.getRow1();
	                            if (row > lastRowWithImages) {
	                                lastRowWithImages = row;
	                            }
	                        }
	                    }
	                }
	            }
	        }

	        // Calculate the next row number for the new image
	        screenshotSheetRowNum = (lastRowWithImages >= 0) ? (lastRowWithImages + IMAGE_ROW_OFFSET) : 0;

	        System.out.println("Last row with image: " + lastRowWithImages);
	        System.out.println("Next row number for new image: " + screenshotSheetRowNum);
	    	}
	    
}

