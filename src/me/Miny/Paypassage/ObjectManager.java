/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.Miny.Paypassage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Simon
 */
public class ObjectManager {

    /**
     * SLAPI = Saving/Loading API API for Saving and Loading Objects. You can
     * use this API in your projects, but please credit the original author of
     * it.
     *
     * @author Tomsik68<tomsik68@gmail.com>
     */
    public static <T extends Object> void save(T obj, String path) throws Exception {
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
        oos.writeObject(obj);
        oos.flush();
        oos.close();
    }

    @SuppressWarnings("unchecked")
	public static <T extends Object> T load(String path) throws Exception {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
        T result = (T) ois.readObject();
        ois.close();
        return result;

    }
}
