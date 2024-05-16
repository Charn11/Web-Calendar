package webCalendarSpring;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank @NotNull
    @Column(nullable = false)
    private String event;

    @NotNull
    @DateTimeFormat(pattern = "YYYY-MM-DD")
    @Column(nullable = false)
    private LocalDate date;

    public Event(){

    }

    public Event(String event, LocalDate date) {
        this.event = event;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public String getEvent() {
        return event;
    }

    public LocalDate getDate() {
        return date;
    }
}

