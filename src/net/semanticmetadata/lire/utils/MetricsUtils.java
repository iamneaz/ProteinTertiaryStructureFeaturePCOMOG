package net.semanticmetadata.lire.utils;

public class MetricsUtils
{
  public static double distL1(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    double d = 0.0D;
    for (int i = 0; i < paramArrayOfInt1.length; i++) {
      d += Math.abs(paramArrayOfInt1[i] - paramArrayOfInt2[i]);
    }
    return d / paramArrayOfInt1.length;
  }
  
  public static double distL1(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    double d = 0.0D;
    for (int i = 0; i < paramArrayOfDouble1.length; i++) {
      d += Math.abs(paramArrayOfDouble1[i] - paramArrayOfDouble2[i]);
    }
    return d / paramArrayOfDouble1.length;
  }
  
  public static double distL2(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    double d = 0.0D;
    for (int i = 0; i < paramArrayOfInt1.length; i++) {
      d += (paramArrayOfInt1[i] - paramArrayOfInt2[i]) * (paramArrayOfInt1[i] - paramArrayOfInt2[i]);
    }
    return Math.sqrt(d);
  }
  
  public static double distL2(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    double d = 0.0D;
    for (int i = 0; i < paramArrayOfDouble1.length; i++) {
      d += (paramArrayOfDouble1[i] - paramArrayOfDouble2[i]) * (paramArrayOfDouble1[i] - paramArrayOfDouble2[i]);
    }
    return Math.sqrt(d);
  }
  
  public static float distL2(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float f = 0.0F;
    for (int i = 0; i < paramArrayOfFloat1.length; i++) {
      f += (paramArrayOfFloat1[i] - paramArrayOfFloat2[i]) * (paramArrayOfFloat1[i] - paramArrayOfFloat2[i]);
    }
    return (float)Math.sqrt(f);
  }
  
  public static double jsd(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    double d = 0.0D;
    for (int i = 0; i < paramArrayOfInt1.length; i++) {
      d += (paramArrayOfInt1[i] > 0 ? paramArrayOfInt1[i] * Math.log(2.0D * paramArrayOfInt1[i] / (paramArrayOfInt1[i] + paramArrayOfInt2[i])) : 0.0D) + (paramArrayOfInt2[i] > 0 ? paramArrayOfInt2[i] * Math.log(2.0D * paramArrayOfInt2[i] / (paramArrayOfInt1[i] + paramArrayOfInt2[i])) : 0.0D);
    }
    return d;
  }
  
  public static float jsd(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float f = 0.0F;
    for (int i = 0; i < paramArrayOfFloat1.length; i++) {
      f = (float)(f + ((paramArrayOfFloat1[i] > 0.0F ? paramArrayOfFloat1[i] / 2.0F * Math.log(2.0F * paramArrayOfFloat1[i] / (paramArrayOfFloat1[i] + paramArrayOfFloat2[i])) : 0.0D) + (paramArrayOfFloat2[i] > 0.0F ? paramArrayOfFloat2[i] / 2.0F * Math.log(2.0F * paramArrayOfFloat2[i] / (paramArrayOfFloat1[i] + paramArrayOfFloat2[i])) : 0.0D)));
    }
    return f;
  }
  
  public static float jsd(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    double d = 0.0D;
    for (int i = 0; i < paramArrayOfDouble1.length; i++) {
      d += (paramArrayOfDouble1[i] > 0.0D ? paramArrayOfDouble1[i] / 2.0D * Math.log(2.0D * paramArrayOfDouble1[i] / (paramArrayOfDouble1[i] + paramArrayOfDouble2[i])) : 0.0D) + (paramArrayOfDouble2[i] > 0.0D ? paramArrayOfDouble2[i] / 2.0D * Math.log(2.0D * paramArrayOfDouble2[i] / (paramArrayOfDouble1[i] + paramArrayOfDouble2[i])) : 0.0D);
    }
    return (float)d;
  }
  
  public static double tanimoto(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
  {
    double d1 = 0.0D;
    double d2 = 0.0D;
    double d3 = 0.0D;
    double d4 = 0.0D;
    double d5 = 0.0D;
    double d6 = 0.0D;
    for (int i = 0; i < paramArrayOfInt1.length; i++)
    {
      d2 += paramArrayOfInt1[i];
      d3 += paramArrayOfInt2[i];
    }
    if ((d2 == 0.0D) || (d3 == 0.0D)) {
      d1 = 100.0D;
    }
    if ((d2 == 0.0D) && (d3 == 0.0D)) {
      d1 = 0.0D;
    }
    if ((d2 > 0.0D) && (d3 > 0.0D))
    {
      for (int i = 0; i < paramArrayOfInt1.length; i++)
      {
        d4 += paramArrayOfInt1[i] / d2 * (paramArrayOfInt2[i] / d3);
        d5 += paramArrayOfInt2[i] / d3 * (paramArrayOfInt2[i] / d3);
        d6 += paramArrayOfInt1[i] / d2 * (paramArrayOfInt1[i] / d2);
      }
      d1 = 100.0D - 100.0D * (d4 / (d5 + d6 - d4));
    }
    return d1;
  }
  
  public static double tanimoto(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    double d1 = 0.0D;
    double d2 = 0.0D;
    double d3 = 0.0D;
    double d4 = 0.0D;
    double d5 = 0.0D;
    double d6 = 0.0D;
    for (int i = 0; i < paramArrayOfFloat1.length; i++)
    {
      d2 += paramArrayOfFloat1[i];
      d3 += paramArrayOfFloat2[i];
    }
    if ((d2 == 0.0D) || (d3 == 0.0D)) {
      d1 = 100.0D;
    }
    if ((d2 == 0.0D) && (d3 == 0.0D)) {
      d1 = 0.0D;
    }
    if ((d2 > 0.0D) && (d3 > 0.0D))
    {
      for (int i = 0; i < paramArrayOfFloat1.length; i++)
      {
        d4 += paramArrayOfFloat1[i] / d2 * (paramArrayOfFloat2[i] / d3);
        d5 += paramArrayOfFloat2[i] / d3 * (paramArrayOfFloat2[i] / d3);
        d6 += paramArrayOfFloat1[i] / d2 * (paramArrayOfFloat1[i] / d2);
      }
      d1 = 100.0D - 100.0D * (d4 / (d5 + d6 - d4));
    }
    return d1;
  }
  
  public static double tanimoto(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    double d1 = 0.0D;
    double d2 = 0.0D;
    double d3 = 0.0D;
    double d4 = 0.0D;
    double d5 = 0.0D;
    double d6 = 0.0D;
    for (int i = 0; i < paramArrayOfDouble1.length; i++)
    {
      d2 += paramArrayOfDouble1[i];
      d3 += paramArrayOfDouble2[i];
    }
    if ((d2 == 0.0D) || (d3 == 0.0D)) {
      d1 = 100.0D;
    }
    if ((d2 == 0.0D) && (d3 == 0.0D)) {
      d1 = 0.0D;
    }
    if ((d2 > 0.0D) && (d3 > 0.0D))
    {
      for (int i = 0; i < paramArrayOfDouble1.length; i++)
      {
        d4 += paramArrayOfDouble1[i] / d2 * (paramArrayOfDouble2[i] / d3);
        d5 += paramArrayOfDouble2[i] / d3 * (paramArrayOfDouble2[i] / d3);
        d6 += paramArrayOfDouble1[i] / d2 * (paramArrayOfDouble1[i] / d2);
      }
      d1 = 100.0D - 100.0D * (d4 / (d5 + d6 - d4));
    }
    return d1;
  }
  
  public static double cosineCoefficient(double[] paramArrayOfDouble1, double[] paramArrayOfDouble2)
  {
    double d1 = 0.0D;
    double d2 = 0.0D;
    double d3 = 0.0D;
    for (int i = 0; i < paramArrayOfDouble1.length; i++)
    {
      d1 += paramArrayOfDouble1[i] * paramArrayOfDouble2[i];
      d2 += paramArrayOfDouble1[i] * paramArrayOfDouble1[i];
      d3 += paramArrayOfDouble2[i] * paramArrayOfDouble2[i];
    }
    if (d2 * d3 > 0.0D) {
      return d1 / (Math.sqrt(d2) * Math.sqrt(d3));
    }
    return 0.0D;
  }
  
  public static float distL1(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2)
  {
    float f = 0.0F;
    for (int i = 0; i < paramArrayOfFloat1.length; i++) {
      f += Math.abs(paramArrayOfFloat1[i] - paramArrayOfFloat2[i]);
    }
    return f;
  }
}


/* Location:           E:\EclipseWorkspace\ComogPhogExtractor\lib\lire.jar
 * Qualified Name:     net.semanticmetadata.lire.utils.MetricsUtils
 * JD-Core Version:    0.7.0.1
 */