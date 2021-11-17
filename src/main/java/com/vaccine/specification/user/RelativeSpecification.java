package com.vaccine.specification.user;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.vaccine.entity.Relative;
import com.vaccine.specification.SearchCriteria;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RelativeSpecification implements Specification<Relative> {
	private static final long serialVersionUID = 1L;
	private SearchCriteria criteria;
	private String foreignEntity;

	@Override
	public Predicate toPredicate(Root<Relative> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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
	 * All specs for family
	 */
	public static Specification<Relative> whereIdEqual(Integer id) {
		Specification<Relative> where = null;
		if (id != null && id.intValue() > 0) {
			SearchCriteria search = new SearchCriteria("id", "=", id);
			where = new RelativeSpecification(search, null);
		}
		return where;
	}

	public static Specification<Relative> whereFullnameLike(String fullname) {
		Specification<Relative> where = null;
		if (fullname != null && !fullname.isEmpty()) {
			SearchCriteria search = new SearchCriteria("fullname", ":", fullname);
			where = new RelativeSpecification(search, null);
		}
		return where;
	}

	public static Specification<Relative> wherePhoneLike(String phone) {
		Specification<Relative> where = null;
		if (phone != null && !phone.isEmpty()) {
			SearchCriteria search = new SearchCriteria("fullname", ":", phone);
			where = new RelativeSpecification(search, null);
		}
		return where;
	}

	public static Specification<Relative> whereAgeGreaterThanOrEqual(Integer number) {
		Specification<Relative> where = null;
		if (number != null && number.intValue() >= 1) {
			where = new RelativeSpecification(new SearchCriteria("age", ">", number), null);
		}
		return where;
	}

	public static Specification<Relative> whereAgeLessThanOrEqual(Integer number) {
		Specification<Relative> where = null;
		if (number != null && number.intValue() >= 1) {
			where = new RelativeSpecification(new SearchCriteria("age", "<", number), null);
		}
		return where;
	}

	/*
	 * All specs for user
	 */
	public static Specification<Relative> whereReferenceIdEqual(Integer userId) {
		Specification<Relative> where = null;
		if (userId != null && userId.intValue() > 0) {
			SearchCriteria search = new SearchCriteria("id", "=", userId);
			where = new RelativeSpecification(search, "account");
		}
		return where;
	}

	/*
	 * Build
	 */
	public static Specification<Relative> searchBuilder(String search) {
		Specification<Relative> where = null;
		// Check if search is a number
		if (search.matches("^(\\d+)$")) {
			where = whereIdEqual(Integer.valueOf(search))
					.or(whereReferenceIdEqual(Integer.valueOf(search)));

		} else {
			where = whereFullnameLike(search)
					.or(wherePhoneLike(search));
		}
		return where;
	}

	public static Specification<Relative> filterBuilder(String filter) {
		Specification<Relative> where = null;
		// Check if filter format is [start-end]
		if (filter.matches("^(\\d+)-(\\d+)$")) {
			String[] extFilter = filter.split("-");
			Integer start = Integer.valueOf(extFilter[0]);
			Integer end = Integer.valueOf(extFilter[1]);
			if (start < end)
				where = whereAgeGreaterThanOrEqual(start)
						.and(whereAgeLessThanOrEqual(end));
		} else {
//			where = whereStatusLike(filter);
		}
		return where;
	}
}
