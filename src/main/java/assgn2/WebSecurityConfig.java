package assgn2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;


@Configuration
@ComponentScan(basePackages = {"assgn2"})
@EnableWebMvcSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return authenticationManager();
    }
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web
            .ignoring()
                .antMatchers("/polls/**");
    }
	
	@Override
protected void configure(HttpSecurity http) throws Exception {
http.httpBasic().and()
.csrf().disable()
			.authorizeRequests()
	            .antMatchers( HttpMethod.GET,"/moderators/**" ).hasRole( "ADMIN" )
                .antMatchers("/polls").permitAll()
                .anyRequest().hasRole("ADMIN")
                .and()
            
                
.formLogin()  
.loginPage("/login")
.permitAll(); 
	}
	
	@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

auth
.inMemoryAuthentication()
.withUser( "foo" )
  .password( "bar" )
  .roles( "ADMIN" );
	}

	
	
}