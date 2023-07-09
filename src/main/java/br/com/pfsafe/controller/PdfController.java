package br.com.pfsafe.controller;

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

import br.com.pfsafe.DTOs.DownloadFileResult;
import br.com.pfsafe.DTOs.UploadFileResponse;
import br.com.pfsafe.exception.ApiError;
import br.com.pfsafe.exception.FileException;
import br.com.pfsafe.service.FileService;

@RestController
@RequestMapping("/api/file")
public class PdfController {

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

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(result.getMediaType());
      headers.setContentDispositionFormData("attachment", result.getOriginalFileName());

      return ResponseEntity.ok()
          .headers(headers)
          .body(result.getFileData());
    } catch (FileException e) {
      return ResponseEntity.unprocessableEntity()
          .body(new ApiError(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", e.getLocalizedMessage()));
    }
  }

  /**
   * The function `viewFile` in a Java Spring application downloads a file and
   * returns it as a response
   * entity with the appropriate headers.
   * 
   * @param fileName The `fileName` parameter is a string that represents the name
   *                 of the file that needs
   *                 to be viewed.
   * @return The method is returning a ResponseEntity object with an
   *         InputStreamResource as the response
   *         body.
   */
  @GetMapping("/view/{fileName}")
  public ResponseEntity<Object> viewFile(@PathVariable String fileName) throws Exception {
    try {
      DownloadFileResult result = fileService.downloadFile(fileName);

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
