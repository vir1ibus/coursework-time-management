package org.vi1ibus.courseworktimemanagement;

public class Task {
    private int id;
    private int tasksListId;
    private String name;
    private String description;
    private int status;

    public Task(int id, int tasksListId, String name, String description, int status){
        this.id = id;
        this.tasksListId = tasksListId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(int tasksListId, String name, String description, int status){
        this.tasksListId = tasksListId;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public void setName(String name) { this.name = name; }

    public void setDescription(String description) { this.description = description; }

    public void setStatus(int status) { this.status = status; }

    public int getId() { return id; }

    public int getTasksListId() { return tasksListId; }

    public String getName() { return name; }

    public String getDescription() { return description; }

    public int getStatus() { return status; }
}
