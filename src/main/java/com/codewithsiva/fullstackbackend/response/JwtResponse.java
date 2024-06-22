package com.codewithsiva.fullstackbackend.response;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Simpson Alfred
 */

@Data

@NoArgsConstructor
public class JwtResponse {
    private Long id;
    private String email;
    private String token;
    private String type = "Bearer";
    private List<String> roles;
    private String base64ProfileImg;

    public JwtResponse(Long id, String email, String token, List<String> roles,String base64ProfileImg) {
        this.id = id;
        this.email = email;
        this.token = token;
        this.roles = roles;
        this.base64ProfileImg = base64ProfileImg;
    }
}
