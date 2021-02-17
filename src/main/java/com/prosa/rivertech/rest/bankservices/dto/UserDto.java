package com.prosa.rivertech.rest.bankservices.dto;


import javax.validation.constraints.Size;

public class UserDto {

    private Long id;

    @Size(min = 2, message = "Name should have at least 2 characters")
//    @ApiModelProperty(notes = "Name should have at least 2 characters")
    private String name;

    public UserDto() {
    }

    public UserDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserDto(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
