package com.codewithsiva.fullstackbackend.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

/**
 * @author Simpson Alfred
 */
@Data
public class LoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String password;
}
