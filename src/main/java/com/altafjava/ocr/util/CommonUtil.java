package com.altafjava.ocr.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import org.springframework.web.multipart.MultipartFile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonUtil {

	public static void storeInputFile(MultipartFile multipartFile) {
		Path rootLocation = Paths.get("script/input");
		try {
			if (!Files.exists(rootLocation)) {
				Files.createDirectory(rootLocation);
			}
			Path destinationFile = rootLocation.resolve(Paths.get("image.png")).normalize().toAbsolutePath();
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
			}
		} catch (IOException e) {
			log.error("Failed to store file - ", e);
			throw new RuntimeException("Failed to store file", e);
		}
	}

	public static boolean executePythonScript() {
		boolean flag = false;
		try {
			Process process = Runtime.getRuntime().exec("python script/ocr.py");
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = br.readLine()) != null) {
				log.info("Python:- " + line);
			}
			flag = true;
		} catch (IOException e) {
			log.error("Failed to execute Python script - ", e);
			throw new RuntimeException("Failed to execute Python script", e);
		}
		return flag;
	}
}
