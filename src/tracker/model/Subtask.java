package tracker.model;

import tracker.modelParametrs.StatusTask;
import tracker.modelParametrs.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private final int epicId;


    public Subtask(String title, String description,LocalDateTime startTime,int duration, int epicId) {
        super(title, description,startTime,duration);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int id, StatusTask statusTask, LocalDateTime startTime,
                   int duration, int epicId) {
        super(title, description,id,statusTask,startTime,duration);
        this.epicId = epicId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusTask getStatus() {
        return statusTask;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setStatus(StatusTask statusTask) {
        this.statusTask = statusTask;
    }

    public TaskType getTaskType(){
        return TaskType.SUBTASK;
    }

    public LocalDateTime getEndTime(){
        return startTime.plus(Duration.ofMinutes(duration));
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public int getDuration(){
        return duration;
    }



    @Override
    public String toString() {
        return "Subtask{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", statusTask='" + statusTask + '\'' +
                '}';
    }
}

