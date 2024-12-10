package agenda;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Repetition {
    private final List<LocalDate> exceptions = new ArrayList<>();
    private Termination termination; //Termination (0..1)
    private final ChronoUnit myFrequency;

    public Repetition(ChronoUnit myFrequency) {
        this.myFrequency = myFrequency;
    }

    public ChronoUnit getFrequency() {
        return myFrequency;
    }

    /**
     * Les exceptions à la répétition
     * @param date un date à laquelle l'événement ne doit pas se répéter
     */
    public void addException(LocalDate date) {
        exceptions.add(date);
    }

    public boolean doesNotOccurAt(LocalDate date) {
        return exceptions.contains(date);
    }

    public Termination getTermination() {
        return termination;
    }

    /**
     * La terminaison d'une répétition (optionnelle)
     * @param termination la terminaison de la répétition
     */
    public void setTermination(Termination termination) {
        this.termination = termination;
    }
}
