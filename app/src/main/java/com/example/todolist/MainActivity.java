package com.example.todolist;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todolist.databinding.ActivityMainBinding;
import com.example.todolist.validator.EmptyValidator;
import com.example.todolist.validator.ValidateResult;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.sidesheet.SideSheetBehavior;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private Disposable startTimeDisposable, endTimeDisposable, dateDisposable;
    TextInputEditText startTimeET,endTimeET,dateET,taskNameET;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        We use binder here
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

         startTimeET = (TextInputEditText) binding.startTimeSelectorInput.getEditText();
         endTimeET = (TextInputEditText) binding.endTimeSelectorInput.getEditText();
         dateET = (TextInputEditText) binding.dateSelectorInput.getEditText();
         taskNameET = (TextInputEditText) binding.taskNameInput.getEditText();

        List<Card> cardList = new ArrayList<>();
        Date calendar = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        binding.dateText.setText(simpleDateFormat.format(calendar));

        // Create a new BottomSheetBehavior
        BottomSheetBehavior<FrameLayout> bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet);
        // Hide the bottom sheet, we don't want to show it on start
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setDraggable(false);
//        Create the recyclerView cards adapter
        CardAdapter cardAdapter = new CardAdapter(cardList, getApplicationContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(cardAdapter);

//        Create the side Sheet manager
        SideSheetBehavior<FrameLayout> sideSheetBehavior = SideSheetBehavior.from(binding.sideSheet);
        sideSheetBehavior.setState(SideSheetBehavior.STATE_HIDDEN);

        binding.FAB.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            binding.FAB.hide();
        });

        binding.createBtn.setOnClickListener(v -> {
            boolean isValid = true;

            String taskName = Objects.requireNonNull(Objects.requireNonNull(taskNameET).getText()).toString();
            String date = Objects.requireNonNull(Objects.requireNonNull(dateET).getText()).toString();
            String startTime = Objects.requireNonNull(Objects.requireNonNull(startTimeET).getText()).toString();
            String endTime = Objects.requireNonNull(Objects.requireNonNull(endTimeET).getText()).toString();

            ValidateResult tasknameEmptyValidation = new EmptyValidator(taskName).validate();
            if (!tasknameEmptyValidation.isSuccess()) {
                binding.taskNameInput.setErrorEnabled(true);
                binding.taskNameInput.setError(tasknameEmptyValidation.getErr());
                isValid = false;
            }

            ValidateResult dateEmptyValidation = new EmptyValidator(date).validate();
            if (!tasknameEmptyValidation.isSuccess()) {
                binding.dateSelectorInput.setErrorEnabled(true);
                binding.dateSelectorInput.setError(dateEmptyValidation.getErr());
                isValid = false;
            }

            ValidateResult startTimeValidation = new EmptyValidator(startTime).validate();
            if (!tasknameEmptyValidation.isSuccess()) {
                binding.startTimeSelectorInput.setErrorEnabled(true);
                binding.startTimeSelectorInput.setError(startTimeValidation.getErr());
                isValid = false;
            }

            ValidateResult endTimeValidation = new EmptyValidator(endTime).validate();
            if (!tasknameEmptyValidation.isSuccess()) {
                binding.endTimeSelectorInput.setErrorEnabled(true);
                binding.endTimeSelectorInput.setError(endTimeValidation.getErr());
                isValid = false;
            }

            if (isValid) {
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                cardAdapter.updateData(new Card(taskName, date, startTime, endTime));
                binding.FAB.show();

                resetBottomSheetContent();
            }
        });


            Objects.requireNonNull(startTimeET).setOnFocusChangeListener((v, hasFocus) -> {
                if (hasFocus) {
                    // Dispose of the previous disposable if it exists
                    dispose(startTimeDisposable);

                    startTimeDisposable = showMaterialTimePicker().subscribe(
                            startTimeET::setText,
                            System.err::println
                    );
                }
            });


        Objects.requireNonNull(endTimeET).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                // Dispose of the previous disposable if it exists
                dispose(endTimeDisposable);

                endTimeDisposable = showMaterialTimePicker().subscribe(
                        endTimeET::setText,
                        System.err::println
                );
            }
        });

        Objects.requireNonNull(binding.dateSelectorInput.getEditText()).setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                dispose(dateDisposable);

                dateDisposable = showDatePicker().subscribe(
                        chosenDate -> binding.dateSelectorInput.getEditText().setText(chosenDate),
                        System.err::println
                );
            }
        });

        binding.menuButton.setOnClickListener(v -> sideSheetBehavior.setState(SideSheetBehavior.STATE_EXPANDED));
        binding.closeButton.setOnClickListener(v -> sideSheetBehavior.setState(SideSheetBehavior.STATE_HIDDEN));

        binding.deleteAllBtn.setOnClickListener(v -> cardAdapter.deleteAll());

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                cardAdapter.removeItem(pos);
            }
        }).attachToRecyclerView(binding.recyclerView);
    }

    public void dispose(Disposable disposable) {
        if (disposable != null) {
            disposable.dispose();
        }
    }
    public Observable<String> showDatePicker() {
        PublishSubject<String> chosenDate = PublishSubject.create();

        MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Pick a date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build();

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(selection));
            chosenDate.onNext(date);
            chosenDate.onComplete();
        });

        materialDatePicker.show(getSupportFragmentManager(), "data-picker");

        return chosenDate;
    }

    public Observable<String> showMaterialTimePicker() {
        PublishSubject<String> chosenTimeSubject = PublishSubject.create();

        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(12)
                .setMinute(10)
                .setTitleText("Select task start time")
                .build();

        materialTimePicker.addOnPositiveButtonClickListener(v1 -> {
            @SuppressLint("DefaultLocale") String chosenTime = String.format("%02d:%02d", materialTimePicker.getHour(), materialTimePicker.getMinute());
            chosenTimeSubject.onNext(chosenTime);
            chosenTimeSubject.onComplete(); // Optional: Complete the observable after emitting the value
        });

        materialTimePicker.show(getSupportFragmentManager(), "time picker");

        return chosenTimeSubject;
    }

    public void resetBottomSheetContent(){
        startTimeET.setText(null);
        endTimeET.setText(null);
        taskNameET.setText(null);
        dateET.setText(null);

        startTimeET.clearFocus();
        endTimeET.clearFocus();
        taskNameET.clearFocus();
        dateET.clearFocus();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (!startTimeDisposable.isDisposed()) {
            startTimeDisposable.dispose();
        }

        if (!endTimeDisposable.isDisposed()) {
            endTimeDisposable.dispose();
        }
    }
}