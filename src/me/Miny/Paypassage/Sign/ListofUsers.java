package me.Miny.Paypassage.Sign;

import java.util.HashMap;

/**
 *
 * @author ibhh
 */
public class ListofUsers {
    private static HashMap<String, PPSign> list = new HashMap<>();

    public static HashMap<String, PPSign> getList() {
        return list;
    }
}
