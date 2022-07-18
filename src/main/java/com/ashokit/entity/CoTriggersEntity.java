package com.ashokit.entity;

import java.sql.Blob;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.Data;



@Data
@Entity
@Table(name = "CO_TRIGGERS")
public class CoTriggersEntity {

	@Id
	@Column(name="TRG_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer trgId;
	
	@Column(name="CASE_NUM")
	private Integer caseNum;
	
	@Column(name="TRG_STATUS")
	private String trgStatus;
	
	@Column(name="NOTICE")
	@Lob
	private byte[] notice;
	
	@Column(name="CREATED_DATE")
	private LocalDate createdDate;
	
	@Column(name="UPDATED_DATE")
	private LocalDate updatedDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
	}
