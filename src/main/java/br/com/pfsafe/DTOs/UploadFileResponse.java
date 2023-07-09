package br.com.pfsafe.DTOs;

import lombok.Data;

@Data
public class UploadFileResponse {
  private String message;
  private String OriginalName;
  private String uniqueName;
  private String fileSize;
  private String fileDownloadUrl;
  private String fileViewdUrl;

  public UploadFileResponse(String message, String originalName, String uniqueName, String fileSize,
      String fileDownloadUrl, String fileViewdUrl) {
    this.message = message;
    this.OriginalName = originalName;
    this.uniqueName = uniqueName;
    this.fileSize = fileSize;
    this.fileDownloadUrl = fileDownloadUrl;
    this.fileViewdUrl = fileViewdUrl;
  }

}
