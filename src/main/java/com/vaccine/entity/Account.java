package com.vaccine.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts", uniqueConstraints = { @UniqueConstraint(columnNames = { "username" }),
		@UniqueConstraint(columnNames = { "phone" }), @UniqueConstraint(columnNames = { "email" }),
		@UniqueConstraint(columnNames = { "citizen_id" }) })
public class Account {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id", nullable = false)
	private int id;

	@Column(name = "username", length = 255, nullable = false)
	private String username;

	@Column(name = "password", length = 255, nullable = false)
	private String password;

	@Column(name = "fullname", length = 255, nullable = false)
	private String fullname;

	@Column(name = "age", nullable = false)
	private int age;

	@Column(name = "address", length = 255, nullable = true)
	private String address;

	@Column(name = "phone", length = 15, nullable = true)
	private String phone;

	@Column(name = "email", length = 255, nullable = false)
	private String email;

	@Column(name = "citizen_id", length = 20, nullable = false)
	private String citizenId;

	@Column(name = "prioritize")
	private int prioritize = 0;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "account_roles", joinColumns = @JoinColumn(name = "account_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
	private Set<Role> roles = new HashSet<Role>();

	@OneToMany(fetch = FetchType.EAGER, targetEntity = Relative.class)
	@JoinColumn(name = "ref_account_id", referencedColumnName = "user_id")
	private Set<Relative> family = new HashSet<Relative>();
}
