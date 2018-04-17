package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import object.InDir_JP2_and_XML_Record;
import view.DCM_GUI;

public class TIFFs_Importer extends Observable implements Serializable {

	public TIFFs_Importer(String path, String dest_path, DCM_GUI gui) throws IOException, InterruptedException {
		this.addObserver(gui);
		File input = new File(path);
		if (input.isDirectory())
			Dir_Handler_Mode_A(path, dest_path);
		else {
			Single_Handler(input.getAbsolutePath(), dest_path, 100);
			Single_Handler(input.getAbsolutePath(), dest_path, 50);
			Single_Handler(input.getAbsolutePath(), dest_path, 30);
		}
	}

	public TIFFs_Importer(String path, String dest_path, DCM_GUI gui, int compression_level) throws IOException, InterruptedException {
		this.addObserver(gui);
		File input = new File(path);
		if (input.isDirectory())
			Dir_Handler(path, dest_path, compression_level);
		else
			Single_Handler(input.getAbsolutePath(), dest_path, compression_level);
	}

	private void Dir_Handler(String src, String dest, int compression_level) throws IOException, InterruptedException {
		// Get the list of tiffs in the directory, the list is in numeric order
		List<String> tiffs = searchTiffsInDir(new File(src));
		// Converting TIFF to JP2 and Extracting metadata from TIFF to XML
		Process proc = null;
		InDir_JP2_and_XML_Record record = new InDir_JP2_and_XML_Record(dest);
		String suffix = compression_level == 100 ? "" : "_" + compression_level;
		for (int i = 0; i < tiffs.size(); i++) {
			System.out.println("*  -Converting " + tiffs.get(i));
			File file = new File(tiffs.get(i));
			int pos2 = tiffs.get(i).lastIndexOf(".");
			int pos1 = tiffs.get(i).lastIndexOf("/");
			String fname = tiffs.get(i);
			if (pos1 > 0 && pos2 > 0) {
				fname = fname.substring(pos1, pos2);
				// Converting TIFF to JP2
				try {
					//proc = Runtime.getRuntime()
							//.exec("convert -quality " + compression_level + ' ' + file + " " + dest + fname + suffix + ".jp2");
					proc = Runtime.getRuntime()
								.exec("convert " + file + " -profile /home/wan/AppleRGB.icc "
										+ " -profile /home/wan/AdobeRGB1998.icc " + dest + fname + suffix + ".jp2");
					TimeUnit.SECONDS.sleep(10);
					record.addJP2(fname + suffix + ".jp2");
					if (i == tiffs.size() - 1 || (i % 40 == 0 && i > 0))
						try {
							proc.waitFor();
						} catch (InterruptedException e) {
							System.out.println("Error in TIFF to JP2 Conversion!");
							e.printStackTrace();
						}
				} catch (IOException e) {
					System.out.println("Error in TIFF to JP2 Conversion!");
					e.printStackTrace();
				}
				// Extracting XML
				try {
					proc = Runtime.getRuntime().exec("exiftool -X " + file);
					record.addXML(fname + ".xml");
					try {
						proc.waitFor();
					} catch (InterruptedException e) {
						System.out.println("Error in Extracting XML from TIFF!");
						e.printStackTrace();
					}
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
					FileWriter fw = new FileWriter(dest + fname + ".xml");
					BufferedWriter bw = new BufferedWriter(fw);
					String s = null;
					s = stdInput.readLine();
					ArrayList<String> xmllines = new ArrayList<String>();
					while ((s = stdInput.readLine()) != null) {
						bw.write(s + "\n");
					}
					bw.close();

				} catch (IOException e) {
					System.out.println("Error detected! Waiting for I/O: " + e.getMessage());
					TimeUnit.SECONDS.sleep(2);
					System.out.println("Process resumed!");
					proc = Runtime.getRuntime().exec("exiftool -X " + file);
					record.addXML(fname + ".xml");
					try {
						proc.waitFor();
					} catch (InterruptedException ex) {
						System.out.println("Error in Extracting XML from TIFF!");
						e.printStackTrace();
					}
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
					String n = dest + fname + ".xml";
					FileWriter fw = new FileWriter(n);
					BufferedWriter bw = new BufferedWriter(fw);
					String s = null;
					s = stdInput.readLine();
					ArrayList<String> xmllines = new ArrayList<String>();
					while ((s = stdInput.readLine()) != null) {
						bw.write(s + "\n");
					}
					bw.close();
					e.printStackTrace();
				}
			}
			if (tiffs.get(i).length() > 70)
				Progress_Report("..." + tiffs.get(i).substring(tiffs.get(i).length() - 70));
			else
				Progress_Report(tiffs.get(i));
		}
		try {
			  Thread.sleep(5000L);
			} catch(InterruptedException ie) {
		}
		if(compression_level == 100) record.OutputToDisk();
		else record.OutputToDisk("" + compression_level); // The Generated File list has been saved into destination directory
	}

	// Only For Converting TIFF with all Compression Levels
	// Optimize performance in Mode A
	private void Dir_Handler_Mode_A(String src, String dest) throws IOException, InterruptedException {
		// Get the list of tiffs in the directory, the list is in numeric order
		List<String> tiffs = searchTiffsInDir(new File(src));
		// Converting TIFF to JP2 and Extracting metadata from TIFF to XML
		Process proc = null;
		InDir_JP2_and_XML_Record record = new InDir_JP2_and_XML_Record(dest);
		InDir_JP2_and_XML_Record record30 = new InDir_JP2_and_XML_Record(dest);
		// InDir_JP2_and_XML_Record record40 = new InDir_JP2_and_XML_Record(dest);
		InDir_JP2_and_XML_Record record50 = new InDir_JP2_and_XML_Record(dest);
		for (int i = 0; i < tiffs.size(); i++) {
			System.out.println("*  -Converting " + tiffs.get(i));
			File file = new File(tiffs.get(i));
			int pos2 = tiffs.get(i).lastIndexOf(".");
			int pos1 = tiffs.get(i).lastIndexOf("/");
			String fname = tiffs.get(i);
			if (pos1 > 0 && pos2 > 0) {
				fname = fname.substring(pos1, pos2);
				// Converting TIFF to JP2
				try {
					proc = Runtime.getRuntime().exec("convert -quality 100 " + file + " " + dest + fname + ".jp2");
					proc = Runtime.getRuntime().exec("convert -quality 30 " + file + " " + dest + fname + "_30.jp2");
					proc = Runtime.getRuntime().exec("convert -quality 50 " + file + " " + dest + fname + "_50.jp2");
					record.addJP2(fname + ".jp2");
					record30.addJP2(fname + "_30.jp2");
					record50.addJP2(fname + "_50.jp2");
					if (i == tiffs.size() - 1 || (i % 14 == 0 && i > 0))
						try {
							proc.waitFor();
						} catch (InterruptedException e) {
							System.out.println("Error in TIFF to JP2 Conversion!");
							e.printStackTrace();
						}
				} catch (IOException e) {
					System.out.println("Error in TIFF to JP2 Conversion!");
					e.printStackTrace();
				}
				// Extracting XML
				try {
					proc = Runtime.getRuntime().exec("exiftool -X " + file);
					record.addXML(fname + ".xml");
					record30.addXML(fname + ".xml");
					record50.addXML(fname + ".xml");
					try {
						proc.waitFor();
					} catch (InterruptedException e) {
						System.out.println("Error in Extracting XML from TIFF!");
						e.printStackTrace();
					}
					BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
					FileWriter fw = new FileWriter(dest + fname + ".xml");
					BufferedWriter bw = new BufferedWriter(fw);
					String s = null;
					s = stdInput.readLine();
					ArrayList<String> xmllines = new ArrayList<String>();
					while ((s = stdInput.readLine()) != null) {
						bw.write(s + "\n");
					}
					bw.close();

				} catch (IOException e) {
					System.out.println("Error in Extracting XML from TIFF!");
					e.printStackTrace();
				}
			}
			
			if (tiffs.get(i).length() > 70)
				Progress_Report("..." + tiffs.get(i).substring(tiffs.get(i).length() - 70), 3);
			else
				Progress_Report(tiffs.get(i), 3);
		}
		record.OutputToDisk(); // The Generated File list has been saved into destination directory
		record30.OutputToDisk("30");
		record50.OutputToDisk("50");
	}
	
	private void Single_Handler(String path, String dest, int compression) throws IOException {
		System.out.println("*  -Converting " + path);
		Process proc;
		InDir_JP2_and_XML_Record record = new InDir_JP2_and_XML_Record(dest);
		File file = new File(path);
		int pos2 = path.lastIndexOf(".");
		int pos1 = path.lastIndexOf("/");
		String fname = path;
		if (pos1 > 0 && pos2 > 0) {
			fname = fname.substring(pos1, pos2);
			// Converting TIFF to JP2
			try {
				proc = Runtime.getRuntime()
						.exec("convert -quality " + compression + ' ' + file + " " + dest + fname + ".jp2");
				record.addJP2(fname + ".jp2");
			} catch (IOException e) {
				System.out.println("Error in TIFF to JP2 Conversion!");
				e.printStackTrace();
			}
			// Extracting XML
			try {
				proc = Runtime.getRuntime().exec("exiftool -X " + file);
				record.addXML(fname + ".xml");
				try {
					proc.waitFor();
				} catch (InterruptedException e) {
					System.out.println("Error in Extracting XML from TIFF!");
					e.printStackTrace();
				}
				BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				FileWriter fw = new FileWriter(dest + fname + ".xml");
				BufferedWriter bw = new BufferedWriter(fw);
				String s = null;
				s = stdInput.readLine();
				ArrayList<String> xmllines = new ArrayList<String>();
				while ((s = stdInput.readLine()) != null) {
					bw.write(s + "\n");
				}
				bw.close();

			} catch (IOException e) {
				System.out.println("Error in Extracting XML from TIFF!");
				e.printStackTrace();
			}
		}
		if (path.length() > 70)
			Progress_Report("..." + path.substring(path.length() - 70));
		else
			Progress_Report(path);
		record.OutputToDisk(); // The Generated File list has been saved into destination directory
	}

	@SuppressWarnings("unchecked")
	private static List<String> searchTiffsInDir(File dir) {
		List<String> tiffs = new ArrayList<>();
		// System.out.println(dir.getAbsolutePath());
		File[] list = dir.listFiles();
		Arrays.sort(list, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				// TODO Auto-generated method stub
				return ((File) o1).getName().compareTo(((File) o2).getName());
			}
		});
		for (File f : list) {
			// System.out.println(f.getName());
			if (isTiffFile(f.getName())) {
				tiffs.add(f.getAbsolutePath());
			}
		}
		return tiffs;
	}

	private static boolean isTiffFile(String fileName) {
		Pattern p = Pattern.compile("\\.[tT][iI][Ff]");// Advantages of im4java: the interface of the IM commandline is
														// quite stable, so your java program (and the im4java-library)
														// will work across many versions of IM. im4java also provides a
														// better OO interface (the "language" of the IM-commandline
														// with it's postfix-operation notation translates very easily
														// into OO-notation). And most important: you can use im4java
														// everywhere JMagick can't be used because of the JNI hazard
														// (e.g. java application servers).
		Matcher m = p.matcher(fileName);
		return m.find();
	}

	public List<String> findNotes(String path) throws IOException {
		File[] files = new File(path).listFiles();
		String note_path = "";
		if(files == null) return new ArrayList<String>();
		for (File f : files) {
			if (f.getName().contains("note.txt")) {
				note_path = f.getAbsolutePath();
				break;
			}else if (f.getName().contains("notes.txt")) {
				note_path = f.getAbsolutePath();
				break;
			}
		}
		if (note_path == "")
			return new ArrayList<String>();
		BufferedReader br = new BufferedReader(new FileReader(note_path));
		String line;
		List<String> lines = new ArrayList<String>();
		while ((line = br.readLine()) != null) {
			lines.add(line);
		}
		br.close();
		return lines;
	}

	public void Progress_Report(String file) {
		setChanged();
		notifyObservers(file);
	}

	public void Progress_Report(String file, int repeat) {
		for (int i = 0; i < repeat; i++) {
			setChanged();
			notifyObservers(file);
		}
	}

}
