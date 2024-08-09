package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private Type type;
    private List<SubTask> subTaskList;
    private Duration duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Epic(String taskName, String description, Duration duration, LocalDateTime startTime) {
        super(taskName, description, Status.NEW, duration, startTime);
        this.type = Type.EPIC;
        this.subTaskList = new ArrayList<>();
    }

    public Epic(String taskName, String description) {
        super(taskName, description, Status.NEW, null, null);
        this.type = Type.EPIC;
        this.subTaskList = new ArrayList<>();
    }

    public Duration calculateEpicsDuration() {
        Duration totalDuration = subTaskList.stream()
                .map(SubTask::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
        if (totalDuration == null) {
            return null;
        }
        return totalDuration;
    }

    public LocalDateTime calculateEpicsStartTime() {
        return startTime = subTaskList.stream()
                .filter(subTask -> subTask.getStartTime() != null)
                .map(SubTask::getStartTime)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    public LocalDateTime calculateEpicsEndTime() {
        return endTime = subTaskList.stream()
                .filter(subTask -> subTask.getStartTime() != null)
                .map(SubTask::getEndTime)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    public void addSubTask(SubTask subTask) {
        subTaskList.add(subTask);
    }


    public ArrayList<SubTask> getSubTaskList() {
        return new ArrayList<>(subTaskList);
    }


    public void deleteSingleSubTask(int subTaskId) {
        subTaskList.removeIf(subTask -> subTask.getId() == subTaskId);
    }

    public void setSubTasksList(ArrayList<SubTask> subTaskList) {
        this.subTaskList = subTaskList;
    }

    public void deleteSubTask() {
        subTaskList.clear();
    }

    public Type getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "EPIC{ name= " + getTaskName() + '\'' +
                ", description = " + getDescription() + '\'' +
                ", id = " + getId() +
                ", status = " + getStatus() +
                ", duration = " + calculateEpicsDuration().toMinutes() +
                ", startTime = " + calculateEpicsStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")) + '}';
    }
}