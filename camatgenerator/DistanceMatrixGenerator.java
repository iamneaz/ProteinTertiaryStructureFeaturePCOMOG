package camatgenerator;

import main.ComparableProtein;
import main.Queues;

import org.apache.log4j.Logger;

public class DistanceMatrixGenerator extends Thread {

	static Logger logger = Logger.getLogger(DistanceMatrixGenerator.class);
	boolean running = false;
	public static DistanceMatrixGenerator instance = null;

	private DistanceMatrixGenerator() {
		super("DistanceMatrixGenerator");
	}
	public static DistanceMatrixGenerator getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}
	private static synchronized void createInstance() {
		if (instance == null) {
			instance = new DistanceMatrixGenerator();
		}
	}

	public void run() {
		running = true;
		int completed=0;
		while (running) {
			try {
				ComparableProtein cp = Queues.CPQueParsedCoordinates.take();
				boolean succcess=cp.generateDistanceMatrix();
				if(succcess)
				Queues.CPQueAlphaMatGenerated.put(cp);
				completed++;
			} catch (Exception e) {
				logger.fatal("Exception at DistanceMatrixGenerator", e);
			}
		}
		if((completed+1)%1000==0){
			logger.debug("completed: "+completed);
		}

	}
}
