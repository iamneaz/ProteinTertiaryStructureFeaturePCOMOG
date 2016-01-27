package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.util.ArrayList;
import java.util.Comparator;
import org.apache.log4j.Logger;

import util.ImageUtil;

public class ComparableProtein implements Comparator<ComparableProtein> {
	static Logger logger = Logger.getLogger(ComparableProtein.class);

	public static final int gradientLevelMax = Configs.gradientLevelMax;
	public static final int spatialLevel = Configs.spatialLevel;
	public static final int binCount = Configs.binCount;
	public static final int pcomogPrecisionMultiplier = 1000000000;
	public static int numberOfNodesAtEveryDerivative;
	public static final int dimx=Configs.imageDimensionX;
	public static final int dimy=Configs.imageDimensionY;
	
	static {
		numberOfNodesAtEveryDerivative = 0;
		for (int spl = 0; spl < spatialLevel; spl++) {
			int div = (int) Math.round(Math.pow(2, spl));
			int partsX = dimx / div;
			int partsY = dimy / div;
			for (int divStartX = 0; divStartX < dimx; divStartX += partsX) {
				for (int divStartY = 0; divStartY < dimy; divStartY += partsY) {
					numberOfNodesAtEveryDerivative++;
				}
			}
		}
		logger.debug("in every derivativeLevel  numberOfNodes: " + numberOfNodesAtEveryDerivative);
		logger.debug("gradientLevelMax: " + gradientLevelMax);
		logger.debug("spatialLevel: " + spatialLevel);
		logger.debug("binCount: " + binCount);
		

	}

	public String name;
	public String scopID;
	public String sunID;

	private int numberOfAlphaCarbon;
	private ArrayList<Double> x;
	private ArrayList<Double> y;
	private ArrayList<Double> z;
	private double[][] alphaCarbonDistanceMatrix;
	private int[][] alphaCarbonDistanceMatrixQuantizedTo255Level;
	private BufferedImage image = null;
	private ArrayList<ArrayList<Integer>> pcomog = new ArrayList<ArrayList<Integer>>();

	public ComparableProtein(ArrayList<Double> x, ArrayList<Double> y, ArrayList<Double> z) {
		this.x = x;
		this.y = y;
		this.z = z;
		numberOfAlphaCarbon = x.size();
	}

	public String getPCOMOGAsString() {
		String pcomogStr = "";

		int featureLen=0;
		if (pcomog != null) {
			for (int i = 0; i < pcomog.size(); i++) {
				ArrayList<Integer> comog = pcomog.get(i);
				if (comog != null) {
					featureLen+=comog.size();
					for (int j = 0; j < comog.size(); j++) {
						pcomogStr += comog.get(j) + "-";

					}
				}
			}
		}
		//logger.debug("featureLen: "+featureLen);
		return pcomogStr;
	}

	public boolean generateDistanceMatrix() {
		return generateDistanceMatrix(this);
	}

	public boolean generateDistanceMatrix(ComparableProtein cp) {
		try {
			if (cp.numberOfAlphaCarbon < 8) {
				logger.debug("Skipping as number of alpha carbon is too low: " + cp.numberOfAlphaCarbon + " scopID:"
						+ cp.scopID);
				return false;
			}
			cp.alphaCarbonDistanceMatrix = new double[cp.numberOfAlphaCarbon][cp.numberOfAlphaCarbon];
			double maxD = Double.MIN_VALUE;
			double minD = Double.MAX_VALUE;
			for (int i = 0; i < cp.numberOfAlphaCarbon; i++) {
				double x1 = cp.x.get(i);
				double y1 = cp.y.get(i);
				double z1 = cp.z.get(i);
				for (int j = 0; j < cp.numberOfAlphaCarbon; j++) {
					double x2 = cp.x.get(j);
					double y2 = cp.y.get(j);
					double z2 = cp.z.get(j);
					double d = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2) + Math.pow(z1 - z2, 2));
					maxD = Math.max(maxD, d);
					minD = Math.min(minD, d);
					cp.alphaCarbonDistanceMatrix[i][j] = d;
					cp.alphaCarbonDistanceMatrix[j][i] = d;
				}
			}
			double rangeD = maxD - minD;
			double normalizer = 255 / rangeD;
			alphaCarbonDistanceMatrixQuantizedTo255Level = new int[cp.numberOfAlphaCarbon][cp.numberOfAlphaCarbon];
			for (int i = 0; i < cp.numberOfAlphaCarbon; i++) {
				for (int j = i; j < cp.numberOfAlphaCarbon; j++) {
					double normalizedD = (cp.alphaCarbonDistanceMatrix[i][j] - minD) * normalizer;
					cp.alphaCarbonDistanceMatrixQuantizedTo255Level[i][j] = (int) Math.round(normalizedD);
					cp.alphaCarbonDistanceMatrixQuantizedTo255Level[j][i] = (int) Math.round(normalizedD);
				}
			}
			return true;
		} catch (Exception e) {
			logger.fatal("", e);
			return false;
		}

	}

	public void computeComog() {

		if(alphaCarbonDistanceMatrixQuantizedTo255Level==null){
			logger.debug("alphaCarbonDistanceMatrixQuantizedTo255Level is null... can't compuet feature..returning...");
			return;
		}
		if (image == null) {
			image = ImageUtil.createGrayScaleImageFromMat(this.alphaCarbonDistanceMatrixQuantizedTo255Level);
		}

		image = toBufferedImage(image.getScaledInstance(128, 128, Image.SCALE_SMOOTH));
		int newdimx = image.getWidth();
		int newdimy = image.getHeight();
		double anglePrecision = (double) 360.0 / binCount;

		for (int gradientLevel = 0; gradientLevel < gradientLevelMax; gradientLevel++) {
			BufferedImage Gy = util.ImageUtil.filtersGradientY[gradientLevel].filter(image, null);
			BufferedImage Gx = util.ImageUtil.filtersGradientX[gradientLevel].filter(image, null);
			double[][] imGradMag = new double[newdimx][newdimy];
			double[][] imGradAngle = new double[newdimx][newdimy];
			Raster rasterGX = Gx.getRaster();
			Raster rasterGY = Gy.getRaster();
			int[] arrayOfInt = { 0 };
			double maxMagnit = 0;
			double minMagnit = 0;
			for (int i = 0; i < rasterGX.getHeight(); i++) {
				for (int j = 0; j < rasterGX.getWidth(); j++) {
					int a = rasterGX.getPixel(i, j, arrayOfInt)[0];
					int b = rasterGY.getPixel(i, j, arrayOfInt)[0];
					imGradMag[i][j] = Math.sqrt(a * a + b * b);
					maxMagnit = Math.max(maxMagnit, imGradMag[i][j]);
					minMagnit = Math.min(minMagnit, imGradMag[i][j]);
					double theta = Math.atan2(a, b) * 180 / Math.PI;
					if (theta < -anglePrecision / 2)
						theta += 360;
					imGradAngle[i][j] = Math.round(theta / anglePrecision);
				}
			}
			double comogThreshold = minMagnit + (maxMagnit - minMagnit) * 0.001;
			computeComog(imGradMag, imGradAngle, binCount, comogThreshold, spatialLevel);
		}
	}

	private void computeComog(double[][] imGradMag, double[][] imGradAngle, int binCount2, double comogThreshold,
			int spatialLevel2) {
		ArrayList<Integer> comogAsList = new ArrayList<Integer>();

		for (int spl = 0; spl < spatialLevel2; spl++) {
			int div = (int) Math.round(Math.pow(2, spl));
			int partsX = imGradMag.length / div;
			int partsY = imGradMag[0].length / div;
			for (int divStartX = 0; divStartX < imGradMag.length; divStartX += partsX) {
				for (int divStartY = 0; divStartY < imGradMag[0].length; divStartY += partsY) {
					double[][] comog = new double[binCount2][binCount2];
					for (int rowNo = divStartX; rowNo < divStartX + partsX; rowNo++) {
						for (int colNo = divStartY; colNo < divStartY + partsY; colNo++) {
							if (imGradMag[rowNo][colNo] > comogThreshold) {
								int x = (int) Math.round(imGradAngle[rowNo][colNo]);
								for (int i = rowNo - 1; i <= rowNo + 1; i++) {
									for (int j = colNo - 1; j <= colNo + 1; j++) {
										if (i >= 0 && j >= 0 && i < imGradAngle.length && j < imGradAngle[i].length
												&& (imGradMag[i][j] > comogThreshold)) {
											int y = (int) Math.round(imGradAngle[i][j]);
											comog[x][y]++;
										}
									}
								}
							}
						}
					}
					double comogUpperIncludingDiagonalSum = 0;
					for (int xpos = 0; xpos < comog.length; xpos++) {
						for (int ypos = xpos; ypos < comog.length; ypos++) {
							comogUpperIncludingDiagonalSum += comog[xpos][ypos];
						}
					}
					double multiplier = pcomogPrecisionMultiplier / (comogUpperIncludingDiagonalSum * gradientLevelMax*numberOfNodesAtEveryDerivative);
					for (int xpos = 0; xpos < comog.length; xpos++) {
						for (int ypos = xpos; ypos < comog.length; ypos++) {
							comogAsList.add((int) (comog[xpos][ypos] * multiplier));
						}
					}
				}
			}
		}
		this.pcomog.add(comogAsList);
	}

	public static BufferedImage toBufferedImage(Image img) {
		if (img instanceof BufferedImage) {
			return (BufferedImage) img;
		}
		// Create a buffered image with transparency
		BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_BYTE_GRAY);
		// Draw the image on to the buffered image
		Graphics2D bGr = bimage.createGraphics();
		bGr.drawImage(img, 0, 0, null);
		bGr.dispose();
		// Return the buffered image
		return bimage;
	}

	public int compare(ComparableProtein o1, ComparableProtein o2) {
		double d = 0;
		for (int i = 0; i < o1.pcomog.size(); i++) {
			ArrayList<Integer> list1 = o1.pcomog.get(i);
			ArrayList<Integer> list2 = o2.pcomog.get(i);
			for (int j = 0; j < list1.size(); i++) {
				d += Math.pow((list1.get(i) - list2.get(i)), 2);
			}
		}
		return (int) (Math.sqrt(d) * 100000);
	}

}
