package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ICC_Profile;
import com.itextpdf.text.pdf.PdfAConformanceLevel;
import com.itextpdf.text.pdf.PdfAWriter;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.xml.xmp.DublinCoreSchema;
import com.itextpdf.text.xml.xmp.PdfASchema;
import com.itextpdf.text.xml.xmp.PdfAXmpWriter;
import com.itextpdf.text.xml.xmp.PdfSchema;
import com.itextpdf.text.xml.xmp.XmpArray;
import com.itextpdf.text.xml.xmp.XmpBasicSchema;
import com.itextpdf.text.xml.xmp.XmpMMProperties;
import com.itextpdf.text.xml.xmp.XmpMMSchema;
import com.itextpdf.text.xml.xmp.XmpSchema;
import com.itextpdf.xmp.XMPException;

import object.Metadata_Manipulation;
import view.DCM_GUI;

public class PDF_Generator extends Observable{
	/** A path to a color profile. */
    public String ICC = "/ICC/sRGB_CS_profile.icm";
	
	String Top_Dir;
	ArrayList<String> jp2s;
	ArrayList<String> xmls;
	private List<String> notes;
	
	public PDF_Generator(String Top_Dir, DCM_GUI gui, int compression, List<String> notes) throws IOException, DocumentException, XMPException{
		this.notes = notes;
		this.addObserver(gui);
		jp2s = new ArrayList<String>();
		xmls = new ArrayList<String>();
		this.Top_Dir = Top_Dir;
		if(new File(Top_Dir).isDirectory())
			Dir_Handler(compression);
		else
			Single_Handler(compression);
	}
	public PDF_Generator(String Top_Dir, DCM_GUI gui, List<String> notes) throws IOException, DocumentException, XMPException{
		this.notes = notes;
		this.addObserver(gui);
		jp2s = new ArrayList<String>();
		xmls = new ArrayList<String>();
		this.Top_Dir = Top_Dir;
		if(new File(Top_Dir).isDirectory()){
			Dir_Handler(30);
			Dir_Handler(50);
			Dir_Handler(-1); //The lossless version
		}else {
			Single_Handler(30);
			Single_Handler(50);
			Single_Handler(100);
		}
	}
	private void Single_Handler(int Compression)throws IOException, DocumentException, XMPException{
			
		// -------------------- Unfinished Work - Implementation Needed ---------------------------
		
	}
	//for generating PDF/A with compression level
    private void Dir_Handler(int Compression) throws IOException, DocumentException, XMPException{
    	FileReader fr = Compression == -1 || Compression == 100 ? 
    			new FileReader(Top_Dir+"log.txt") : new FileReader(Top_Dir+"log_" + Compression + ".txt");
    			
		BufferedReader br = new BufferedReader(fr);
		try{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			if(line.compareTo(Top_Dir) != 0)
				System.out.println("Log file does not match!");
			while((line = br.readLine()) != null){
				String[] splited = line.split("\\s+"); //split the string by space, first one is JP2 path, second one is XML path
				jp2s.add(splited[0]);
				xmls.add(splited[1]);
			}
		}finally{
			br.close();
		}
		
		if(jp2s.isEmpty())
			return;
		//generating PDF file
    	Document doc = new Document();
    	String title = Top_Dir.substring(Top_Dir.substring(0, Top_Dir.length()-1).lastIndexOf('/')+1, Top_Dir.length()-1);
    	String filename = (Compression == -1 || Compression == 100) ? Top_Dir + title + "_m.pdf" : Top_Dir + title + "_w_" + Compression + ".pdf";
    	PdfAWriter writer = PdfAWriter.getInstance(doc, new FileOutputStream(filename), PdfAConformanceLevel.PDF_A_3B);
	    writer.setPdfVersion(PdfAWriter.VERSION_1_7);
        writer.setTagged();
        writer.setViewerPreferences(PdfWriter.DisplayDocTitle);

        /* Adding Custom Metadata Tags on ROOT LEVEL */
        ByteArrayOutputStream os = Generating_Root_XMP(writer, title);
        writer.setXmpMetadata(os.toByteArray());
        //writer.createXmpMetadata(); //Automatically generating root XMP with empty content, we do not need this
        
        doc.open();
        ICC_Profile icc;
        System.out.println("Current Directory: " + System.getProperty("user.dir"));
        icc = ICC_Profile.getInstance(this.getClass().getResourceAsStream(ICC));
        writer.setOutputIntents("Custom", "", "http://www.color.org", "sRGB IEC61966-2.1", icc);
        System.out.println("*  -Converting ... " + jp2s.size() + " pages for " + filename);
        ArrayList<String> errors = new ArrayList<>();
        for(int j= 0; j< jp2s.size(); j ++){
        	String JP2_PATH = Top_Dir + jp2s.get(j);
        	String XML_PATH = Top_Dir + xmls.get(j);
        	// Added Bug Handling (If files are missing!)
        	if(!new File(JP2_PATH).exists() || !new File(XML_PATH).exists()){
        		errors.add(jp2s.get(j));
        		errors.add(xmls.get(j));
        		continue;
        	}
    		//System.out.println("Now processing ... " + JP2_PATH);
        	Image i = null;
    		try{
    			i = Image.getInstance(JP2_PATH);
    		}catch(Exception e){
    			errors.add(jp2s.get(j));
        		errors.add(xmls.get(j));
        		continue;
    		}
	        //System.out.println("X and Y are: " + i.getWidth() + "," + i.getHeight());
	        Rectangle one = new Rectangle(i.getWidth(), i.getHeight());
	        doc.setPageSize(one);
	        doc.newPage();
	        Metadata_Manipulation sm = new Metadata_Manipulation(XML_PATH);
	        byte[] bxml = sm.getBytes();
	        writer.setPageXmpMetadata(bxml);
		    String fname = jp2s.get(j).substring(1);
		    int pos = fname.lastIndexOf(".");
		    if(pos > 0)
		    	fname = fname.substring(0, pos);
    		doc.addHeader(fname,"");
	        doc.add(i);  
    	}
        if(errors.isEmpty()) doc.close(); 
        if(!errors.isEmpty()){
        	FileWriter fw = new FileWriter(Top_Dir+"ErrorFiles.txt");
	  		BufferedWriter bw = new BufferedWriter(fw);
	    	for(int i = 0; i < errors.size(); i += 2){
	    		bw.write(errors.get(i) + ' ');
	    		bw.write(errors.get(i+1) + '\n');
	    	}
	    	bw.close();
        }
        if(Top_Dir.length() > 20) Progress_Report("..."+Top_Dir.substring(Top_Dir.length()-20) + title + "_merge.pdf");
        else Progress_Report(Top_Dir + title + "_merge.pdf");
        
        jp2s.clear();
        xmls.clear();
	}
	
	/* Adding Custom Metadata Tags on ROOT LEVEL */
	private ByteArrayOutputStream Generating_Root_XMP(PdfAWriter writer, String title) throws IOException{
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		PdfAXmpWriter xmp = new PdfAXmpWriter(os, PdfAConformanceLevel.PDF_A_3B, writer);
        // Adding tags
		XmpSchema dc = new com.itextpdf.text.xml.xmp.DublinCoreSchema();
		dc.setProperty(DublinCoreSchema.TITLE, title);
        XmpArray contributor = new XmpArray(XmpArray.UNORDERED);
        contributor.add("The University of Arizona Libraries");
        contributor.add("Afghan Center at Kabul University");
        dc.setProperty(DublinCoreSchema.CONTRIBUTOR, contributor);
        dc.setProperty(DublinCoreSchema.RELATION, "Afghanistan Digital Collections");
        if(notes.size() != 0){
        	XmpArray note = new XmpArray(XmpArray.UNORDERED);
        	for(String a : notes)
        		note.add(a);       
        	dc.setProperty(DublinCoreSchema.DESCRIPTION, note);
        }
        // More dc tags add here
        xmp.addRdfDescription(dc);
        
        
        //Set XmpSchema in rdf:Description
        XmpBasicSchema xmpbasic = new XmpBasicSchema();
        xmpbasic.addCreateDate(new PdfDate().getW3CDate());
        xmpbasic.addModDate(new PdfDate().getW3CDate());
        xmp.addRdfDescription(xmpbasic);
        
        // Adding UUID
        XmpMMSchema xmpmm = new XmpMMSchema();
        
        xmpmm.setProperty(xmpmm.DOCUMENTID, "123");
        //xmpmm.setProperty("xmp:InstanceID", "234");
        xmp.addRdfDescription(xmpmm);
        
        

        
        // Set PdfASChema in rdf:Description
        PdfASchema pdfa = new PdfASchema();
        pdfa.setProperty(PdfASchema.PART, "3");
        pdfa.setProperty(PdfASchema.CONFORMANCE, "B");
        xmp.addRdfDescription(pdfa);
        
        PdfSchema pdf = new PdfSchema();
        pdf.setProperty(PdfSchema.PRODUCER, "ADCM v1.6 (UAZ Afghanistan Digital Collections Management Software)");
        xmp.addRdfDescription(pdf);
        
        xmp.close();
        return os;
	}
    public void Progress_Report(String file){
    	setChanged();
    	notifyObservers(file);
    }
}
