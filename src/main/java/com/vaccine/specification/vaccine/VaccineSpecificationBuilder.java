package com.vaccine.specification.vaccine;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.vaccine.entity.Vaccine;
import com.vaccine.specification.SearchCriteria;

public class VaccineSpecificationBuilder {
	
	private String search;

	public VaccineSpecificationBuilder(String search) {
		this.search = search;
	}

	@SuppressWarnings("deprecation")
	public Specification<Vaccine> build() {
		SearchCriteria seachIDCriteria = new SearchCriteria("id", "=", search);
		SearchCriteria seachNameCriteria = new SearchCriteria("name", "Like", search);
		SearchCriteria seachManufactureCriteria = new SearchCriteria("manufacture", "Like", search);
		Specification<Vaccine> where = null;

		// search
		if (!StringUtils.isEmpty(search)) {
			where = new VaccineSpecification(seachNameCriteria);
			where = where.or(new VaccineSpecification(seachManufactureCriteria));
			// regex check is Integer 
			if (search.matches("-?\\d+")) {
				where = where.or(new VaccineSpecification(seachIDCriteria));
			}
		}

		return where;
	}
}
