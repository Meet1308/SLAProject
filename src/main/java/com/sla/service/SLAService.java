package com.sla.service;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

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
			String typel1 = typeSubtype.get("typeidl1");
			String typel2 = typeSubtype.get("typeidl2");

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
		String typel1 = typeSubtype.get("typeidl1");
		String typel2 = typeSubtype.get("typeidl2");

		String decodedJson = new String(Base64.getDecoder().decode(request), StandardCharsets.UTF_8);
		ClauseRequestDTO dto = mapper.readValue(decodedJson, ClauseRequestDTO.class);
		Map<String, Object> payload = jsonBuilder.setClauseData(dto, typel1, typel2);

		// 1. convert payload → JSON string
		String jsonString = mapper.writeValueAsString(payload);
		String encodedPayload = utilityMethods.buildEncodedPayload(jsonString);
//		System.out.println("Encoded Payload: " + encodedPayload);

		try {

			RestTemplate restTemplate = restTemplateData.getRestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(encodedPayload, headers);

			String response = restTemplate.postForObject(URL, entity, String.class);
			JsonNode root = mapper.readTree(response);

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

			Map<String, Object> finalObj = new LinkedHashMap<>();
			finalObj.put("status", status);
			finalObj.put("status_message", "Clause Created Successfully Done.");
			finalObj.put("data", innerJsonString);

			List<Map<String, Object>> finalResponse = new ArrayList<>();
			finalResponse.add(finalObj);

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

	public String getData(String pown_eid, String org_eid, int type) {
		try {
			if (type == 1) {
				String response = getResponse(pown_eid, org_eid, type, null);
				return createContractJson(response);
			} else if (type == 2) {
				String response = getResponse(pown_eid, org_eid, type, null);
				return createClauseResponse(response);
			} else if (type == 3) {
				String response = getResponse(pown_eid, org_eid, type, "SLA Template");
				return createSLAResponse(response);
			} else {
				return "{\"status\":0,\"message\":\"Invalid type parameter\"}";
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private String getResponse(String pown_eid, String org_eid, int type, String subtype) {

		Map<String, String> typeSubtype = utilityMethods.fatchTypeSubtype(type, subtype);
		String baseUrl = null, response = null;
		try {

			if (type == 3) {
				String typel2 = typeSubtype.get("typeidl2");
				String typel3 = typeSubtype.get("typeidl3");
				System.out.println("Type L2: " + typel2 + ", Type L3: " + typel3);

				baseUrl = URL + "/" + pown_eid + "/" + org_eid + "/" + typel2 + "/" + typel3;
				response = restTemplateData.getRestTemplate().getForObject(baseUrl, String.class);
				System.out.println("Response from microservice: " + response);

				return response;
			}

			String typel1 = typeSubtype.get("typeidl1");
			String typel2 = typeSubtype.get("typeidl2");

			baseUrl = URL + "/" + pown_eid + "/" + org_eid + "/" + typel1 + "/" + typel2;
			response = restTemplateData.getRestTemplate().getForObject(baseUrl, String.class);
			System.out.println("Response from microservice: " + response);

			return response;

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	private String createClauseResponse(String response) {

		try {

			JsonNode rootNode = mapper.readTree(response);
			int status = rootNode.path("status").asInt();
			JsonNode dataArray = rootNode.get("data");
			if (status == 0 || dataArray == null || dataArray.isNull() || !dataArray.isArray()
					|| dataArray.size() == 0) {

				ObjectNode errorResponse = mapper.createObjectNode();

				errorResponse.put("status", 0);
				errorResponse.put("message", "Data not available");
				errorResponse.putNull("data");
				return mapper.writeValueAsString(errorResponse);
			}

			// Final output array
			ArrayNode finalArray = mapper.createArrayNode();

			for (JsonNode obj : dataArray) {

				ObjectNode finalObj = mapper.createObjectNode();
				// Dynamic Mapping
				finalObj.put("applicationid", obj.path("info_obj_int1").asInt());
				finalObj.put("documenttype", obj.path("info_obj_typeL2").asInt());
				finalObj.put("documentid", obj.path("info_obj_id").asInt());
				finalObj.put("documentname", obj.path("info_obj_name").asString());
				finalObj.put("documentdescription", obj.path("info_obj_desc").asString());
				finalObj.put("documentclassifier", obj.path("info_obj_typeL3").asString());
				finalObj.put("documentjson", obj.path("obj_master_uuid_info_obj").asString());
				finalObj.put("documentparentid", obj.path("info_obj_int2").asInt());
				finalObj.put("documentparenttype", obj.path("info_obj_int3").asInt());
				finalObj.put("startdate", obj.path("start_epc").asLong());

				// enddate
				if (obj.path("end_epc").isNull()) {
					finalObj.putNull("enddate");
				} else {
					finalObj.put("enddate", obj.path("end_epc").asLong());
				}
				finalObj.put("status", obj.path("status").asInt());
				finalObj.put("srno", obj.path("info_obj_int4").asLong());
				finalArray.add(finalObj);
			}

			System.out.println(
					"Final Clause JSON : " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(finalArray));

			return mapper.writeValueAsString(finalArray);

		} catch (Exception e) {

			e.printStackTrace();
			return e.getMessage();
		}
	}

	private String createSLAResponse(String response) {

		try {
			JsonNode rootNode = mapper.readTree(response);

			int status = rootNode.path("status").asInt();
			JsonNode dataArray = rootNode.get("data");

			if (status == 0 || dataArray == null || dataArray.isNull() || !dataArray.isArray()
					|| dataArray.size() == 0) {

				ObjectNode error = mapper.createObjectNode();
				error.put("status", 0);
				error.put("message", "Data not available");
				error.putNull("data");

				return mapper.writeValueAsString(error);
			}

			ArrayNode finalArray = mapper.createArrayNode();

			for (JsonNode obj : dataArray) {

				JsonNode descJson = obj.get("info_obj_desc_json");
				ObjectNode finalObj = mapper.createObjectNode();

				finalObj.put("applicationid", obj.path("info_obj_int1").asInt());
				finalObj.put("documenttype", obj.path("info_obj_int2").asInt());
				finalObj.put("documentid", obj.path("info_obj_id").asInt());
				finalObj.put("versionnumbar", obj.path("info_obj_int3").asInt());
				finalObj.put("documentname", obj.path("info_obj_name").asString());
				finalObj.put("documentdescription", obj.path("info_obj_desc").asString());
				finalObj.put("documentclassifier", obj.path("info_obj_typeL3").asString());
				finalObj.put("documentjson", obj.path("obj_master_uuid_info_obj").asString());
				finalObj.put("documentparenttype", obj.path("info_obj_int4").asInt());
				finalObj.put("documentparentid", obj.path("info_obj_id").asInt());
				finalObj.put("startdate", obj.path("start_epc").asString());

				if (obj.path("end_epc").isNull()) {
					finalObj.putNull("enddate");
				} else {
					finalObj.put("enddate", obj.path("end_epc").asString());
				}
				finalObj.put("status", obj.path("status").asInt());
				finalObj.put("srno", obj.path("info_obj_int4").asInt());
				finalObj.put("vendername", descJson.path("domain").asString());
				finalObj.put("venderid", "10");
				finalObj.put("pown_eid", obj.path("pown_eid").isNull() ? null : obj.path("pown_eid").asLong());
				finalObj.put("org_eid", obj.path("org_eid").isNull() ? null : obj.path("org_eid").asLong());
				finalArray.add(finalObj);
			}

			return mapper.writeValueAsString(finalArray);

		} catch (Exception e) {

			e.printStackTrace();
			return e.getMessage();
		}
	}

	private String createContractJson(String response) {

		try {
			JsonNode rootNode = mapper.readTree(response);
			JsonNode dataArray = rootNode.get("data");
			ArrayNode finalArray = mapper.createArrayNode();

			for (JsonNode obj : dataArray) {

				JsonNode descJson = obj.get("info_obj_desc_json");
				ObjectNode finalObj = mapper.createObjectNode();

				// Mapping fields
				finalObj.put("applicationid", obj.path("info_obj_int1").asInt());
				finalObj.put("documenttype", obj.path("info_obj_int2").asInt());
				finalObj.put("documentid", obj.path("info_obj_id").asInt());
				finalObj.put("versionnumbar", obj.path("info_obj_int3").asInt());
				finalObj.put("documentname", obj.path("info_obj_name").asString());
				finalObj.put("documentdescription", obj.path("info_obj_desc").asString());
				finalObj.put("documentclassifier", obj.path("info_obj_typeL3").asString());
				finalObj.put("documentjson", obj.path("obj_master_uuid_info_obj").asString());
				finalObj.put("documentparenttype", obj.path("info_obj_int4").asInt());
				finalObj.put("documentparentid", obj.path("deviceid").asBigInteger());
				finalObj.put("startdate", obj.path("start_epc").asLong());

				if (obj.path("end_epc").isNull()) {
					finalObj.putNull("enddate");
				} else {
					finalObj.put("enddate", obj.path("end_epc").asLong());
				}

				finalObj.put("status", obj.path("status").asInt());
				finalObj.put("srno", 0);
				finalObj.put("vendername", descJson.path("domain").asString());
				finalObj.put("venderid", "10");

				if (obj.path("pown_eid").isNull()) {
					finalObj.putNull("pown_eid");
				} else {
					finalObj.put("pown_eid", obj.path("pown_eid").asInt());
				}

				if (obj.path("org_eid").isNull()) {
					finalObj.putNull("org_eid");
				} else {
					finalObj.put("org_eid", obj.path("org_eid").asInt());
				}

				finalArray.add(finalObj);
			}

			System.out.println(
					"Final Mapped JSON: " + mapper.writerWithDefaultPrettyPrinter().writeValueAsString(finalArray));
			return mapper.writeValueAsString(finalArray);

		} catch (Exception e) {

			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String getSLADetailsData(String pown_eid, String org_eid, String info_obj_id) {
		try {

			String url = URL + "/detail" + "/" + pown_eid + "/" + org_eid + "/" + info_obj_id;
			String response = restTemplateData.getRestTemplate().getForObject(url, String.class);

			return createSLADetailResponse(response);

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":0,\"message\":\"Error occurred\"}";
		}
	}

	private String createSLADetailResponse(String response) {

		try {

			JsonNode rootNode = mapper.readTree(response);

			int status = rootNode.path("status").asInt();
			JsonNode dataArray = rootNode.path("data");

			if (status == 0 || dataArray == null || !dataArray.isArray() || dataArray.size() == 0) {

				ObjectNode error = mapper.createObjectNode();
				error.put("status", 0);
				error.put("message", "Data not available");
				error.putNull("data");

				return mapper.writeValueAsString(error);
			}

			// Final SLA Array
			ArrayNode slaArray = mapper.createArrayNode();

			for (JsonNode obj : dataArray) {

				ObjectNode slaObj = mapper.createObjectNode();

				slaObj.put("recordserialnumber", obj.path("info_obj_id").asInt());
				slaObj.put("applicationid", obj.path("pown_eid").asInt());

				slaObj.put("documenttype", obj.path("info_obj_typeL2").asInt());
				slaObj.put("documentid", obj.path("info_obj_id").asInt());
				slaObj.put("documentsubid", obj.path("info_obj_typeL3").asInt());

				// 🔥 Mapping SLA values from INFO API
				slaObj.put("docparalelevel1id", obj.path("info_obj_typeL1").asInt());
				slaObj.put("docparalelevel1name", obj.path("info_parm_typeL1").asString());
				slaObj.put("docparalelevel2id", obj.path("info_obj_typeL2").asInt());
				slaObj.put("docparalelevel2name", obj.path("info_parm_nameL2").asString());
				slaObj.put("docparalelevel3id", obj.path("info_obj_typeL3").asInt());
				slaObj.put("docparalelevel3name", obj.path("info_parm_nameL3").asString());
				slaObj.put("documentvalue1", obj.path("info_parm_typeL1").asString());
				slaObj.put("documentvalue2", obj.path("info_parm_nameL2").asString());
				slaObj.put("documentvalue3", obj.path("info_value_status").asString());

				// JSON field
				slaObj.put("docdetailjson", obj.path("info_value_varchar").asString());

				// STATUS
				slaObj.put("status", obj.path("info_value_status").asInt());

				// Optional timestamps (if needed)
				slaObj.put("startdate", obj.path("info_value_epc").asLong());
				slaObj.put("enddate", obj.path("info_value_epc").asLong());

				slaArray.add(slaObj);
			}

			return mapper.writeValueAsString(slaArray);

		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":0,\"message\":\"Parsing error\"}";
		}
	}

}
