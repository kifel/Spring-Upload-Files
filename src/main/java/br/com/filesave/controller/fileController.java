package br.com.filesave.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.filesave.DTOs.DownloadFileResult;
import br.com.filesave.DTOs.UploadFileResponse;
import br.com.filesave.exception.ApiError;
import br.com.filesave.exception.FileException;
import br.com.filesave.service.FileService;

@RestController
@RequestMapping("/api/file")
public class fileController {

  @Autowired
  private FileService fileService;

  /**
   * The function handles a POST request to upload a file and returns the uploaded
   * file's name.
   * 
   * @param file The "file" parameter is of type MultipartFile, which is a Spring
   *             class used to represent
   *             a file that has been uploaded.
   * @return The method is returning a ResponseEntity object.
   */
  @PostMapping("/upload")
  public ResponseEntity<Object> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
    try {
      UploadFileResponse uploadedFile = fileService.uploadFile(file);
      return ResponseEntity.status(HttpStatus.OK).body(uploadedFile);
    } catch (Exception e) {
      return ResponseEntity.unprocessableEntity()
          .body(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", e.getLocalizedMessage()));
    }
  }

  /**
   * The function `downloadFile` in a Java controller downloads a file by setting
   * the appropriate headers
   * and returning the file data as a byte array in a ResponseEntity.
   * 
   * @param fileName The `fileName` parameter is a string that represents the name
   *                 of the file that needs
   *                 to be downloaded.
   * @return The method is returning a ResponseEntity<byte[]> object.
   */
  @GetMapping("/download/{fileName}")
  public ResponseEntity<Object> downloadFile(@PathVariable String fileName) throws Exception {
    try {
      DownloadFileResult result = fileService.downloadFile(fileName);

      InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(result.getFileData()));
      MediaType mediaType = result.getMediaType();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(mediaType);
      headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + result.getOriginalFileName() + "\"");

      return ResponseEntity.ok()
          .headers(headers)
          .body(resource);
    } catch (FileException e) {
      return ResponseEntity.unprocessableEntity()
          .body(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", e.getLocalizedMessage()));
    }
  }

  /**
   * The function `viewFile` retrieves a file from the file service, creates an
   * input stream resource
   * from the file data, sets the media type of the file, and returns a
   * ResponseEntity with the file
   * resource and headers.
   * 
   * @param fileName The `fileName` parameter is a String that represents the name
   *                 of the file that needs
   *                 to be viewed.
   * @return The method is returning a ResponseEntity<Object>.
   */
  @GetMapping("/view/{fileName}")
  public ResponseEntity<Object> viewFile(@PathVariable String fileName) throws Exception {
    try {
      DownloadFileResult result = fileService.viewFile(fileName);

      InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(result.getFileData()));
      MediaType mediaType = result.getMediaType();

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(mediaType);

      return ResponseEntity.ok()
          .headers(headers)
          .body(resource);
    } catch (FileException e) {
      return ResponseEntity.unprocessableEntity()
          .body(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", e.getLocalizedMessage()));
    }
  }

}
