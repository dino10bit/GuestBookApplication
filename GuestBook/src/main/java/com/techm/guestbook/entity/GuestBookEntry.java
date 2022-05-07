package com.techm.guestbook.entity;

import java.sql.Blob;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "GUEST_BOOK_ENTRY")
public class GuestBookEntry {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotEmpty
    @Column(name="user_name")
    private String user;
    @Column(name="text_entry")
    private String textEntry;
    @Column(name="image_entry")
	@Lob
	@JsonIgnore
	private Blob content;
    @Column(name="file_name")
    private String fileName;
   
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	@Column(name="created_datetime")
    private Timestamp createdDate;
    @Column(name="updated_datetime")
    private Timestamp updatedDate;
	@Column(name="updated_by")
    private String updatedBy;
	@Column(name="status")
    private String status;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getTextEntry() {
		return textEntry;
	}
	public void setTextEntry(String textEntry) {
		this.textEntry = textEntry;
	}
	public Blob getContent() {
		return content;
	}
	public void setContent(Blob content) {
		this.content = content;
	}
	public Timestamp getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Timestamp createdDate) {
		this.createdDate = createdDate;
	}
	public Timestamp getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(Timestamp updatedDate) {
		this.updatedDate = updatedDate;
	}
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public GuestBookEntry() {
		
	}
	 public GuestBookEntry(Integer id, @NotEmpty String user, String textEntry, Blob content, Timestamp createdDate,
				Timestamp updatedDate, String updatedBy) {
			this.id = id;
			this.user = user;
			this.textEntry = textEntry;
			this.content = content;
			this.createdDate = createdDate;
			this.updatedDate = updatedDate;
			this.updatedBy = updatedBy;
		}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
}