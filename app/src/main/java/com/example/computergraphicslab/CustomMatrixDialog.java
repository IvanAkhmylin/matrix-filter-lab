package com.example.computergraphicslab;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;



public class CustomMatrixDialog extends AppCompatDialogFragment {
    EditText editText_1,editText_2,editText_3,
            editText_4,editText_5,editText_6,
            editText_7,editText_8,editText_9;
    private DialogListener dialogListener;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_fragment,null);

        builder.setView(view)
                .setTitle("Custom Matrix Builder")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if ((editText_1.getText().toString().isEmpty()) || (editText_2.getText().toString().isEmpty()) || (editText_3.getText().toString().isEmpty())
                                || (editText_4.getText().toString().isEmpty()) || (editText_5.getText().toString().isEmpty()) || (editText_6.getText().toString().isEmpty())
                                || (editText_7.getText().toString().isEmpty()) || (editText_8.getText().toString().isEmpty())  || (editText_9.getText().toString().isEmpty()) ){
                            return ;
                        }
                        float[][] matrix  = new float[][]{
                                {Float.parseFloat(editText_1.getText().toString()),Float.parseFloat(editText_2.getText().toString()),Float.parseFloat(editText_3.getText().toString())},
                                {Float.parseFloat(editText_4.getText().toString()),Float.parseFloat(editText_5.getText().toString()),Float.parseFloat(editText_6.getText().toString())},
                                {Float.parseFloat(editText_7.getText().toString()),Float.parseFloat(editText_8.getText().toString()),Float.parseFloat(editText_9.getText().toString())},
                        };
                        dialogListener.applyMatrix(matrix);
                    }
                });
                editText_1 = view.findViewById(R.id.edit_1);
                editText_2 = view.findViewById(R.id.edit_2);
                editText_3 = view.findViewById(R.id.edit_3);
                editText_4 = view.findViewById(R.id.edit_4);
                editText_5 = view.findViewById(R.id.edit_5);
                editText_6 = view.findViewById(R.id.edit_6);
                editText_7 = view.findViewById(R.id.edit_7);
                editText_8 = view.findViewById(R.id.edit_8);
                editText_9 = view.findViewById(R.id.edit_9);
                return builder.create();
    }

    @Override
    public void onAttach(@  NonNull Context context) {
        super.onAttach(context);
        try {
            dialogListener = (DialogListener) context;
        } catch (ClassCastException e) {
           throw  new ClassCastException(context.toString() + "ERROR 404");
        }
    }

    public interface DialogListener{
        void applyMatrix(float[][] matrix);
    }
}
