package Ibrahim.SpringBoot.security;

import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.Role;
import Ibrahim.SpringBoot.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private AgentRepository aRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String email = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        Agent agent = aRepo.readByEmail(email);
        if (null != agent && agent.getId() > 0 &&
                passwordEncoder.matches(pwd, agent.getPwd()) &&
                agent.getStatus().equals("Accepted")){
            return new UsernamePasswordAuthenticationToken(
                    email, null, getGrantedAuthorities(agent.getRoles()));
        } else {
            throw new BadCredentialsException("Invalid credentials!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Role roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + roles.getRoleName()));
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
