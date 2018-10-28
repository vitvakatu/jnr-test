package jnr;

import jnr.ffi.Runtime;
import jnr.ffi.*;
import jnr.ffi.annotations.Delegate;
import jnr.ffi.annotations.Out;
import jnr.ffi.types.size_t;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class Main {
    public interface Callback {
        @Delegate int call(int value);
    }

    public final static class Identity implements Callback {
        public int call(int value) {
            return value;
        }
    }

    public interface TestLib {
        int abs(int value);
        int max(int[] array, @size_t int size);
        void fill_vec(@Out ByteBuffer buffer, @size_t int size);
        int use_callback(Callback callback, int value);
        Callback return_callback();
    }

    public static void main(String[] args) {
        TestLib lib = LibraryLoader.create(TestLib.class).load("binding_test_library");
        Runtime runtime = Runtime.getRuntime(lib);

        System.out.println("ABS(0) = " + lib.abs(0));
        System.out.println("ABS(1) = " + lib.abs(1));
        System.out.println("ABS(-1) = " + lib.abs(-1));

        System.out.println("MAX(1, 2, 3) = " + lib.max(new int[]{1, 2, 3}, 3));
        System.out.println("MAX(3) = " + lib.max(new int[]{3}, 1));
        System.out.println("MAX() = " + lib.max(new int[]{}, 0));
        System.out.println("MAX(3, 4, 2) = " + lib.max(new int[]{1, 4, 2}, 3));

        ByteBuffer buffer = ByteBuffer.allocate(30).order(runtime.byteOrder());
        lib.fill_vec(buffer.slice(), 30);
        System.out.println("CREATE VEC: " + Arrays.toString(buffer.array()));

        System.out.println("CALLBACK(0) = " + lib.use_callback(new Identity(), 0));
        System.out.println("CALLBACK(1) = " + lib.use_callback(new Identity(), 1));
        System.out.println("CALLBACK(-1) = " + lib.use_callback(new Identity(), -1));

        Callback callback = lib.return_callback();
        System.out.println("LIB CALLBACK(0) = " + lib.use_callback(callback, 0));
        System.out.println("LIB CALLBACK(1) = " + lib.use_callback(callback, 1));
        System.out.println("LIB CALLBACK(-1) = " + lib.use_callback(callback, -1));
    }
}
