package net.semanticmetadata.lire.imageanalysis;

import java.awt.image.BufferedImage;

public abstract interface LireFeature
{
  public abstract void extract(BufferedImage paramBufferedImage);
  
  public abstract byte[] getByteArrayRepresentation();
  
  public abstract void setByteArrayRepresentation(byte[] paramArrayOfByte);
  
  public abstract void setByteArrayRepresentation(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
  
  public abstract double[] getDoubleHistogram();
  
  public abstract float getDistance(LireFeature paramLireFeature);
  
  public abstract String getStringRepresentation();
  
  public abstract void setStringRepresentation(String paramString);
}


/* Location:           E:\EclipseWorkspace\ComogPhogExtractor\lib\lire.jar
 * Qualified Name:     net.semanticmetadata.lire.imageanalysis.LireFeature
 * JD-Core Version:    0.7.0.1
 */