package com.altafjava.ocr.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author Altaf
 *
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResponse {

	private String name;
	private String downloadUrl;
	private String type;
	private long size;
	private String content;

}
