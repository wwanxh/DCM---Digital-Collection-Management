package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.io.FileUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.xmp.XMPException;

import model.Directory_Files_Trimmer;
import model.Directory_Name_Trimmer;
import model.Directory_Reconstructor;
import model.Error_Files_Picker;
import model.PDF_Generator;
import model.Progress_Reporter;
import model.TIFFs_Importer;
import view.DCM_GUI;

public class Controller{
	
	final String MV = "move";
	final String CP = "copy";
	
	String Top_Dir; // end with '/'
	String Top_Dest_Dir; //end with '/'
	Progress_Reporter PR;
	DCM_GUI gui;
	String MVorCP = "";
	
	public Controller(String Top_Dir, String Top_Dest_Dir, DCM_GUI gui, String MVorCP, int compression) throws IOException, DocumentException, XMPException, InterruptedException{
		this.MVorCP = MVorCP;
		//Initial Observer
		PR = new Progress_Reporter(Top_Dir);
		this.gui = gui;
		this.Top_Dir = Top_Dir;
		this.Top_Dest_Dir = Top_Dest_Dir;
		new File(Top_Dest_Dir).mkdirs();
		System.out.println("**" + Top_Dest_Dir + "was created!");
		Converting(Top_Dir, Top_Dest_Dir, compression);
		new Directory_Files_Trimmer(Top_Dest_Dir, true, false); // Copy / Move / Remain raw tiff files operations
		System.out.println("Move Error Files ...");
		String errors = Top_Dest_Dir.substring(0, Top_Dest_Dir.lastIndexOf("/")) + "Error_Files/";
		new Error_Files_Picker(errors); // Grabbing out all error files
		//new Directory_Reconstructor(Top_Dest_Dir); // Moving all PDFs to top directory
		System.out.println("** ALL PDFs have been generated!");
	}
	// This constructor is built only for Mode A
	public Controller(String Top_Dir, String Top_Dest_Dir, DCM_GUI gui, String MVorCPorRemain) throws IOException, DocumentException, XMPException, InterruptedException{
		MVorCP = MVorCPorRemain;
		//Initial Observer
		PR = new Progress_Reporter(Top_Dir);
		this.gui = gui;
		this.Top_Dir = Top_Dir;
		this.Top_Dest_Dir = Top_Dest_Dir;
		new File(Top_Dest_Dir).mkdirs();
		System.out.println("**" + Top_Dest_Dir + "was created!");
		Coverting_Mode_A(Top_Dir, Top_Dest_Dir);
		//new Directory_Name_Trimmer(Top_Dest_Dir);
		new Directory_Files_Trimmer(Top_Dest_Dir, true, false);
		System.out.println("Move Error Files ...");
		String errors = Top_Dest_Dir.substring(0, Top_Dest_Dir.lastIndexOf("/")) + "Error_Files/";
		new Error_Files_Picker(errors); // Grabbing out all error files
		new Directory_Reconstructor(Top_Dest_Dir); // Moving all PDFs to top directory
		System.out.println("** ALL PDFs have been generated!");
	}
	
	private void Converting(String src, String dest, int compression) throws IOException, DocumentException, XMPException, InterruptedException{
		System.out.println("* Processing " + src + "!");
		File src_dir = new File(src);
		String[] subdirs = src_dir.list(new FilenameFilter(){
			public boolean accept(File current, String name){
				return new File(current, name).isDirectory();
			}
		});
		TIFFs_Importer ti = new TIFFs_Importer(src, dest, gui, compression);
		PDF_Generator pg = new PDF_Generator(dest, gui, compression, ti.findNotes(src));
		Deleting(dest, compression == 100 ? "" : "_"+compression);
		if(MVorCP.equals(MV)) movingTIFF(src, dest);
		else if(MVorCP.equals(CP)) copyingTIFF(src,dest);
		if(subdirs != null)
			for(int i = 0; i < subdirs.length; i ++){
				new File(dest + subdirs[i]).mkdirs();
				String subdest = dest + subdirs[i] + '/';
				String subsrc = src + subdirs[i] + '/';
				Converting(subsrc, subdest, compression);
				System.out.println("*  -TIFFs in " + subsrc + " have been converted and extracted into JP2s and XMLs!");
			}
	}
	private void Coverting_Mode_A(String src, String dest) throws IOException, DocumentException, XMPException, InterruptedException{
		System.out.println("* Processing " + src + "!");
		File src_dir = new File(src);
		String[] subdirs = src_dir.list(new FilenameFilter(){
			public boolean accept(File current, String name){
				return new File(current, name).isDirectory();
			}
		});
		System.out.println("Generating 3 PDF/A-B2 files with Compression Level lossless, 30 ,50");
		TIFFs_Importer ti = new TIFFs_Importer(src, dest, gui);
		PDF_Generator pg = new PDF_Generator(dest, gui, ti.findNotes(src));
		Deleting(dest, "");
		Deleting(dest, "_30");
		Deleting(dest, "_50");
		if(MVorCP.equals(MV)) movingTIFF(src, dest);
		else if(MVorCP.equals(CP)) copyingTIFF(src,dest);
		else ;
		for(int i = 0; i < subdirs.length; i ++){
			new File(dest + subdirs[i]).mkdirs();
			String subdest = dest + subdirs[i] + '/';
			String subsrc = src + subdirs[i] + '/';
			Coverting_Mode_A(subsrc, subdest);
			System.out.println("*  -TIFFs in " + subsrc + " have been converted and extracted into JP2s and XMLs!");
		}
		
	}
	private void Deleting(String src, String suffix) throws IOException{
		System.out.println("*  -Deleting " + src + "!");
		BufferedReader br = new BufferedReader(new FileReader(src+"log" + suffix + ".txt"));
		try{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			if(line.compareTo(src) != 0)
				System.out.println("Log file does not match!");
			while((line = br.readLine()) != null){
				String[] splited = line.split("\\s+"); //split the string by space, first one is JP2 path, second one is XML path
				new File(src + splited[0]).delete();
				new File(src + splited[1]).delete();
			}
		}finally{
			br.close();
		}
	}
	private void movingTIFF(String src, String dest) throws IOException{
		System.out.println("** -Moving TIFFs into " + dest);
		File[] list = new File(src).listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				return !pathname.isDirectory();
			}
		});
		for(File a : list)
			FileUtils.moveFileToDirectory(new File(a.getAbsolutePath()), new File(dest), false);
	}
	private void copyingTIFF(String src, String dest) throws IOException{
		System.out.println("** -Copying TIFFs into " + dest);
		FileFilter filter = new FileFilter(){
			@Override
			public boolean accept(File arg0) {
				// TODO Auto-generated method stub
				return !arg0.isDirectory();
			}
		};
		FileUtils.copyDirectory(new File(src), new File(dest), filter);
	}
}
