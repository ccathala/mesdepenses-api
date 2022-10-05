package com.ccathala.mesdepensesapi.security.jwtUtils.models;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ccathala.mesdepensesapi.security.jwtUtils.JwtUserDetailsService;
import com.ccathala.mesdepensesapi.security.jwtUtils.TokenManager;

@RestController
@CrossOrigin
public class JwtController {

    /**
     * 
     */
    @Autowired
    private JwtUserDetailsService userDetailsService;

    /**
     * 
     */
    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * 
     */
    @Autowired
    private TokenManager tokenManager;

    /**
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody JwtRequestModel request,
            HttpServletResponse response) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwtToken = tokenManager.generateJwtToken(userDetails);

        Cookie cookie = new Cookie("SESSION", jwtToken);
        response.addCookie(cookie);

        return ResponseEntity.ok(new JwtResponseModel(jwtToken));
    }
}
