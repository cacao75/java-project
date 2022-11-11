import java.util.BitSet;

/**
 * Constructor의 인자를 통해 들어온 Gene 값을 BitSet 로 바꾸어 Class 내부에 length와 같이 저장한다.
 */
public class CompressedGene {

    private BitSet bitSet;
    private int length;

    public CompressedGene(String gene) {
        compress(gene);
    }

    private void compress(String gene) {
        length = gene.length();
        // BitSet 는 vector of bits. Gene 하나당 2개의 bit를 사용하면 된다.
        bitSet = new BitSet(length * 2);
        final String upperGene = gene.toUpperCase();
        // convert String to bit representation
        for (int i = 0; i < length; i++) {
            final int firstLocation = 2 * i;
            final int secondLocation = 2 * i + 1;

            switch (upperGene.charAt(i)) {
                case 'A' -> { // 00 are next two bits
                    bitSet.set(firstLocation, false);
                    bitSet.set(secondLocation, false);
                }
                case 'C' -> { // 01 are next two bits
                    bitSet.set(firstLocation, false);
                    bitSet.set(secondLocation, true);
                }
                case 'G' -> { // 10 are next two bits
                    bitSet.set(firstLocation, true);
                    bitSet.set(secondLocation, false);
                }
                case 'T' -> { // 11 are next two bits
                    bitSet.set(firstLocation, true);
                    bitSet.set(secondLocation, true);
                }
                default ->
                        throw new IllegalArgumentException("The provided gene String contains characters other than ACGT");
            }
        }
    }

    public String decompress() {
        if (bitSet == null) {
            return "";
        }

        // create a mutable place for characters with right capacity
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < (length * 2); i += 2) {
            final int firstBit = (bitSet.get(i) ? 1 : 0);
            final int secondBit = (bitSet.get(i + 1) ? 1 : 0);
            // int 형인 firstBit를 이진수로 바꿔서 왼쪽으로 비트 이동한 다음 secondBit 와 | 논리연산을 한다.
            final int result = firstBit << 1 | secondBit;
            switch (result) {
                // binary literal로 구분하자.
                case 0b00 -> // 00 is 'A'
                        builder.append('A');
                case 0b01 -> // 01 is 'C'
                        builder.append('C');
                case 0b10 -> // 10 is 'G'
                        builder.append('G');
                case 0b11 -> // 11 is 'T'
                        builder.append('T');
            }
        }

        return builder.toString();
    }
}
