package com.ratwareid.danspro.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/***********************************************************************
 * Module:  com.sisapp.ayodesajuaraapi.entity.TokenAuthEntity
 * Author:  Ratwareid
 * Created: 17/10/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/

@Entity
@Data
@Table(name = "s_account")
public class AccountEntity implements Serializable {

    @Id
    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "jwt_token")
    private String jwtToken;
}
