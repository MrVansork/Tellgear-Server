package com.tellgear.net;

import com.tellgear.util.Constants;
import com.tellgear.util.Utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class User {

    public static List<User> users = new ArrayList<>();

    private String name;
    private String passwd;
    private String permissions;
    private String last_connection;
    private String path;

    public static boolean exists(String name){
        for(User user:users){
            if(user.getName().equals(name))
                return true;
        }
        return false;
    }

    public static void addUser(String name, String passwd){
        File file = new File(Constants.ProgramData+"User Profiles/"+name+"/");
        file.mkdirs();

        users.add(new User(name, passwd, "user", Utilities.getDate(), Constants.ProgramData+"User Profiles/"+name+"/"));
    }

    public static boolean checkLogin(String user, String passwd){
        if(!exists(user)) return false;

        if(findUser(user).getPasswd().equals(passwd))
            return true;

        return false;
    }

    public static User findUser(String name){
        for(User user:users){
            if(user.getName().equals(name))
                return user;
        }
        return null;
    }

    public User(String name, String passwd, String permissions, String last_connection, String path) {
        this.name = name;
        this.passwd = passwd;
        this.permissions = permissions;
        this.last_connection = last_connection;
        this.path = path;
    }

    public User(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getLastConnection() {
        return last_connection;
    }

    public void setLastConnection(String last_connection) {
        this.last_connection = last_connection;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", passwd=" + passwd + ", permissions="
                + permissions + ", last_connection=" + last_connection + ", path=" + path + "]";
    }

}
