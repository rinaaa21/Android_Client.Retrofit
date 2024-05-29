package com.example.android_client;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> userList = new ArrayList<>();
    private String[] opsiProdi = {"Teknik Informatika", "Sistem Informasi", "Desain Komunikasi Visual", "Manajemen Informatika", "Teknik Sipil",
                                    "Manajemen", "Akuntansi", "Bisnis Digital", "Ilmu Lingkungan", "Kehutanan"};
    private String[] opsiFakultas = {"Fakultas Ilmu Komputer", "Fakultas Ekonomi dan Bisnis", "Fakultas Kehutanan"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userAdapter = new UserAdapter(userList);
        recyclerView.setAdapter(userAdapter);

        findViewById(R.id.button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAddUserDialog();
            }
        });

        fetchUsers();
    }
    private void showAddUserDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add User");

        View view = getLayoutInflater().inflate(R.layout.dialog_add_user, null);
        final EditText editTextName = view.findViewById(R.id.editTextName);
        final EditText editTextEmail = view.findViewById(R.id.editTextEmail);
        final AutoCompleteTextView dataprodi = view.findViewById(R.id.dataprodi);
        ArrayAdapter<String> prodiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, opsiProdi);
        dataprodi.setAdapter(prodiAdapter);

        final EditText datalistfakultas = view.findViewById(R.id.datalistfakultas);
        datalistfakultas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFakultasList(datalistfakultas, dataprodi);
            }
        });

        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = editTextName.getText().toString();
                String email = editTextEmail.getText().toString();
                String prodi = dataprodi.getText().toString();
                String fakultas = datalistfakultas.getText().toString();
                addUser(name, email, prodi, fakultas);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    private void showFakultasList(final EditText fakultasEditText, final AutoCompleteTextView prodiAutoComplete) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Fakultas");

        final ListView fakultasListView = new ListView(this);
        ArrayAdapter<String> fakultasAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, opsiFakultas);
        fakultasListView.setAdapter(fakultasAdapter);

        builder.setView(fakultasListView);

        final AlertDialog dialog = builder.create();

        fakultasListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedFakultas = opsiFakultas[position];
                fakultasEditText.setText(selectedFakultas);
                setProdiBasedOnFakultas(selectedFakultas, prodiAutoComplete);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void setProdiBasedOnFakultas(String fakultas, AutoCompleteTextView prodiAutoComplete) {
        String[] prodiOptions;

        if (fakultas.equals("Fakultas Ilmu Komputer")) {
            prodiOptions = new String[]{"Teknik Informatika", "Sistem Informasi", "Manajemen Informatika", "Desain Komunikasi Visual", "Teknik Sipil"};
        } else if (fakultas.equals("Fakultas Ekonomi dan Bisnis")) {
            prodiOptions = new String[]{"Manajemen", "Akuntansi", "Bisnis Digital"};
        } else if (fakultas.equals("Fakultas Kehutanan")) {
            prodiOptions = new String[]{"Ilmu Lingkungan", "Kehutanan"};
        } else {
            prodiOptions = new String[0];
        }

        ArrayAdapter<String> prodiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, prodiOptions);
        prodiAutoComplete.setAdapter(prodiAdapter);
    }

    private void addUser(String name, String email, String prodi, String fakultas) {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        User user = new User(name, email, prodi, fakultas);
        Call<Void> call = apiService.insertUser(user);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "User added successfully",
                            Toast.LENGTH_SHORT).show();
                    fetchUsers();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to add user",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to add user",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUsers() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<User>> call = apiService.getUsers();

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response)
            {
                if (response.isSuccessful()) {
                    userList.clear();
                    userList.addAll(response.body());
                    userAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Failed to fetch users", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch users",
                        Toast.LENGTH_SHORT).show();
                Log.e("Rina"," "+t);
            }
        });
    }
}
