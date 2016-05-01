package com.ap.common.util;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleWrap {

    private final Console con = System.console();
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public ConsoleWrap() {
    }

    public String readLine(String msgEnterTeamName) {
        if (con != null) {
            return con.readLine(msgEnterTeamName);
        } else {
            System.out.print(msgEnterTeamName);
            try {
                return br.readLine();
            } catch (IOException ex) {
                System.out.print(ex);
            }
        }
        return "";
    }

    public void printf(String... strings) {
        if (con != null) {
            for(String s : strings)
            con.printf(s);
        } else {
            for(String s : strings)
            System.out.print(s);
        }
    }
}
