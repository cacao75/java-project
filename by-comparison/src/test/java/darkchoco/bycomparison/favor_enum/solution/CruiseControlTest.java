package darkchoco.bycomparison.favor_enum.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CruiseControlTest {

    private CruiseControl cruiseControl;

    @BeforeEach
    void setUp() {
        cruiseControl = new CruiseControl();
    }

    @Test
    void testSetPreset_Stop() {
        cruiseControl.setPreset(SpeedPreset.STOP);
        assertEquals(0, getTargetSpeedKmh(), "STOP 프리셋을 설정한 후 속도는 0이어야 합니다.");
        assertEquals(0, cruiseControl.getTargetSpeedKmh(), "STOP 프리셋을 설정한 후 속도는 0이어야 합니다.");
    }

    @Test
    void testSetPreset_PlanetarySpeed() {
        cruiseControl.setPreset(SpeedPreset.PLANETARY_SPEED);
        assertEquals(7667, getTargetSpeedKmh(), "PLANETARY_SPEED 프리셋을 설정한 후 속도는 7667이어야 합니다.");
        assertEquals(7667, cruiseControl.getTargetSpeedKmh(), "PLANETARY_SPEED 프리셋을 설정한 후 속도는 7667이어야 합니다.");
    }

    @Test
    void testSetPreset_CruiseSpeed() {
        cruiseControl.setPreset(SpeedPreset.CRUISE_SPEED);
        assertEquals(16944, getTargetSpeedKmh(), "CRUISE_SPEED 프리셋을 설정한 후 속도는 16944이어야 합니다.");
        assertEquals(16944, cruiseControl.getTargetSpeedKmh(), "CRUISE_SPEED 프리셋을 설정한 후 속도는 16944이어야 합니다.");
    }

    @Test
    void testSetTargetSpeedKmh() {
        cruiseControl.setTargetSpeedKmh(5000);
        assertEquals(5000, getTargetSpeedKmh(), "직접 속도를 설정한 경우 5000이어야 합니다.");
        assertEquals(5000, cruiseControl.getTargetSpeedKmh(), "직접 속도를 설정한 경우 5000이어야 합니다.");
    }

    @Test
    void testSetPreset_Null_ShouldThrowException() {
        assertThrows(NullPointerException.class, () -> cruiseControl.setPreset(null),
                "null 프리셋 설정 시 NullPointerException이 발생해야 합니다.");
    }

    /**
     * Private 필드 `targetSpeedKmh`의 값을 리플렉션을 사용하여 가져오는 메서드.
     */
    private double getTargetSpeedKmh() {
        try {
            // getDeclaredField()는 private 필드도 포함해서 가져오는 반면, getField()는 public 필드만 가져온다
            var field = CruiseControl.class.getDeclaredField("targetSpeedKmh");
            field.setAccessible(true);  // 접근 제어 규칙을 무시하고, private 필드에도 접근할 수 있도록 허용
            return field.getDouble(cruiseControl);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
