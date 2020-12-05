package com.altafjava.ocr.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.altafjava.ocr.beans.UploadFileResponse;
import com.altafjava.ocr.util.CommonUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OcrService {

	public ResponseEntity<?> convert(MultipartFile multipartFile) {
		CommonUtil.storeInputFile(multipartFile);
		StringBuilder sb = new StringBuilder();
		boolean isConvertedSuccessfully = false;
		String outputDirName = "script/output/";
		String targetFileNameWithExtension = "output.txt";
		File outputFile = new File(outputDirName + targetFileNameWithExtension);
		if (CommonUtil.executePythonScript()) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(outputFile)));
				String line = null;
				while ((line = br.readLine()) != null) {
					sb.append(line + "\n");
				}
				isConvertedSuccessfully = true;
				br.close();
			} catch (IOException e) {
				log.error("Failed to convert image to text - ", e);
				throw new RuntimeException("Failed to convert image to text", e);
			}
		}
		if (isConvertedSuccessfully) {
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(targetFileNameWithExtension)
					.toUriString();
			String contentType = "text/plain";
			return new ResponseEntity<>(
					new UploadFileResponse(targetFileNameWithExtension, fileDownloadUri, contentType, outputFile.length(), sb.toString()),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Sorry, currently we are not able convert" + multipartFile.getContentType(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public ResponseEntity<Resource> download(String fileName) {
		String outputDirName = "script/output";
		Path outputPath = Paths.get(outputDirName).toAbsolutePath().normalize();
		Path filePath = outputPath.resolve("output.txt").normalize();
		Resource resource = null;
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			log.error("Failed to create Resource - ", e);
			throw new RuntimeException("Failed to create Resource", e);
		}
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("text/plain"))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"").header("Accept", "*/*")
				.body(resource);
	}
}
