package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analysis.MaxDurationFunction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxDurationFunctionTest {

    @Test
    void testEmptyList() {
        MaxDurationFunction func = new MaxDurationFunction();
        SleepAnalysisResult result = func.apply(List.of());
        assertEquals(0L, result.getValue());
    }

    @Test
    void testMaxDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(5), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(10), SleepQuality.BAD)
        );
        MaxDurationFunction func = new MaxDurationFunction();
        SleepAnalysisResult result = func.apply(sessions);
        assertEquals(600L, result.getValue()); // 10 hours = 600 minutes
    }
}