package files_examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class FileReaderCLI {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("사용법: java FileReaderCLI <파일_전체_경로>");
            System.exit(1);
        }

        Path filePath = Path.of(args[0]);

        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            System.err.println("오류: 지정된 파일이 존재하지 않거나 읽을 수 없습니다.");
            System.exit(2);
        }

        try (Stream<String> lines = Files.lines(filePath)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("파일 읽기 중 오류 발생: " + e.getMessage());
            System.exit(3);
        }
    }
}
