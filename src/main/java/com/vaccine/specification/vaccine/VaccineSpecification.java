package com.vaccine.specification.vaccine;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.vaccine.entity.Vaccine;
import com.vaccine.specification.SearchCriteria;

public class VaccineSpecification implements Specification<Vaccine> {
	private static final long serialVersionUID = 1L;
	private SearchCriteria criteria;

	public VaccineSpecification(SearchCriteria criteria) {
		this.criteria = criteria;
	}

	@Override
	public Predicate toPredicate(Root<Vaccine> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {

		if (criteria.getOperator().equalsIgnoreCase("Like")) {
			return criteriaBuilder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
		}
		
		if (criteria.getOperator().equalsIgnoreCase("=")) {
			return criteriaBuilder.equal(root.get(criteria.getKey()), Integer.valueOf((criteria.getValue().toString())));
		}


		if (criteria.getOperator().equalsIgnoreCase(">=")) {
			return criteriaBuilder.greaterThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
		}

		if (criteria.getOperator().equalsIgnoreCase("<=")) {
			return criteriaBuilder.lessThanOrEqualTo(root.get(criteria.getKey()), criteria.getValue().toString());
		}

		return null;
	}
}
