package com.vaccine.specification.injection;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.vaccine.entity.Injection;
import com.vaccine.specification.SearchCriteria;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class InjectionSpecification implements Specification<Injection> {
	private static final long serialVersionUID = 1L;
	private SearchCriteria criteria;
	private String foreignEntity;

	@Override
	public Predicate toPredicate(Root<Injection> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (foreignEntity != null && !foreignEntity.isEmpty()) {
			if (criteria.getOperator().equalsIgnoreCase(">")) {
				return criteriaBuilder.greaterThanOrEqualTo(
						root.<String>get(foreignEntity).get(criteria.getKey()), criteria.getValue().toString());
			} else if (criteria.getOperator().equalsIgnoreCase("<")) {
				return criteriaBuilder.lessThanOrEqualTo(
						root.<String>get(foreignEntity).get(criteria.getKey()), criteria.getValue().toString());
			} else if (criteria.getOperator().equalsIgnoreCase("=")) {
				return criteriaBuilder.equal(root.<String>get(foreignEntity).get(criteria.getKey()),
						Integer.valueOf(criteria.getValue().toString()));
			} else if (criteria.getOperator().equalsIgnoreCase(":")) {
				return criteriaBuilder.like(
						root.<String>get(foreignEntity).get(criteria.getKey()), "%" + criteria.getValue() + "%");
			}
		} else {
			if (criteria.getOperator().equalsIgnoreCase(">")) {
				return criteriaBuilder.greaterThanOrEqualTo(
						root.<String>get(criteria.getKey()), criteria.getValue().toString());
			} else if (criteria.getOperator().equalsIgnoreCase("<")) {
				return criteriaBuilder.lessThanOrEqualTo(
						root.<String>get(criteria.getKey()), criteria.getValue().toString());
			} else if (criteria.getOperator().equalsIgnoreCase("=")) {
				return criteriaBuilder.equal(root.<String>get(criteria.getKey()),
						Integer.valueOf(criteria.getValue().toString()));
			} else if (criteria.getOperator().equalsIgnoreCase(":")) {
				return criteriaBuilder.like(
						root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
			}
		}
		return null;
	}

	/*
	 * All specs for injection
	 */
	public static Specification<Injection> whereStatusLike(String status) {
		Specification<Injection> where = null;
		if (status != null && !status.isEmpty()) {
			SearchCriteria search = new SearchCriteria("status", ":", status);
			where = new InjectionSpecification(search, null);
		}
		return where;
	}

	/*
	 * All specs for user
	 */
	public static Specification<Injection> whereUserIdEqual(Integer userId) {
		Specification<Injection> where = null;
		if (userId != null && userId.intValue() > 0) {
			SearchCriteria search = new SearchCriteria("id", "=", userId);
			where = new InjectionSpecification(search, "user");
		}
		return where;
	}

	public static Specification<Injection> whereFullnameLike(String fullname) {
		Specification<Injection> where = null;
		if (fullname != null && !fullname.isEmpty()) {
			SearchCriteria search = new SearchCriteria("fullname", ":", fullname);
			where = new InjectionSpecification(search, "user");
		}
		return where;
	}

	public static Specification<Injection> wherePhoneLike(String phone) {
		Specification<Injection> where = null;
		if (phone != null && !phone.isEmpty()) {
			SearchCriteria search = new SearchCriteria("phone", ":", phone);
			where = new InjectionSpecification(search, "user");
		}
		return where;
	}

	public static Specification<Injection> whereAgeGreaterThanOrEqual(Integer number) {
		Specification<Injection> where = null;
		if (number != null && number.intValue() >= 1) {
			where = new InjectionSpecification(new SearchCriteria("age", ">", number), "user");
		}
		return where;
	}

	public static Specification<Injection> whereAgeLessThanOrEqual(Integer number) {
		Specification<Injection> where = null;
		if (number != null && number.intValue() >= 1) {
			where = new InjectionSpecification(new SearchCriteria("age", "<", number), "user");
		}
		return where;
	}

	/*
	 * All specs for location
	 */
	public static Specification<Injection> whereLocationIdEqual(Integer locationId) {
		Specification<Injection> where = null;
		if (locationId != null && locationId.intValue() > 0) {
			SearchCriteria search = new SearchCriteria("id", "=", locationId);
			where = new InjectionSpecification(search, "location");
		}
		return where;
	}

	public static Specification<Injection> whereLocationNameLike(String locationName) {
		Specification<Injection> where = null;
		if (locationName != null && !locationName.isEmpty()) {
			SearchCriteria search = new SearchCriteria("name", ":", locationName);
			where = new InjectionSpecification(search, "location");
		}
		return where;
	}

	/*
	 * All specs for vaccine
	 */
	public static Specification<Injection> whereVaccineIdEqual(Integer vaccineId) {
		Specification<Injection> where = null;
		if (vaccineId != null && vaccineId.intValue() > 0) {
			SearchCriteria search = new SearchCriteria("id", "=", vaccineId);
			where = new InjectionSpecification(search, "vaccine");
		}
		return where;
	}

	/*
	 * Build
	 */
	public static Specification<Injection> searchBuilder(String search) {
		Specification<Injection> where = null;
		// Check if search is a number
		if (search.matches("^(\\d+)$")) {
			where = whereUserIdEqual(Integer.valueOf(search))
					.or(whereLocationIdEqual(Integer.valueOf(search)))
					.or(whereVaccineIdEqual(Integer.valueOf(search)));

		} else {
			where = whereFullnameLike(search)
					.or(wherePhoneLike(search))
					.or(whereLocationNameLike(search));
		}
		return where;
	}

	public static Specification<Injection> filterBuilder(String filter) {
		Specification<Injection> where = null;
		// Check if filter format is [start-end]
		if (filter.matches("^(\\d+)-(\\d+)$")) {
			String[] extFilter = filter.split("-");
			Integer start = Integer.valueOf(extFilter[0]);
			Integer end = Integer.valueOf(extFilter[1]);
			if (start < end)
				where = whereAgeGreaterThanOrEqual(start)
						.and(whereAgeLessThanOrEqual(end));
		} else {
			where = whereStatusLike(filter);
		}
		return where;
	}
}
