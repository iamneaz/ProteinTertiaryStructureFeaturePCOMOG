package main;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import camatgenerator.DistanceMatrixGenerator;
import dbwriter.DBWriterThread;
import featuregenerator.FeatureGeneratorThread;
import filewriter.DiskFileWriter;
import pdbparser.PDBParser;
//import statuslogger.JobStatusLogger;

public class Main {
	static Logger logger=Logger.getLogger(Main.class);
	static final long sleepTime=3*60*1000;
	public static void main(String[] args) {
		PropertyConfigurator.configure("log4j.properties");
		startServices();
		System.out.println("Application started.");
		while(true){
			logger.debug("CPQueParsedCoordinates.size(): "+Queues.CPQueParsedCoordinates.size());
			logger.debug("CPQueAlphaMatGenerated.size(): "+Queues.CPQueAlphaMatGenerated.size());
			logger.debug("CPQueFeatureExtracted.size(): "+Queues.CPQueFeatureExtracted.size());
			logger.debug("CPQueFeatureInsertedToDB.size(): "+Queues.CPQueDiskFileWriter.size());
			try{
				Thread.sleep(sleepTime);
			}catch(Exception e){logger.fatal("",e);}
		}
	}

	private static void startServices() {
//		JobStatusLogger.getInstance().start();
		PDBParser.getInstance().start();
		DistanceMatrixGenerator.getInstance().start();
		FeatureGeneratorThread.getInstance().start();
		DBWriterThread.getInstance().start();
		DiskFileWriter.getInstance().start();
	}
}
