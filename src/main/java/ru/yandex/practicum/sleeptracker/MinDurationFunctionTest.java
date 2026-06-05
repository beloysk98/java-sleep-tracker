package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analysis.MinDurationFunction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinDurationFunctionTest {

    @Test
    void testEmptyList() {
        MinDurationFunction func = new MinDurationFunction();
        SleepAnalysisResult result = func.apply(List.of());
        assertEquals(0L, result.getValue());
    }

    @Test
    void testMinDuration() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(5), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), SleepQuality.BAD)
        );
        MinDurationFunction func = new MinDurationFunction();
        SleepAnalysisResult result = func.apply(sessions);
        assertEquals(300L, result.getValue()); // 5 hours = 300 minutes
    }
}