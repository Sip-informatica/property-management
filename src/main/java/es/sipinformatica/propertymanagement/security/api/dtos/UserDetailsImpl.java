package es.sipinformatica.propertymanagement.security.api.dtos;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import es.sipinformatica.propertymanagement.security.data.model.User;

public class UserDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;

	private String username;
	private String email;
	private String phone;
	private String dni;
	@JsonIgnore
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private Boolean isAccountNonExpired;
	private Boolean isAccountNonLocked;
	private Boolean isCredentialsNonExpired;
	private Boolean isEnabled;

	public UserDetailsImpl(String username, String email, String phone, String dni, String password,
			Boolean isAccountNonExpired, Boolean isAccountNonLocked, Boolean isCredentialsNonExpired,
			Boolean isEnabled, List<GrantedAuthority> authorities) {

		this.username = username;
		this.email = email;
		this.phone = phone;
		this.dni = dni;
		this.password = password;
		this.isAccountNonExpired = isAccountNonExpired;
		this.isAccountNonLocked = isAccountNonLocked;
		this.isCredentialsNonExpired = isCredentialsNonExpired;
		this.isEnabled = isEnabled;
		this.authorities = authorities;

	}

	public static UserDetailsImpl build(User user) {
		List<GrantedAuthority> authorities = user.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name()))
				.collect(Collectors.toList());

		return new UserDetailsImpl(

				user.getUsername(),
				user.getEmail(),
				user.getPhone(),
				user.getDni(),
				user.getPassword(),
				user.getIsAccountNonExpired(),
				user.getIsAccountNonLocked(),
				user.getIsCredentialsNonExpired(),
				user.getIsEnabled(),
				authorities);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public String getEmail() {
		return email;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return isAccountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return isAccountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return isCredentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return isEnabled;
	}

	public String getPhone() {
		return phone;
	}

	public String getDni() {
		return dni;
	}

}
