package com.vaccine.specification.location;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.vaccine.entity.Location;
import com.vaccine.specification.SearchCriteria;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class LocationSpecification implements Specification<Location> {
	private static final long serialVersionUID = 1L;
	private SearchCriteria criteria;

	@Override
	public Predicate toPredicate(Root<Location> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		if (criteria.getOperator().equalsIgnoreCase(">")) {
			return criteriaBuilder.greaterThanOrEqualTo(
					root.<String>get(criteria.getKey()), criteria.getValue().toString());
		} else if (criteria.getOperator().equalsIgnoreCase("<")) {
			return criteriaBuilder.lessThanOrEqualTo(
					root.<String>get(criteria.getKey()), criteria.getValue().toString());
		} else if (criteria.getOperator().equalsIgnoreCase("=")) {
			return criteriaBuilder.equal(root.<String>get(criteria.getKey()), criteria.getValue().toString());
		} else if (criteria.getOperator().equalsIgnoreCase(":")) {
			return criteriaBuilder.like(
					root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
		}
		return null;
	}

	public static Specification<Location> whereLocationIdEqual(Integer id) {
		Specification<Location> where = null;

		if (id != null && id.intValue() > 0) {
			SearchCriteria searchById = new SearchCriteria("id", "=", id);
			where = new LocationSpecification(searchById);
		}

		return where;
	}

	public static Specification<Location> whereLocationNameLike(String name) {
		Specification<Location> where = null;

		if (name != null && !name.isEmpty()) {
			SearchCriteria searchByName = new SearchCriteria("name", ":", name);
			where = new LocationSpecification(searchByName);
		}

		return where;
	}
}
