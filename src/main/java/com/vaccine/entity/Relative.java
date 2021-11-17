package com.vaccine.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
@Table(name = "relative", uniqueConstraints = { @UniqueConstraint(columnNames = { "citizen_id" }) })
public class Relative {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private int id;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = Account.class)
	@JoinColumn(name = "ref_account_id", referencedColumnName = "user_id")
	@JsonBackReference
	private Account account;

	@Column(name = "fullname", length = 255, nullable = false)
	private String fullname;

	@Column(name = "age", nullable = false)
	private int age;

	@Column(name = "address", length = 255, nullable = true)
	private String address;

	@Column(name = "phone", length = 15, nullable = true)
	private String phone;

	@Column(name = "citizen_id", length = 20, nullable = false)
	private String citizenId;

	@Column(name = "prioritize")
	private int prioritize = 0;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_injection")
	private Date date;

}
