package bytearray;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;

public class ByteArray {

    public static void main(String[] args) {
        // 1. Print data in byte array as character.
        byte[] byteArray = new byte[] {-1, -128, 1, 127};  // -128 ~ 127 사이 숫자만 가능.

        System.out.println(Arrays.toString(byteArray));

        // 2. String encoding, decoding
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();

        // 주어진 charset 을 사용하여 문자열을 byte 시퀀스로 인코딩 후 Byte Array 를 return 한다.
        byte[] byteArray1 = str.getBytes();

        System.out.println(Arrays.toString(byteArray1));

        // Byte Array --> default charset 로 decoding 한 문자열 생성.
        // new String(byte[], charset) 을 사용하여 charset 을 지정할 수도 있다.
        String str1 = new String(byteArray1);
        System.out.println(str1);
    }
}
