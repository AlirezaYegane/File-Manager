package org.example.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@lombok.ToString(exclude = "password")
public class User {
    private String username;
    private String password;
}
