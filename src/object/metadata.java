package object;

import java.awt.List;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class metadata implements Serializable{
	
	String filename;
	String SubfileType;
	//tiff
	String ImageWidth;
	String ImageLength;
	String XResolution;
	String YResolution;
	String[] BitsPerSample; 
	String Compression;
	String SamplesPerPixel;
	String PlanarConfiguration;
	String PhotometricInterpretation; //B&W
	String RowsPerStrip; //B&W
	String ResolutionUnit; //B&W
	
	//ICC-Header
	String ProfileCMMType;
	String ProfileVersion;
	String ProfileClass;
	String ColorSpaceData;
	String ProfileConnectionSpace;
	String ProfileDateTime;
	String ProfileFileSignature;
	String PrimaryPlatform;
	String CMMFlags;
	String DeviceManufacturer;
	String DeviceModel;
	String DeviceAttributes;
	String RenderingIntent;
	String ConnectionSpaceIlluminant;
	String ProfileCreator;
	String ProfileID;
	
	//ICC-Profile
	String ProfileDescription;
	String RedMatrixColumn;
	String GreenMatrixColumn;
	String BlueMatrixColumn;
	String MediaWhitePoint;
	String ProfileCopyright;
	String RedTRC;
	String GreenTRC;
	String BlueTRC;
	String MediaBlackPoint; 
	
	String ICCRAW;
	
	boolean ICC_Avalible = false;
	
	public metadata(){
		
	}
	public byte[] getAllBytes() throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		DataOutputStream out = new DataOutputStream(baos);
		String indent = "";
		out.writeBytes(indent + "<?xpacket begin='﻿' id='W5M0MpCehiHzreSzNTczkc9d'?>\n");
		out.writeBytes(indent + "<x:xmpmeta xmlns:x=\"adobe:ns:meta/\">\n");
		indent += "  ";
		out.writeBytes(indent + "<rdf:RDF rdf:about='" + filename + "' xmlns:rdf='http://www.w3.org/1999/02/22-rdf-syntax-ns#'>\n");
		indent += "  ";
		if(ProfileCMMType != null || ProfileVersion != null)
			out.writeBytes(indent + "<rdf:Description xmlns:tiff=\"http://ns.adobe.com/tiff/1.0/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:ICC-header='http://ns.exiftool.ca/ICC_Profile/ICC-header/1.0/' xmlns:ICC_Profile='http://ns.exiftool.ca/ICC_Profile/ICC_Profile/1.0/'>\n");
		else
			out.writeBytes(indent + "<rdf:Description xmlns:tiff=\"http://ns.adobe.com/tiff/1.0/\">\n");
		indent += "  ";
		out.writeBytes(one_line_formation("tiff", "ImageWidth", ImageWidth, indent));
		out.writeBytes(one_line_formation("tiff", "ImageLength", ImageLength, indent));
		out.writeBytes(one_line_formation("tiff", "XResolution", XResolution, indent));
		out.writeBytes(one_line_formation("tiff", "YResolution", YResolution, indent));
		out.writeBytes(BPS_formation(indent));
		//out.writeBytes(indent + one_line_formation("tiff", "Compression", Compression, indent)); //exception 
		//out.writeBytes(indent + one_line_formation("tiff", "PhotometricInterpretation", PhotometricInterpretation, indent)); //exception
		out.writeBytes(one_line_formation("tiff", "SamplesPerPixel", SamplesPerPixel, indent));
		//out.writeBytes(indent + one_line_formation("tiff", "PlanarConfiguration", PlanarConfiguration, indent)); //exception
		//ICC-Header
		out.writeBytes(one_line_formation("ICC-header", "ProfileCMMType", ProfileCMMType, indent));
		out.writeBytes(one_line_formation("ICC-header", "ProfileVersion", ProfileVersion, indent));
		out.writeBytes(one_line_formation("ICC-header", "ProfileClass", ProfileClass, indent));
		out.writeBytes(one_line_formation("ICC-header", "ColorSpaceData", ColorSpaceData, indent));
		out.writeBytes(one_line_formation("ICC-header", "ProfileConnectionSpace", ProfileConnectionSpace, indent));
		out.writeBytes(one_line_formation("ICC-header", "ProfileDateTime", ProfileDateTime, indent));
		out.writeBytes(one_line_formation("ICC-header", "ProfileFileSignature", ProfileFileSignature, indent));
		out.writeBytes(one_line_formation("ICC-header", "PrimaryPlatform", PrimaryPlatform, indent));
		out.writeBytes(one_line_formation("ICC-header", "CMMFlags", CMMFlags, indent));
		out.writeBytes(one_line_formation("ICC-header", "DeviceManufacturer", DeviceManufacturer, indent));
		out.writeBytes(one_line_formation("ICC-header", "DeviceModel", DeviceModel, indent));
		out.writeBytes(one_line_formation("ICC-header", "DeviceAttributes", DeviceAttributes, indent));
		out.writeBytes(one_line_formation("ICC-header", "RenderingIntent", RenderingIntent, indent));
		out.writeBytes(one_line_formation("ICC-header", "ConnectionSpaceIlluminant", ConnectionSpaceIlluminant, indent));
		out.writeBytes(one_line_formation("ICC-header", "ProfileCreator", ProfileCreator, indent));
		out.writeBytes(one_line_formation("ICC-header", "ProfileID", ProfileID, indent));
		//ICC_Profile
		out.writeBytes(one_line_formation("ICC_Profile", "ProfileDescription", ProfileDescription, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "RedMatrixColumn", RedMatrixColumn, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "GreenMatrixColumn", GreenMatrixColumn, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "BlueMatrixColumn", BlueMatrixColumn, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "MediaWhitePoint", MediaWhitePoint, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "ProfileCopyright", ProfileCopyright, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "RedTRC", RedTRC, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "GreenTRC", GreenTRC, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "BlueTRC", BlueTRC, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "MediaBlackPoint", MediaBlackPoint, indent));
		out.writeBytes(one_line_formation("ICC_Profile", "ICCRawData", ICCRAW, indent)); //newly added - needed to test
		if(ICC_Avalible)
			out.writeBytes("    " + rest);
		else
			out.writeBytes("    " + rest_withoutSelfDefinedNamespace);
		return baos.toByteArray();
	}
	private String one_line_formation(String prefix, String tag, String value, String indent){
		
		if(value != null){
			if(prefix.equals("ICC-header") || prefix.equals("ICC_Profile"))
				ICC_Avalible = true;
			return indent + '<' + prefix + ':' + tag + '>' + value + "</" + prefix + ':' + tag + '>' + '\n';
		}
		return "";
	}
	private String  BPS_formation(String indent){
		if(BitsPerSample != null){
			String t = indent + "<tiff:BitsPerSample>\n" + indent + "\t<rdf:Seq>\n";
			for(int i = 0; i < BitsPerSample.length; i++)
				t += (indent + "\t" + "\t<rdf:li>" + BitsPerSample[i] + "</rdf:li>\n"); 
			t += (indent + "\t</rdf:Seq>\n" + indent + "</tiff:BitsPerSample>\n");
			return t;
		}
		return "";
	}
	private String rest_withoutSelfDefinedNamespace = "</rdf:Description>\n</rdf:RDF>\n" + 
			"</x:xmpmeta>\n" + 
			"<?xpacket end='w'?>";
	
	private String rest = "</rdf:Description>\n" + 
			"    <rdf:Description rdf:about=\"\" xmlns:pdfaExtension=\"http://www.aiim.org/pdfa/ns/extension/\" xmlns:pdfaSchema=\"http://www.aiim.org/pdfa/ns/schema#\" xmlns:pdfaProperty=\"http://www.aiim.org/pdfa/ns/property#\" xmlns:pdfaType=\"http://www.aiim.org/pdfa/ns/type#\" xmlns:pdfaField=\"http://www.aiim.org/pdfa/ns/field#\">\n" + 
			"          <pdfaExtension:schemas>\n" + 
			"            <rdf:Bag>\n" + 
			"              <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                <pdfaSchema:schema>ICC header</pdfaSchema:schema>\n" + 
			"                <pdfaSchema:namespaceURI>http://ns.exiftool.ca/ICC_Profile/ICC-header/1.0/</pdfaSchema:namespaceURI>\n" + 
			"                <pdfaSchema:prefix>ICC-header</pdfaSchema:prefix>\n" + 
			"                <pdfaSchema:property>\n" + 
			"                  <rdf:Seq>\n" + 
			"                    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileCMMType</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileVersion</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileClass</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ColorSpaceData</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileConnectionSpace</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileDateTime</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileFileSignature</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>PrimaryPlatform</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>CMMFlags</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>DeviceManufacturer</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>DeviceModel</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>DeviceAttributes</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>RenderingIntent</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ConnectionSpaceIlluminant</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileCreator</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileID</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"                  </rdf:Seq>\n" + 
			"                </pdfaSchema:property>\n" + 
			"	       </rdf:li>\n" + 
			"	       <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                <pdfaSchema:schema>ICC Profile</pdfaSchema:schema>\n" + 
			"                <pdfaSchema:namespaceURI>http://ns.exiftool.ca/ICC_Profile/ICC_Profile/1.0/</pdfaSchema:namespaceURI>\n" + 
			"                <pdfaSchema:prefix>ICC_Profile</pdfaSchema:prefix>\n" + 
			"                <pdfaSchema:property>\n" + 
			"                  <rdf:Seq>\n" + 
			"                    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileDescription</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>RedMatrixColumn</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>GreenMatrixColumn</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>BlueMatrixColumn</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>MediaWhitePoint</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ProfileCopyright</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>RedTRC</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>GreenTRC</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>BlueTRC</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>MediaBlackPoint</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		    <rdf:li rdf:parseType=\"Resource\">\n" + 
			"                      <pdfaProperty:name>ICCRawData</pdfaProperty:name>\n" + 
			"                      <pdfaProperty:valueType>ProperName</pdfaProperty:valueType>\n" + 
			"                      <pdfaProperty:category>external</pdfaProperty:category>\n" + 
			"                      <pdfaProperty:description>some description</pdfaProperty:description>\n" + 
			"                    </rdf:li>\n" + 
			"		  </rdf:Seq>\n" + 
			"                </pdfaSchema:property>\n" + 
			"	       </rdf:li>\n" + 
			"            </rdf:Bag>\n" + 
			"          </pdfaExtension:schemas>\n" + 
			"        </rdf:Description>  \n" + 
			"  </rdf:RDF>\n" + 
			"</x:xmpmeta>\n" + 
			"<?xpacket end='w'?>";
}
