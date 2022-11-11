import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class CompressedGeneTest {

    private static final Logger LOG = LoggerFactory.getLogger(CompressedGeneTest.class);

    @Test
    public void givenGeneString_thenCompressed() {
        final String original = "TAGGGATTAACCGTTATATATATATAGCCATGGATCGATTATATAGGGATTAACCGTTATATATATATAGCCATGGATCGATTATA";

        CompressedGene compressedGene = new CompressedGene(original);

        final String decompressedGene = compressedGene.decompress();

        LOG.debug(decompressedGene);

        assertThat(original).isEqualTo(decompressedGene);
    }
}
