package com.salesianostriana.gameetapi.security.models.user.dto;


import com.salesianostriana.gameetapi.validation.annotations.FieldsValueMatch;
import com.salesianostriana.gameetapi.validation.annotations.StrongPassword;
import lombok.*;

import javax.persistence.Lob;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@FieldsValueMatch(field = "password", fieldMatch = "password2", message = "{fieldsvalue.not.match}" )
public class CreateUserDto {

    @NotEmpty
    @NotNull
    private String username;

    //TODO: Una imagen default para los usuarios que no escogen imagen
    @Builder.Default
    private String avatar = "";

    @NotEmpty
    @NotNull
    private String fullName;
    @Email
    private String email;

    @Past
    private LocalDate birthdate;

    @Lob
    private String biography;

    @Builder.Default
    private String [] interests = {};



    @NotNull
    private String latlng;

    @StrongPassword(min = 8, max = 120, hasUpper = true, hasOther = true, hasNumber = true, hasLower = true, message = "{password.not.strong}")
    private String password;
    private String password2;

}
