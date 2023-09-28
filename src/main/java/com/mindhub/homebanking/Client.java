package com.mindhub.homebanking;
@Entity
public class Client {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}
