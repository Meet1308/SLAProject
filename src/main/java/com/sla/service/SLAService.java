package com.sla.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.sla.dto.ClauseRequestDTO;
import com.sla.dto.ContractRequestDTO;
import com.sla.dto.SLARequestDTO;
import com.sla.dto.SlaDetailDTO;
import com.sla.json.JsonBuilder;
import com.sla.resttemplate.RestTemplateData;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class SLAService {

	@Autowired
	private RestTemplateData restTemplateData;
	@Autowired
	private JsonBuilder jsonBuilder;
	@Autowired
	private UtiliityMethods utilityMethods;

	private final ObjectMapper mapper;
	private static String URL = "http://192.168.100.150:9092/api/v1/info-object";

	public SLAService(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public String insertContractData(String request) {

		try {
			Map<String, String> typeSubtype = utilityMethods.fatchTypeSubtype(1, null);
			String typel1 = typeSubtype.get("typeidl2");
			String typel2 = typeSubtype.get("typeidl3");

			String decodedJson = new String(Base64.getDecoder().decode(request), StandardCharsets.UTF_8);
			ContractRequestDTO dto = mapper.readValue(decodedJson, ContractRequestDTO.class);
			String innerJson = jsonBuilder.buildMasterJson(dto, typel1, typel2);
			String payload = utilityMethods.buildEncodedPayload(innerJson);
			RestTemplate restTemplate = restTemplateData.getRestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(payload, headers);

			@SuppressWarnings("unchecked")
			Map<String, Object> apiResponse = restTemplate.postForObject(URL, entity, Map.class);
			Integer statusVal = (Integer) apiResponse.get("status"), infoId = null;

			if (apiResponse != null && apiResponse.get("data") != null) {
				@SuppressWarnings("unchecked")
				Map<String, Object> dataMap = (Map<String, Object>) apiResponse.get("data");

				if (dataMap != null) {
					infoId = (Integer) dataMap.get("info_obj_id");
				}
			}

			List<Map<String, String>> innerList = new ArrayList<>();
			Map<String, String> docMap = new LinkedHashMap<>();

			docMap.put("DocumentID", infoId != null ? String.valueOf(infoId) : "");
			innerList.add(docMap);

			String innerDataString = mapper.writeValueAsString(innerList);
			List<Map<String, Object>> finalList = new ArrayList<>();
			Map<String, Object> finalMap = new LinkedHashMap<>();
			finalMap.put("status", statusVal);
			finalMap.put("status_message", "Contract Created Successfully Done.");
			finalMap.put("data", innerDataString);

			finalList.add(finalMap);
			return mapper.writeValueAsString(finalList);

		} catch (

		Exception e) {
			e.printStackTrace();

			Map<String, Object> errorResp = new LinkedHashMap<>();
			errorResp.put("status", 0);
			errorResp.put("message", "Error while calling microservice");
			errorResp.put("data", null);

			try {
				return new ObjectMapper().writeValueAsString(errorResp);
			} catch (Exception ex) {
				return "{\"status\":0,\"message\":\"Serialization Error\",\"data\":null}";
			}
		}
	}

	public String insertClauseData(String request) throws Exception {

		Map<String, String> typeSubtype = utilityMethods.fatchTypeSubtype(2, null);
		String typel1 = typeSubtype.get("typeidl2");
		String typel2 = typeSubtype.get("typeidl3");

		String decodedJson = new String(Base64.getDecoder().decode(request), StandardCharsets.UTF_8);
		ClauseRequestDTO dto = mapper.readValue(decodedJson, ClauseRequestDTO.class);
		Map<String, Object> payload = jsonBuilder.setClauseData(dto, typel1, typel2);

		// 1. convert payload → JSON string
		String jsonString = mapper.writeValueAsString(payload);
		String encodedPayload = utilityMethods.buildEncodedPayload(jsonString);

		try {

			RestTemplate restTemplate = restTemplateData.getRestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(encodedPayload, headers);

			String response = restTemplate.postForObject(URL, entity, String.class);
			JsonNode root = mapper.readTree(response);

			// extract values safely
			int status = root.path("status").asInt(1);
			int infoObjId = root.path("data").path("info_obj_id").asInt();

			Map<String, Object> inner = new LinkedHashMap<>();
			inner.put("status", "1");
			inner.put("status_message", "Clause Created Successfully Done.");

			List<Map<String, String>> innerDataList = new ArrayList<>();
			Map<String, String> doc = new HashMap<>();
			doc.put("DocumentID", String.valueOf(infoObjId));
			innerDataList.add(doc);

			inner.put("data", innerDataList);

			String innerJsonString = mapper.writeValueAsString(inner);

			// ===== Build FINAL RESPONSE =====
			Map<String, Object> finalObj = new LinkedHashMap<>();
			finalObj.put("status", status);
			finalObj.put("status_message", "Clause Created Successfully Done.");
			finalObj.put("data", innerJsonString);

			List<Map<String, Object>> finalResponse = new ArrayList<>();
			finalResponse.add(finalObj);

			// return as JSON string
			return mapper.writeValueAsString(finalResponse);
		} catch (HttpStatusCodeException e) {

			System.out.println("HTTP STATUS: " + e.getStatusCode());
			System.out.println("RESPONSE BODY: " + e.getResponseBodyAsString());

			return e.getResponseBodyAsString();
		}
	}

	public String insertSLAdata(String request) {

		String decodedJson = new String(Base64.getDecoder().decode(request), StandardCharsets.UTF_8);

		SLARequestDTO dto = mapper.readValue(decodedJson, SLARequestDTO.class);

		Map<String, String> typeSubtype = utilityMethods.fatchTypeSubtype(3, dto.getVendrmngt_sla_descb());
		String typel1 = typeSubtype.get("typeidl2");
		String typel2 = typeSubtype.get("typeidl3");

		Map<String, Object> payload = jsonBuilder.getPayloadMap(dto, typel1, typel2);
		Map<String, Object> desc = jsonBuilder.getDescMap(dto, typel1, typel2);
		List<SlaDetailDTO> list = dto.getStrdata();

		if (list == null || list.isEmpty())
			return "{\"status\":0,\"message\":\"details/strdata is missing\"}";

		desc.put("total_computers", list.size());
		payload.put("info_obj_desc_json", desc);

		payload.put("info_obj_action_json", null);
		payload.put("info_obj_notify_json", null);
		payload.put("info_obj_kb_json", null);

		List<Map<String, Object>> detailsList = new ArrayList<>();
		jsonBuilder.setInnerData(dto, detailsList, typel1, typel2);
		payload.put("details", detailsList);
		String finalJson = mapper.writeValueAsString(payload);
		String encodedPayload = Base64.getEncoder().encodeToString(finalJson.getBytes());

		Map<String, String> wrapper = new HashMap<>();
		wrapper.put("user_json", encodedPayload);

		String requestBody = mapper.writeValueAsString(wrapper);
		try {
			RestTemplate restTemplate = restTemplateData.getRestTemplate();

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
			restTemplate.postForObject(URL, entity, String.class);

			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("status", 1);
			responseMap.put("status_message", "SLA Created Successfully Done.");

			List<Map<String, Object>> responseList = new ArrayList<>();
			responseList.add(responseMap);

			return mapper.writeValueAsString(responseList);

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":0,\"message\":\"Error occurred\"}";
		}
	}

}
