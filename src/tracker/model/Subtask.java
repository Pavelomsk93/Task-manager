package tracker.model;

import tracker.modelParametrs.StatusTask;
import tracker.modelParametrs.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private final Epic epic;


    public Subtask(String title, String description,LocalDateTime startTime,Duration duration, Epic epic) {
        super(title, description,startTime,duration);
        this.epic = epic;
    }

    public Subtask(String title, String description, int id, StatusTask status, LocalDateTime startTime,
                   Duration duration, Epic epic) {
        super(title, description,id,status,startTime,duration);
        this.epic = epic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusTask getStatus() {
        return status;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public TaskType getTaskType(){
        return TaskType.SUBTASK;
    }

    public LocalDateTime getEndTime(){
        return startTime.plus(duration);
    }

    public LocalDateTime getStartTime(){
        return startTime;
    }

    public Duration getDuration(){
        return duration;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}

