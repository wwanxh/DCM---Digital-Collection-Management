package add_ons;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Anchor;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfAction;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfBorderDictionary;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfLayer;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.PdfWriter;

public class content_hider {
	public content_hider(String filename, String imagePath) throws DocumentException, IOException {
		Document doc = new Document();
		PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(filename));
		writer.setViewerPreferences(PdfWriter.PageModeUseOC);
		writer.setPdfVersion(PdfWriter.VERSION_1_5);
		doc.open();
		PdfLayer layer = new PdfLayer("Invisible Layer", writer);
		layer.setOn(false);
		BaseFont bf = BaseFont.createFont();
		PdfContentByte cb = writer.getDirectContent();
		cb.beginText();
		cb.setFontAndSize(bf, 18);
		cb.showTextAligned(Element.ALIGN_LEFT, "Optional content group:", 50, 790, 0);
		cb.beginLayer(layer);
		cb.showTextAligned(Element.ALIGN_LEFT, "TESTING ... ", 50, 766, 0);
		cb.endLayer();
		cb.endText();
		
		
		// Image
		Image image = Image.getInstance(imagePath);
		image.setAbsolutePosition(50f, 500f);
		//image.setLayer(layer);
		doc.add(image);
		
		// Annotation
//		Anchor anchor = new Anchor("Testing ... !!@.@");
//		anchor.setName("Anchor1");
//		doc.add(anchor);	
		Rectangle rect = new Rectangle(50, 500, 350, 700);
        PdfAnnotation annotation = PdfAnnotation.createFreeText(writer, rect, "DESCRIPTION HERE>>>>>", writer.getDirectContent().createAppearance(100, 100));
        PdfArray le = new PdfArray();
        le.add(new PdfName("OpenArrow"));
        le.add(new PdfName("None"));
        annotation.setTitle("You are here:");
        annotation.setFlags(PdfAnnotation.FLAGS_PRINT);
        annotation.setBorderStyle(
                new PdfBorderDictionary(5, PdfBorderDictionary.STYLE_DASHED));
        annotation.setLayer(layer);
        writer.addAnnotation(annotation);
		layer.setName("REF#1");
		
		writer.newPage();
		PdfLayer layer1 = new PdfLayer("Invisible Layer", writer);
		layer.setOn(false);
		BaseFont bf1 = BaseFont.createFont();
		PdfContentByte cb1 = writer.getDirectContent();
		cb1.beginText();
		cb1.setFontAndSize(bf1, 18);
		cb1.showTextAligned(Element.ALIGN_LEFT, "Optional content group:", 50, 790, 0);
		cb1.beginLayer(layer1);
		cb1.showTextAligned(Element.ALIGN_LEFT, "TESTING ... ", 50, 766, 0);
		cb1.endLayer();
		cb1.endText();
		layer1.setName("REF#2");
		
		
		doc.close();
		
	}
}
