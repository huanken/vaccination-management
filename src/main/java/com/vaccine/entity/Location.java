package com.vaccine.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "locations", uniqueConstraints = { @UniqueConstraint(columnNames = { "location_name" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "location_id")
	private int id;

	@Column(name = "location_name", nullable = false, length = 255)
	private String name;
}
