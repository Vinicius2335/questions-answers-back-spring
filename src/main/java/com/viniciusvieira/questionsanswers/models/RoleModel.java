package com.viniciusvieira.questionsanswers.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import com.viniciusvieira.questionsanswers.Enums.RoleNames;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TB_ROLE")
public class RoleModel implements GrantedAuthority, Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idRole;
	
	@Enumerated(EnumType.STRING)
	@Column(nullable = false, unique = true)
	private RoleNames roleName;

	@Override
	public String getAuthority() {
		return this.roleName.toString();
	}
}
