package model;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class Directory_Name_Trimmer {
	
	public Directory_Name_Trimmer(String src) throws IOException{
		File src_dir = new File(src);
		String[] subdirs = src_dir.list(new FilenameFilter(){
			public boolean accept(File current, String name){
				return new File(current, name).isDirectory();
			}
		});
		for(int i = 0; i < subdirs.length; i ++){
			File temp = new File(src + subdirs[i]);
			String subsrc = src + subdirs[i] + '/';
			run(subsrc);
		}
		
	}
	private void run(String src) throws IOException{
		File src_dir = new File(src);
		String[] subdirs = src_dir.list(new FilenameFilter(){
			public boolean accept(File current, String name){
				return new File(current, name).isDirectory();
			}
		});
		for(int i = 0; i < subdirs.length; i ++){
			File temp = new File(src + subdirs[i]);
			String subsrc = src + subdirs[i] + '/';
			Moving(subsrc, src);
		}
		for(int i = 0; i < subdirs.length; i ++){
			FileUtils.deleteDirectory(new File(src + subdirs[i]));
		}
	}
	private void Moving(String subsrc, String top_dir) throws IOException{
		File[] list = new File(subsrc).listFiles(new FileFilter(){

			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				return pathname.isDirectory();
			}
			
		});
		for(File a : list)
			FileUtils.moveDirectoryToDirectory(new File(a.getAbsolutePath()), new File(top_dir), false);
	}

}
