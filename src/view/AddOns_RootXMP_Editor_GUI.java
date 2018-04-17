package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;

import add_ons.AddOns_RootXMP_Editor;
import object.AddOns_root_metadata;

public class AddOns_RootXMP_Editor_GUI {

	JFrame main, xmp;
	Container pane, subpane;
	JLabel lblSoftware, lblMe;
	JButton btnFileInput, btnFileOutput;
	JLabel lblFileDisplay;
	JLabel lblContributor, lblCoverage, lblCreator, lblDate, lblDescription, lblFormat, lblIdentifier, 
		lblLanguage, lblPublisher, lblRelation, lblRights, lblSource, lblSubject, lblTitle, lblType;
	JTextField txtContributor, txtCoverage, txtCreator, txtDate, txtDescription, txtFormat, txtIdentifier, 
		txtLanguage, txtPublisher, txtRelation, txtRights, txtSource, txtSubject, txtTitle, txtType;
	JLabel lblNotice;
	Insets insets;
	JCheckBox choosing_xmp;
	boolean xmp_entered = false; //indicates this are xmp tags needed to be inserted
	
	private String path;
	private AddOns_root_metadata rm;
	
	public AddOns_RootXMP_Editor_GUI(String version){
		
		//create the main frame
		main = new JFrame("PDF RootXMP Editor " + version +  "- DCM Add-Ons (Digital Collections Management " + version + ")");
		main.setSize(700, 700);
		pane = main.getContentPane();
		insets = pane.getInsets();
		pane.setLayout(null);
		
		//Create JButton for file chooser
		lblSoftware = new JLabel("PDF RootXMP Editor - DCM " + version + " Add-Ons");
		lblMe = new JLabel("by Xueheng Wan 05/19/2017");
		lblMe.setFont(new Font("Ubuntu", Font.PLAIN, 14));
		btnFileInput = new JButton("Open File");
		btnFileOutput = new JButton("Complete");
		lblFileDisplay = new JLabel("No File has been chosen!");
		lblFileDisplay.setFont(new Font("Ubuntu", Font.PLAIN, 18));
		choosing_xmp = new JCheckBox("Adding XMP tags?");
		lblContributor = new JLabel("dc:contributors");
		txtContributor = new JTextField(5);
		lblCoverage = new JLabel("dc:coverage");
		txtCoverage = new JTextField();
		lblCreator = new JLabel("dc:creator");
		txtCreator = new JTextField();
		lblDate = new JLabel("dc:date");
		txtDate = new JTextField();
		lblDescription = new JLabel("dc:description");
		txtDescription = new JTextField();
		lblFormat = new JLabel("dc:format");
		txtFormat = new JTextField();
		lblIdentifier = new JLabel("dc:identifier");
		txtIdentifier = new JTextField();
		lblLanguage = new JLabel("dc:language");
		txtLanguage = new JTextField();
		lblPublisher = new JLabel("dc:publisher");
		txtPublisher = new JTextField();
		lblRelation = new JLabel("dc:relation");
		txtRelation = new JTextField();
		lblRights = new JLabel("dc:rights");
		txtRights = new JTextField();
		lblSource = new JLabel("dc:source");
		txtSource = new JTextField();
		lblSubject = new JLabel("dc:subject");
		txtSubject = new JTextField();
		lblTitle = new JLabel("dc:title");
		txtTitle = new JTextField();
		lblType = new JLabel("dc:type");
		txtType = new JTextField();
		lblNotice = new JLabel("<html>According to DCMI (Dublin Core Metadata Initiative), all input values of fields are <br>supposed in formats:<br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;1. Unordered array of ProperName: dc:contributor, dc:publisher; <br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;2. Unordered array of Text: relation, dc:subject, dc:type, dc:language (Locale);<br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;3. Ordered array of ProperName: dc:creator; <br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;4. Ordered array of Date: dc:date;<br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;5. Text: dc:coverage, dc:identifier, dc:source; <br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;6. MIMEType: dc:format;<br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;7. Language Alternatice: dc:description, dc:rights, dc:title;<br>"
				+ "Values entering into Field 'Date' and 'Format' shall be well-formed. (Wrong format may fail in PDF/A validation) <br>"
				+ "Use Semicolon ';' to separate the input values. (Only Applied to Case 1, 2, 3, and 4) </html>");
		
		//Place all components
		lblSoftware.setBounds(30, 10, 450, 40);
		lblSoftware.setFont(new Font("Ubuntu", Font.BOLD, 22));
		btnFileInput.setBounds(30, 60, 200, 30);
		btnFileOutput.setBounds(230, 60, 200, 30);
		lblMe.setBounds(440, 60, 200, 30);
		lblFileDisplay.setBounds(30, 90, 500, 30);
		choosing_xmp.setBounds(500, 90, 200, 30);
		lblContributor.setBounds(30, 130, 110, 30);
		txtContributor.setBounds(lblContributor.getX() + lblContributor.getWidth(), lblContributor.getY(), 500, 30);
		lblCoverage.setBounds(30, 160, 110, 30);
		txtCoverage.setBounds(lblCoverage.getX() + lblCoverage.getWidth(), lblCoverage.getY(), 200, 30);
		lblDescription.setBounds(340, 160, 105, 30);
		txtDescription.setBounds(lblDescription.getX() + lblDescription.getWidth(), lblDescription.getY(), 195, 30);
		lblCreator.setBounds(30, 190, 110,30);
		txtCreator.setBounds(lblCreator.getX() + lblCreator.getWidth(), lblCreator.getY(), 500, 30);
		lblDate.setBounds(30, 220, 110,30);
		txtDate.setBounds(lblDate.getX() + lblDate.getWidth(), lblDate.getY(), 500, 30);
		lblFormat.setBounds(30, 250, 110, 30);
		txtFormat.setBounds(lblFormat.getX() + lblFormat.getWidth(), lblFormat.getY(), 200, 30);
		lblIdentifier.setBounds(340, 250, 105, 30);
		txtIdentifier.setBounds(lblIdentifier.getX() + lblIdentifier.getWidth(), lblIdentifier.getY(), 195, 30);
		lblLanguage.setBounds(30, 280, 110, 30);
		txtLanguage.setBounds(lblLanguage.getX() + lblLanguage.getWidth(), lblLanguage.getY(), 500, 30);
		lblPublisher.setBounds(30, 310, 110, 30);
		txtPublisher.setBounds(lblPublisher.getX() + lblPublisher.getWidth(), lblPublisher.getY(), 500, 30);
		lblRelation.setBounds(30, 340, 110, 30);
		txtRelation.setBounds(lblRelation.getX() + lblRelation.getWidth(), lblRelation.getY(), 500, 30);
		lblRights.setBounds(30, 370, 110, 30);
		txtRights.setBounds(lblRights.getX() + lblRights.getWidth(), lblRights.getY(), 200, 30);
		lblSource.setBounds(340, 370, 105, 30);
		txtSource.setBounds(lblSource.getX() + lblSource.getWidth(), lblSource.getY(), 195, 30);
		lblSubject.setBounds(30, 400, 110, 30);
		txtSubject.setBounds(lblSubject.getX() + lblSubject.getWidth(), lblSubject.getY(), 500, 30);
		lblTitle.setBounds(30, 430, 110, 30);
		txtTitle.setBounds(lblTitle.getX() + lblTitle.getWidth(), lblTitle.getY(), 500, 30);
		lblType.setBounds(30, 460, 110, 30);
		txtType.setBounds(lblType.getX() + lblType.getWidth(), lblType.getY(), 500, 30);
		lblNotice.setBounds(30, 490, 600, 180);
		
		//Adding Components into Main Frame
		pane.add(lblSoftware);
		pane.add(lblMe);
		pane.add(btnFileInput);
		pane.add(btnFileOutput);
		pane.add(lblFileDisplay);
		pane.add(choosing_xmp);
		pane.add(lblContributor);
		pane.add(txtContributor);
		pane.add(lblCoverage);
		pane.add(txtCoverage);
		pane.add(lblCreator);
		pane.add(txtCreator);
		pane.add(lblDate);
		pane.add(txtDate);
		pane.add(lblDescription);
		pane.add(txtDescription);
		pane.add(lblFormat);
		pane.add(txtFormat);
		pane.add(lblIdentifier);
		pane.add(txtIdentifier);
		pane.add(lblLanguage);
		pane.add(txtLanguage);
		pane.add(lblPublisher);
		pane.add(txtPublisher);
		pane.add(lblRelation);
		pane.add(txtRelation);
		pane.add(lblRights);
		pane.add(txtRights);
		pane.add(lblSource);
		pane.add(txtSource);
		pane.add(lblSubject);
		pane.add(txtSubject);
		pane.add(lblTitle);
		pane.add(txtTitle);
		pane.add(lblType);
		pane.add(txtType);
		pane.add(lblNotice);
		
		//Set Frame Visible
		main.setVisible(true);
		
		//Button's Action
		btnFileInput.addActionListener(new click_filechooser());
		btnFileOutput.addActionListener(new click_run());
		choosing_xmp.addActionListener(new click_xmp_choosing());
	}
	 public class click_filechooser implements ActionListener{
		public void actionPerformed (ActionEvent e){
			JFileChooser fileChooser = new JFileChooser();
			int returnVal = fileChooser.showOpenDialog((Component)e.getSource());
		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		        File file = fileChooser.getSelectedFile();
		        try {
		           path = file.getAbsolutePath();
		           lblFileDisplay.setText(path);
		           StringMatching(path);
		        } catch (Exception ex) {
		          System.out.println("problem accessing file"+file.getAbsolutePath());
		        }
		    } 
		    else {
		        System.out.println("File access cancelled by user.");
		    }       
		}
	 }
	 public class click_run implements ActionListener{
		 public void actionPerformed (ActionEvent e){
			 AddOns_RootXMP_Editor pre = new AddOns_RootXMP_Editor(path, false);
			 //rm = new root_metadata();
			 importing_field();
			 try {
				pre.manipulatePDF(rm);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (DocumentException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		 }
	 }
	 
	 JLabel lblcreatedate, lblcreatortool, lblidentifier, lblmetadatadate, lblmodifydate, 
	 		lblderivedfrom, lbldocumentid, lblrenditionclass, lblrenditionparams;
	 JTextField txtcreatedate, txtcreatortool, txtidentifier, txtmetadatadate, txtmodifydate,
	 		txtderivedfrom, txtdocumentid, txtrenditionclass, txtrenditionparams;
	 
	 public class click_xmp_choosing implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(choosing_xmp.isSelected()){
				xmp_entered = true;
				xmp = new JFrame("Adding XMP Properties - DCM RootXMP Editor");
				xmp.setSize(550, 350);
				subpane = xmp.getContentPane();
				subpane.setLayout(null);
				lblcreatedate = new JLabel("xmp:CreateDate");
				txtcreatedate = new JTextField();
				lblcreatortool = new JLabel("xmp:CreatorTool");
				txtcreatortool = new JTextField();
				lblidentifier = new JLabel("xmp:Identifier");
				txtidentifier = new JTextField();
				lblmetadatadate = new JLabel("xmp:MetadataDate");
				txtmetadatadate = new JTextField();
				lblmodifydate = new JLabel("xmp:ModifyDate");
				txtmodifydate = new JTextField();
				lblderivedfrom = new JLabel("xmpMM:DerivedFrom");
				txtderivedfrom = new JTextField();
				lbldocumentid = new JLabel("xmpMM:DocumentID");
				txtdocumentid = new JTextField();
				lblrenditionclass = new JLabel("xmpMM:RenditionClass");
				txtrenditionclass = new JTextField();
				lblrenditionparams = new JLabel("xmpMM:RenditionParams");
				txtrenditionparams = new JTextField();
				
				lblcreatedate.setBounds(30, 10, 210, 30);
				txtcreatedate.setBounds(210, 10, 300, 30);
				lblcreatortool.setBounds(30, 40, 210, 30);
				txtcreatortool.setBounds(210, 40, 300, 30);
				lblidentifier.setBounds(30, 70, 210, 30);
				txtidentifier.setBounds(210, 70, 300, 30);
				lblmetadatadate.setBounds(30, 100, 210, 30);
				txtmetadatadate.setBounds(210, 100, 300, 30);
				lblmodifydate.setBounds(30, 130, 210, 30);
				txtmodifydate.setBounds(210, 130, 300, 30);
				lblderivedfrom.setBounds(30, 160, 210, 30);
				txtderivedfrom.setBounds(210, 160, 300, 30);
				lbldocumentid.setBounds(30, 190, 210, 30);
				txtdocumentid.setBounds(210, 190, 300, 30);
				lblrenditionclass.setBounds(30, 220, 210, 30);
				txtrenditionclass.setBounds(210, 220, 300, 30);
				lblrenditionparams.setBounds(30, 250, 210, 30);
				txtrenditionparams.setBounds(210, 250, 300, 30);
				
				subpane.add(lblcreatedate);
				subpane.add(txtcreatedate);
				subpane.add(lblcreatortool);
				subpane.add(txtcreatortool);
				subpane.add(lblidentifier);
				subpane.add(txtidentifier);
				subpane.add(lblmetadatadate);
				subpane.add(txtmetadatadate);
				subpane.add(lblmodifydate);
				subpane.add(txtmodifydate);
				subpane.add(lblderivedfrom);
				subpane.add(txtderivedfrom);
				subpane.add(lbldocumentid);
				subpane.add(txtdocumentid);
				subpane.add(lblrenditionclass);
				subpane.add(txtrenditionclass);
				subpane.add(lblrenditionparams);
				subpane.add(txtrenditionparams);
				
				JLabel notice = new JLabel("<html>Pre-detecting exisiting XMP tags will be added in the future. <br>"
						+ "Use semicolon ';' to seperate multiple values (Only for xmp:Identifier). <br>More "
						+ "tags will be added in further version.</html>");
				notice.setBounds(30, 280, 510, 60);
				notice.setFont(new Font("Ubuntu", Font.PLAIN, 12));
				subpane.add(notice);
				xmp.setVisible(true);
			}else{
				xmp.setVisible(false);
				xmp_entered = false;
			}
		}
	 }
	 private void importing_field(){
		 rm.contributors.addAll(separating(txtContributor.getText()));
		 rm.coverage = txtCoverage.getText();
		 rm.creators.addAll(separating(txtCreator.getText()));
		 rm.date.addAll(separating(txtDate.getText()));
		 rm.description = txtDescription.getText();
		 rm.format = txtFormat.getText();
		 rm.identifier = txtIdentifier.getText();
		 rm.languages.addAll(separating(txtLanguage.getText()));
		 rm.publishers.addAll(separating(txtPublisher.getText()));
		 rm.relations.addAll(separating(txtRelation.getText()));
		 rm.rights = txtRights.getText();
		 rm.source = txtSource.getText();
		 rm.subject.addAll(separating(txtSubject.getText()));
		 rm.title = txtTitle.getText();
		 rm.types.addAll(separating(txtType.getText()));
		 
		 if(xmp_entered){
			 rm.createdate = txtcreatedate.getText();
			 rm.creatortool = txtcreatortool.getText();
			 rm.identifier = txtidentifier.getText();
			 rm.metadatadate = txtmetadatadate.getText();
			 rm.modifydate = txtmodifydate.getText();
			 rm.derived_from = txtderivedfrom.getText();
			 rm.document_id = txtdocumentid.getText();
			 rm.rendition_class = txtrenditionclass.getText();
			 rm.rendition_params = txtrenditionparams.getText();
		 }
	 }
	 private ArrayList<String> separating(String text){
		 if(text.isEmpty())
			 return new ArrayList<String>(); // Discard the empty field
		 return new ArrayList<String>(Arrays.asList(text.split(";")));
	 }
	 private void StringMatching(String path) throws IOException{
		 rm = new AddOns_root_metadata();
		 PdfReader r = new PdfReader(path);
		 String raw_meta = new String(r.getMetadata());
		 //System.out.println("METADATA: " + raw_meta);
		 r.close();
		 
		 List<String> meta = Arrays.asList(raw_meta.split("\n"));
		 System.out.println("The file Root Level Metadata contains " + meta.size() + " lines!\n");
		 /* String Matching */
		 boolean hasContributor = false;
		 boolean hasDescription = false;
		 boolean hasCreator = false;
		 boolean hasDate = false;
		 boolean hasLanguage = false;
		 boolean hasPublisher = false;
		 boolean hasRelation = false;
		 boolean hasRights = false;
		 boolean hasSubject = false;
		 boolean hasTitle = false;
		 boolean hasType = false;
		 String temp = "";
		 for(String line : meta){
			 // dc:contributor
			 if(line.contains("<dc:contributor>")){
				 hasContributor = true;
				 continue;
			 }else if(line.contains("</dc:contributor>")){
				 txtContributor.setText(temp.substring(0, temp.length()-1));
				 hasContributor = false;
				 temp = "";
				 continue;
			 }
			 if(hasContributor){
				 if(line.contains("rdf:Bag"))
					 ;
				 else
					 temp += (line.substring(line.indexOf("li>")+3, line.lastIndexOf("</rdf")) + ';');
				 continue;
			 }
			 // dc:coverage
			 if(line.contains("dc:coverage=")){
				 txtCoverage.setText(line.substring(line.indexOf("age=\"") + 5, line.lastIndexOf("\"")));
				 continue;
			 }
			 // dc:description
			 if(line.contains("<dc:description>")){
				 hasDescription = true;
				 continue;
			 }else if(line.contains("</dc:description>")){
				 txtDescription.setText(temp.substring(0, temp.length()-1));
				 hasDescription = false;
				 temp = "";
				 continue;
			 }
			 if(hasDescription){
				 if(line.contains("rdf:Alt"))
					 ;
				 else
					 temp += (line.substring(line.indexOf(">")+1, line.lastIndexOf("</rdf")));
				 continue;
			 }
			// dc:creator
			 if(line.contains("<dc:creator>")){
				 hasCreator = true;
				 continue;
			 }else if(line.contains("</dc:creator>")){
				 txtCreator.setText(temp.substring(0, temp.length()-1));
				 hasCreator = false;
				 temp = "";
				 continue;
			 }
			 if(hasCreator){
				 if(line.contains("rdf:Seq"))
					 ;
				 else
					 temp += (line.substring(line.indexOf("li>")+3, line.lastIndexOf("</rdf")) + ';');
				 continue;
			 }
			// dc:date
			 if(line.contains("<dc:date>")){
				 hasDate = true;
				 continue;
			 }else if(line.contains("</dc:date>")){
				 txtDate.setText(temp.substring(0, temp.length()-1));
				 hasDate = false;
				 temp = "";
				 continue;
			 }
			 if(hasDate){
				 if(line.contains("rdf:Seq"))
					 ;
				 else
					 temp += (line.substring(line.indexOf("li>")+3, line.lastIndexOf("</rdf")) + ';');
				 continue;
			 }
			// dc:format
			 if(line.contains("dc:format=")){
				 txtFormat.setText(line.substring(line.indexOf("mat=\"") + 5, line.lastIndexOf("\"")));
				 continue;
			 }
			// dc:identifier
			 if(line.contains("dc:identifier=")){
				 txtIdentifier.setText(line.substring(line.indexOf("ier=\"") + 5, line.lastIndexOf("\"")));
				 continue;
			 }
			// dc:language
			 if(line.contains("<dc:language>")){
				 hasLanguage = true;
				 continue;
			 }else if(line.contains("</dc:language>")){
				 txtLanguage.setText(temp.substring(0, temp.length()-1));
				 hasLanguage = false;
				 temp = "";
				 continue;
			 }
			 if(hasLanguage){
				 if(line.contains("rdf:Bag"))
					 ;
				 else
					 temp += (line.substring(line.indexOf("li>")+3, line.lastIndexOf("</rdf")) + ';');
				 continue;
			 }
			// dc:publisher
			 if(line.contains("<dc:publisher>")){
				 hasPublisher = true;
				 continue;
			 }else if(line.contains("</dc:publisher>")){
				 txtPublisher.setText(temp.substring(0, temp.length()-1));
				 hasPublisher = false;
				 temp = "";
				 continue;
			 }
			 if(hasPublisher){
				 if(line.contains("rdf:Bag"))
					 ;
				 else
					 temp += (line.substring(line.indexOf("li>")+3, line.lastIndexOf("</rdf")) + ';');
				 continue;
			 }
			// dc:relation
			 if(line.contains("<dc:relation>")){
				 hasRelation = true;
				 continue;
			 }else if(line.contains("</dc:relation>")){
				 txtRelation.setText(temp.substring(0, temp.length()-1));
				 hasRelation = false;
				 temp = "";
				 continue;
			 }
			 if(hasRelation){
				 if(line.contains("rdf:Bag"))
					 ;
				 else
					 temp += (line.substring(line.indexOf("li>")+3, line.lastIndexOf("</rdf")) + ';');
				 continue;
			 }
			// dc:rights
			 if(line.contains("<dc:rights>")){
				 hasRights = true;
				 continue;
			 }else if(line.contains("</dc:rights>")){
				 txtRights.setText(temp.substring(0, temp.length()-1));
				 hasRights = false;
				 temp = "";
				 continue;
			 }
			 if(hasRights){
				 if(line.contains("rdf:Alt"))
					 ;
				 else
					 temp += (line.substring(line.indexOf(">")+1, line.lastIndexOf("</rdf")));
				 continue;
			 }
			// dc:source
			 if(line.contains("dc:source=")){
				 txtSource.setText(line.substring(line.indexOf("rce=\"") + 5, line.lastIndexOf("\"")));
				 continue;
			 }
			// dc:subject
			 if(line.contains("<dc:subject>")){
				 hasSubject = true;
				 continue;
			 }else if(line.contains("</dc:subject>")){
				 txtSubject.setText(temp.substring(0, temp.length()-1));
				 hasSubject = false;
				 temp = "";
				 continue;
			 }
			 if(hasSubject){
				 if(line.contains("rdf:Bag"))
					 ;
				 else
					 temp += (line.substring(line.indexOf("li>")+3, line.lastIndexOf("</rdf")) + ';');
				 continue;
			 }
			// dc:title
			 if(line.contains("<dc:title>")){
				 hasTitle = true;
				 continue;
			 }else if(line.contains("</dc:title>")){
				 txtTitle.setText(temp.substring(0, temp.length()-1));
				 hasTitle = false;
				 temp = "";
				 continue;
			 }
			 if(hasTitle){
				 if(line.contains("rdf:Alt"))
					 ;
				 else
					 temp += (line.substring(line.indexOf(">")+1, line.lastIndexOf("</rdf")));
				 continue;
			 }
			// dc:type
			 if(line.contains("<dc:type>")){
				 hasType = true;
				 continue;
			 }else if(line.contains("</dc:type>")){
				 txtType.setText(temp.substring(0, temp.length()-1));
				 hasType = false;
				 temp = "";
				 continue;
			 }
			 if(hasType){
				 if(line.contains("rdf:Bag"))
					 ;
				 else
					 temp += (line.substring(line.indexOf("li>")+3, line.lastIndexOf("</rdf")) + ';');
				 continue;
			 }
			 
		 }
		 
	 }
	
}
