package jnr;

import jnr.ffi.*;
import jnr.ffi.types.size_t;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {
    public interface TestLib {
        public int abs(int value);
        public int max(int[] array, @size_t int size);
        public ByteBuffer create_vec();
    }

    public static void main(String[] args) {
        TestLib lib = LibraryLoader.create(TestLib.class).load("binding_test_library");

        System.out.println("ABS(0) = " + lib.abs(0));
        System.out.println("ABS(1) = " + lib.abs(1));
        System.out.println("ABS(-1) = " + lib.abs(-1));

        int[] buffer = new int[]{1, 2, 3};
        System.out.println("MAX(1, 2, 3) = " + lib.max(buffer, 3));

        System.out.println("CREATE VEC: " + lib.create_vec());
    }
}
