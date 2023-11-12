package pl.edu.pb.wi.zadanie3systemymobilne;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;

import java.util.List;

import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class TaskListFragment extends Fragment {
	private RecyclerView recyclerView;
	private static final String KEY_SUBTITLE_VISIBLE = "subtitleVisible";
	private boolean subtitleVisible;

	public TaskListFragment() {
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		if (savedInstanceState != null) {
			subtitleVisible = savedInstanceState.getBoolean(KEY_SUBTITLE_VISIBLE);
		}
	}

	public static final String KEY_EXTRA_TASK_ID = "Extra_task_id";
	private TaskAdapter adapter;

	private void updateView() {
		TaskStorage taskStorage = TaskStorage.getInstance();
		List<Task> tasks = taskStorage.getTasks();
		if (adapter == null) {
			adapter = new TaskAdapter(tasks);
			recyclerView.setAdapter(adapter);
		} else {
			adapter.notifyDataSetChanged();
		}
		updateSubtitle();
	}


	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

		inflater.inflate(R.menu.fragment_task_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
		MenuItem subtitleItem = menu.findItem(R.id.show_subtitle);
		if (subtitleVisible) {
			subtitleItem.setTitle(R.string.hide_subtitle);
		} else {
			subtitleItem.setTitle(R.string.show_subtitile);
		}

	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		int itemId = item.getItemId();

		if (itemId == R.id.new_task) {
			Task task = new Task();
			TaskStorage.getInstance().addTask(task);
			Intent intent = new Intent(getActivity(), MainActivity.class);
			intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
			startActivity(intent);
			return true;
		} else if (itemId == R.id.show_subtitle) {
			subtitleVisible = !subtitleVisible;
			getActivity().invalidateOptionsMenu();
			updateSubtitle();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
	}


	public void updateSubtitle() {
		String subtitle = null;
		AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
		if (appCompatActivity != null && appCompatActivity.getSupportActionBar() != null) {
			if (subtitleVisible) {
				TaskStorage taskStorage = TaskStorage.getInstance();
				List<Task> tasks = taskStorage.getTasks();
				int todoTasksCount = 0;
				for (Task task : tasks) {
					if (!task.isDone()) {
						todoTasksCount++;
					}

				}
				subtitle = getString(R.string.subtitle_format, todoTasksCount);
			}
			appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
		}
	}
	@Override
	public void onSaveInstanceState(@NonNull Bundle outState){
		super.onSaveInstanceState(outState);
		outState.putBoolean(KEY_SUBTITLE_VISIBLE, subtitleVisible);
	}
	private class TaskHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


		private TextView nameTextView;
		private TextView dateTextView;

		private ImageView categoryIconImageView;

		private CheckBox doneCheckBox;

		private Task task;

		public CheckBox getCheckBox() {
			return doneCheckBox;
		}

		private void updateTaskNameStyle() {
			nameTextView.setTypeface(null, Typeface.BOLD);
			if (task.isDone()) {
				nameTextView.setPaintFlags(nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
				nameTextView.setTextColor(Color.parseColor("#006400"));


			} else {
				nameTextView.setPaintFlags(nameTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
				nameTextView.setTextColor(Color.parseColor("#8B0000"));
			}
		}

		private String getFormattedTaskName(String name) {

			int maxNameLength = 17;
			if (name.length() > maxNameLength) {
				return name.substring(0, maxNameLength - 3) + "...";
			} else {
				return name;
			}
		}

		public TaskHolder(LayoutInflater inflater, ViewGroup parent) {
			super(inflater.inflate(R.layout.list_item_task, parent, false));
			itemView.setOnClickListener(this);

			nameTextView = itemView.findViewById(R.id.task_item_name);
			dateTextView = itemView.findViewById(R.id.task_item_date);
			categoryIconImageView = itemView.findViewById(R.id.task_item_category_icon);
			doneCheckBox = itemView.findViewById(R.id.task_item_checkbox);
		}

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), MainActivity.class);
			intent.putExtra(KEY_EXTRA_TASK_ID, task.getId());
			startActivity(intent);
		}

		public void bind(Task task) {
			this.task = task;
			nameTextView.setText(getFormattedTaskName(task.getName()));
			dateTextView.setText(task.getDate().toString());

			if (task.getCategory() == Category.DOM) {
				categoryIconImageView.setImageResource(R.drawable.ic_dom);
			} else {
				categoryIconImageView.setImageResource(R.drawable.ic_studia);
			}
			doneCheckBox.setChecked(task.isDone());
			updateTaskNameStyle();
		}
	}

	private class TaskAdapter extends RecyclerView.Adapter<TaskHolder> {
		private List<Task> tasks;

		public TaskAdapter(List<Task> tasks) {
			this.tasks = tasks;
		}

		@NonNull
		@Override
		public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
			LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
			return new TaskHolder(layoutInflater, parent);
		}

		@Override
		public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
			Task task = tasks.get(position);
			holder.bind(task);
			CheckBox checkBox = holder.getCheckBox();
			checkBox.setChecked(tasks.get(position).isDone());
			checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
					tasks.get(holder.getBindingAdapterPosition()).setDone(isChecked));

		}

		@Override
		public int getItemCount() {
			return tasks.size();
		}

	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_task_list, container, false);
		recyclerView = view.findViewById(R.id.task_recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		setHasOptionsMenu(true);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		updateView();
	}

}
