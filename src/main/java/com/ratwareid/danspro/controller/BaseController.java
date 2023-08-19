package com.ratwareid.danspro.controller;

import com.ratwareid.danspro.entity.AccountEntity;
import com.ratwareid.danspro.repository.AccountRepository;
import org.hibernate.exception.GenericJDBCException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;

/***********************************************************************
 * Module:  com.sisapp.ayodesajuaraapi.controller.ErrorHandler
 * Author:  Ratwareid
 * Created: 22/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
public class BaseController {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<?> handleConflict(Exception ex) {
        ex.printStackTrace();

        if (ex.getCause() instanceof GenericJDBCException){
            if (((GenericJDBCException) ex.getCause()).getSQLException() instanceof PSQLException){
                PSQLException psqlException = (PSQLException) ((GenericJDBCException) ex.getCause()).getSQLException();
                if (psqlException.getServerErrorMessage() != null) {
                    return new ResponseEntity<>(psqlException.getServerErrorMessage().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    public static AccountEntity getAuthenticateAccount(AccountRepository accountRepository) throws Exception{
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<AccountEntity> accountEntity = accountRepository.findFirstByUsername(username);
        if (accountEntity.isEmpty()) throw new Exception("Pengguna dengan username "+username+" tidak ditemukan!");
        return accountEntity.get();
    }
}
