package com.sla.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sla.service.SLAService;

@RestController
@RequestMapping("/api/v1/sla")
public class SLAController {

	private SLAService service;

	public SLAController(SLAService service) {
		this.service = service;
	}

	@PostMapping("/createContract")
	public String addContractData(@RequestBody Map<String, String> request) {
		return service.insertContractData(request.get("user_json"));
	}

	@PostMapping("/createClause")
	public String addClauseData(@RequestBody Map<String, String> request) throws Exception {
		return service.insertClauseData(request.get("user_json"));
	}

	@PostMapping("/createSla")
	public String insertSLA(@RequestBody Map<String, String> request) {
		return service.insertSLAdata(request.get("user_json"));
	}

}
