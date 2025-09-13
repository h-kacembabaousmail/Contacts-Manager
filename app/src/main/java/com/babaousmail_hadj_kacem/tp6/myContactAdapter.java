package com.babaousmail_hadj_kacem.tp6;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class myContactAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<Contact> contacts;

    public myContactAdapter(Context context, ArrayList<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.contact_layout, null);

        final Contact contact = contacts.get(position);
        TextView name = convertView.findViewById(R.id.contact_name);
        TextView tel  = convertView.findViewById(R.id.contact_telNum);
        name.setText(contact.getName());
        tel.setText(contact.getTel());

        ImageView btnCall = convertView.findViewById(R.id.call_button);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.getTel()));
                context.startActivity(dial);
            }
        });

        // bouton de modification (ouvre une boîte de dialogue)
        ImageButton editBtn = convertView.findViewById(R.id.edit_button);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Modify Contact");
                final EditText inputName = new EditText(context);
                inputName.setText(contact.getName());
                final EditText inputTel  = new EditText(context);
                inputTel.setText(contact.getTel());
                LinearLayout layout = new LinearLayout(context);
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setPadding(16,16,16,16);
                layout.addView(inputName);
                layout.addView(inputTel);
                builder.setView(layout);

                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newName = inputName.getText().toString().trim();
                        String newTel  = inputTel.getText().toString().trim();
                        if (newName.isEmpty() || newTel.isEmpty()) {
                            Toast.makeText(context, "Les deux champs sont obligatoires", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ContactsDb db = new ContactsDb(context, "contacts.db");
                        db.updateContact(contact.getTel(), newName, newTel);
                        contact.setName(newName);
                        contact.setTel(newTel);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Contact modifié", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", null); // annulation
                builder.show();
            }
        });

        // bouton de suppression
        ImageButton deleteBtn = convertView.findViewById(R.id.delete_button);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactsDb db = new ContactsDb(context, "contacts.db");
                db.deleteContactByTel(contact.getTel());
                contacts.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Contact supprimé", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}