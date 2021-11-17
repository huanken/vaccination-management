package com.vaccine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "injection_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Injection {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private int id;

	@ManyToOne(targetEntity = Account.class)
	@JoinColumn(name = "user_id", referencedColumnName = "user_id")
	private Account user;

	@ManyToOne(targetEntity = Vaccine.class)
	@JoinColumn(name = "vaccine_id", referencedColumnName = "vaccine_id")
	private Vaccine vaccine;

	@ManyToOne(targetEntity = Location.class)
	@JoinColumn(name = "location_id", referencedColumnName = "location_id")
	private Location location;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_injection", nullable = false)
	private Date dateInjection;

	@Column(name = "status", nullable = false)
	private String status;
}
