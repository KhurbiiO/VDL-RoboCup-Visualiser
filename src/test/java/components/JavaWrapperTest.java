package components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JavaWrapperTest {

    @BeforeEach
    void setUp() {

    }

    @Test
    void TestHelloTest() {
       String info = interface_wrapper.GetLibraryInfo();
       System.out.println(info);
    }

}
