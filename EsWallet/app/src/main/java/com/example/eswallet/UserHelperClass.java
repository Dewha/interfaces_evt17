package com.example.eswallet;

public class UserHelperClass {
    String login, name, second_name, password;

    public UserHelperClass(String login, String name, String second_name, String password) {
        this.login = login;
        this.name = name;
        this.second_name = second_name;
        this.password = password;
    }

    public UserHelperClass() {
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }
    public String getSecondName() {
        return second_name;
    }

    public String getPassword() {
        return password;
    }
}
