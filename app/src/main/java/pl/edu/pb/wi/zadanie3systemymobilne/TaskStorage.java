package pl.edu.pb.wi.zadanie3systemymobilne;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import pl.edu.pb.wi.zadanie3systemymobilne.Category;

public class TaskStorage {
	private static final TaskStorage taskStorage = new TaskStorage();

	private List<Task> tasks;

	public static TaskStorage getInstance(){return taskStorage;}

	private TaskStorage(){
		tasks = new ArrayList<>();
		for(int i = 1; i<= 125; i++){
			Task task = new Task();
			task.setName("Zadanie numer "+ i);
			task.setDone(i % 3 ==0);
			if(i%3==0){
				task.setCategory(Category.STUDIA);
			}
			else{
				task.setCategory(Category.DOM);
			}

			tasks.add(task);
		}
	}
	public List<Task> getTasks(){
		return tasks;
	}
	public void addTask(Task task) {
		tasks.add(task);
	}
	public Task getTask(UUID id){
		for(Task task:tasks){
			if (task.getId().equals(id)){
				return task;
			}
		}
		return null;
	}
}
