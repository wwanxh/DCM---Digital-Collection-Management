package controller;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.Reverse_JP2_TIFFs;
import view.DCM_GUI;

public class Reverse_Controller {

	public final String FORMAT_JP2 = "JP2";
	public final String FORMAT_TIFF = "TIFF";
	boolean isDir = false;
	String src;
	DCM_GUI gui;
	
	public Reverse_Controller(String src){
		if(new File(src).isDirectory())
			isDir = true;
		this.src = src;
	}
	public Reverse_Controller(String src, DCM_GUI gui){
		this.gui = gui;
		if(new File(src).isDirectory())
			isDir = true;
		this.src = src;
	}
	public void Conversion(String dest, String FORMAT, boolean Retain_JP2) throws IOException{
		
		if(!isDir){
			Reverse_JP2_TIFFs rjt = new Reverse_JP2_TIFFs(src, gui);
			rjt.setRetain_JP2(Retain_JP2);
			if(FORMAT == FORMAT_JP2) rjt.PDFtoJP2(dest);
			else if(FORMAT == FORMAT_TIFF) rjt.PDFtoTIFF(dest);
		}else
			ConversionHelper(src, dest, FORMAT, Retain_JP2);
	}
	private void ConversionHelper(String s, String dest, String FORMAT, boolean Retain_JP2) throws IOException{
		System.out.println("* Processing " + s + "!");
		File src_dir = new File(s);
		String[] subdirs = src_dir.list(new FilenameFilter(){
			public boolean accept(File current, String name){
				return new File(current, name).isDirectory();
			}
		});
		String[] pdfs = src_dir.list(new FilenameFilter(){
			public boolean accept(File current, String name){
				return isPDF(name);
			}
		});
		//Converting PDF in current folder
		System.out.println("There are " + pdfs.length + " PDF files in " + s);
		for(String pdfFile : pdfs){
			Reverse_JP2_TIFFs rjt = new Reverse_JP2_TIFFs(s + pdfFile, gui);
			rjt.setRetain_JP2(Retain_JP2);
			if(FORMAT == FORMAT_JP2) rjt.PDFtoJP2(dest);
			else if(FORMAT == FORMAT_TIFF) rjt.PDFtoTIFF(dest);
		}
		//Subdirectories
		for(int i = 0; i < subdirs.length; i ++){
			new File(dest + subdirs[i]).mkdirs();
			String subdest = dest + subdirs[i] + '/';
			String subsrc = s + subdirs[i] + '/';
			ConversionHelper(subsrc, subdest, FORMAT, Retain_JP2);
			System.out.println("*  -PDFs in " + subsrc + " have been extracted into JP2s or TIFFs!");
		}
	}
	private static boolean isPDF(String fileName){
        Pattern p = Pattern.compile("\\.[pP][dD][Ff]");//Advantages of im4java: the interface of the IM commandline is quite stable, so your java program (and the im4java-library) will work across many versions of IM. im4java also provides a better OO interface (the "language" of the IM-commandline with it's postfix-operation notation translates very easily into OO-notation). And most important: you can use im4java everywhere JMagick can't be used because of the JNI hazard (e.g. java application servers).
        Matcher m = p.matcher(fileName);
        return m.find();
    }
	
}
