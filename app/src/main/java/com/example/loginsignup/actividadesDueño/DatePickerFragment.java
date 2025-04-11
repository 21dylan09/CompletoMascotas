package com.example.loginsignup.actividadesDueño;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

public class DatePickerFragment {

    public static void showDatePicker(Context context, final EditText editText) {
        final Calendar calendario = Calendar.getInstance();

        int anio = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    // Formato: YYYY-MM-DD
                    String fechaSeleccionada = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editText.setText(fechaSeleccionada);
                }, anio, mes, dia);
        datePickerDialog.show();
    }
}
