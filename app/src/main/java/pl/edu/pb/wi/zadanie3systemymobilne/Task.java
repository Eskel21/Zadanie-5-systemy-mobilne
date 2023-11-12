package pl.edu.pb.wi.zadanie3systemymobilne;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Date;
import java.util.UUID;


public class Task {
	private UUID id;
	private String name;
	private Date date;
	private boolean done;
	private Category category;

	public Task() {
		id = UUID.randomUUID();
		date = new Date();
		category = Category.STUDIA;

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public UUID getId() {
		return id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
