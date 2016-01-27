package pdbparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;
import main.ComparableProtein;
import main.Configs;
import org.apache.log4j.Logger;

public class PDBParser extends Thread {

	static Logger logger = Logger.getLogger(PDBParser.class);
	boolean running = false;
	public static PDBParser instance = null;

	private PDBParser() {
		super("PDBParser");
	}

	public static PDBParser getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private static synchronized void createInstance() {
		if (instance == null) {
			instance = new PDBParser();
		}
	}

	public void run() {
		running = true;
		File inDir = new File(Configs.inDirName);
		parseAllRecursivelyFromDir(inDir);
	}

	private void parseAllRecursivelyFromDir(File inFIle) {
		try {
			if (!inFIle.exists()) {
				logger.debug("Not exists " + inFIle);
				return;
			}
			if (inFIle.isFile()) {

				logger.debug("Parsing started for file: " + inFIle);
				parseScopMultiDomainFile(inFIle);
				logger.debug("Parsing complete: " + inFIle);
				return;
			}

			if (inFIle.isDirectory()) {
				logger.debug("Parsing all from directory: " + inFIle);
				File[] files = inFIle.listFiles();
				for (File file : files) {
					parseAllRecursivelyFromDir(file);
				}
			}

		} catch (Exception e) {
			logger.fatal("", e);
		}
	}

	public void parseScopMultiDomainFile(String multiDomainFileName) {
		File multiDomainFile = new File(multiDomainFileName);
		parseScopMultiDomainFile(multiDomainFile);
	}

	public void parseScopMultiDomainFile(File multiDomainFile) {

		try {
			logger.debug("parsing file: " + multiDomainFile.getName());
			Scanner sc = new Scanner(multiDomainFile);
			int domainNo = 0;
			ArrayList<Double> x = new ArrayList<Double>();
			ArrayList<Double> y = new ArrayList<Double>();
			ArrayList<Double> z = new ArrayList<Double>();
			int numOfAtoms = 0;
			String SCOPe_sid = "";
			String SCOPe_sun = "";
			String line;
			int seqNo = 0;
			if (sc.hasNext()) {
				line = sc.nextLine();
				while (sc.hasNext() && !line.startsWith("REMARK")) {
					line = sc.nextLine();
				}
			}

			while (sc.hasNext()) {
				line = sc.nextLine();
				if (line.startsWith("TER") || line.startsWith("END")) {
					numOfAtoms = seqNo;
					ComparableProtein cp = new ComparableProtein(x, y, z);
					cp.scopID = SCOPe_sid;
					cp.sunID = SCOPe_sun;
					try {
						main.Queues.CPQueParsedCoordinates.put(cp);
					} catch (InterruptedException e) {
						logger.fatal("Exception at insert to queuse", e);
					}
					System.gc();
					if (domainNo % 1000 == 0) {
						logger.debug("Completed domains: " + domainNo);
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							logger.fatal(e.getMessage(), e);
						}
					}
					// skip upto next model
					if (sc.hasNext()) {
						line = sc.nextLine();
					}
					while (sc.hasNext() && !line.startsWith("REMARK")) {
						line = sc.nextLine();
					}
				}
				StringTokenizer strTok = new StringTokenizer(line, " ");
				int numOfTokens;
				numOfTokens = strTok.countTokens();
				if (numOfTokens < 3) {
					continue;
				}
				String tokens[] = new String[numOfTokens];
				int tokenid = 0;
				while (strTok.hasMoreTokens()) {
					tokens[tokenid++] = strTok.nextToken();
				}

				if ("REMARK".equalsIgnoreCase(tokens[0].trim())) {
					if (tokens[3].startsWith("SCOPe-sid")) {
						// start of new domain
						SCOPe_sid = tokens[4].trim();
						domainNo++;
						seqNo = 0;
						//This commented part was experimental to use multiple process for same experiment communicating through a common database
//						boolean isAlreadyProcessed = statuslogger.JobStatusLogger
//								.isAlreadyProcessed(Configs.experimentNo, SCOPe_sid);
//						if (isAlreadyProcessed) {
//							logger.debug("found already processed. scopid: " + SCOPe_sid);
//							// skip this domain
//							while (sc.hasNext()) {
//								line = sc.nextLine();
//								if (line.startsWith("TER") || line.startsWith("END")) {
//									break;
//								}
//							}
//							if (sc.hasNext()) {
//								line = sc.nextLine();
//							}
//							while (sc.hasNext() && !line.startsWith("REMARK")) {
//								line = sc.nextLine();
//							}
//						} else 
//						
						{
							// /initialize to parse this domain
							x = new ArrayList<Double>();
							y = new ArrayList<Double>();
							z = new ArrayList<Double>();
						}

					} else if (tokens[3].startsWith("SCOPe-sun")) {
						SCOPe_sun = tokens[4].trim();
					}
				} else if ("ATOM".equalsIgnoreCase(tokens[0].trim())) {
					if (tokens[2].equalsIgnoreCase("ca")) {

						double xx = 0;
						double yy = 0;
						double zz = 0;

						if (numOfTokens >= 12) {
							try {
								xx = (Double.parseDouble(tokens[6]));
							} catch (Exception e) {
								logger.fatal("", e);
							}
							try {
								yy = (Double.parseDouble(tokens[7]));
							} catch (Exception e) {
								logger.fatal("", e);
							}
							try {
								zz = (Double.parseDouble(tokens[8]));
							} catch (Exception e) {
								logger.fatal("", e);
							}
						}

						if (numOfTokens < 12) {
							if (tokens[6].substring(1).contains("-")) {
								int xendsAt = tokens[6].indexOf("-", 1);
								try {
									xx = Double.parseDouble(tokens[6].substring(0, xendsAt));
								} catch (Exception e) {
								}

								if (tokens[6].substring(xendsAt + 1).contains("-")) {
									int yendsAt = tokens[6].indexOf("-", xendsAt + 1);
									try {
										yy = Double.parseDouble(tokens[6].substring(xendsAt + 1, yendsAt));
									} catch (Exception e) {
									}
									try {
										zz = Double.parseDouble(tokens[6].substring(yendsAt + 1));
									} catch (Exception e) {
									}
								} else {
									try {
										yy = Double.parseDouble(tokens[6].substring(xendsAt + 1));
									} catch (Exception e) {
									}
									try {
										zz = Double.parseDouble(tokens[7]);
									} catch (Exception e) {
									}
								}
							} else {
								try {
									xx = Double.parseDouble(tokens[6]);
								} catch (Exception e) {
								}

								if (tokens[7].substring(1).contains("-")) {
									int yendsAt = tokens[7].indexOf("-", 1);
									try {
										yy = Double.parseDouble(tokens[7].substring(0, yendsAt));
									} catch (Exception e) {
									}
									try {
										zz = Double.parseDouble(tokens[7].substring(yendsAt + 1));
									} catch (Exception e) {
									}
								}
							}
						}
						x.add(xx);
						y.add(yy);
						z.add(zz);
						seqNo++;
					}
				}
			}
		} catch (FileNotFoundException ex) {
			logger.fatal(ex.getMessage(), ex);
		}
		logger.debug("Completed parsing file: " + multiDomainFile.getName());
	}

	public void parsePDBSingleFile(String fileName) {
		parsePDBSingleFile(new File(fileName));
	}

	public void parsePDBSingleFile(File pdbFormatFile) {

		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		ArrayList<Double> z = new ArrayList<Double>();
	
		
		if (!(pdbFormatFile.getName().endsWith(".pdb") || pdbFormatFile.getName().endsWith(".ent")
				|| pdbFormatFile.getName().endsWith(".txt"))) {
			logger.debug("file extension not like pdb files");
			return;
		}
		int seqNo = 0;
		try {
			Scanner sc = new Scanner(pdbFormatFile);
			seqNo = 0;
			while (sc.hasNext()) {
				String line = sc.nextLine();
				StringTokenizer strTok = new StringTokenizer(line, " ");
				int numOfTokens;
				numOfTokens = strTok.countTokens();
				String tokens[] = new String[numOfTokens];
				int tokenid = 0;
				while (strTok.hasMoreTokens()) {
					tokens[tokenid++] = strTok.nextToken();
				}
				if (tokens[0].equalsIgnoreCase("ENDMDL")) {
					// System.out.println(" first model of multiple model is
					// considered ");
					break;
				}
				if (tokens[0].equalsIgnoreCase("END")) {
					break;
				}
				if (numOfTokens < 8) {
					continue;
				}
				if (!tokens[0].equalsIgnoreCase("atom")) {
					continue;
				}
				if (tokens[0].equalsIgnoreCase("atom")) {
					if (tokens[2].equalsIgnoreCase("ca")) {
						if (numOfTokens >= 12) {
							x.add( Double.parseDouble(tokens[6]));
							y.add(Double.parseDouble(tokens[7]));
							z.add (Double.parseDouble(tokens[8]));
						}
						if (numOfTokens < 12) {
							if (tokens[6].substring(1).contains("-")) {
								int xendsAt = tokens[6].indexOf("-", 1);
								x.add(Double.parseDouble(tokens[6].substring(0, xendsAt)));
								if (tokens[6].substring(xendsAt + 1).contains("-")) {
									int yendsAt = tokens[6].indexOf("-", xendsAt + 1);
									y.add (Double.parseDouble(tokens[6].substring(xendsAt + 1, yendsAt)));
									z.add(Double.parseDouble(tokens[6].substring(yendsAt + 1)));
								} else {
									y.add(Double.parseDouble(tokens[6].substring(xendsAt + 1)));
									z.add(Double.parseDouble(tokens[7]));
								}
							} else {
								z.add(Double.parseDouble(tokens[6]));
								if (tokens[7].substring(1).contains("-")) {
									int yendsAt = tokens[7].indexOf("-", 1);
									y.add(Double.parseDouble(tokens[7].substring(0, yendsAt)));
									z.add (Double.parseDouble(tokens[7].substring(yendsAt + 1)));
								}
							}
						}
						seqNo++;
					}
				}
			}
		} catch (Exception e) {
			logger.fatal(e.getMessage(), e);
		}
		int numOfCaAtom = seqNo - 1;

		//x
		//y
		//z
		
		// xyz coordinates
	}

}
