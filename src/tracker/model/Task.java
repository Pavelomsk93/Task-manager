package tracker.model;

import tracker.modelParametrs.StatusTask;
import tracker.modelParametrs.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;


public class Task {
    protected String title;
    protected String description;
    protected int id;
    protected StatusTask status;
    protected TaskType type;
    protected LocalDateTime startTime;
    protected Duration duration ;

    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.status = StatusTask.NEW;
    }

    public Task(String title, String description, int id, StatusTask status) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime =getStartTime();
        this.duration = getDuration();
    }

    public Task(String title, String description,LocalDateTime startTime,Duration duration) {
        this.title = title;
        this.description = description;
        this.status = StatusTask.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String title, String description,int id,StatusTask status,LocalDateTime startTime,Duration duration) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public String getTitle(){
        return title;
    }

    public String getDescription(){
        return description;
    }

    public void setId(int id) {
        this.id = id;
    }

    public StatusTask getStatus() {
        return status;
    }

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public TaskType getTaskType(){
        return TaskType.TASK;
    }

    public LocalDateTime getEndTime(){
        if(startTime!=null) {
            return startTime.plus(duration);
        }else{
            return null;
        }
    }

    public LocalDateTime getStartTime(){
            return startTime;
    }

    public Duration getDuration(){
        return duration;
    }

    public static class TaskComparator implements Comparator<Task> {

        @Override
        public int compare(Task task1, Task task2) {

            if (task1.getStartTime()!= null && task2.getStartTime()!=null)  {
                return task1.getStartTime().compareTo(task2.getStartTime());
            } else if(task1.getStartTime()==null && task2.getStartTime()==null){
                return task1.getId()- task2.getId();
            }else if (task1.getStartTime()!= null && task2.getStartTime()==null) {
                return -1;
            } else {
                return 1;
            }
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(title, task.title) && Objects.equals(description, task.description) && Objects.equals(status, task.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, description, id, status);
    }

    @Override
    public String toString() {
        return "Task{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
