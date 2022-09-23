package com.viniciusvieira.questionsanswers.security.service;

import com.viniciusvieira.questionsanswers.entity.ApplicationUser;
import com.viniciusvieira.questionsanswers.repository.ApplicationUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final ApplicationUserRepository applicationUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = loadAplicationUserByUsername(username);
        return new CustomUserDetails(applicationUser);
    }

    public ApplicationUser loadAplicationUserByUsername(String username) {
        return Optional.ofNullable(applicationUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException("ApplicationUser not found"));
    }

    // Classe estatica que é um aplicationUser e um UserDetails
    // Gambiarra para usar o metodo loadUserByUsername que retorna um UserDetails pre definido pelo spring
    // mas queremos enviar o nosso UserDetails
    private final static class CustomUserDetails extends ApplicationUser implements UserDetails {
        private CustomUserDetails(ApplicationUser applicationUser) {
            super(applicationUser);
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            List<GrantedAuthority> authoritiesListProfessor = AuthorityUtils.createAuthorityList("ROLE_PROFESSOR");
            //TODO: CRIAMOS A ROLE SEM CRIAR O STUDENT
            List<GrantedAuthority> authoritiesListStudent = AuthorityUtils.createAuthorityList("ROLE_STUDENT");
            return this.getProfessor() != null ? authoritiesListProfessor : authoritiesListStudent;
        }

        @Override
        public boolean isAccountNonExpired() {
            return false;
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
}

// aula 06 -> 11 min
// -> verificar como ficou o banco apos alterar o nome da tabela
