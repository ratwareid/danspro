package com.ratwareid.danspro.repository;


import com.ratwareid.danspro.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/***********************************************************************
 * Module:  com.sisapp.ayodesajuaraapi.repository.TokenRepository
 * Author:  Ratwareid
 * Created: 17/10/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity,Long> {

    Optional<AccountEntity> findFirstByUsernameAndPassword(String username,String password);
    Optional<AccountEntity> findFirstByJwtTokenEquals(String token);
    Optional<AccountEntity> findFirstByUsername(String username);
}
