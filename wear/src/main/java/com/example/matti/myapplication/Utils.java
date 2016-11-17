package com.example.matti.myapplication;

/**
 * Created by mario on 12/11/16.
 */

import java.nio.ByteBuffer;

/**
 * A collection of some static utility functions
 */
public class Utils {
    public static byte[] floatToByte(float n) {
        return ByteBuffer.allocate(4).putFloat(n).array();
    }
}
