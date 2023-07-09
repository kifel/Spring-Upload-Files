package br.com.filesave.utils;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

public class FileUtils {

  private FileUtils() {
  }

  /**
   * The function creates a Blob object from a byte array.
   * 
   * @param data The "data" parameter is a byte array that represents the content
   *             of the Blob object that
   *             you want to create.
   * @return The method is returning a Blob object.
   */
  public static Blob createBlob(byte[] data) {
    try {
      return new SerialBlob(data);
    } catch (SQLException e) {
      throw new IllegalStateException("Error creating Blob: " + e.getMessage(), e);
    }
  }

  /**
   * The function converts a Blob object to a byte array in Java.
   * 
   * @param blob The "blob" parameter is of type Blob, which represents a binary
   *             large object in a
   *             database. It can store large amounts of binary data, such as
   *             images, audio, or video files.
   * @return The method is returning a byte array.
   */
  public static byte[] blobToByteArray(Blob blob) throws SQLException, IOException {
    if (blob == null)
      return null;

    int length = (int) blob.length();
    byte[] data = blob.getBytes(1, length);
    blob.free();
    return data;
  }

  /**
   * The function formats a file size in bytes into a human-readable format with
   * appropriate units (B,
   * KB, MB, GB, etc.).
   * 
   * @param size The size parameter represents the file size in bytes.
   * @return The method is returning a formatted string representation of the file
   *         size.
   */
  public static String formatFileSize(long size) {
    final int unit = 1024;
    if (size < unit) {
      return size + " B";
    }
    int exp = (int) (Math.log(size) / Math.log(unit));
    String pre = "KMGTPE".charAt(exp - 1) + "";
    return String.format("%.1f %sB", size / Math.pow(unit, exp), pre);
  }
}
