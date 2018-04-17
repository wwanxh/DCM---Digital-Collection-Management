package test;

import java.io.IOException;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.parser.FilteredTextRenderListener;
import com.itextpdf.text.pdf.parser.LocationTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;

public class find_annot {

	public static void main(String arg[]) throws IOException {
		PdfReader reader = new PdfReader("/home/wan/Downloads/Spare-Part-Catalog_Engine.pdf");
                 
        // For each PDF page
        for (int i = 1; i <= reader.getNumberOfPages(); i++) {

            // Get a page a PDF page 
            PdfDictionary page = reader.getPageN(i);
            // Get all the annotations of page i
            PdfArray annotsArray = page.getAsArray(PdfName.ANNOTS);

            // If page does not have annotations
            if (page.getAsArray(PdfName.ANNOTS) == null) {
                continue;
            }

            // For each annotation
            for (int j = 0; j < annotsArray.size(); ++j) {
                // For current annotation
                PdfDictionary curAnnot = annotsArray.getAsDict(j);
                // Check the annotation subtype and print its text if not null
                //writeAnnotation(curAnnot, reader, i);
                System.out.println(curAnnot.get(PdfName.SUBTYPE));
            }
        }
	}
	 /**
     * Check the annotation subtype and print its text.
     * 
     * @param annot annotation to write.
     * @param reader pdf document containing the annotation.
     * @param pageNumber pdf page number containing the annotation.
     * @throws IOException 
     */
    public static void writeAnnotation(PdfDictionary annot, PdfReader reader, int pageNumber) throws IOException {
        
        if(annot == null) {
            return;
        }
        
        System.out.print(annot.get(PdfName.SUBTYPE));
        System.out.print(" -> Rect: " + annot.get(PdfName.RECT)); 
        
        PdfString text = null;
        boolean mayHaveTextAnnotated = false;
        
        // Highlights with comments (balloons) and Highliths 
        if (PdfName.HIGHLIGHT.equals(annot.get(PdfName.SUBTYPE))) {
            // Only Highlights with comments may have text
            text = (PdfString) annot.get(PdfName.CONTENTS);
            mayHaveTextAnnotated = true;
        } else if (PdfName.UNDERLINE.equals(annot.get(PdfName.SUBTYPE))) {
            text = annot.getAsString(PdfName.CONTENTS);
            mayHaveTextAnnotated = true;
        // A comment (a balloon with a comment)
        } else if (PdfName.TEXT.equals(annot.get(PdfName.SUBTYPE))) {
            text = annot.getAsString(PdfName.CONTENTS);
        } else {
            text = annot.getAsString(PdfName.CONTENTS);
        }
        
        if(text != null) {
           System.out.println(" -> " + text); 
        }
  
        if(mayHaveTextAnnotated) {
            PdfArray rectangle = (PdfArray) annot.get(PdfName.RECT); // ex: [82.1569, 757.575, 124.395, 769.305]
            String textHighlighted = getTextFromRectangle(rectangle, reader, pageNumber);
            if(textHighlighted != null) {
                System.out.println(" Annotated text -> " + textHighlighted);
            }
        }
    }
    
    
    /**
     * Extracts the text {@code rectangle}, located on page {@code pageNumber}
     * of the pdf {@code reader}.
     * <p>The text extracted by this method is not perfect. Usually extracts an 
     * unnecessary (not desirable) extra character. For instance, for an annotations
     * like "[This is an annotated] text", the method will extract 
     * "This is an annotated t". </p> The extra character may appear before or 
     * after the annotated text.
     * 
     * @param reader
     * @param pageNumber
     * @param rectangle ex: [82.1569, 757.575, 124.395, 769.305]
     * @return the extracted text or null.
     * @throws IOException 
     */
    public static String getTextFromRectangle(PdfArray rectangle, 
            PdfReader reader, int pageNumber) throws IOException {
        
        if(rectangle == null) {
            return null;
        }
        
        // Get the retangle coodinates
        float llx = rectangle.getAsNumber(0).floatValue();
        float lly = rectangle.getAsNumber(1).floatValue();
        float urx = rectangle.getAsNumber(2).floatValue();
        float ury = rectangle.getAsNumber(3).floatValue();
        
        Rectangle rect = new Rectangle(llx, lly, urx, ury);
        RenderFilter filter = new RegionTextRenderFilter(rect);
        TextExtractionStrategy strategy = 
                new FilteredTextRenderListener(
                    new LocationTextExtractionStrategy(), filter);
        
        return PdfTextExtractor.getTextFromPage(reader, pageNumber, strategy);
    }
}
