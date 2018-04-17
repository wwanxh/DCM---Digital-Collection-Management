package object;

import java.util.ArrayList;

public class AddOns_root_metadata {

	/* Dublin Core Properties */
	public ArrayList<String> contributors; //Unordered array of ProperName
	public String coverage; //Text
	public ArrayList<String> creators; //Ordered array of ProperName
	public ArrayList<String> date; ////Ordered array of Date
	public String description; //Language Alternative
	public String format; //MIMEType
	public String identifier; //Text
	public ArrayList<String> languages; //Unordered array of Locale
	public ArrayList<String> publishers; //Unordered array of ProperName
	public ArrayList<String> relations; //Unordered array of Text
	public String rights; //Language Alternative
	public String source; //Text
	public ArrayList<String> subject; //Unordered array of Text
	public String title; //Language Alternative
	public ArrayList<String> types; //Unordered array of Text
	
	/* XMP Properties */
	public String createdate = "";
	public String creatortool = "";;
	public ArrayList<String> identifier_xmp = new ArrayList<String>(); //unordered
	public String metadatadate = "";
	public String modifydate = "";
	
	
	/* XMP Media Management Properties */
	public String derived_from = "";
	public String document_id = "";
	public String rendition_class = "";
	public String rendition_params = "";
	
	
	public AddOns_root_metadata(){
		contributors = new ArrayList<String>();
		contributors.add("The University of Arizona Libraries");
	    contributors.add("Afghan Center at Kabul University");
		coverage = "";
		creators = new ArrayList<String>();
		date = new ArrayList<String>();
		description = "";
		format = "";
		identifier = "";
		languages = new ArrayList<String>();
		publishers = new ArrayList<String>();
		relations = new ArrayList<String>();
		relations.add("Afghanistan Digital Collections");
		rights = "";
		source = "";
		subject = new ArrayList<String>();
		title = "";
		types = new ArrayList<String>();
		
	}
}
