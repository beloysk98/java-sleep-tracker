package ru.yandex.practicum.sleeptracker;

import ru.yandex.practicum.sleeptracker.analysis.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SleepTrackerApp {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    private final List<SleepAnalysisFunction> analysisFunctions = new ArrayList<>();

    public SleepTrackerApp() {
        analysisFunctions.add(new CountSessionsFunction());
        analysisFunctions.add(new MinDurationFunction());
        analysisFunctions.add(new MaxDurationFunction());
        analysisFunctions.add(new AvgDurationFunction());
        analysisFunctions.add(new BadQualitySessionsCountFunction());
        analysisFunctions.add(new SleeplessNightsCountFunction());
        analysisFunctions.add(new ChronotypeFunction());
    }

    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println("Пожалуйста, укажите путь к файлу журнала сна.");
            return;
        }

        Path filePath = Path.of(args[0]);
        SleepTrackerApp app = new SleepTrackerApp();

        try {
            List<SleepingSession> sessions = app.readSessions(filePath);
            System.out.println("Результаты анализа сна\n");

            app.analysisFunctions.stream()
                    .map(function -> function.apply(sessions))
                    .forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Ошибка при чтении файла: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<SleepingSession> readSessions(Path path) throws IOException {
        return Files.readAllLines(path).stream()
                .filter(line -> !line.isBlank())
                .map(this::parseLine)
                .filter(session -> session != null)
                .collect(Collectors.toList());
    }

    private SleepingSession parseLine(String line) {
        String[] parts = line.split(";");
        if (parts.length != 3) {
            System.err.println("Пропуск недопустимой строки: " + line);
            return null;
        }

        try {
            LocalDateTime start = LocalDateTime.parse(parts[0], FORMATTER);
            LocalDateTime end = LocalDateTime.parse(parts[1], FORMATTER);
            SleepQuality quality = SleepQuality.valueOf(parts[2]);
            return new SleepingSession(start, end, quality);
        } catch (Exception e) {
            System.err.println("Ошибка при разборе строки: " + line + " - " + e.getMessage());
            return null;
        }
    }
}