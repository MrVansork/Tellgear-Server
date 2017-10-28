package com.tellgear;

import com.tellgear.util.Constants;

public class Main {

    public static void main(String[] args){
        Server server = new Server(Constants.PORT);
        server.init();

    }

}
