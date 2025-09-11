package org.example.auth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@lombok.ToString(exclude = "password")
public class User implements Serializable {
    private String username;
    private String password;
}
