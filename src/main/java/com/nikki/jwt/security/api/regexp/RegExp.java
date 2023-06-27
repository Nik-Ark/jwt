package com.nikki.jwt.security.api.regexp;

public class RegExp {
    public final static String name = "^[A-zА-яЁё]{1,44}$";
    public final static String email = "^[A-z0-9._%+-]+@[A-z0-9.-]+\\.[A-z]{1,44}$";
    public final static String password = "^(?=.*[A-z])(?=.*[0-9])(?=.*[!@#$%&*-_=+]).{8,44}$";
//    "\\A(?=[\\x21-\\x7E]*?[0-9])(?=[\\x21-\\x7E]*?[a-z])(?=[\\x21-\\x7E]*?[A-Z])(?=[\\x21-\\x7E]*?[!@#$%^&+=])[\\x21-\\x7E]{8,44}\\z";
}
