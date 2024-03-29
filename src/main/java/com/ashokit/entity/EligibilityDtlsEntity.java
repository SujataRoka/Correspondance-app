package com.ashokit.entity;

import java.time.LocalDate;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "ELIG_DTLS")
public class EligibilityDtlsEntity {

	@Id
	@Column(name="ELIG_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer eligId;
	
	@Column(name="CASE_NUM")
	private Integer caseNum;
	
	@Column(name="PLAN_NAME")
	private String planName;
	
	@Column(name="PLAN_STATUS")
	private String planStatus;
	
	@Column(name="START_DATE")
	private Date startDate;
		
	@Column(name="END_DATE")
	private Date endDate;
	
	@Column(name="BENEFIT_AMT")
	private Double benifitAmt;
	
	@Column(name="DENIAL_REASON")
	private String DenialReason;
	
	@Column(name="CREATED_DATE")
	private LocalDate createdDate;
	
	@Column(name="UPDATED_DATE")
	private LocalDate updatedDate;
	
	@Column(name="CREATED_BY")
	private String createdBy;
	
	@Column(name="UPDATED_BY")
	private String updatedBy;
}
