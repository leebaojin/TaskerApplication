package com.taskmanager.tasker.db;

import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.taskmanager.tasker.api.Task;

import io.dropwizard.hibernate.AbstractDAO;

public class TaskDAO extends AbstractDAO<Task>{
	
	SessionFactory sessionFactory;

	public TaskDAO(SessionFactory sessionFactory) {
		super(sessionFactory);
		// TODO Auto-generated constructor stub
		this.sessionFactory = sessionFactory;
	}
	
	public Task findById(UUID id) {
		return get(id);
	}
	
	public List<Task> findAll() {
		//list get results of a query
		//query - return a typed query
        return list(query("SELECT t FROM Task t"));
    }
	
	public UUID save(Task task) {
		return persist(task).getId();
	}
	

	
	public void deleteCompletedTask() {
		List<Task> taskCompleted = list(query("SELECT t FROM Task t WHERE t.taskCompleted = true"));
		if(taskCompleted.size() > 0) {
			for(Task task:taskCompleted) {
				sessionFactory.getCurrentSession().delete(task);
			}
		}
		
	}

}
