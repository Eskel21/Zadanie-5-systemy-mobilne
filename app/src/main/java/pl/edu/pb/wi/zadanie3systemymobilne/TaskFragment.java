package pl.edu.pb.wi.zadanie3systemymobilne;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class TaskFragment extends Fragment {
	private EditText nameField;
	private CheckBox doneCheckBox;
	private Task task;

	private EditText dateField;
	private Spinner categorySpinner;

	public static final String ARG_TASK_ID="task_id";

	private final Calendar calendar = Calendar.getInstance();

	public static TaskFragment newInstance(UUID taskId){
		Bundle bundle = new Bundle();
		bundle.putSerializable(ARG_TASK_ID, taskId);
		TaskFragment taskFragment = new TaskFragment();
		taskFragment.setArguments(bundle);
		return taskFragment;
	}
	private void setupDateFieldValue(Date date){
		Locale locale = new Locale("pl","PL");
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy",locale);
		dateField.setText(dateFormat.format(date));
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
		task = TaskStorage.getInstance().getTask(taskId);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate(R.layout.fragment_task, container, false);
		setHasOptionsMenu(true);
		nameField = view.findViewById(R.id.task_name);
		doneCheckBox = view.findViewById(R.id.task_done);
		dateField = view.findViewById(R.id.task_date);
		DatePickerDialog.OnDateSetListener date = (view12, year, month, day) -> {
			calendar.set(Calendar.YEAR, year);
			calendar.set(Calendar.MONTH, month);
			calendar.set(Calendar.DAY_OF_MONTH, day);
			setupDateFieldValue(calendar.getTime());
			task.setDate(calendar.getTime());
		};

		dateField.setOnClickListener(view1 ->
				new DatePickerDialog(getContext(), date, calendar.get(Calendar.YEAR),
						calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
						.show());
		setupDateFieldValue(task.getDate());

		nameField.setText(task.getName());

		doneCheckBox.setChecked(task.isDone());

		nameField.addTextChangedListener(new TextWatcher(){
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after){

			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count){
				task.setName(s.toString());
			}
			@Override
			public void afterTextChanged(Editable s){
				String taskName = s.toString();
				task.setName(taskName);
			}
		});
		categorySpinner = view.findViewById(R.id.task_category);
		categorySpinner.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_item, Category.values()));
		categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
				task.setCategory(Category.values()[position]);
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent){

			}
		});
		categorySpinner.setSelection(task.getCategory().ordinal());
		doneCheckBox.setChecked(task.isDone());
		doneCheckBox.setOnCheckedChangeListener((buttonView, isChecked)->{task.setDone(isChecked);}
		);

		return view;
	}
}
