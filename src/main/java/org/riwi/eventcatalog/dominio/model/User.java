package org.riwi.eventcatalog.dominio.model;

import lombok.Data;

@Data
public class User {

    private String id;
    private String username;
    private String password;
    private Role role;

}
