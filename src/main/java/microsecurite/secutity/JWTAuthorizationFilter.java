package microsecurite.secutity;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcherEditor;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import microsecurite.entities.AppRole;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin","*");
		response.addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With,Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers,authorization");
		response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Credentials, authorization");
		
		
		if(request.getMethod().equals("OPTIONS")) {
			response.setStatus(HttpServletResponse.SC_OK);
		}
		else if(request.getRequestURI().equals("/login")) {
			filterChain.doFilter(request, response);
		}
		else {
			String jwt =request.getHeader(SecurityParams.HEADER_NAME);
			if(jwt==null || !jwt.startsWith(SecurityParams.HEADER_PREFIX)) {
				filterChain.doFilter(request, response);
				return ;
			}
			JWTVerifier verifier=JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();
			String JW=jwt.substring(SecurityParams.HEADER_PREFIX.length());
			System.out.println(JW);
			DecodedJWT decodedJWT=verifier.verify(JW);
			String username=decodedJWT.getSubject();
			List<String> roles=decodedJWT.getClaim("roles").asList(String.class);
			Collection<GrantedAuthority> authorities=new ArrayList<GrantedAuthority>();
			roles.forEach(rn->{
				System.out.println(rn);
				authorities.add(new SimpleGrantedAuthority(rn));
			});
			UsernamePasswordAuthenticationToken user= new UsernamePasswordAuthenticationToken(username,null,authorities);
			System.out.println(user.getPrincipal());
		SecurityContextHolder.getContext().setAuthentication(user);
		filterChain.doFilter(request, response);
	
		}
	}

}
