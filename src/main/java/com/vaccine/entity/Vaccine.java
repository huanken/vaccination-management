package com.vaccine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vaccines", uniqueConstraints = { @UniqueConstraint(columnNames = { "vaccine_name" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vaccine {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "vaccine_id")
	private int id;

	@Column(name = "vaccine_name", nullable = false, length = 50)
	private String name;

	@Column(name = "description", length = 255)
	private String description;

	@Column(name = "price")
	private float price;

	@Column(name = "amount")
	private int amount;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "expiry_date")
	private Date expiryDate;

	@Column(name = "manufacture", length = 255)
	private String manufacture;
}
