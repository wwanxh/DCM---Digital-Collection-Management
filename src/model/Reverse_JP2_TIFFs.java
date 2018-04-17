package model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Observable;

import com.itextpdf.text.exceptions.UnsupportedPdfException;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;

import view.DCM_GUI;

public class Reverse_JP2_TIFFs extends Observable{

	public String src;
	boolean Retain_JP2 =false;
	public DCM_GUI gui;
	
	public Reverse_JP2_TIFFs(String PDF_Path){
		src = PDF_Path;
	}
	public Reverse_JP2_TIFFs(String PDF_Path, DCM_GUI gui){
		this.gui = gui;
		this.addObserver(gui);
		src = PDF_Path;
	}
	public void PDFtoJP2(String dest) throws IOException{
		PdfReader reader = new PdfReader(src);
		PdfObject obj;
		int counter = 0;
		ArrayList<String> jp2list = new ArrayList<>();
		for(int i = 1; i <= reader.getXrefSize(); i ++){
			obj = reader.getPdfObject(i);
			if(obj != null && obj.isStream()){
				PRStream stream = (PRStream) obj;
				byte[] b;
				try{
						b = PdfReader.getStreamBytes(stream);
				}catch(UnsupportedPdfException e){
						b = PdfReader.getStreamBytesRaw(stream);
				}
				PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
				//System.out.println(pdfsubtype);
				FileOutputStream fos = null;
				if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.XML.toString())) {
					fos = new FileOutputStream(String.format(dest + counter+".xmp", i));
					//System.out.println("Page XML has been Extracted!");
					fos.write(b);
					fos.flush();
					fos.close();
				}
				if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
					counter ++;
					//System.out.println("height:" + stream.get(PdfName.HEIGHT));
					//System.out.println("width:" + stream.get(PdfName.WIDTH));
					fos = new FileOutputStream(String.format(dest + counter+".jp2", i));
					jp2list.add(dest + counter+".jp2");
					fos.write(b);
					fos.flush();
					fos.close();
				}
			}
		}
		System.out.println("File Conversion Finished!!!");
		Process proc;
		for(String file : jp2list){
			proc = Runtime.getRuntime().exec("exiv2 -iXX " + file);
			new File(file.substring(0, file.length()-3) + "xmp").delete(); //delete metadata sidecar files
		}
	}
	// The only parameter indicates the need to retain JP2 files. (PDF extract-> JP2 transform-> TIFF)
	public void PDFtoTIFF(String dest) throws IOException{ 
		System.out.println("User chooses retaining JP2 intermediate files: " + Retain_JP2);
		PdfReader reader = new PdfReader(src);
		PdfObject obj;
		int counter = 1;
		ArrayList<String> tifflist = new ArrayList<>();
		System.out.println('+'+reader.getXrefSize());
		for(int i = 0; i < reader.getXrefSize(); i ++){
			obj = reader.getPdfObject(i);
			if(obj != null && obj.isStream()){
				PRStream stream = (PRStream) obj;
				byte[] b;
				try{
						b = PdfReader.getStreamBytes(stream);
				}catch(UnsupportedPdfException e){
						b = PdfReader.getStreamBytesRaw(stream);
				}
				PdfObject pdfsubtype = stream.get(PdfName.SUBTYPE);
				//System.out.println(pdfsubtype);
				FileOutputStream fos = null;
				if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.XML.toString())) {
					counter ++;
					System.out.println("XML: " + i + ' ' + counter/2);
					fos = new FileOutputStream(String.format(dest + counter/2 +".xmp", i));
					//System.out.println("Page XML has been Extracted!");
					fos.write(b);
					fos.flush();
					fos.close();
				}
				if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString())) {
					counter ++;
					//System.out.println("height:" + stream.get(PdfName.HEIGHT));
					//System.out.println("width:" + stream.get(PdfName.WIDTH));
					String filename = dest + counter/2;
					fos = new FileOutputStream(String.format(filename +".jp2", i));
					tifflist.add(filename);
					fos.write(b);
					fos.flush();
					fos.close();
				}
			}
		}
		// Rename last xml file, it contains the root level metadata
		new File(dest + counter/2 + ".xmp").renameTo(new File(dest + "root.xmp"));
		Progress_Report(src);
		System.out.println("All Pages of the PDF have been converted to TIFFs!");
		Process proc;
		for(String file : tifflist){
			//System.out.println("*** Max Memory: " + Runtime.getRuntime().maxMemory() + "\n Total Using Memory: "
					//+ Runtime.getRuntime().totalMemory() + "\n Total Avalible Processors: " + Runtime.getRuntime().availableProcessors());
			proc = Runtime.getRuntime().exec("convert " + file + ".jp2 " + file + ".tiff");
			/*try {
 				proc.waitFor();
 				} catch (InterruptedException e) {
 					System.out.println("Error in Converting from JP2 to TIFF!");
 					e.printStackTrace();
 				}*/
			proc = Runtime.getRuntime().exec("exiv2 -iXX " + file + ".tiff");
			if(Retain_JP2) Runtime.getRuntime().exec("exiv2 -iXX " + file + ".jp2");
			new File(file.substring(0, file.length()-3) + "xmp").delete(); //delete metadata sidecar files
			if(!Retain_JP2) new File(file + ".jp2").delete(); //if user choose option of deleting JP2 intermediate files
		}
	}
	public void setRetain_JP2(boolean choice){
		this.Retain_JP2 = choice;
	}
	public void Progress_Report(String file){
    	setChanged();
    	notifyObservers(file);
    }
}
