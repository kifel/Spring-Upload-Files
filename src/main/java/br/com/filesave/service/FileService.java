package br.com.filesave.service;

import java.io.IOException;
import java.sql.Blob;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.slugify.Slugify;

import br.com.filesave.DTOs.DownloadFileResult;
import br.com.filesave.DTOs.UploadFileResponse;
import br.com.filesave.exception.FileException;
import br.com.filesave.model.File;
import br.com.filesave.repositories.FileRepository;
import br.com.filesave.utils.FileUtils;
import jakarta.transaction.Transactional;

@Service
public class FileService {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private Slugify slugify;

    @Autowired
    @Value("${backend.base-url}")
    private String backendBaseUrl;

    /**
     * This function uploads a file, saves it to the database, and returns a
     * response with information
     * about the uploaded file.
     * 
     * @param uploadedFile The uploaded file that needs to be saved and processed.
     *                     It is of type
     *                     MultipartFile, which is a Spring class that represents an
     *                     uploaded file received in a multipart
     *                     request.
     * @return The method is returning an instance of the `UploadFileResponse`
     *         class.
     */
    @Transactional
    public UploadFileResponse uploadFile(MultipartFile uploadedFile) throws IOException {
        File file = new File();
        String originalName = uploadedFile.getOriginalFilename();
        file.setOriginalName(originalName);
        file.setUniqueName(generateUniqueName(originalName));
        file.setType(uploadedFile.getContentType());

        Blob fileBlob = FileUtils.createBlob(uploadedFile.getBytes());
        file.setFile(fileBlob);

        fileRepository.save(file);

        String fileDownloadUrl = backendBaseUrl + "/api/file/download/" + file.getUniqueName();
        String fileViewUrl = backendBaseUrl + "/api/file/view/" + file.getUniqueName();
        String formattedFileSize = FileUtils.formatFileSize(uploadedFile.getSize());
        return new UploadFileResponse("File uploaded successfully", originalName, file.getUniqueName(),
                formattedFileSize,
                fileDownloadUrl, fileViewUrl);
    }

    /**
     * The function downloads a file by retrieving it from the file repository and
     * converting it to a byte
     * array.
     * 
     * @param fileName The name of the file to be downloaded.
     * @return The method is returning a DownloadFileResult object.
     */
    @Transactional
    public DownloadFileResult downloadFile(String fileName) throws Exception {
        File file = fileRepository.findByUniqueName(fileName)
                .orElseThrow(() -> new FileException("Could not find file name: " + fileName));
        byte[] fileData = FileUtils.blobToByteArray(file.getFile());
        MediaType mediaType = MediaType.parseMediaType(file.getType());

        return new DownloadFileResult(file.getOriginalName(), fileData, mediaType);
    }

    /**
     * The function `viewFile` retrieves a file from the file repository, checks if
     * the file extension is
     * allowed, converts the file data to byte array, and returns a
     * `DownloadFileResult` object containing
     * the file name, data, and media type.
     * 
     * @param fileName The `fileName` parameter is a string that represents the
     *                 unique name of the file
     *                 that needs to be viewed.
     * @return The method is returning a DownloadFileResult object.
     */
    @Transactional
    public DownloadFileResult viewFile(String fileName) throws Exception {
        File file = fileRepository.findByUniqueName(fileName)
                .orElseThrow(() -> new FileException("Could not find file name: " + fileName));

        String fileExtension = getFileExtension(file.getOriginalName());
        List<String> allowedExtensions = Arrays.asList(
                "jpg", "jpeg", "png", "gif", // Imagens
                "txt", "md", // Documentos de texto
                "pdf", // Documentos em formato PDF
                "mp3", "wav", "ogg", // Arquivos de áudio
                "mp4" // Arquivos de vídeo
        );

        if (!allowedExtensions.contains(fileExtension)) {
            String fileDownloadUrl = backendBaseUrl + "/api/file/download/" + file.getUniqueName();
            throw new FileException(
                    "Visualização de arquivo não permitida! Faça o Download do arquivo: " + fileDownloadUrl);
        }

        byte[] fileData = FileUtils.blobToByteArray(file.getFile());
        MediaType mediaType = MediaType.parseMediaType(file.getType());

        return new DownloadFileResult(file.getOriginalName(), fileData, mediaType);
    }

    /**
     * The getFileExtension function returns the file extension of a given file
     * name.
     * 
     * @param fileName The `fileName` parameter is a string that represents the name
     *                 of a file, including
     *                 its extension.
     * @return The method is returning the file extension of the given fileName.
     */
    private String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }

    /**
     * The function generates a unique name for a file by slugifying the original
     * name and appending a
     * counter or a UUID if the maximum number of attempts is reached.
     * 
     * @param originalName The original name of the file that needs to be made
     *                     unique.
     * @return The method `generateUniqueName` returns a unique name for a file.
     */
    private String generateUniqueName(String originalName) {
        String slugifiedName = slugify.slugify(originalName);
        int maxAttempts = 100; // Limite máximo de tentativas
        int counter = 1;

        String extension = "";
        int extensionIndex = originalName.lastIndexOf(".");
        if (extensionIndex != -1) {
            extension = originalName.substring(extensionIndex);
            slugifiedName = slugifiedName.substring(0, extensionIndex);
        }

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            String uniqueName = slugifiedName + "-" + counter + extension;
            if (!fileRepository.existsByUniqueName(uniqueName)) {
                return uniqueName;
            }
            counter++;
        }

        // Se o número máximo de tentativas for atingido, gerar um UUID
        String uuid = UUID.randomUUID().toString();
        return slugifiedName + "-" + uuid + extension;
    }

}
