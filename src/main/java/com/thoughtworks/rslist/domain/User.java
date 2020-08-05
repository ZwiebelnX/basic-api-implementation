package com.thoughtworks.rslist.domain;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class User {

    @NotNull
    @NotEmpty
    @Size(max = 8)
    private String name;

    @Pattern(regexp = "(fe)?male")
    private String gender;

    @Max(100)
    @Min(18)
    private int age;

    @Pattern(regexp = "[\\w|.]+@\\w+.com")
    private String email;

    @Pattern(regexp = "1\\d{10}")
    private String phone;

    public User(String name, String gender, int age, String email, String phone) {
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.email = email;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == User.class && ((User) obj).getName().equals(this.name);
    }
}
