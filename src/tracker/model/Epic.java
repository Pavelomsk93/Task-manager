package tracker.model;

import tracker.modelParametrs.StatusTask;
import tracker.modelParametrs.TaskType;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Epic extends Task {
    private final List<Subtask> subtaskList = new ArrayList<>();
    protected LocalDateTime startTime ;
    protected Duration duration ;

    public Epic(String title, String description) {
       super(title,description);
       this.startTime = getStartTime();
       this.duration = getDuration();
    }

    public Epic(String title, String description, int id, StatusTask status ,LocalDateTime startTime,
                Duration duration) {
        super(title,description,id,status);
        this.startTime = startTime;
        this.duration = duration;
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

    public void setStatus(StatusTask status) {
        this.status = status;
    }

    public List<Subtask> getSubTaskList() {
        return subtaskList;
    }

    public void addSubtaskList(Subtask subtask) {
        subtaskList.add(subtask);
    }

    public TaskType getTaskType(){
        return TaskType.EPIC;
    }

    public LocalDateTime getEndTime(){

        if(subtaskList!=null && subtaskList.size()!=0) {
            return subtaskList.get(subtaskList.size()-1).getEndTime();
        }else{
            return null;
        }
    }

    public LocalDateTime getStartTime(){
        LocalDateTime start = null;
        if(subtaskList!=null && subtaskList.size()!=0){
            for(Subtask task:subtaskList){
                if(task.getStartTime()!=null) {
                    start = task.getStartTime();
                    break;
                }
            }
            return start;
        }else{
            return null;
        }
    }

    public Duration getDuration() {
        if (getStartTime() != null && getEndTime() != null) {
            return Duration.between(getStartTime(), getEndTime());
        }else{
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtaskList, epic.subtaskList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtaskList);
    }

    @Override
    public String toString() {
        return "Epic{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status='" + status + '\'' +
                '}';
    }
}
