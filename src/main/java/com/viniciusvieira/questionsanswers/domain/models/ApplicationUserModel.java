package com.viniciusvieira.questionsanswers.domain.models;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Entity
@Table(name = "TB_USER")
public class ApplicationUserModel implements UserDetails, Serializable {
	private static final long serialVersionUID = 1L;

	@Schema(description = "The id of the user")
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUser;

	@Schema(description = "The username of the user, needed to access the features", required = true)
    @Column(nullable = false, unique = true, length = 128)
    private String username;

	@Schema(description = "The password of the user, need to access the features", required = true)
    @Column(nullable = false, length = 128)
    private String password;
    
	@Schema(description = "one-to-one relationship with professor")
    @OneToOne
    @JoinColumn(name = "professor_id")
    private ProfessorModel professor;

	@Schema(description = "many-to-many relationship between user and role")
    // fetch = FetchType.EAGER Resolve o problema de lazy load
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TB_USERS_ROLES",
    		joinColumns = @JoinColumn(name = "user_id"),
    		inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<RoleModel> roles;
    
    
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roles;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
