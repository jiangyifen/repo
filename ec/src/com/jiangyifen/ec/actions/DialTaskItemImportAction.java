package com.jiangyifen.ec.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jiangyifen.ec.dao.DialTaskItem;

public class DialTaskItemImportAction extends BaseAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1830426197386372996L;
	private long id;
	private File upload;
	private String uploadContentType;
	private String uploadFileName;
	private String savePath;

	public String execute() throws Exception {

		FileInputStream fis = null;
		BufferedReader br = null;
		String line = null;
		DialTaskItem dti = null;
		try {
			fis = new FileInputStream(getUpload());
			br = new BufferedReader(new InputStreamReader(fis));

			while ((line = br.readLine()) != null) {
				// 只允数字
				String regEx = "[^0-9]";
				Pattern p = Pattern.compile(regEx);
				Matcher m = p.matcher(line);
				line = m.replaceAll("").trim();

				dti = new DialTaskItem();
				dti.setPhoneNumber(line);
				dti.setTaskid(id);
				dti.setStatus(DialTaskItem.STATUS_READY);
				dialTaskItemManager.save(dti);

			}
			return SUCCESS;
		} catch (Exception e) {

		}finally{
			br.close();
		}
		return SUCCESS;

	}

	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public File getUpload() {
		return upload;
	}

	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	public String getUploadContentType() {
		return uploadContentType;
	}

	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	public String getUploadFileName() {
		return uploadFileName;
	}

	public void setSavePath(String savePath) {
		this.savePath = savePath;
	}

	public String getSavePath() {
		return savePath;
	}
}
