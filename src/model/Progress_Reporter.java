package model;
import java.io.File;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import java.lang.Object;

import model.PDF_Generator;
import model.TIFFs_Importer;
import view.DCM_GUI;

public class Progress_Reporter implements Observer, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int totalFiles;
	private int count;
	public Progress_Reporter(String path){
		count = 0;
		System.out.println("* Analyzing Source Directory \"" + path + "\"......");
		totalFiles = fileCounter(path);
		System.out.println("* Calculated Target Directory Size " + totalFiles);
		
	}
	private int fileCounter(String path){
		int counter = 0;
		File src_dir = new File(path);
		String[] subdirs = src_dir.list(new FilenameFilter(){
			public boolean accept(File current, String name){
				return new File(current, name).isDirectory();
			}
		});
		if(!ArrayUtils.isEmpty(subdirs))
			for(String i : subdirs){
				counter += fileCounter(path + i + '/');
			}
		counter += searchTiffsInDir(src_dir);
		return counter;
	}
	private static int searchTiffsInDir(File dir){
        List<String> tiffs = new ArrayList<>();
        //System.out.println(dir.getAbsolutePath());
        File[] list = dir.listFiles();
        if(list == null)
			return 0;
        for (File f: list){
            if(isTiffFile(f.getName())){
                tiffs.add(f.getAbsolutePath());
            }
        }
        if(tiffs.isEmpty())
        	return 0;
        return tiffs.size()+1;
    }
	private static boolean isTiffFile(String fileName){
        Pattern p = Pattern.compile("\\.[tT][iI][Ff]");//Advantages of im4java: the interface of the IM commandline is quite stable, so your java program (and the im4java-library) will work across many versions of IM. im4java also provides a better OO interface (the "language" of the IM-commandline with it's postfix-operation notation translates very easily into OO-notation). And most important: you can use im4java everywhere JMagick can't be used because of the JNI hazard (e.g. java application servers).
        Matcher m = p.matcher(fileName);
        return m.find();
    }
	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		count ++;
		int percent = (int)(((double)count/(double)totalFiles) * 100.0);
		System.out.println("****************Finished " + count + '/' + totalFiles + " - "+ percent + "%****************");
	}
	
}
