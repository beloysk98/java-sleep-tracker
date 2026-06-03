package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analysis.AvgDurationFunction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AvgDurationFunctionTest {

    @Test
    void testEmptyList() {
        AvgDurationFunction func = new AvgDurationFunction();
        SleepAnalysisResult result = func.apply(List.of());
        assertEquals(0.0, (double) result.getValue());
    }

    @Test
    void testAverageDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(10), SleepQuality.BAD)
        );
        AvgDurationFunction func = new AvgDurationFunction();
        SleepAnalysisResult result = func.apply(sessions);
        assertEquals(480.0, (double) result.getValue(), 0.01); // 8+6+10=24 hours = 1440 minutes, average = 480
    }
}