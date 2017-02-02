package com.devcortes.components.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "storage", catalog = "webcrawler")
public class StorageFiles implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "url")
	private String url;

	@Column(name = "file_txt")
	private byte[] fileTxt;

	@Column(name = "file_pdf")
	private byte[] filePdf;

	@Column(name = "file_csv")
	private byte[] fileCsv;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public byte[] getFileTxt() {
		return fileTxt;
	}

	public void setFileTxt(byte[] fileTxt) {
		this.fileTxt = fileTxt;
	}

	public byte[] getFilePdf() {
		return filePdf;
	}

	public void setFilePdf(byte[] filePdf) {
		this.filePdf = filePdf;
	}

	public byte[] getFileCsv() {
		return fileCsv;
	}

	public void setFileCsv(byte[] fileCsv) {
		this.fileCsv = fileCsv;
	}

}
