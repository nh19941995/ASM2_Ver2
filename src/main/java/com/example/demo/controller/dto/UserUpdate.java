package com.example.demo.controller.dto;

import com.example.demo.entity.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserUpdate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "is required")
    private String fullName;

    @NotBlank(message = "is required")
    @Email(message = "Email is not valid")
    private String email;

    private String username;

    @NotBlank(message = "is required")
    private String address;

//    @NotBlank(message = "is required")
    private String image;

    @NotBlank(message = "is required")
//    @Pattern(regexp = "^\\d{10}$", message = "phone must be 10 digits")
    private String phone;

    @NotBlank(message = "is required")
    private String description;

    public UserUpdate(User user) {
        this.id = user.getId();
        this.fullName = user.getFullName();
        this.email = user.getEmail();
        this.address = user.getAddress();
        this.image = user.getImage();
        this.phone = user.getPhone();
        this.description = user.getDescription();
        this.username = user.getUsername();
    }
}
