/* 
 * Author: 	Xueheng Wan
 * Date:	Sep. 2017
 * 
 * Class:	Duplicates_Finder
 * Project: DCM - Digital Collections Management
 * 
 * Description:	This class takes two directory paths as parameters, computes and return number of 
 * 				duplicate files that appear in both directories.
 * 
 */
package add_ons;

import java.io.File;

public class Duplicates_Finder {
	public Duplicates_Finder(String path1, String path2) {
		File[] files1 = new File(path1).listFiles();
		File[] files2 = new File(path2).listFiles();
		int counter = 0;
		for(File f1 : files1)
			for(File f2 : files2)
				if(f1.getName().compareTo(f2.getName()) == 0) {
					System.out.println("Duplicate Found : " + f1.getName());
					counter ++;
				}
		System.out.println("Total duplicates: " + counter);
	}
}
