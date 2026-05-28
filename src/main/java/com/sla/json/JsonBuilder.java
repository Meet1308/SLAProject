package com.sla.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sla.dto.ClauseRequestDTO;
import com.sla.dto.ContractRequestDTO;
import com.sla.dto.SLARequestDTO;
import com.sla.dto.SlaDetailDTO;
import com.sla.service.UtiliityMethods;

@Component
public class JsonBuilder {
	
	@Autowired
	private UtiliityMethods utilityMethods;
	
	public String buildMasterJson(ContractRequestDTO dto, String typel1, String typel2) {

		String jsonData = "{"

				+ "\"pown_eid\":1101," + "\"org_eid\":1101," + "\"info_obj_typeL1\":\"" + typel1 + "\","
				+ "\"info_obj_typeL2\":\" " + typel2 + "\","

				+ "\"info_obj_typeL3\":1,"

				+ "\"info_obj_name\":\"" + dto.getAdd_contract_name() + "\"," + "\"info_obj_desc\":\""
				+ dto.getAdd_contract_description() + "\","

				// NEW FIELDS
				+ "\"info_obj_int1\":1," + "\"info_obj_int2\":2," + "\"info_obj_int3\":3," + "\"info_obj_int4\":4,"

				+ "\"info_obj_var1\":\"value1\"," + "\"info_obj_var2\":\"value2\"," + "\"info_obj_var3\":\"value3\","

				+ "\"info_obj_var4\":\"value4\"," + "\"start_time\":\"" + dto.getAdd_contract_start_date() + "\","

				+ "\"end_time\":\"" + dto.getAdd_contract_end_date() + "\"," + "\"status\":1,"

				+ "\"info_obj_desc_json\":{" + "\"type1\":\"" + typel1 + "\"," + "\"type2\":\"" + typel2 + "\","

				+ "\"deviceid\":\"1694312130001111\"," + "\"epoctime\":\"1773466938\"," + "\"source\":\"CONTRACT\","

				+ "\"domain\":\"" + dto.getAdd_contract_vender_text() + "\"," + "\"total_computers\":1" + "},"

				+ "\"info_obj_action_json\":null," + "\"info_obj_notify_json\":null," + "\"info_obj_kb_json\":null,"

				+ "\"details\":[" + "{" + "\"pown_eid\":1101," + "\"org_eid\":1101," + "\"obj_master_uuid_info_obj\":1,"

				+ "\"info_obj_typeL1\":\"" + typel1 + "\"," + "\"info_obj_typeL2\":\"" + typel2 + "\","
				+ "\"info_obj_typeL3\":\""

				+ dto.getApplicationid() + "\"," + "\"info_obj_name\":\"" + dto.getAdd_contract_name() + "\","

				+ "\"info_obj_desc\":\"" + dto.getAdd_contract_description() + "\","

				+ "\"info_parm_typeL1\":\"Contract\"," + "\"info_parm_nameL1\":\"" + dto.getAdd_contract_name() + "\","

				+ "\"info_parm_typeL2\":\"Vendor\"," + "\"info_parm_nameL2\":\"" + dto.getAdd_contract_vender_text()

				+ "\"," + "\"info_parm_typeL3\":\"Duration\"," + "\"info_parm_nameL3\":\""

				+ dto.getAdd_contract_start_date() + " to " + dto.getAdd_contract_end_date() + "\","

				+ "\"info_value_int1\":null," + "\"info_value_int2\":null," + "\"info_value_varchar\":\""

				+ dto.getAdd_contract_description() + "\"," + "\"info_value_epc\":1773466938,"

				+ "\"info_value_status\":1" + "}" + "]" + "}";

		return jsonData;
	}

	public void setInnerData(SLARequestDTO request, List<Map<String, Object>> detailsList, String typel1,
			String typel2) {
		for (SlaDetailDTO node : request.getStrdata()) {

			Map<String, Object> detail = new LinkedHashMap<>();

			detail.put("pown_eid", 1101);
			detail.put("org_eid", 1101);
			detail.put("obj_master_uuid_info_obj", 0);

			detail.put("info_obj_typeL1", typel1);
			detail.put("info_obj_typeL2", typel2);
			detail.put("info_obj_typeL3", 1);

			detail.put("info_obj_name", request.getVendrmngt_slaname());
			detail.put("info_obj_desc", request.getVendrmngt_sla_descb());

			detail.put("info_parm_typeL1", "Level");
			detail.put("info_parm_nameL1", node.getLevel());

			detail.put("info_parm_typeL2", "Role");
			detail.put("info_parm_nameL2", node.getRole());

			detail.put("info_parm_typeL3", "ResolutionType");
			detail.put("info_parm_nameL3", node.getResolutionType());

			detail.put("info_value_int1", null);
			detail.put("info_value_int2", null);

			detail.put("info_value_varchar", "ResolutionTime:" + node.getResolutionTime());

			detail.put("info_value_epc", System.currentTimeMillis() / 1000);
			detail.put("info_value_status", 1);

			Map<String, Object> infoValueJson = new LinkedHashMap<>();
			infoValueJson.put("SLA_type", typel1);
			infoValueJson.put("SLA_category", typel2);
			infoValueJson.put("source", "SLA_API");

			detail.put("info_value_json", infoValueJson);

			detailsList.add(detail);
		}
	}

	public Map<String, Object> getPayloadMap(SLARequestDTO request, String typel1, String typel2) {
		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("pown_eid", 1101);
		payload.put("org_eid", 1101);
		payload.put("info_obj_typeL1", typel1);
		payload.put("info_obj_typeL2", typel2);
		payload.put("info_obj_typeL3", request.getStrdata().get(2).getLevel());
		payload.put("info_obj_name", request.getVendrmngt_slaname());
		payload.put("info_obj_desc", request.getVendrmngt_sla_descb());
		payload.put("info_obj_int1", 1);
		payload.put("info_obj_int2", 2);
		payload.put("info_obj_int3", 3);
		payload.put("info_obj_int4", 4);
		payload.put("info_obj_var1", "value1");
		payload.put("info_obj_var2", "value2");
		payload.put("info_obj_var3", "value3");
		payload.put("info_obj_var4", "value4");

		return payload;
	}

	public Map<String, Object> getDescMap(SLARequestDTO request, String typel1, String typel2) {
		Map<String, Object> desc = new LinkedHashMap<>();
		desc.put("type1", typel1);
		desc.put("type2", typel2);
		desc.put("deviceid", request.getDocumentid());
		desc.put("epoctime", System.currentTimeMillis() / 1000);
		desc.put("source", "SLA_API");
		desc.put("domain", "N/A");

		return desc;
	}

	public Map<String, Object> setClauseData(ClauseRequestDTO dto, String typel1, String typel2) {

		Map<String, Object> payload = new LinkedHashMap<>();
		payload.put("pown_eid", 1101);
		payload.put("org_eid", 1101);
		payload.put("info_obj_typeL1", typel1);
		payload.put("info_obj_typeL2", typel2);
		payload.put("info_obj_typeL3", 1);
		payload.put("info_obj_name", dto.getAdd_clause_name());
		payload.put("info_obj_desc", dto.getAdd_clause_description());

		// ===== JSON OBJECT (SAFE) =====
		Map<String, Object> descJson = new LinkedHashMap<>();
		descJson.put("type1", typel1);
		descJson.put("type2", typel2);
		descJson.put("deviceid", dto.getAdd_clause_refno());
		descJson.put("epoctime", System.currentTimeMillis() / 1000);
		descJson.put("source", "CLAUSE_API");
		descJson.put("domain", "N/A");
		descJson.put("total_computers", 1);

		payload.put("info_obj_desc_json", descJson);
		payload.put("info_obj_action_json", new HashMap<>());
		payload.put("info_obj_notify_json", new HashMap<>());
		payload.put("info_obj_kb_json", new HashMap<>());

		// ===== DETAILS ARRAY =====
		List<Map<String, Object>> detailsList = new ArrayList<>();

		Map<String, Object> detail = new LinkedHashMap<>();

		detail.put("pown_eid", 1101);
		detail.put("org_eid", 1101);
		detail.put("obj_master_uuid_info_obj", 0);
		detail.put("info_obj_typeL1", typel1);
		detail.put("info_obj_typeL2", typel2);
		detail.put("info_obj_typeL3", 1);
		detail.put("info_obj_name", dto.getAdd_clause_name());
		detail.put("info_obj_desc", dto.getAdd_clause_description());
		detail.put("info_parm_typeL1", "Clause");
		detail.put("info_parm_nameL1", dto.getAdd_clause_name());

		detail.put("info_parm_typeL2", "StartDate");
		detail.put("info_parm_nameL2", utilityMethods.convertDateFormat(dto.getAdd_clause_start_date()));

		detail.put("info_parm_typeL3", "EndDate");
		detail.put("info_parm_nameL3", utilityMethods.convertDateFormat(dto.getAdd_clause_end_date()));

		// ===== VALUES (MATCH DB SCHEMA) =====
		detail.put("info_value_int1", null);
		detail.put("info_value_int2", null);
		detail.put("info_value_bint3", null);
		detail.put("info_value_bint4", null);

		detail.put("info_value_varchar", dto.getAdd_clause_description());
		detail.put("info_value_varchar2", null);

		Map<String, Object> infoValueJson = new LinkedHashMap<>();
		infoValueJson.put("clause_type", typel1);
		infoValueJson.put("clause_category", typel2);
		infoValueJson.put("source", "CLAUSE_API");
		detail.put("info_value_json", infoValueJson);

		detail.put("info_value_epc", System.currentTimeMillis() / 1000);
		detail.put("info_value_status", 1);

		detailsList.add(detail);

		payload.put("details", detailsList);

		return payload;
	}

}
