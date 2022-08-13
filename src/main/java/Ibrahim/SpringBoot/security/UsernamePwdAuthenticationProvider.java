package Ibrahim.SpringBoot.security;

import Ibrahim.SpringBoot.model.Agent;
import Ibrahim.SpringBoot.model.AgentStatus;
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
                agent.getStatus().equals(AgentStatus.CONFIRMED)) {
            return new UsernamePasswordAuthenticationToken(
                    email, null, getGrantedAuthorities(agent.getRole()));
        } else if (null != agent && agent.getId() > 0 &&
                passwordEncoder.matches(pwd, agent.getPwd()) &&
                agent.getStatus().equals(AgentStatus.AWAITING_CONFIRMATION)) {
            throw new BadCredentialsException("Account not activated yet !");
        } else if (null != agent && agent.getId() > 0 &&
                !passwordEncoder.matches(pwd, agent.getPwd())) {
            throw new BadCredentialsException("Invalid password !");
        } else {
            throw new BadCredentialsException("Invalid Email !");
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
