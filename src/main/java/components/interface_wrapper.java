package components;

import org.bytedeco.javacpp.annotation.*;
import org.bytedeco.javacpp.*;

// @Platform(include = {
// "src\\main\\java\\components\\java_data_wrapper\\java_data_wrapper\\src\\main\\cpp\\dataManager.hpp"
// }, link = { "..\\..\\build\\libmylibrary.so" })
@Platform(include = {"../../resources/cpp/WslInterface" }, link = { "../../resources/build/libwslinterface.so" })
public class interface_wrapper {
    // declare native method using JavaCPP annotation

    static {
        // load the library based on the configuration in platform
        Loader.load();
    }

    /*
     * - function do not need extended pointer
     * - class need since it needs to manage toe memory
     */
    public static native String GetLibraryInfo();
}
