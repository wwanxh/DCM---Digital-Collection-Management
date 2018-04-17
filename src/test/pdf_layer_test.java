package test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfLayer;
import com.itextpdf.text.pdf.PdfLayerMembership;
import com.itextpdf.text.pdf.PdfWriter;

public class pdf_layer_test {
	 
    /** The resulting PDF. */
    public static String RESULT = "layer_membership1.pdf";
 
    /**
     * Creates a PDF document.
     * @param filename the path to the new PDF document
     * @throws DocumentException 
     * @throws IOException 
     */
    public void createPdf(String filename) throws DocumentException, IOException  {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(
                document, new FileOutputStream(RESULT));
        writer.setPdfVersion(PdfWriter.VERSION_1_5);
        // step 3
        document.open();
        // step 4
        PdfContentByte cb = writer.getDirectContent();
 
        PdfLayer a = new PdfLayer("layer A", writer);
        PdfLayer b1 = new PdfLayer("layer B1", writer);
        PdfLayerMembership a1 = new PdfLayerMembership(writer);
        a1.addMember(a);
        a1.setVisibilityPolicy(PdfLayerMembership.ALLON);
        PdfLayerMembership a2 = new PdfLayerMembership(writer);
        a2.addMember(a);
        a2.setVisibilityPolicy(PdfLayerMembership.ALLON);
        PdfLayerMembership hasB= new PdfLayerMembership(writer);
        hasB.addMember(b1);
        cb.beginLayer(a);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("THIS IS A"),
                50, 775, 0);
        cb.endLayer();
        cb.beginLayer(a1);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("THIS IS A - 1"),
                50, 750, 0);
        cb.endLayer();
        cb.beginLayer(b1);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, new Phrase("THIS IS B1"),
                50, 725, 0);
        cb.endLayer();

 
        // step 5
        document.newPage();

        cb.beginLayer(a2);
        ColumnText.showTextAligned(cb, Element.ALIGN_LEFT,
                new Phrase("This is A - 2"), 50, 775, 0);
        cb.endLayer();
        document.close();
    }
 
    /**
     * A simple example with optional content.
     * 
     * @param args
     *            no arguments needed here
     * @throws IOException
     * @throws DocumentException
     */
    public static void main(String[] args) throws DocumentException,
            IOException {
        new pdf_layer_test().createPdf(RESULT);
    }
}