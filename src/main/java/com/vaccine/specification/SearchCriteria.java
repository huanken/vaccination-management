package com.vaccine.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SearchCriteria {
	private String key;
	private String operator;
	private Object value;
}
