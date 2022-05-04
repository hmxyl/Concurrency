package practice.util.countdown;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
class Event {
    private String eventName;

    public Event(String eventName) {
        this.eventName = eventName;
    }
}