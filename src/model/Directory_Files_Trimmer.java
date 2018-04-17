/* 
 * Author: 	Xueheng Wan
 * Date:	Sep. 2017
 * 
 * Class:	Directory_Files_Trimmer
 * Project: DCM - Digital Collections Management
 * 
 * Description:	Remove immediate files
 * 
 */
package model;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class Directory_Files_Trimmer {
	private static boolean deleteTXT = false;
	private static boolean deletePDF = false;
	public Directory_Files_Trimmer(String path, boolean deleteTXT, boolean deletePDF){
		this.deleteTXT = deleteTXT;
		this.deletePDF = deletePDF;
		System.out.println("Starting Cleaning Directory " + path);
		recursion(path);
		System.out.println("ALL DONE!");
	}
	private void recursion(String path){
		String[] subdirs = new File(path).list(new FilenameFilter(){
			public boolean accept(File current, String name){
				return new File(current, name).isDirectory();
			}
		});
		for(int i = 0; i < subdirs.length; i ++){
			String subsrc = path + subdirs[i] + '/';
			recursion(subsrc);
		}
		List<String> needRemoved = searchFilestoBeDeleted(new File(path));
		for(String x : needRemoved){
			System.out.println("-------> Deleting " + x);
			new File(x).delete();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static List<String> searchFilestoBeDeleted(File dir){
        List<String> files = new ArrayList<>();
        //System.out.println(dir.getAbsolutePath());
        File[] list = dir.listFiles();
        Arrays.sort(list, new Comparator(){
        	@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return ((File) o1).getName().compareTo(((File) o2).getName());
			}
        });
        for (File f: list){
        	//Delete PDF Files
            if(deletePDF && isPDFFile(f.getName()))
                files.add(f.getAbsolutePath());
        	//Delete Log Files
            if(deleteTXT && isLogFile(f.getName()))
            	files.add(f.getAbsolutePath());
        }
        return files;
    }
	private static boolean isPDFFile(String fileName){
        Pattern p = Pattern.compile("\\.[pP][dD][Ff]");//Advantages of im4java: the interface of the IM commandline is quite stable, so your java program (and the im4java-library) will work across many versions of IM. im4java also provides a better OO interface (the "language" of the IM-commandline with it's postfix-operation notation translates very easily into OO-notation). And most important: you can use im4java everywhere JMagick can't be used because of the JNI hazard (e.g. java application servers).
        Matcher m = p.matcher(fileName);
        return m.find();
    }

	private static boolean isLogFile(String fileName){
        return fileName.contains("log");
    }

}
