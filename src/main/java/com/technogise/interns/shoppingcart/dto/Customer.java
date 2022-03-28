package com.technogise.interns.shoppingcart.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Setter
@Getter
@ApiModel(description = "Details about the customer")
public class Customer {

    @ApiModelProperty(notes = "The unique id of the customer")
    private UUID id;
    @ApiModelProperty(notes = "The first name of the customer")
    private String first_name;
    @ApiModelProperty(notes = "The last name of the customer")
    private String last_name;
    @ApiModelProperty(notes = "The phone number of the customer")
    private String phone_number;
    @ApiModelProperty(notes = "The address of the customer")
    private String address;
    @ApiModelProperty(notes = "The email id of the customer")
    private String email;
    @ApiModelProperty(notes = "The password of the customer")
    private String password;
}