package org.vi1ibus.courseworktimemanagement;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class TaskList {
    private int id;
    private String name;
    private int everyone;
    private int owner;
    private LocalDate start;
    private LocalDate end;

    public TaskList(int id, String name, int everyone, int owner){
        this.id = id;
        this.name = name;
        this.everyone = everyone;
        this.owner = owner;
        this.start = LocalDate.of(2021, 11, 1);
        this.end = LocalDate.now();
    }

    public TaskList(String name, int everyone, int owner){
        this.name = name;
        this.everyone = everyone;
        this.owner = owner;
    }
    /** GETTERS **/
    public int getId() { return id; }

    public String getName() { return name; }

    public int getOwner() { return owner; }

    public int getEveryone() { return everyone; }

    public LocalDate getStart() { return start; }

    public LocalDate getEnd() { return end; }

    /** SETTERS **/
    public void setName(String name) { this.name = name; }

    public void setOwner(int owner) { this.owner = owner; }

    public void setEveryone(int everyone) { this.everyone = everyone; }

    public void setStart(LocalDate start) { this.start = start; }

    public void setEnd(LocalDate end) { this.end = end; }
}
