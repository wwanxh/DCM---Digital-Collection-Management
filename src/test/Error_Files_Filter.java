package test;

import java.io.IOException;

import model.Error_Files_Picker;
import view.DCM_GUI;

public class Error_Files_Filter {

	public static void main(String arg[]) throws IOException{
		//Error_Files_Picker efp = new Error_Files_Picker("/media/wan/AZU_ACKU_2016/ACKU2016/");
		Error_Files_Picker efp = new Error_Files_Picker("/media/wan/AZU_ACKU_2016/ACKU2015/");
		Error_Files_Picker efp1 = new Error_Files_Picker("/media/wan/AZU_ACKU_2016/Hami2017/");
		Error_Files_Picker efp2 = new Error_Files_Picker("/media/wan/AZU_ACKU_2016/remove_check/");
	}
	
}
