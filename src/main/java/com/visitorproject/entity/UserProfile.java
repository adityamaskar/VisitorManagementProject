package com.visitorproject.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table
@NoArgsConstructor
@Data
@Entity
@AllArgsConstructor
public class UserProfile {

    @Id
    private Integer id;

    private String userName;

    private String password;

    private String email;

}
