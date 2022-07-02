package com.taskmanager.tasker;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.taskmanager.tasker.api.Task;
import com.taskmanager.tasker.db.TaskDAO;
import com.taskmanager.tasker.resources.TaskManagerResource;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class TaskerApplication extends Application<TaskerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new TaskerApplication().run(args);
    }

    @Override
    public String getName() {
        return "Tasker";
    }

    @Override
    public void initialize(final Bootstrap<TaskerConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(final TaskerConfiguration configuration,
                    final Environment environment) {
    	final TaskDAO dao = new TaskDAO(hibernate.getSessionFactory());
        environment.jersey().register(new TaskManagerResource(dao));
        
      //Add the filter for cross origin
    	final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);
    	
    	// Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
    }
    
    private final HibernateBundle<TaskerConfiguration> hibernate = new HibernateBundle<TaskerConfiguration>(Task.class) {
        @Override
        public DataSourceFactory getDataSourceFactory(TaskerConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

}
