package org.chlorinelabs.protonx.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class DirectStreamReader {

    public static Vector<String> read(String fn) {
        Vector<String> vec = new Vector<>(1,1);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(fn));
            while(true) {
                String st = reader.readLine();
                if(st == null) break;
                vec.addElement(st);
            }
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return vec;
    }

}
