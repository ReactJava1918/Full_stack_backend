package com.codewithsiva.fullstackbackend.service;

import java.util.List;

import com.codewithsiva.fullstackbackend.model.RegUsers;

/**
 * @author Simpson Alfred
 */

public interface IUserService {
    RegUsers registerUser(RegUsers regusers);
   // List<RegUsers> getUsers();
  //  void deleteUser(String email);
  //  User getUser(String email);
}
