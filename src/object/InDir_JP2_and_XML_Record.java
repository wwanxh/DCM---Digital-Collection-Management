package object;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

public class InDir_JP2_and_XML_Record {
	
	ArrayList<String> jp2s;
	ArrayList<String> xmls;
	String Dir_Path;
	
	public InDir_JP2_and_XML_Record(String path){
		Dir_Path = path;
		jp2s = new ArrayList<String>();
		xmls = new ArrayList<String>();
	}
	public void addJP2(String filename){
		jp2s.add(filename);
	}
	public void addXML(String filename){
		xmls.add(filename);
	}
	public void OutputToDisk() throws IOException{
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Dir_Path + "log.txt"), "utf-8"));
		writer.write(Dir_Path + '\n');
		for(int i = 0; i < jp2s.size(); i ++)
			writer.write(jp2s.get(i) + ' ' + xmls.get(i) + '\n'); // For each line (JP2_PATH + EMPTY_CHARACTER + XML_PATH)
		writer.close();
	}
	public void OutputToDisk(String suffix) throws IOException{
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Dir_Path + "log_" + suffix + ".txt"), "utf-8"));
		writer.write(Dir_Path + '\n');
		for(int i = 0; i < jp2s.size(); i ++)
			writer.write(jp2s.get(i) + ' ' + xmls.get(i) + '\n'); // For each line (JP2_PATH + EMPTY_CHARACTER + XML_PATH)
		writer.close();
	}
}
