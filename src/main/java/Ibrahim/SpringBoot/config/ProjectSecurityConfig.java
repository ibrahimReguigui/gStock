package Ibrahim.SpringBoot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
public class ProjectSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().ignoringAntMatchers("/register")
                .and().authorizeRequests()
                .mvcMatchers("/saveStore").anonymous()
                .mvcMatchers("/saveAgent").anonymous()
                .mvcMatchers("/finalizeRegistration").anonymous()
                .mvcMatchers("/createPdf").authenticated()
                .mvcMatchers("/profile").authenticated()
                .mvcMatchers("/updateProfile").authenticated()
                .mvcMatchers("/changePasswordForm").authenticated()
                .mvcMatchers("/changePassword").authenticated()
                .mvcMatchers("/updateMobileNumber").authenticated()
                .mvcMatchers("/showProducts").authenticated()
                .mvcMatchers("/updateImage").authenticated()
                .mvcMatchers("/showBills").authenticated()
                .mvcMatchers("/billDetails").authenticated()
                .mvcMatchers("/billForm").hasRole("VENDOR")
                .mvcMatchers("/createBill").hasRole("VENDOR")
                .mvcMatchers("/splitProductForm").hasRole("VENDOR")
                .mvcMatchers("/splitProduct").hasRole("VENDOR")
                .mvcMatchers("/addProductToBillForm").hasRole("VENDOR")
                .mvcMatchers("/removeProduct").hasRole("VENDOR")
                .mvcMatchers("/addProductToBill").hasRole("VENDOR")
                .mvcMatchers("/deleteBill").hasRole("MANAGER")
                .mvcMatchers("/saveProduct").hasRole("MANAGER")
                .mvcMatchers("/updateForm").hasRole("MANAGER")
                .mvcMatchers("/deleteProduct").hasRole("MANAGER")
                .mvcMatchers("/addProductForm").hasRole("MANAGER")
                .mvcMatchers("/confirmedAgentsList").hasRole("OWNER")
                .mvcMatchers("/awaitingConfirmationAgentsList").hasRole("OWNER")
                .mvcMatchers("/deleteAgent").hasRole("OWNER")
                .mvcMatchers("/updateAgentForm").hasRole("OWNER")
                .mvcMatchers("/updateAgent").hasRole("OWNER")
                .mvcMatchers("/confirmedAgentsSearch").hasRole("OWNER")
                .mvcMatchers("/awaitingConfirmationAgentsSearch").hasRole("OWNER")
                .mvcMatchers("/addProductToBillDetails").hasAnyRole("VENDOR","MANAGER")
                .mvcMatchers("/removeProductBillDetails").hasAnyRole("VENDOR","MANAGER")
                .mvcMatchers("/confirmBill").hasAnyRole("VENDOR","MANAGER")
                .mvcMatchers("/cancelBill").hasAnyRole("VENDOR","MANAGER")
                .mvcMatchers("/splitProductBillDetails").hasAnyRole("VENDOR","MANAGER")
                .mvcMatchers("/home").permitAll()
                .mvcMatchers("/login").permitAll()
                .mvcMatchers("/register").permitAll()
                .and().formLogin().loginPage("/login")
                .defaultSuccessUrl("/home")
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
                        response.sendRedirect("/login?error=" + exception.getMessage());
                    }
                })
                .and().logout().logoutSuccessUrl("/home").invalidateHttpSession(true).permitAll()
                .and().httpBasic();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

