package net.semanticmetadata.lire.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class SerializationUtils
{
  public static int toInt(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length != 4)) {
      return 0;
    }
    return (0xFF & paramArrayOfByte[0]) << 24 | (0xFF & paramArrayOfByte[1]) << 16 | (0xFF & paramArrayOfByte[2]) << 8 | (0xFF & paramArrayOfByte[3]) << 0;
  }
  
  public static byte[] toBytes(int paramInt)
  {
    return new byte[] { (byte)(paramInt >> 24 & 0xFF), (byte)(paramInt >> 16 & 0xFF), (byte)(paramInt >> 8 & 0xFF), (byte)(paramInt >> 0 & 0xFF) };
  }
  
  public static byte[] toBytes(long paramLong)
  {
    return new byte[] { (byte)(int)(paramLong >> 56 & 0xFF), (byte)(int)(paramLong >> 48 & 0xFF), (byte)(int)(paramLong >> 40 & 0xFF), (byte)(int)(paramLong >> 32 & 0xFF), (byte)(int)(paramLong >> 24 & 0xFF), (byte)(int)(paramLong >> 16 & 0xFF), (byte)(int)(paramLong >> 8 & 0xFF), (byte)(int)(paramLong >> 0 & 0xFF) };
  }
  
  public static long toLong(byte[] paramArrayOfByte)
  {
    if ((paramArrayOfByte == null) || (paramArrayOfByte.length != 8)) {
      return 0L;
    }
    return (0xFF & paramArrayOfByte[0]) << 56 | (0xFF & paramArrayOfByte[1]) << 48 | (0xFF & paramArrayOfByte[2]) << 40 | (0xFF & paramArrayOfByte[3]) << 32 | (0xFF & paramArrayOfByte[4]) << 24 | (0xFF & paramArrayOfByte[5]) << 16 | (0xFF & paramArrayOfByte[6]) << 8 | (0xFF & paramArrayOfByte[7]) << 0;
  }
  
  public static byte[] toByteArray(int[] paramArrayOfInt)
  {
    byte[] arrayOfByte2 = new byte[paramArrayOfInt.length * 4];
    for (int i = 0; i < paramArrayOfInt.length; i++)
    {
      byte[] arrayOfByte1 = toBytes(paramArrayOfInt[i]);
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, i * 4, 4);
    }
    return arrayOfByte2;
  }
  
  public static int[] toIntArray(byte[] paramArrayOfByte)
  {
    int[] arrayOfInt = new int[paramArrayOfByte.length / 4];
    byte[] arrayOfByte = new byte[4];
    for (int i = 0; i < arrayOfInt.length; i++)
    {
      System.arraycopy(paramArrayOfByte, i * 4, arrayOfByte, 0, 4);
      arrayOfInt[i] = toInt(arrayOfByte);
    }
    return arrayOfInt;
  }
  
  public static int[] toIntArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    int[] arrayOfInt = new int[paramInt2 >> 2];
    byte[] arrayOfByte = new byte[4];
    for (int i = paramInt1; i < paramInt2 >> 2; i++)
    {
      System.arraycopy(paramArrayOfByte, paramInt1 + (i - paramInt1) * 4, arrayOfByte, 0, 4);
      arrayOfInt[i] = toInt(arrayOfByte);
    }
    return arrayOfInt;
  }
  
  public static byte[] toBytes(float paramFloat)
  {
    return toBytes(Float.floatToRawIntBits(paramFloat));
  }
  
  public static float toFloat(byte[] paramArrayOfByte)
  {
    return Float.intBitsToFloat(toInt(paramArrayOfByte));
  }
  
  public static byte[] toByteArray(float[] paramArrayOfFloat)
  {
    byte[] arrayOfByte2 = new byte[paramArrayOfFloat.length * 4];
    for (int i = 0; i < paramArrayOfFloat.length; i++)
    {
      byte[] arrayOfByte1 = toBytes(paramArrayOfFloat[i]);
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, i * 4, 4);
    }
    return arrayOfByte2;
  }
  
  public static float[] toFloatArray(byte[] paramArrayOfByte)
  {
    float[] arrayOfFloat = new float[paramArrayOfByte.length / 4];
    byte[] arrayOfByte = new byte[4];
    for (int i = 0; i < arrayOfFloat.length; i++)
    {
      System.arraycopy(paramArrayOfByte, i * 4, arrayOfByte, 0, 4);
      arrayOfFloat[i] = toFloat(arrayOfByte);
    }
    return arrayOfFloat;
  }
  
  public static float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    float[] arrayOfFloat = new float[paramInt2 / 4];
    byte[] arrayOfByte = new byte[4];
    for (int i = paramInt1; i < paramInt2 / 4; i++)
    {
      System.arraycopy(paramArrayOfByte, (i - paramInt1) * 4 + paramInt1, arrayOfByte, 0, 4);
      arrayOfFloat[i] = toFloat(arrayOfByte);
    }
    return arrayOfFloat;
  }
  
  public static byte[] toBytes(double paramDouble)
  {
    return toBytes(Double.doubleToLongBits(paramDouble));
  }
  
  public static double toDouble(byte[] paramArrayOfByte)
  {
    return Double.longBitsToDouble(toLong(paramArrayOfByte));
  }
  
  public static byte[] toByteArray(double[] paramArrayOfDouble)
  {
    byte[] arrayOfByte2 = new byte[paramArrayOfDouble.length * 8];
    for (int i = 0; i < paramArrayOfDouble.length; i++)
    {
      byte[] arrayOfByte1 = toBytes(paramArrayOfDouble[i]);
      System.arraycopy(arrayOfByte1, 0, arrayOfByte2, i * 8, 8);
    }
    return arrayOfByte2;
  }
  
  public static double[] toDoubleArray(byte[] paramArrayOfByte)
  {
    double[] arrayOfDouble = new double[paramArrayOfByte.length / 8];
    byte[] arrayOfByte = new byte[8];
    for (int i = 0; i < arrayOfDouble.length; i++)
    {
      System.arraycopy(paramArrayOfByte, i * 8, arrayOfByte, 0, 8);
      arrayOfDouble[i] = toDouble(arrayOfByte);
    }
    return arrayOfDouble;
  }
  
  public static double[] toDoubleArray(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
  {
    double[] arrayOfDouble = new double[paramInt2 / 8];
    byte[] arrayOfByte = new byte[8];
    for (int i = paramInt1; i < paramInt2 / 8; i++)
    {
      System.arraycopy(paramArrayOfByte, (i - paramInt1) * 8 + paramInt1, arrayOfByte, 0, 8);
      arrayOfDouble[i] = toDouble(arrayOfByte);
    }
    return arrayOfDouble;
  }
  
  public static String arrayToString(int[] paramArrayOfInt)
  {
    return Arrays.toString(paramArrayOfInt).replace('[', ' ').replace(']', ' ').replace(',', ' ');
  }
  
  public static double[] doubleArrayFromString(String paramString)
  {
    double[] arrayOfDouble = null;
    LinkedList localLinkedList = new LinkedList();
    paramString = paramString.replace('[', ' ');
    paramString = paramString.replace(']', ' ');
    paramString = paramString.replace(',', ' ');
    StringTokenizer localStringTokenizer = new StringTokenizer(paramString);
    while (localStringTokenizer.hasMoreTokens()) {
      localLinkedList.add(Double.valueOf(Double.parseDouble(localStringTokenizer.nextToken())));
    }
    arrayOfDouble = new double[localLinkedList.size()];
    int i = 0;
    Iterator localIterator = localLinkedList.iterator();
    while (localIterator.hasNext())
    {
      Double localDouble = (Double)localIterator.next();
      arrayOfDouble[i] = localDouble.doubleValue();
      i++;
    }
    return arrayOfDouble;
  }
  
  public static double[] toDoubleArray(float[] paramArrayOfFloat)
  {
    double[] arrayOfDouble = new double[paramArrayOfFloat.length];
    for (int i = 0; i < arrayOfDouble.length; i++) {
      arrayOfDouble[i] = paramArrayOfFloat[i];
    }
    return arrayOfDouble;
  }
}


/* Location:           E:\EclipseWorkspace\ComogPhogExtractor\lib\lire.jar
 * Qualified Name:     net.semanticmetadata.lire.utils.SerializationUtils
 * JD-Core Version:    0.7.0.1
 */