package com.taskmanager.tasker.api;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name="task")
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@GenericGenerator(
	        name = "uuid",
	        strategy = "uuid2")
	@Column(name="id", columnDefinition = "char(36)")
	@Type(type = "org.hibernate.type.UUIDCharType")
	private UUID id;
	
	@Column(name="description")
	private String taskDescription;
	
	@Column(name="date")
	private Date taskDate;
	
	@Column(name="completed")
	private boolean taskCompleted;

	public Task() {
		super();
	}

	public Task(UUID id, String taskDescription, Date taskDate, boolean taskCompleted) {
		super();
		this.id = id;
		this.taskDescription = taskDescription;
		this.taskDate = taskDate;
		this.taskCompleted = taskCompleted;
	}
	
	@JsonProperty
	public UUID getId() {
		return id;
	}
	@JsonProperty
	public void setId(UUID id) {
		this.id = id;
	}
	@JsonProperty
	public String getTaskDescription() {
		return taskDescription;
	}
	@JsonProperty
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	@JsonProperty
	public Date getTaskDate() {
		return taskDate;
	}
	@JsonProperty
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}

	public boolean isTaskCompleted() {
		return taskCompleted;
	}

	public void setTaskCompleted(boolean taskCompleted) {
		this.taskCompleted = taskCompleted;
	}
	
	
	

}
