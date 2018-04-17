/* 
 * Author: 	Xueheng Wan
 * Date:	Sep. 2017
 * 
 * Class:	Error_Files_Picker
 * Project: DCM - Digital Collections Management
 * 
 * Description:	This class takes two directory paths as parameters, computes and return number of 
 * 				duplicate files that appear in both directories.
 * 
 */
package model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

public class Error_Files_Picker {
	private String Top_Error_Folder;
	private ArrayList<String> error_folders;
	public Error_Files_Picker(String Top_Dir) throws IOException{
		error_folders = new ArrayList<String>();
		recursion_to_findfolders(Top_Dir);
		Top_Error_Folder = Top_Dir + "ErrorFolders/";
		new File(Top_Error_Folder).mkdirs();
		savingLogFile();
		movingErrorFolders();
	}
	private void savingLogFile() throws IOException{
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Top_Error_Folder + "log.txt"), "utf-8"));
		writer.write(Top_Error_Folder + '\n');
		for(int i = 0; i < error_folders.size(); i ++)
			writer.write(error_folders.get(i) + '\n'); // For each line (JP2_PATH + EMPTY_CHARACTER + XML_PATH)
		writer.close();
	}
	private void movingErrorFolders() throws IOException{
		System.out.println("** -Moving " + error_folders.size() + " Error Folders into " + Top_Error_Folder);
		for(String a : error_folders){
			String filename = a.substring(0, a.lastIndexOf('/'));
			filename = filename.substring(filename.lastIndexOf('/'), filename.length());
			FileUtils.moveDirectory(new File(a), new File(Top_Error_Folder+filename));
		}
	}
	private void recursion_to_findfolders(String path){
		String[] subdirs = new File(path).list(new FilenameFilter(){
			public boolean accept(File current, String name){
				return new File(current, name).isDirectory();
			}
		});
		if(subdirs == null) return;
		for(int i = 0; i < subdirs.length; i ++){
			String subsrc = path + subdirs[i] + '/';
			recursion_to_findfolders(subsrc);
		}
		if(checkIsAErrorFolder(path)){
			System.out.println(path);
			error_folders.add(path);
		}
	}
	private boolean checkIsAErrorFolder(String dir){
		String[] files = new File(dir).list(new FilenameFilter(){
			public boolean accept(File current, String name){
				if(new File(current, name).isFile())
					return name.contains("ErrorFiles.txt");
				return false;
			}
		});
		if(files.length != 0)
			return true;
		return false;
	}

}
