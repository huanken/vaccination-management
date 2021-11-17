package com.vaccine.specification.user;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import com.vaccine.entity.Account;
import com.vaccine.specification.SearchCriteria;

public class UserSpecificationBuilder {
	
	private String search;

	public UserSpecificationBuilder(String search) {
		this.search = search;
	}

	@SuppressWarnings("deprecation")
	public Specification<Account> build() {
		SearchCriteria seachIDCriteria = new SearchCriteria("id", "=", search);
		SearchCriteria seachUsernameCriteria = new SearchCriteria("username", "Like", search);
		SearchCriteria seachFullnameCriteria = new SearchCriteria("fullname", "Like", search);
		SearchCriteria seachCityCriteria = new SearchCriteria("citizenId", "=", search);

//		SearchCriteria minTotalMemberCriteria = new SearchCriteria("totalMember", ">=", filter.getMinTotalMember());
//		SearchCriteria maxTotalMemberCriteria = new SearchCriteria("totalMember", "<=", filter.getMaxTotalMember());

		Specification<Account> where = null;

		// search
		if (!StringUtils.isEmpty(search)) {
			where = new UserSpecification(seachUsernameCriteria);
			where = where.or(new UserSpecification(seachFullnameCriteria));
			if (search.matches("-?\\d+")) {
				where = where.or(new UserSpecification(seachIDCriteria));
				where = where.or(new UserSpecification(seachCityCriteria));
			}
		}

		// min totalMember filter
//		if (filter.getMinTotalMember() != 0) {
//			if (where != null) {
//				where = where.and(new GroupSpecification(minTotalMemberCriteria));
//			} else {
//				where = new UserSpecification(minTotalMemberCriteria);
//			}
//		}

		// max totalMember filter
//		if (filter.getMaxTotalMember() != 0) {
//			if (where != null) {
//				where = where.and(new GroupSpecification(maxTotalMemberCriteria));
//			} else {
//				where = new UserSpecification(maxTotalMemberCriteria);
//			}
//		}

		return where;
	}
}
