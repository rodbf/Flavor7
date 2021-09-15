package com.flavorplus.support.ticket;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.flavorplus.R;

public class TicketCloseFragment extends DialogFragment {
    public interface TicketCloseDialogListener{
        void onConfirm(int solved);
    }

    private RadioGroup solvedRadioGroup;
    private Button btConfirm;
    private TicketCloseDialogListener listener;
    private int state = 0;

    public TicketCloseFragment(TicketCloseDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final CharSequence[] charSequence = new CharSequence[]{"Sim", "Não"};
        builder.setTitle("Seu problema foi resolvido?").setSingleChoiceItems(charSequence, state, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                state = which;
                Toast.makeText(requireContext(), charSequence[which], Toast.LENGTH_SHORT).show();
            }
        }).setPositiveButton("Fechar ticket", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onConfirm((state-(charSequence.length-1))*(-1));
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        return builder.create();
    }
}
