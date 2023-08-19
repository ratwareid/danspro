package com.ratwareid.danspro.configuration;

import com.ratwareid.danspro.entity.AccountEntity;
import com.ratwareid.danspro.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/***********************************************************************
 * Module:  com.sisapp.ayodesajuaraapi.controller.jwt.JwtUserDetailsService
 * Author:  Ratwareid
 * Created: 17/10/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String nik) throws UsernameNotFoundException {
        Optional<AccountEntity> account = accountRepository.findFirstByUsername(nik);

        if (account.isPresent()) {
            return new User(account.get().getUsername(), account.get().getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("Session Pengguna dengan nik: " + nik + " tidak ditemukan !");
        }
    }
}
