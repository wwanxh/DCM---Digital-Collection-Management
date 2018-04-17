package model;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;

public class Directory_Reconstructor {
	String top_path;
	public Directory_Reconstructor(String top_path) throws IOException {
		System.out.println("Reconstructing directory ... " + top_path);
		this.top_path = top_path;
		processing(top_path);
		File[] dirs = new File(top_path).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				return pathname.isDirectory();
			}
		});
		for(File d : dirs)
			d.delete();
		System.out.println("The directory " + top_path + " has been reconstructed!");
	}
	private void processing(String dir) throws IOException {
		File[] dirs = new File(dir).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				return pathname.isDirectory();
			}
		});
		movingPDF(dir, top_path);
		for(File d : dirs) {
			processing(d.getAbsolutePath());
			d.delete();
		}
	}
	private void movingPDF(String src, String dest){
		System.out.println("** -Moving PDFs from " + src + " into " + dest);
		File[] list = new File(src).listFiles(new FileFilter(){
			@Override
			public boolean accept(File pathname) {
				// TODO Auto-generated method stub
				return !pathname.isDirectory() && isPDFFile(pathname.getName());
			}
		});
		for(File a : list) {
			try {
				FileUtils.moveFileToDirectory(new File(a.getAbsolutePath()), new File(dest), false);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("Skipping duplicates ... ");
				a.delete();
			}
		}
	}
	private static boolean isPDFFile(String fileName){
        Pattern p = Pattern.compile("\\.[pP][dD][Ff]");//Advantages of im4java: the interface of the IM commandline is quite stable, so your java program (and the im4java-library) will work across many versions of IM. im4java also provides a better OO interface (the "language" of the IM-commandline with it's postfix-operation notation translates very easily into OO-notation). And most important: you can use im4java everywhere JMagick can't be used because of the JNI hazard (e.g. java application servers).
        Matcher m = p.matcher(fileName);
        return m.find();
    }
}
