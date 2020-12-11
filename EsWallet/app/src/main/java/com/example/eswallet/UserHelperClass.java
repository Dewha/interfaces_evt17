package com.example.eswallet;

public class UserHelperClass {
    String login, name, second_name, password, sum;

    public UserHelperClass(String login, String name, String second_name, String password, String sum) {
        this.login = login;
        this.name = name;
        this.second_name = second_name;
        this.password = password;
        this.sum = sum;
    }
    public UserHelperClass() {
    }

    public String getLogin() {
        return login;
    }

    public String getName() {
        return name;
    }

    public String getSecond_name() {
        return second_name;
    }

    public String getPassword() {
        return password;
    }

    public String getSum() {
        return sum;
    }
}
