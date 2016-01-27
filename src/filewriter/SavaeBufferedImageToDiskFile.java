package filewriter;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;

public class SavaeBufferedImageToDiskFile {

	public static void save(BufferedImage bi,File outDir,String name){
		
		try{
		if(outDir==null){
			 outDir = new File("outDir");
			
			if(!outDir.exists()||!outDir.isDirectory()){
				outDir.mkdir();
			}
		}
		
		if(name==null){
			
			Date d=new Date();
			name="image_"+d.getTime()+".png";
		}
		
		File outFile=new File(outDir.getAbsoluteFile(),name);
		outFile.createNewFile();
		
		ImageIO.write(bi, "png", outFile);
		
				
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
