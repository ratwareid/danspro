package com.ratwareid.danspro.controller;

import com.ratwareid.danspro.configuration.JwtTokenUtil;
import com.ratwareid.danspro.entity.AccountEntity;
import com.ratwareid.danspro.model.LoginRequest;
import com.ratwareid.danspro.model.LoginResponse;
import com.ratwareid.danspro.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
public class LoginController extends BaseController{

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private AccountRepository accountRepository;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody LoginRequest request) throws Exception {
        LoginResponse response = new LoginResponse();
        Optional<AccountEntity> accountEntity = accountRepository.findFirstByUsername(request.getUsername());
        if (accountEntity.isEmpty()) throw new Exception("Akun tidak ditemukan");
        AccountEntity account = accountEntity.get();
        if (!account.getPassword().equals(request.getPassword())) throw new Exception("Password salah !");

        long expiredTimestamp = System.currentTimeMillis() + JwtTokenUtil.JWT_TOKEN_VALIDITY * 1000;
        final String token = jwtTokenUtil.generateToken(account,expiredTimestamp);
        account.setJwtToken(token);
        accountRepository.save(account);

        response.setJwttoken(token);
        return ResponseEntity.ok().body(response);
    }
}
