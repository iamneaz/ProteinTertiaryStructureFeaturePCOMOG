package util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class ImageUtil {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	public	static int filterGy1 = 0;
	public	static int filterGx1 = 1;
	public	static int filterGy2 = 2;
	public	static int filterGx2 = 3;
	public	static int filterGy3 = 4;
	public	static int filterGx3 = 5;
	
	public 	static BufferedImageOp[] filtersGradientX = new BufferedImageOp[] {
			// Gx
			new ConvolveOp(
					new Kernel(5, 1, new float[] { 1f, -8f, 0, 8f, -1f })),
			// Gxx
			new ConvolveOp(new Kernel(5, 1, new float[] { -1f, 16f, -30f, 16f,
					-1f })),
			// Gxxx
			new ConvolveOp(new Kernel(7, 1, new float[] { 1f, -8f, 13f, 0f,
					-13f, 8f, -1f }))
			
	};



	public 	static BufferedImageOp[] filtersGradientY = new BufferedImageOp[] {
				// Gy
				new ConvolveOp(
						new Kernel(1, 5, new float[] { 1f, -8f, 0, 8f, -1f })),
				// Gyy
				new ConvolveOp(new Kernel(1, 5, new float[] { -1f, 16f, -30f, 16f,
						-1f })),
				// Gyyy
				new ConvolveOp(new Kernel(1, 7, new float[] { 1f, -8f, 13f, 0f,
						-13f, 8f, -1f }))
				};
	

		public static double[][] getMagnitudeImage(BufferedImage Gx,BufferedImage Gy){
			
		Raster rasterGX=	Gx.getRaster();
		Raster rasterGY=	Gy.getRaster();
		double[][] ret=new double[Gx.getWidth()] [Gx.getHeight()];
		  int[] arrayOfInt = { 0 };
		for(int i=0;i<rasterGX.getHeight();i++){
			for(int j=0;j<rasterGX.getWidth();j++){
			int a=	rasterGX.getPixel(i, j, arrayOfInt)[0];
			int b=	rasterGY.getPixel(i, j, arrayOfInt)[0];
				ret[i][j]=Math.sqrt(a*a+b*b); 
			}
		}
			return ret;
		}


	
	
    public static BufferedImage toBufferedImage(Image img)
    {
        if (img instanceof BufferedImage)
        {
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

	
	public static BufferedImage createGrayScaleImageFromMat(int[][] mat){
		
		int dimx=mat.length,dimy=mat[0].length;
		BufferedImage image = new BufferedImage(mat.length, mat[0].length, BufferedImage.TYPE_BYTE_GRAY);
	//	WritableRaster wr=  image.getRaster();
		for(int y = 0; y<dimy; y++){
		    for(int x = 0; x<dimx; x++){
		    	int value = mat[y][x] << 16 | mat[y][x] << 8 | mat[y][x];
		        image.setRGB(x, y, value);
		    }
		}
		return image;
	}
	

}
