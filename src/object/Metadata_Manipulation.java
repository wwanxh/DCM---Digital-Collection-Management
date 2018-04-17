package object;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Metadata_Manipulation {
	public metadata obj;
	public Metadata_Manipulation(String Path) throws IOException{
		obj = new metadata();
		readFile(Path);
	}
	public Metadata_Manipulation(ArrayList<String> list) throws IOException{
		obj = new metadata();
		readList(list);
	}
	private void extractICCProfile(String Path){
		obj.ICCRAW = ""; 
	}
	private void readFile(String path) throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(path));
		String line = null;
		//pq.fileName = getFileName(path);
		while((line = br.readLine()) != null){
			if(line.indexOf("System:FileName") != -1){
				obj.filename = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:SubfileType") != -1){
				obj.SubfileType = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:ImageWidth") != -1){
				obj.ImageWidth = line.substring(line.indexOf("h>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:ImageHeight") != -1){
				obj.ImageLength = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:BitsPerSample") != -1){
				obj.BitsPerSample = line.substring(line.indexOf("e>")+2, line.indexOf("</")).split("\\s+");//split String by space
			}else if(line.indexOf("IFD0:Compression") != -1){
				obj.Compression = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:PhotometricInterpretation") != -1){
				obj.PhotometricInterpretation = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:SamplesPerPixel") != -1){
				obj.SamplesPerPixel = line.substring(line.indexOf("l>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:RowsPerStrip") != -1){
				obj.RowsPerStrip = line.substring(line.indexOf("p>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:XResolution") != -1){
				obj.XResolution = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:YResolution") != -1){
				obj.YResolution = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:ResolutionUnit") != -1){
				obj.ResolutionUnit = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}
			/* Exclusively for Color TIFF File below */ 
			else if(line.indexOf("ICC-header:ProfileCMMType") != -1){
				obj.ProfileCMMType = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileVersion") != -1){
				obj.ProfileVersion = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileClass") != -1){
				obj.ProfileClass = line.substring(line.indexOf("s>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ColorSpaceData") != -1){
				obj.ColorSpaceData = line.substring(line.indexOf("a>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileConnectionSpace") != -1){
				obj.ProfileConnectionSpace = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileDateTime") != -1){
				obj.ProfileDateTime = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileFileSignature") != -1){
				obj.ProfileFileSignature = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:PrimaryPlatform") != -1){
				obj.PrimaryPlatform = line.substring(line.indexOf("m>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:CMMFlags") != -1){
				obj.CMMFlags = line.substring(line.indexOf("s>")+2, line.indexOf("</"));
			}else if(line.indexOf("IICC-header:DeviceManufacturer") != -1){
				obj.DeviceManufacturer = line.substring(line.indexOf("r>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:DeviceModel>") != -1){
				obj.DeviceModel = line.substring(line.indexOf("l>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:DeviceAttributes>") != -1){
				obj.DeviceAttributes = line.substring(line.indexOf("s>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:RenderingIntent>") != -1){
				obj.RenderingIntent = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:ConnectionSpaceIlluminant>") != -1){
				obj.ConnectionSpaceIlluminant = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:ProfileCreator>") != -1){
				obj.ProfileCreator = line.substring(line.indexOf("r>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:ProfileID>") != -1){
				obj.ProfileID = line.substring(line.indexOf("D>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:ProfileDescription>") != -1){
				obj.ProfileDescription = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:RedMatrixColumn>") != -1){
				obj.RedMatrixColumn = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:GreenMatrixColumn>") != -1){
				obj.GreenMatrixColumn = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:BlueMatrixColumn>") != -1){
				obj.BlueMatrixColumn = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:MediaWhitePoint>") != -1){
				obj.MediaWhitePoint = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:MediaBlackPoint>") != -1){
				obj.MediaBlackPoint = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:ProfileCopyright>") != -1){
				obj.ResolutionUnit = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}
			
		}
		//
		br.close();
	}
	private void readList(ArrayList<String> list) throws IOException{
		String line = null;
		for(int i = 0; i < list.size(); i ++){
			line = list.get(i);
			if(line.indexOf("System:FileName") != -1){
				obj.filename = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:SubfileType") != -1){
				obj.SubfileType = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:ImageWidth") != -1){
				obj.ImageWidth = line.substring(line.indexOf("h>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:ImageHeight") != -1){
				obj.ImageLength = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:BitsPerSample") != -1){
				obj.BitsPerSample = line.substring(line.indexOf("e>")+2, line.indexOf("</")).split("\\s+");//split String by space
			}else if(line.indexOf("IFD0:Compression") != -1){
				obj.Compression = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:PhotometricInterpretation") != -1){
				obj.PhotometricInterpretation = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:SamplesPerPixel") != -1){
				obj.SamplesPerPixel = line.substring(line.indexOf("l>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:RowsPerStrip") != -1){
				obj.RowsPerStrip = line.substring(line.indexOf("p>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:XResolution") != -1){
				obj.XResolution = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:YResolution") != -1){
				obj.YResolution = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("IFD0:ResolutionUnit") != -1){
				obj.ResolutionUnit = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}
			/* Exclusively for Color TIFF File below */ 
			else if(line.indexOf("ICC-header:ProfileCMMType") != -1){
				obj.ProfileCMMType = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileVersion") != -1){
				obj.ProfileVersion = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileClass") != -1){
				obj.ProfileClass = line.substring(line.indexOf("s>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ColorSpaceData") != -1){
				obj.ColorSpaceData = line.substring(line.indexOf("a>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileConnectionSpace") != -1){
				obj.ProfileConnectionSpace = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileDateTime") != -1){
				obj.ProfileDateTime = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:ProfileFileSignature") != -1){
				obj.ProfileFileSignature = line.substring(line.indexOf("e>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:PrimaryPlatform") != -1){
				obj.PrimaryPlatform = line.substring(line.indexOf("m>")+2, line.indexOf("</"));
			}else if(line.indexOf("ICC-header:CMMFlags") != -1){
				obj.CMMFlags = line.substring(line.indexOf("s>")+2, line.indexOf("</"));
			}else if(line.indexOf("IICC-header:DeviceManufacturer") != -1){
				obj.DeviceManufacturer = line.substring(line.indexOf("r>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:DeviceModel>") != -1){
				obj.DeviceModel = line.substring(line.indexOf("l>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:DeviceAttributes>") != -1){
				obj.DeviceAttributes = line.substring(line.indexOf("s>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:RenderingIntent>") != -1){
				obj.RenderingIntent = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:ConnectionSpaceIlluminant>") != -1){
				obj.ConnectionSpaceIlluminant = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:ProfileCreator>") != -1){
				obj.ProfileCreator = line.substring(line.indexOf("r>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC-header:ProfileID>") != -1){
				obj.ProfileID = line.substring(line.indexOf("D>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:ProfileDescription>") != -1){
				obj.ProfileDescription = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:RedMatrixColumn>") != -1){
				obj.RedMatrixColumn = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:GreenMatrixColumn>") != -1){
				obj.GreenMatrixColumn = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:BlueMatrixColumn>") != -1){
				obj.BlueMatrixColumn = line.substring(line.indexOf("n>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:MediaWhitePoint>") != -1){
				obj.MediaWhitePoint = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:MediaBlackPoint>") != -1){
				obj.MediaBlackPoint = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}else if(line.indexOf("<ICC_Profile:ProfileCopyright>") != -1){
				obj.ResolutionUnit = line.substring(line.indexOf("t>")+2, line.indexOf("</"));
			}
			
		}
	}
	
	public byte[] getBytes() throws IOException{
		byte[] temp = obj.getAllBytes();
		return temp;
	}
	
}
