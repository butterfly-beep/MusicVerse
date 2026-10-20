
package com.music.model;

import java.sql.Date;
import java.sql.Time;

public class Event {

    private int id;
    private String name;
    private String description;
    private Date date;
    private Time startTime;
    private Time endTime;
    private int capacity;
    private String city;

    public Event(int id, String name, String description,
                 Date date, Time startTime, Time endTime,
                 int capacity, String city) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.capacity = capacity;
        this.city = city;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public Date getDate() { return date; }
    public Time getStartTime() { return startTime; }
    public Time getEndTime() { return endTime; }
    public int getCapacity() { return capacity; }
    public String getCity() { return city; }
}
