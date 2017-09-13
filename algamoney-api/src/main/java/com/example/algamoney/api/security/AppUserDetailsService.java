package com.example.algamoney.api.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.algamoney.api.model.Permissao;
import com.example.algamoney.api.model.Usuario;
import com.example.algamoney.api.repository.UsusarioRepository;

@Service
public class AppUserDetailsService implements UserDetailsService {

	@Autowired
	private UsusarioRepository ususarioRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Usuario> usuarioOptinal = ususarioRepository.findByEmail(email);
		Usuario usuario = usuarioOptinal.orElseThrow(() -> new UsernameNotFoundException("Usuário e/ou senha incorretos."));
		return new UsuarioSistema(usuario, getPermissoes(usuario));
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		
		for (Permissao simpleGrantedAuthority : usuario.getPermissoes()) {
			authorities.add(new SimpleGrantedAuthority(simpleGrantedAuthority.getDescricao().toUpperCase()));
		}
		
//		JAVA 8
//		usuario.getPermissoes().forEach(permissao -> authorities.add(new SimpleGrantedAuthority(permissao.getDescricao().toUpperCase())));
		
		return authorities;
	}
}