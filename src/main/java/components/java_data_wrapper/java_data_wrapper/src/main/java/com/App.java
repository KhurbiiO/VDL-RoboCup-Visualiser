package com;

import org.bytedeco.javacpp.annotation.*;
import org.bytedeco.javacpp.*;
/**
 * Hello world!
 *
 */
@Platform(include = { "../../cpp/DataManager.hpp" }, link = { "../../build/libmylibrary" })

public class App {
    // declare native method using JavaCPP annotation

    static {
        // load the library based on the configuration in platform
        Loader.load();
    }
    /*
     * - function do not need extended pointer
     * - class need since it needs to manage toe memory
     */

    public static native void hello_test();

    public static void main(String[] args) {

        System.out.println("Hello World!");
        hello_test();
    }
}
