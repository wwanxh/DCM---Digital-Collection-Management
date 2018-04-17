package test;

import java.io.IOException;

import com.itextpdf.text.DocumentException;

import add_ons.content_hider;


public class content_hider_test {

	public static void main(String arg[]) throws IOException, DocumentException{
		content_hider ch = new content_hider("1.pdf", "/home/wan/Desktop/VMW-Jun2017-PROS-Move_Out_of_Your_DC-300x250.jpg");
	}
	
}
