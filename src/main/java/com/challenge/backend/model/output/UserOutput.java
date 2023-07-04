package com.challenge.backend.model.output;

import lombok.Data;

@Data
public class UserOutput {
	private Address address;
    private int id;
    private String email;
    private String username;
    private String password;
    private Name name;
    private String phone;
    private int __v;
    
    @Data
    public static class Address {
        private Geolocation geolocation;
        private String city;
        private String street;
        private int number;
        private String zipcode;
    }
    
    @Data
    public static class Geolocation {
        private String lat;
        private String lon;
    }
    
    @Data
    public static class Name {
        private String firstname;
        private String lastname;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "address=" + address +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name=" + name +
                ", phone='" + phone + '\'' +
                ", __v=" + __v +
                '}';
    }
}
