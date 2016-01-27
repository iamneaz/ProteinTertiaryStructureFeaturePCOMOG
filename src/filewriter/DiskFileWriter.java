package filewriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import org.apache.log4j.Logger;
import main.ComparableProtein;
import main.Configs;
import main.Queues;

public class DiskFileWriter extends Thread{

    static Logger logger = Logger.getLogger(DiskFileWriter.class);
	
    
    
    
    boolean running = false;
	public static DiskFileWriter instance = null;

	private DiskFileWriter() {
		super("DiskFileWriter");
	}

	public static DiskFileWriter getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private static synchronized void createInstance() {
		if (instance == null) {
			instance = new DiskFileWriter();
		}
	}

	public void run() {
		running = true;
		int completed=0;
		while (running) {
			try {
				ComparableProtein cp = Queues.CPQueDiskFileWriter.take();
				writeFeatureToFile(cp.scopID,cp.sunID,cp.getPCOMOGAsString());
				completed++;
			} catch (Exception e) {
				logger.fatal("", e);
			}
			if((completed+1)%1000==0){
				logger.debug("completed: "+completed);
			}
		}
	}
    
	
	public  void writeFeatureToFile(String scopID, String sunID,
			String feature) {
		PrintWriter pw = null;
		try {
			File outDir = new File(Configs.compgphogOutDirName);
			if(!outDir.exists()){
				outDir.mkdir();
			}
			
			if(!outDir.isDirectory()||!outDir.exists()){
				logger.fatal("Output directory error...."+outDir);
			}
			
			
			String featureFileName = scopID + ".ent";
			File comogphogFile = new File(outDir, featureFileName);
			pw = new PrintWriter(comogphogFile);
			pw.println(feature);
			pw.flush();
			//logger.debug("written file: "+featureFileName);
		} catch (FileNotFoundException ex) {
			logger.fatal(ex.getMessage(), ex);
		} finally {
			pw.close();
		}
	}
    

}
