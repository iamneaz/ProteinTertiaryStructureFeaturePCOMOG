package net.semanticmetadata.lire.utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class ImageUtils
{
  public static BufferedImage scaleImage(BufferedImage paramBufferedImage, int paramInt)
  {
    assert (paramInt > 0);
    double d1 = paramBufferedImage.getWidth();
    double d2 = paramBufferedImage.getHeight();
    double d3 = 0.0D;
    if (d1 > d2) {
      d3 = paramInt / d1;
    } else {
      d3 = paramInt / d2;
    }
    BufferedImage localBufferedImage = new BufferedImage((int)(d1 * d3), (int)(d2 * d3), 1);
    Graphics localGraphics = localBufferedImage.getGraphics();
    ((Graphics2D)localGraphics).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
    localGraphics.drawImage(paramBufferedImage, 0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight(), null);
    return localBufferedImage;
  }
  
  public static BufferedImage scaleImage(BufferedImage paramBufferedImage, int paramInt1, int paramInt2)
  {
    assert ((paramInt1 > 0) && (paramInt2 > 0));
    BufferedImage localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
    Graphics localGraphics = localBufferedImage.getGraphics();
    localGraphics.drawImage(paramBufferedImage, 0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight(), null);
    return localBufferedImage;
  }
  
  public static BufferedImage cropImage(BufferedImage paramBufferedImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    assert ((paramInt3 > 0) && (paramInt4 > 0));
    BufferedImage localBufferedImage = new BufferedImage(paramInt3, paramInt4, 1);
    Graphics localGraphics = localBufferedImage.getGraphics();
    localGraphics.drawImage(paramBufferedImage, paramInt1, paramInt2, localBufferedImage.getWidth(), localBufferedImage.getHeight(), null);
    return localBufferedImage;
  }
  
  public static BufferedImage convertImageToGrey(BufferedImage paramBufferedImage)
  {
    BufferedImage localBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 10);
    localBufferedImage.getGraphics().drawImage(paramBufferedImage, 0, 0, null);
    return localBufferedImage;
  }
  
  public static void invertImage(BufferedImage paramBufferedImage)
  {
    WritableRaster localWritableRaster = paramBufferedImage.getRaster();
    int[] arrayOfInt = new int[3];
    float f = 0.0F;
    for (int i = 0; i < localWritableRaster.getWidth(); i++) {
      for (int j = 0; j < localWritableRaster.getHeight(); j++)
      {
        localWritableRaster.getPixel(i, j, arrayOfInt);
        arrayOfInt[0] = (255 - arrayOfInt[0]);
        localWritableRaster.setPixel(i, j, arrayOfInt);
      }
    }
  }
  
  public static BufferedImage createWorkingCopy(BufferedImage paramBufferedImage)
  {
    BufferedImage localBufferedImage;
    if (paramBufferedImage.getType() == 1)
    {
      localBufferedImage = paramBufferedImage;
    }
    else
    {
      localBufferedImage = new BufferedImage(paramBufferedImage.getWidth(), paramBufferedImage.getHeight(), 1);
      Graphics2D localGraphics2D = localBufferedImage.createGraphics();
      localGraphics2D.drawImage(paramBufferedImage, null, 0, 0);
    }
    return localBufferedImage;
  }
  
  public static BufferedImage trimWhiteSpace(BufferedImage paramBufferedImage)
  {
    WritableRaster localWritableRaster = paramBufferedImage.getRaster();
    int i = 1;
    int j = 0;
    int k = localWritableRaster.getHeight() - 1;
    int m = 0;
    int n = localWritableRaster.getWidth() - 1;
    int[] arrayOfInt = new int[3 * localWritableRaster.getWidth()];
    int i1 = 250;
    int i2 = 5;
    int i3;
    while (i != 0)
    {
      localWritableRaster.getPixels(0, j, localWritableRaster.getWidth(), 1, arrayOfInt);
      for (i3 = 0; i3 < arrayOfInt.length; i3++) {
        if ((arrayOfInt[i3] < i1) && (arrayOfInt[i3] > i2)) {
          i = 0;
        }
      }
      if (i != 0) {
        j++;
      }
    }
    i = 1;
    while ((i != 0) && (k > j))
    {
      localWritableRaster.getPixels(0, k, localWritableRaster.getWidth(), 1, arrayOfInt);
      for (i3 = 0; i3 < arrayOfInt.length; i3++) {
        if ((arrayOfInt[i3] < i1) && (arrayOfInt[i3] > i2)) {
          i = 0;
        }
      }
      if (i != 0) {
        k--;
      }
    }
    arrayOfInt = new int[3 * localWritableRaster.getHeight()];
    i = 1;
    while (i != 0)
    {
      localWritableRaster.getPixels(m, 0, 1, localWritableRaster.getHeight(), arrayOfInt);
      for (i3 = 0; i3 < arrayOfInt.length; i3++) {
        if ((arrayOfInt[i3] < i1) && (arrayOfInt[i3] > i2)) {
          i = 0;
        }
      }
      if (i != 0) {
        m++;
      }
    }
    i = 1;
    while ((i != 0) && (n > m))
    {
      localWritableRaster.getPixels(n, 0, 1, localWritableRaster.getHeight(), arrayOfInt);
      for (i3 = 0; i3 < arrayOfInt.length; i3++) {
        if ((arrayOfInt[i3] < i1) && (arrayOfInt[i3] > i2)) {
          i = 0;
        }
      }
      if (i != 0) {
        n--;
      }
    }
    BufferedImage localBufferedImage = new BufferedImage(n - m, k - j, 1);
    localBufferedImage.getGraphics().drawImage(paramBufferedImage, 0, 0, localBufferedImage.getWidth(), localBufferedImage.getHeight(), m, j, n, k, null);
    return localBufferedImage;
  }
  
  public static float[] makeGaussianKernel(int paramInt, float paramFloat)
  {
    float[] arrayOfFloat = new float[paramInt * paramInt];
    float f = 0.0F;
    for (int i = 0; i < paramInt; i++) {
      for (int j = 0; j < paramInt; j++)
      {
        int k = i * paramInt + j;
        int m = j - paramInt / 2;
        int n = i - paramInt / 2;
        arrayOfFloat[k] = ((float)Math.pow(2.718281828459045D, -(m * m + n * n) / (2.0F * (paramFloat * paramFloat))));
        f += arrayOfFloat[k];
      }
    }
    for (int i = 0; i < arrayOfFloat.length; i++) {
      arrayOfFloat[i] /= f;
    }
    return arrayOfFloat;
  }
}


/* Location:           E:\EclipseWorkspace\ComogPhogExtractor\lib\lire.jar
 * Qualified Name:     net.semanticmetadata.lire.utils.ImageUtils
 * JD-Core Version:    0.7.0.1
 */