package main;

import java.util.concurrent.LinkedBlockingQueue;
//import statuslogger.JobStatusDTO;

public class Queues {
//	public static LinkedBlockingQueue<JobStatusDTO> mediationPacketQueue=new LinkedBlockingQueue<JobStatusDTO>(1000);
	public static LinkedBlockingQueue<ComparableProtein> CPQueParsedCoordinates=new LinkedBlockingQueue<ComparableProtein>(100);
	public static LinkedBlockingQueue<ComparableProtein> CPQueAlphaMatGenerated=new LinkedBlockingQueue<ComparableProtein>(100);
	public static LinkedBlockingQueue<ComparableProtein> CPQueFeatureExtracted=new LinkedBlockingQueue<ComparableProtein>(100);
	public static LinkedBlockingQueue<ComparableProtein> CPQueDiskFileWriter=new LinkedBlockingQueue<ComparableProtein>(100);
	
	}
