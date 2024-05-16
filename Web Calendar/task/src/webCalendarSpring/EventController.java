package webCalendarSpring;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@Component
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/event/{id}")
    public ResponseEntity<Event> getEventByID(@PathVariable int id){
        List<Event> todayEvents = eventRepository.findAll();
        for(Event e: todayEvents){
            if(e.getId()==id){
                return ResponseEntity.ok().body(e);
            }
        }

        throw new EventNotFoundException("The event doesn't exist!");
    }

    @GetMapping(value="/event", params = {"start_time","end_time"})
    public ResponseEntity<?> getEventsWithinTime(@RequestParam LocalDate start_time, @RequestParam LocalDate end_time){
        List<Event> todayEvents = eventRepository.findAll();
        List<Event> returnEvents = new ArrayList<>();
        if(todayEvents.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        if(start_time!=null&&end_time!=null){
            for(Event e: todayEvents){
                if(e.getDate().isAfter(start_time)&&e.getDate().isBefore(end_time)){
                    returnEvents.add(e);
                }
            }
            return ResponseEntity.ok().body(returnEvents);
        }
        return ResponseEntity.ok().body(todayEvents);
    }

    @DeleteMapping("/event/{id}")
    public ResponseEntity<?> deleteByID(@PathVariable long id){
        List<Event> todayEvents = eventRepository.findAll();
        for(Event e: todayEvents){
            if(e.getId()==id){
                eventRepository.deleteById(id);
                return ResponseEntity.ok().body(e);
            }
        }

        throw new EventNotFoundException("The event doesn't exist!");
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/event/today")
    public ResponseEntity<?> today() {
        List<Event> todayEvents = eventRepository.findAll();
        List<Event> returnEvents = new ArrayList<>();
        for (Event todayEvent : todayEvents) {
            if (todayEvent.getDate().equals(LocalDate.now())) {
                returnEvents.add(todayEvent);
            }
        }
        return ResponseEntity.ok(returnEvents);
    }

    @GetMapping("/event")
    public ResponseEntity<?> getEvents(){
        if(eventRepository.findAll().isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(new ArrayList<>(eventRepository.findAll()));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/event")
    public ResponseEntity<Map<String, String>> addEvent(@RequestBody @Valid Event event) {
        Map<String, String> response = Map.of(
                "message", "The event has been added!",
                "event", event.getEvent(),
                "date", String.valueOf(event.getDate()));
        Event saveEvent = new Event(event.getEvent(), event.getDate());
        eventRepository.save(saveEvent);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}