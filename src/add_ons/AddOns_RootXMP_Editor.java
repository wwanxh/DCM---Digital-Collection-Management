/* 
 * Author: 	Xueheng Wan
 * Date:	Sep. 2017
 * 
 * Class:	AddOns_RootXMP_Editor
 * Project: DCM - Digital Collections Management
 * 
 * Description:	This class implements a DCM add-on function called Root XMP Editor,
 * 				which provides an ability to modify root level XMP data of PDF or PDF/A file.
 * 
 */

package add_ons;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAStamper;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.xml.xmp.DublinCoreSchema;
import com.itextpdf.text.xml.xmp.PdfASchema;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import com.itextpdf.text.xml.xmp.PdfSchema;
import com.itextpdf.text.xml.xmp.XmpArray;
import com.itextpdf.text.xml.xmp.XmpBasicProperties;
import com.itextpdf.text.xml.xmp.XmpBasicSchema;
import com.itextpdf.text.xml.xmp.XmpMMSchema;
import com.itextpdf.text.xml.xmp.XmpSchema;
import com.itextpdf.text.xml.xmp.XmpWriter;

import object.AddOns_root_metadata;

public class AddOns_RootXMP_Editor {
	
	String src;
	String dest;
	boolean overwritten;
	
	// path - path of target PDF file
	// overwritten - true if XMP data is wanted to be overwritten
	// Result file will be renamed as filename + "_Edited.pdf"
	public AddOns_RootXMP_Editor(String path, boolean overwritten){
		
		this.overwritten = overwritten;
		dest = path.substring(0, path.lastIndexOf('.')) + "_Edited.pdf";
		src = path;
		
	}
	
	public void manipulatePDF(AddOns_root_metadata rmobj) throws IOException, DocumentException{
		System.out.println(src);
		Document doc = new Document();
		System.out.println("DEST: " + dest);
		PdfAWriter writer = PdfAWriter.getInstance(doc, new FileOutputStream(dest+"123"), PdfAConformanceLevel.PDF_A_2B);
		writer.setPdfVersion(PdfAWriter.VERSION_1_7);
		
		/* Editing Root-Level Metadata */
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PdfAXmpWriter xmp = new PdfAXmpWriter(os, PdfAConformanceLevel.PDF_A_2B, writer);
        // Adding tags
		XmpSchema dc = new com.itextpdf.text.xml.xmp.DublinCoreSchema();
        XmpBasicSchema xbp = new XmpBasicSchema();
        XmpMMSchema xms = new XmpMMSchema();
		// More dc tags add here
        editing_DublinCoreProperties(dc, rmobj);
        xmp.addRdfDescription(dc);
        //More XMP and XmpMM tags add here
        editing_XMPProperties(xbp, xms, rmobj);
        xmp.addRdfDescription(xbp);
        xmp.addRdfDescription(xms);
        
        
        //Set XmpSchema in rdf:Description
        XmpBasicSchema xmpbasic = new XmpBasicSchema();
        xmpbasic.addCreateDate(new PdfDate().getW3CDate());
        xmpbasic.addModDate(new PdfDate().getW3CDate());
        xmp.addRdfDescription(xmpbasic);
        // Set PdfASChema in rdf:Description
        PdfASchema pdfa = new PdfASchema();
        pdfa.setProperty(PdfASchema.PART, "2");
        pdfa.setProperty(PdfASchema.CONFORMANCE, "B");
        xmp.addRdfDescription(pdfa);
        PdfSchema pdf = new PdfSchema();
        pdf.setProperty(PdfSchema.PRODUCER, "ADCM v1.0 (UAZ Afghanistan Digital Collections Management Software)");
        xmp.addRdfDescription(pdf);
        xmp.close();
        
        /* Output the New File */
        PdfReader r = new PdfReader(src);
//        byte[] meta = r.getMetadata();
//        System.out.println("METADATA: " + new String(meta));
		PdfAStamper stamper = new PdfAStamper(r, new FileOutputStream(dest), PdfAConformanceLevel.PDF_A_2B);
        stamper.createXmpMetadata();
		stamper.setXmpMetadata(os.toByteArray());
        stamper.close();
        r.close();
	}
	private void editing_DublinCoreProperties(XmpSchema dc, AddOns_root_metadata rmobj){
		editing_dc_helper(dc, DublinCoreSchema.CONTRIBUTOR, rmobj.contributors);
		editing_dc_helper(dc, DublinCoreSchema.COVERAGE, rmobj.coverage);
		editing_dc_helper(dc, DublinCoreSchema.CREATOR, rmobj.creators);
		editing_dc_helper(dc, DublinCoreSchema.DATE, rmobj.date);
		editing_dc_helper(dc, DublinCoreSchema.DESCRIPTION, rmobj.description);
		editing_dc_helper(dc, DublinCoreSchema.FORMAT, rmobj.format);
		editing_dc_helper(dc, DublinCoreSchema.IDENTIFIER, rmobj.identifier);
		editing_dc_helper(dc, DublinCoreSchema.LANGUAGE, rmobj.languages);
		editing_dc_helper(dc, DublinCoreSchema.PUBLISHER, rmobj.publishers);
		editing_dc_helper(dc, DublinCoreSchema.RELATION, rmobj.relations);
		editing_dc_helper(dc, DublinCoreSchema.RIGHTS, rmobj.rights);
		editing_dc_helper(dc, DublinCoreSchema.SOURCE, rmobj.source);
		editing_dc_helper(dc, DublinCoreSchema.SUBJECT, rmobj.subject);
		editing_dc_helper(dc, DublinCoreSchema.TITLE, rmobj.title);
		editing_dc_helper(dc, DublinCoreSchema.TYPE, rmobj.types);
	}
	private void editing_dc_helper(XmpSchema dc, String name, String value){
		if(value.isEmpty())
			; // Discard the empty field
		else
			dc.setProperty(name, value);
	}
	private void editing_dc_helper(XmpSchema dc, String name, ArrayList<String> value){
		if(value.size() == 0)
			; // Discard the empty field
		else{
			XmpArray arr;
			if(name.equals(DublinCoreSchema.CREATOR) || name.equals(DublinCoreSchema.DATE))
				arr = new XmpArray(XmpArray.ORDERED);
			else
				arr = new XmpArray(XmpArray.UNORDERED);
			for(int i = 0; i < value.size(); i ++)
				arr.add(value.get(i));
			dc.setProperty(name, arr);
		}
	}
	private void editing_XMPProperties(XmpBasicSchema xbp, XmpMMSchema xms, AddOns_root_metadata rmobj){
		editing_XMP_helper(xbp, XmpBasicSchema.CREATEDATE, rmobj.createdate);
		editing_XMP_helper(xbp, XmpBasicSchema.CREATORTOOL, rmobj.creatortool);
		editing_XMP_helper(xbp, XmpBasicSchema.IDENTIFIER, rmobj.identifier);
		editing_XMP_helper(xbp, XmpBasicSchema.METADATADATE, rmobj.metadatadate);
		editing_XMP_helper(xbp, XmpBasicSchema.MODIFYDATE, rmobj.modifydate);
		editing_XMP_helper(xms, XmpMMSchema.DERIVEDFROM, rmobj.derived_from);
		editing_XMP_helper(xms, XmpMMSchema.DOCUMENTID, rmobj.document_id);
		editing_XMP_helper(xms, XmpMMSchema.RENDITIONCLASS, rmobj.rendition_class);
		editing_XMP_helper(xms, XmpMMSchema.DERIVEDFROM, rmobj.derived_from);
		editing_XMP_helper(xms, XmpMMSchema.RENDITIONPARAMS, rmobj.rendition_params);
	}
	private void editing_XMP_helper(XmpBasicSchema xbp, String name, String value){
		if(value.isEmpty())
			; // Discard the empty field
		else
			xbp.setProperty(name, value);
	}
	private void editing_XMP_helper(XmpMMSchema xbp, String name, String value){
		if(value.isEmpty())
			; // Discard the empty field
		else
			xbp.setProperty(name, value);
	}
	private void editing_XMP_helper(XmpBasicSchema xbp, String name, ArrayList<String> value){
		if(value.size() == 0)
			; // Discard the empty field
		else{
			XmpArray arr;
			if(name.equals(DublinCoreSchema.CREATOR) || name.equals(DublinCoreSchema.DATE))
				arr = new XmpArray(XmpArray.ORDERED);
			else
				arr = new XmpArray(XmpArray.UNORDERED);
			for(int i = 0; i < value.size(); i ++)
				arr.add(value.get(i));
			xbp.setProperty(name, arr);
		}
	}
}
