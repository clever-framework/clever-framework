package io.github.toquery.framework.ueditor.upload;

import io.github.toquery.framework.ueditor.define.State;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

public class Uploader {
	private HttpServletRequest request = null;
	private String uploadFileParamName = null;
	private String storeFilePath = null;
	private Map<String, Object> conf = null;

	public Uploader(HttpServletRequest request, String uploadFileParamName,String storeFilePath, Map<String, Object> conf) {
		this.request = request;
		this.conf = conf;
		this.uploadFileParamName = uploadFileParamName;
		this.storeFilePath = storeFilePath;
	}

	public final State doExec() throws IOException, ServletException {
		String filedName = (String) this.conf.get("fieldName");
		State state = null;

		if ("true".equals(this.conf.get("isBase64"))) {
			state = Base64Uploader.save(this.request.getParameter(filedName), this.conf);
		} else {
			state = BinaryUploader.save(this.request, uploadFileParamName,storeFilePath, this.conf);
		}

		return state;
	}
}
