/* 
 * Author: 	Xueheng Wan
 * Date:	Sep. 2017
 * 
 * Class:	Attachment_Core
 * Project: DCM - Digital Collections Management
 * 
 * Description:	This class provides an ability to attach a plain text (txt files) to PDF
 * 				*** This class is in test phase, so please modify code before using.
 * 
 */
package model;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAStamper;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.xml.xmp.XmpWriter;

public class Attachment_Core {
	
	public Attachment_Core(String src, String dest, String notefilename) throws IOException, DocumentException{
		PdfReader reader = new PdfReader(src);
		PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(dest), PdfAConformanceLevel.PDF_A_3B);
		
		PdfDictionary parameters = new PdfDictionary();
		parameters.put(PdfName.MODDATE, new PdfDate());
		PdfFileSpecification fs = PdfFileSpecification.fileEmbedded(stamper.getWriter(), notefilename, "notes", null, 
				"text/plain", parameters, 0);
		fs.put(new PdfName("AFRelationship"), new PdfName("Data"));
		
		stamper.addFileAttachment(new File(notefilename).getName(), fs);
		PdfArray array = new PdfArray();
		array.add(fs.getReference());
		stamper.getWriter().getExtraCatalog().put(new PdfName("AF"), array);
		
		stamper.close();
		
		
	}
	public Attachment_Core(String src, String dest, String notefilename, boolean asMetadata) throws IOException, DocumentException{
		PdfReader reader = new PdfReader(src);
		PdfAStamper stamper = new PdfAStamper(reader, new FileOutputStream(dest), PdfAConformanceLevel.PDF_A_3B);
		
		Map info = reader.getInfo();
		info.put("Description", "123missed");
		stamper.setMoreInfo(info);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XmpWriter xmp = new XmpWriter(baos,info);
		xmp.close();
		stamper.setXmpMetadata(baos.toByteArray());
		stamper.close();
		reader.close();
	}
	
}
