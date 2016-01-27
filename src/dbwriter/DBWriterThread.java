package dbwriter;

import main.ComparableProtein;
import main.Queues;

import org.apache.log4j.Logger;

public class DBWriterThread extends Thread {

	static Logger logger = Logger.getLogger(DBWriterThread.class);
	boolean running = false;
	public static DBWriterThread instance = null;

	private DBWriterThread() {
		super("DBWriterThread");
	}

	public static DBWriterThread getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private static synchronized void createInstance() {
		if (instance == null) {
			instance = new DBWriterThread();
		}
	}

	public void run() {
		running = true;
		int completed=0;
		while (running) {
			try {
				ComparableProtein cp = Queues.CPQueFeatureExtracted.take();
				insertToDB(cp);
				Queues.CPQueDiskFileWriter.put(cp);
				completed++;
			} catch (Exception e) {
				logger.fatal("Exception at DBWriterThread", e);
			}

			if((completed+1)%1000==0){
				logger.debug("completed: "+completed);
			}
		}
	}

	private void insertToDB(ComparableProtein cp) {
		try {
			//logger.debug("Inside unimplemented stub method insertToDB, cp.scopID: " + cp.scopID);
		} catch (Exception e) {
			logger.fatal("", e);
		}
	}
}
