package com.vaccine.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles", uniqueConstraints = { @UniqueConstraint(columnNames = { "role_name" }) })
public class Role {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id", nullable = false)
	private int id;

	@Column(name = "role_name", length = 100, nullable = false)
	private String name;

	@JsonBackReference
	@ManyToMany(mappedBy = "roles")
	private Set<Account> users = new HashSet<Account>();
}
