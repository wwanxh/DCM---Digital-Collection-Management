package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.DocumentException;
import com.itextpdf.xmp.XMPException;

import add_ons.AddOns_RootXMP_Editor;
import controller.Controller;
import controller.Reverse_Controller;

public class DCM_GUI implements Observer, Serializable {

	public final String version = "v2.0";
	public final String updateDate = "09/15/2017";
	public final String Mode_A = "Mode A - TIFF -> 3 vers -> PDF/A 3B with Raws Moving (Lossless, lossy 50 30)";
	public final String Mode_A1 = "MOde A1 - TIFF -> 3 vers -> PDF/A 3B with Raws Copying (Lossless, lossy 50 30)";
	public final String Mode_A2 = "Mode A2 - TIFF -> 3 vers -> PDF/A 3B with Raws Remaining (Lossless, lossy 50 30)";
	public final String Mode_B1 = "Mode B1 - TIFF -> PDF/A 3B with RAWs Moving";
	public final String Mode_B2 = "Mode B2 - TIFF -> PDF/A 3B with RAWs Copying";
	public final String Mode_B3 = "Mode B3 - TIFF -> PDF/A 3B only with customized compression level";
	public final String Mode_C1 = "Mode C1 - PDF/A 3B -> JP2 -> TIFF (JP2 RETAINED)";
	public final String Mode_C2 = "Mode C2 - PDF/A 3B -> TIFF (JP2 NOT RETAINED)";
	public final String UNSELECTED = "Choose Conversion Mode... (Description in Developer Log)";

	JFrame main;
	Container pane;
	JLabel title, dirname, destdir, author, add_ons_title, status_bar, rootxmpeditor, lblcompression;
	public JProgressBar progress_bar;
	JButton Import, Export, rootxmp_editor, developer_log;
	JComboBox<String> options;
	String[] conversions = new String[] { UNSELECTED, Mode_A, Mode_A1, Mode_A2, Mode_B1, Mode_B2, Mode_B3, Mode_C1,
			Mode_C2 };
	JTextArea console;
	JTextField compression;

	String path;
	String selected_mode = UNSELECTED;

	public DCM_GUI() {
		main = new JFrame("DCM " + version + " - Digital Collections Management");
		main.setSize(860, 700);
		pane = main.getContentPane();
		title = new JLabel("Digital Collections Management " + version);
		Import = new JButton("Import");
		Export = new JButton("Export");
		options = new JComboBox<>(conversions);
		lblcompression = new JLabel("Compression Level (*Non-linear)");
		compression = new JTextField(100);
		author = new JLabel("by Xueheng Wan " + updateDate);
		dirname = new JLabel("No directory has been imported!");
		destdir = new JLabel("No destination directory has been chosen!");
		status_bar = new JLabel("No file is processing!");
		progress_bar = new JProgressBar();
		progress_bar.setMinimum(0);
		progress_bar.setMaximum(100);
		progress_bar.setValue(0);
		progress_bar.setStringPainted(true);
		console = new JTextArea(800, 300);
		JScrollPane scroll = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setAutoscrolls(true);
		// Now create a new TextAreaOutputStream to write to our JTextArea control and
		// wrap a
		// PrintStream around it to support the println/printf methods.
		PrintStream out = new PrintStream(new CustomOutputStream(console));
		// redirect standard output stream to the TextAreaOutputStream
		System.setOut(out);
		// redirect standard error stream to the TextAreaOutputStream
		System.setErr(out);
		add_ons_title = new JLabel("DCM Add-Ons");
		rootxmp_editor = new JButton("PDF Root XMP Editor");
		rootxmpeditor = new JLabel(
				"A tool to add dublin core and xmp properties into generated PDF/A-B2 root-level metadata.");
		developer_log = new JButton("Developer Log");

		title.setBounds(30, 20, 500, 50);
		title.setFont(new Font("Ubuntu", Font.BOLD, 26));
		Import.setBounds(30, 70, 200, 30);
		Export.setBounds(230, 70, 200, 30);
		options.setBounds(430, 70, 400, 30);
		lblcompression.setBounds(570, 50, 250, 20);
		compression.setBounds(800, 50, 30, 20);
		compression.setText("100");
		compression.setEditable(false);
		dirname.setBounds(30, 100, 800, 30);
		dirname.setFont(new Font("Ubuntu", Font.BOLD, 14));
		destdir.setBounds(30, 130, 800, 30);
		destdir.setFont(new Font("Ubuntu", Font.BOLD, 14));
		scroll.setBounds(30, 160, 800, 300);
		status_bar.setBounds(30, 460, 800, 30);
		status_bar.setFont(new Font("Ubuntu", Font.ITALIC, 14));
		progress_bar.setBounds(30, 490, 800, 30);
		add_ons_title.setBounds(30, 550, 1000, 40);
		add_ons_title.setFont(new Font("Ubuntu", Font.BOLD, 22));
		rootxmp_editor.setBounds(30, 580, 200, 30);
		rootxmpeditor.setBounds(250, 580, 600, 30);
		rootxmpeditor.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		developer_log.setBounds(30, 620, 200, 30);
		author.setBounds(500, 640, 300, 30);
		author.setFont(new Font("Ubuntu", Font.ITALIC, 16));
		JLabel temp = new JLabel();
		temp.setBounds(30, 670, 800, 30);

		pane.add(title);
		pane.add(Import);
		pane.add(Export);
		pane.add(options);
		pane.add(dirname);
		pane.add(destdir);
		pane.add(status_bar);
		pane.add(progress_bar);
		pane.add(scroll);
		pane.add(add_ons_title);
		pane.add(rootxmp_editor);
		pane.add(rootxmpeditor);
		pane.add(author);
		pane.add(developer_log);
		pane.add(lblcompression);
		pane.add(compression);
		pane.add(temp);

		main.setVisible(true);
		Import.addActionListener(new click_filechooser());
		Export.addActionListener(new click_run());
		rootxmp_editor.addActionListener(new click_RootXmpEditor());
		developer_log.addActionListener(new click_openlog());
		options.addActionListener(new click_select_mode());
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					progress_bar.setValue(progress);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		thread.start();
	}

	public class click_openlog implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			try {
				Desktop.getDesktop().open(new File(System.getProperty("user.dir") + "/src/ICC/notice.txt"));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}

	}

	public class click_filechooser implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			int returnVal = fileChooser.showOpenDialog((Component) e.getSource());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				try {
					path = file.getAbsolutePath() + "/";
					dirname.setText("Source Directory: " + path);
					if (!selected_mode.equals(UNSELECTED)) {
						totalFiles = fileCounter(path);
						System.out.println("Total " + totalFiles + " are about to be processed!");
					}
				} catch (Exception ex) {
					System.out.println("problem accessing file" + file.getAbsolutePath());
				}
			} else {
				System.out.println("File access cancelled by user.");
			}
		}
	}

	public class click_run implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int returnVal = fileChooser.showOpenDialog((Component) e.getSource());
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fileChooser.getSelectedFile();
				String dest = file.getAbsolutePath() + "/";
				destdir.setText("Dest Directory: " + dest);
				compression.setEditable(false);
				int comprs = Integer.parseInt(compression.getText());
				System.out.println("* -Compression Level is " + comprs);
				if (selected_mode.equals("Choose Conversion Mode...")) {
					System.out.println("Invalid Operation! Please Choose a Conversion Mode!");
				} else if (selected_mode.equals(Mode_A) || selected_mode.equals(Mode_A1) 
						|| selected_mode.equals(Mode_A2) || selected_mode.equals(Mode_B1)
						|| selected_mode.equals(Mode_B2) || selected_mode.equals(Mode_B3)) {

					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							long startTime = System.currentTimeMillis();
							try {
								switch (selected_mode) {
								case Mode_A:
									totalFiles *= 3;
									new Controller(path, dest, DCM_GUI.this, "move");
									break;
								case Mode_A1:
									totalFiles *= 3;
									new Controller(path, dest, DCM_GUI.this, "copy");
									break;
								case Mode_A2:
									totalFiles *= 3;
									new Controller(path, dest, DCM_GUI.this, "remain");
									break;
								case Mode_B1:
									new Controller(path, dest, DCM_GUI.this, "move", comprs);
									break;
								case Mode_B2:
									new Controller(path, dest, DCM_GUI.this, "copy", comprs);
									break;
								case Mode_B3:
									new Controller(path, dest, DCM_GUI.this, "remain", comprs);
									break;
								}
							} catch (IOException | DocumentException | XMPException | InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							long stopTime = System.currentTimeMillis();
							status_bar.setText(
									"Completed! - 100% [Total RunTime: " + (stopTime - startTime) / 1000 + " sec!]");
						}
					});
					thread.start();
				} else {
					Reverse_Controller rc = new Reverse_Controller(path, DCM_GUI.this);
					Thread thread = new Thread(new Runnable() {
						@Override
						public void run() {
							long startTime = System.currentTimeMillis();
							try {
								switch (selected_mode) {
								case Mode_C1:
									rc.Conversion(dest, rc.FORMAT_TIFF, true);
									break;
								case Mode_C2:
									rc.Conversion(dest, rc.FORMAT_TIFF, false);
									break;
								}
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							long stopTime = System.currentTimeMillis();
							status_bar.setText(
									"Completed! - 100% [Total RunTime: " + (stopTime - startTime) / 1000 + " sec!]");
						}
					});
					thread.start();
				}

			} else {
				System.out.println("File access cancelled by user.");
			}
			compression.setEditable(true);
		}
	}

	public class click_select_mode implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			JComboBox<String> combo = (JComboBox<String>) e.getSource();
			selected_mode = (String) combo.getSelectedItem();
			if (selected_mode.equals(Mode_B3) || selected_mode.equals(Mode_B1) || selected_mode.equals(Mode_B2))
				compression.setEditable(true);
			else {
				compression.setText("100");
				compression.setEditable(false);
			}
			System.out.println("User selected mode: " + selected_mode);
			if (path != null) {
				totalFiles = fileCounter(path);
				System.out.println("Total " + totalFiles + " operations are about to be processed!");
			}
		}

	}

	public class click_RootXmpEditor implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("User Request: Open DCM RootXMP Editor");
			AddOns_RootXMP_Editor_GUI rxe = new AddOns_RootXMP_Editor_GUI(version);
		}
	}

	public class CustomOutputStream extends OutputStream {
		private JTextArea textArea;

		public CustomOutputStream(JTextArea textArea) {
			this.textArea = textArea;
		}

		@Override
		public void write(int b) throws IOException {
			// redirects data to the text area
			textArea.append(String.valueOf((char) b));
			// scrolls the text area to the end of data
			textArea.setCaretPosition(textArea.getDocument().getLength());
		}
	}

	// Progress Reporter Part
	public int progress = 0;
	public int count = 0;
	public int totalFiles = 0;

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		status_bar.setText(
				"Processed File: " + arg1.toString() + " [" + count + '/' + totalFiles + " - " + progress + "%]");
		count++;
		progress = (int) (((double) count / (double) totalFiles) * 100.0);
	}

	private int fileCounter(String path) {
		int counter = 0;
		File src_dir = new File(path);
		String[] subdirs = src_dir.list(new FilenameFilter() {
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		for (int i = 0; i < subdirs.length; i++)
			if (StringUtils.containsWhitespace(subdirs[i])) {
				System.out.println("A File name containing Spaces is Found! - " + path + subdirs[i]);
				// if file name contains space then remove all of these
				new File(path + subdirs[i]).renameTo(new File(path + subdirs[i].replaceAll("\\s+", "")));
				subdirs[i] = subdirs[i].replaceAll("\\s+", "");
			}
		if (!ArrayUtils.isEmpty(subdirs))
			for (String i : subdirs) {
				counter += fileCounter(path + i + '/');
			}
		if (selected_mode.equals(this.Mode_B1) || selected_mode.equals(this.Mode_B2)
				|| selected_mode.equals(this.Mode_B3) || selected_mode.equals(this.Mode_A)
				|| selected_mode.equals(this.Mode_A1) || selected_mode.equals(this.Mode_A2))
			counter += searchTiffsInDir(src_dir);
		else
			counter += searchPDFsInDir(src_dir);
		return counter;
	}

	private static int searchTiffsInDir(File dir) {
		List<String> tiffs = new ArrayList<>();
		// System.out.println(dir.getAbsolutePath());
		File[] list = dir.listFiles();
		if (list == null)
			return 0;
		for (File f : list) {
			if (isTiffFile(f.getName())) {
				if (isFileNameContainingSpaces(f.getName()))
					f.renameTo(new File(f.getAbsolutePath().replaceAll("\\s", "")));
				tiffs.add(f.getAbsolutePath());
			}
		}
		if (tiffs.isEmpty())
			return 0;
		return tiffs.size() + 1;
	}

	private static boolean isFileNameContainingSpaces(String fileName) {
		Pattern p = Pattern.compile("\\s");// Advantages of im4java: the interface of the IM commandline is quite
											// stable, so your java program (and the im4java-library) will work across
											// many versions of IM. im4java also provides a better OO interface (the
											// "language" of the IM-commandline with it's postfix-operation notation
											// translates very easily into OO-notation). And most important: you can use
											// im4java everywhere JMagick can't be used because of the JNI hazard (e.g.
											// java application servers).
		Matcher m = p.matcher(fileName);
		return m.find();
	}

	private static boolean isTiffFile(String fileName) {
		Pattern p = Pattern.compile("\\.[tT][iI][Ff]");
		Matcher m = p.matcher(fileName);
		return m.find();
	}

	private static int searchPDFsInDir(File dir) {
		List<String> tiffs = new ArrayList<>();
		// System.out.println(dir.getAbsolutePath());
		File[] list = dir.listFiles();
		if (list == null)
			return 0;
		for (File f : list) {
			if (isPDFFile(f.getName())) {
				if (isFileNameContainingSpaces(f.getName()))
					f.renameTo(new File(f.getAbsolutePath().replaceAll("\\s", "")));
				tiffs.add(f.getAbsolutePath());
			}
		}
		if (tiffs.isEmpty())
			return 0;
		return tiffs.size();
	}

	private static boolean isPDFFile(String fileName) {
		Pattern p = Pattern.compile("\\.[pP][dD][Ff]");
		Matcher m = p.matcher(fileName);
		return m.find();
	}
}
