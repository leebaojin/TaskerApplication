package com.taskmanager.tasker.resources;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.taskmanager.tasker.api.Task;
import com.taskmanager.tasker.db.TaskDAO;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/api/taskmanager")
@Produces(MediaType.APPLICATION_JSON)
public class TaskManagerResource {
	
	private TaskDAO taskdao;
	
	
	public TaskManagerResource(TaskDAO taskdao) {
		this.taskdao = taskdao;
	}
	
	@GET
	@Path("/list")
	@UnitOfWork
	public List<Task> getTaskList(){
		List<Task> tasklist = taskdao.findAll();
		//Sort by date (earliest in front)
		sortTaskList(tasklist);
		return tasklist;
	}
	
	@POST
	@Path("/save")
	@UnitOfWork
	public Task saveTask(Task task) {
		if(task.getId() != null) {
			Task taskstored = taskdao.findById(task.getId());
			if(taskstored == null) {
				throw new WebApplicationException("No such task found", Status.BAD_REQUEST);
			}
			taskstored.setTaskCompleted(task.isTaskCompleted());
			taskdao.save(taskstored);
		}else {
			task.setTaskCompleted(false);
			taskdao.save(task);
		}
		
		return task;
	}
	
	@GET
	@Path("/get/{id}")
	@UnitOfWork
	public Task getTaskById(@PathParam("id") UUID id) {
		Task task = taskdao.findById(id);
		if(task == null) {
			throw new WebApplicationException("No such task found", Status.BAD_REQUEST);
		}
		return task;
	}
	
	@DELETE
	@Path("/clear")
	@UnitOfWork
	public Response clearCompleteTask() {
		taskdao.deleteCompletedTask();
		return Response.ok().build();
	}
	
	private void sortTaskList(List<Task> tasklist) {
		Collections.sort(tasklist, new Comparator<Task>() {
			@Override
			public int compare(Task t1, Task t2) {
				return t1.getTaskDate().compareTo(t2.getTaskDate());
			}
		});
	}
	

}
