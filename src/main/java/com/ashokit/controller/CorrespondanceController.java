package com.ashokit.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ashokit.service.CorrespondanceServcie;

@RestController
public class CorrespondanceController {
	
	@Autowired
	private CorrespondanceServcie service;
	
	@GetMapping("/notices")
	public Map<String,Integer> generateNotices(){
		
		return service.generateNotices();
	}

}
