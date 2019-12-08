package microsecurite.secutity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import microsecurite.entities.AppUser;

public class JWTAuhenticationFilter extends UsernamePasswordAuthenticationFilter {
@Autowired
private AuthenticationManager authenticationManager;


public JWTAuhenticationFilter(AuthenticationManager authenticationManager) {
	super();
	this.authenticationManager = authenticationManager;
}
@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		try {
			AppUser appUser= new ObjectMapper().readValue(request.getInputStream(), AppUser.class);
			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUsername(), appUser.getPassword()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		
	}
@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		User user= (User)authResult.getPrincipal();
			List<String>roles=new ArrayList<String>();
			authResult.getAuthorities().forEach(r->{
				roles.add(r.getAuthority());
			});
			String jwt=JWT.create()
					.withIssuer(request.getRequestURI())
					.withSubject(user.getUsername())
					.withArrayClaim("roles", roles.toArray(new String[roles.size()]))
					.withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION ))
					.sign(Algorithm.HMAC256(SecurityParams.SECRET));
			response.addHeader(SecurityParams.HEADER_NAME, jwt);
	}
@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		// TODO Auto-generated method stub
		super.unsuccessfulAuthentication(request, response, failed);
	}

}
