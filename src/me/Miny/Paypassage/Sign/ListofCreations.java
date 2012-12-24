package me.Miny.Paypassage.Sign;

import java.util.HashMap;

/**
 *
 * @author ibhh
 */
public class ListofCreations {
    private static HashMap<String, SignCreate> list = new HashMap<>();

    public static HashMap<String, SignCreate> getList() {
        return list;
    }
}
