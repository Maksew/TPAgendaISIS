package agenda;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

public class Termination {
    private final LocalDate start;
    private final ChronoUnit frequency;
    private LocalDate terminationDate;
    private long numberOfOccurrences;

    public Termination(@NotNull LocalDate start,
                       @NotNull ChronoUnit frequency,
                       @NotNull LocalDate terminationInclusive) {
        this.start = start;
        this.frequency = frequency;
        this.terminationDate = terminationInclusive;
        switch (frequency) {
            case DAYS:
                this.numberOfOccurrences = ChronoUnit.DAYS.between(start, terminationInclusive) + 1;
                break;
            case WEEKS:
                this.numberOfOccurrences = ChronoUnit.WEEKS.between(start, terminationInclusive) + 1;
                break;
            case MONTHS:
                this.numberOfOccurrences = ChronoUnit.MONTHS.between(start, terminationInclusive) + 1;
                break;
        }
    }

    public Termination(@NotNull LocalDate start,
                       @NotNull ChronoUnit frequency,
                       long numberOfOccurrences) {
        this.start = start;
        this.frequency = frequency;
        this.numberOfOccurrences = numberOfOccurrences;
        switch (frequency) {
            case DAYS:
                this.terminationDate = start.plusDays(numberOfOccurrences - 1);
                break;
            case WEEKS:
                this.terminationDate = start.plusWeeks(numberOfOccurrences - 1);
                break;
            case MONTHS:
                this.terminationDate = start.plusMonths(numberOfOccurrences - 1);
                break;
        }
    }

    public LocalDate terminationDateInclusive() {
        return terminationDate;
    }

    public long numberOfOccurrences() {
        return numberOfOccurrences;
    }

}
