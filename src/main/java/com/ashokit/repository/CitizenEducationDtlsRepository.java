package com.ashokit.repository;

import javax.persistence.Id;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ashokit.entity.CitizenEducationDtlsEntity;
import com.ashokit.entity.CitizenIncomeDtlsEntity;

public interface CitizenEducationDtlsRepository extends JpaRepository<CitizenEducationDtlsEntity, Integer> {

	public CitizenEducationDtlsEntity findByCaseNum(Integer caseNum);
}
