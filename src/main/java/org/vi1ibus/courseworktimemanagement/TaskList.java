package org.vi1ibus.courseworktimemanagement;

public class TaskList {
    private int id;
    private String name;
    private int everyone;
    private int owner;

    public TaskList(int id, String name, int owner, int everyone){
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.everyone = everyone;
    }

    public int getId() { return id; }

    public String getName() { return name; }

    public int getOwner() { return owner; }

    public int getEveryone() { return everyone; }

    public void setName(String name) { this.name = name; }

    public void setOwner(int owner) { this.owner = owner; }

    public void setEveryone(int everyone) { this.everyone = everyone; }
}
