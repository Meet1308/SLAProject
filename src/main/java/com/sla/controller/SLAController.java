package com.sla.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/getContractData/{pown_eid}/{org_eid}")
	public String getContractData(@PathVariable String pown_eid, @PathVariable String org_eid) {
		return service.getData(pown_eid, org_eid, 1);
	}

	@GetMapping("/getClauseData/{pown_eid}/{org_eid}")
	public String getClauseData(@PathVariable String pown_eid, @PathVariable String org_eid) {
		return service.getData(pown_eid, org_eid, 2);
	}

	@GetMapping("/getSLAData/{pown_eid}/{org_eid}")
	public String getSLAData(@PathVariable String pown_eid, @PathVariable String org_eid) {
		return service.getData(pown_eid, org_eid, 3);
	}

	@GetMapping("/getSLADetailsData/{pown_eid}/{org_eid}/{info_obj_id}")
	public String getSLADetailsData(@PathVariable String pown_eid, @PathVariable String org_eid,
			@PathVariable String info_obj_id) {
		return service.getSLADetailsData(pown_eid, org_eid, info_obj_id);
	}

}
