package com.caution.commeet.repository;


import com.caution.commeet.domain.User;

import java.util.List;

public interface UserRepositoryCustom {

    List<User> searchProfessors(String department , String name);

}
