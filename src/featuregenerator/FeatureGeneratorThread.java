package featuregenerator;

import main.ComparableProtein;
import main.Queues;
import org.apache.log4j.Logger;

public class FeatureGeneratorThread extends Thread {

	static Logger logger = Logger.getLogger(FeatureGeneratorThread.class);
	boolean running = false;
	public static FeatureGeneratorThread instance = null;

	private FeatureGeneratorThread() {
		super("FeatureGeneratorThread");
	}

	public static FeatureGeneratorThread getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private static synchronized void createInstance() {
		if (instance == null) {
			instance = new FeatureGeneratorThread();
		}
	}

	public void run() {
		running = true;
		int completed=0;
		while (running) {
			try {
				ComparableProtein cp = Queues.CPQueAlphaMatGenerated.take();
				generateFeatures(cp);
				Queues.CPQueFeatureExtracted.put(cp);
				completed++;
			} catch (Exception e) {
				logger.fatal("", e);
			}
			if((completed+1)%1000==0){
				logger.debug("completed: "+completed);
			}
		}
	}

	private void generateFeatures(ComparableProtein cp) {
		cp.computeComog();
	}

}
