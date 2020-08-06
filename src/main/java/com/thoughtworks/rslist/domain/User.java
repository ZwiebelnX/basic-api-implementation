package com.thoughtworks.rslist.domain;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @NotNull
    @NotEmpty
    @Size(max = 8)
    @JsonProperty("user_name")
    @JsonAlias("name")
    private String name;

    @NotNull
    @Pattern(regexp = "(fe)?male")
    @JsonProperty("user_gender")
    @JsonAlias("gender")
    private String gender;

    @Max(100)
    @Min(18)
    @NotNull
    @JsonProperty("user_age")
    @JsonAlias("age")
    private int age;

    @NotNull
    @Pattern(regexp = "[\\w|.]+@\\w+.com")
    @JsonProperty("user_email")
    @JsonAlias("email")
    private String email;

    @NotNull
    @Pattern(regexp = "1\\d{10}")
    @JsonProperty("user_phone")
    @JsonAlias("phone")
    private String phone;

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == User.class && ((User) obj).getName().equals(this.name);
    }
}
