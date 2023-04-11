package com.nikki.jwt.security.domen.constant;

public class RegExp {
    public final static String name = "^[A-zА-яЁё]{1,44}$";
    public final static String email = "^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{1,44}$";
    public final static String password = "^(?=.*[A-z])(?=.*[0-9])(?=.*[!@#$%&*-_=+]).{8,44}$";
}
