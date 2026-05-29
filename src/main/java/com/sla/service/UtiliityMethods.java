package com.sla.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class UtiliityMethods {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public Map<String, String> fatchTypeSubtype(int type, String subtypeName) {

		String typeidl2descshort = null;

		if (type == 1) {
			typeidl2descshort = "Contract";
		} else if (type == 2) {
			typeidl2descshort = "Clause";
		} else if (type == 3) {
			typeidl2descshort = "SLA";
		} else {
			return null;
		}

		String sql;
		List<Map<String, Object>> result;

		if (type == 3) {
			// SLA → only typeidl2 + typeidl3
			sql = "SELECT typeidl2, typeidl3 FROM obb1101_type_master "
					+ "WHERE typeidl2descshort = ? AND typeidl3descshort = ?";
			result = jdbcTemplate.queryForList(sql, typeidl2descshort, subtypeName);

		} else {
			// Contract & Clause → typeidl1 + typeidl2
			sql = "SELECT typeidl1, typeidl2 FROM obb1101_type_master WHERE typeidl2descshort = ?";
			result = jdbcTemplate.queryForList(sql, typeidl2descshort);
		}

		if (!result.isEmpty()) {

			Map<String, Object> row = result.get(0);
			Map<String, String> response = new HashMap<>();

			if (type == 3) {
				// SLA → ONLY these two
				response.put("typeidl2", String.valueOf(row.get("typeidl2")));
				response.put("typeidl3", String.valueOf(row.get("typeidl3")));
			} else {
				// Contract & Clause
				response.put("typeidl1", String.valueOf(row.get("typeidl1")));
				response.put("typeidl2", String.valueOf(row.get("typeidl2")));
			}

			return response;
		}

		return null;
	}

	public String convertDateFormat(String inputDate) {
		DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate date = LocalDate.parse(inputDate, inputFormatter);
		return date.format(outputFormatter);
	}

	public String buildEncodedPayload(String jsonData) {
		String encodedData = Base64.getEncoder().encodeToString(jsonData.getBytes());
		return """
				{
				  "user_json": "%s"
				}
				""".formatted(encodedData);
	}
}
