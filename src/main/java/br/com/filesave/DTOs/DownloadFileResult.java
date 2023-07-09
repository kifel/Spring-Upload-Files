package br.com.filesave.DTOs;

import org.springframework.http.MediaType;

import lombok.Data;

/**
 * The DownloadFileResult class represents the result of downloading a file, including the original
 * file name, file data, and media type.
 */
@Data
public class DownloadFileResult {
    private String originalFileName;
    private byte[] fileData;
    private MediaType mediaType;

    public DownloadFileResult(String originalFileName, byte[] fileData, MediaType mediaType) {
        this.originalFileName = originalFileName;
        this.fileData = fileData;
        this.mediaType = mediaType;
    }
}
