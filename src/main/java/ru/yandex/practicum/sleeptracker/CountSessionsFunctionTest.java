package ru.yandex.practicum.sleeptracker;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.sleeptracker.analysis.CountSessionsFunction;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountSessionsFunctionTest {

    @Test
    void testEmptyList() {
        CountSessionsFunction func = new CountSessionsFunction();
        SleepAnalysisResult result = func.apply(List.of());
        assertEquals(0, result.getValue());
        assertEquals("Общее количество сессий", result.getDescription());
    }

    @Test
    void testThreeSessions() {
        List<SleepingSession> sessions = List.of(
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(8), SleepQuality.GOOD),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(7), SleepQuality.NORMAL),
                new SleepingSession(LocalDateTime.now(), LocalDateTime.now().plusHours(6), SleepQuality.BAD)
        );
        CountSessionsFunction func = new CountSessionsFunction();
        SleepAnalysisResult result = func.apply(sessions);
        assertEquals(3, result.getValue());
    }
}