package com.altafjava.ocr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.altafjava.ocr.service.OcrService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class OcrController {

	@Autowired
	private OcrService ocrService;
	
	@PostMapping("/convert")
	public ResponseEntity<?> convert(@RequestParam("file") MultipartFile multipartFile) {
		return ocrService.convert(multipartFile);
	}

	@GetMapping("/download/{fileName}")
	public ResponseEntity<Resource> download(@PathVariable String fileName) {
		return ocrService.download(fileName);
	}
}
