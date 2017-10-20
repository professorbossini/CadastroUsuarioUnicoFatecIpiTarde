package br.com.bossini.cadastrousuariounicofatecipitarde;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView nomeTextView, foneTextView, emailTextView;
    private ImageView fotoImageView;
    private FirebaseDatabase database;
    private DatabaseReference usuarioReference;
    private StorageReference rootReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nomeTextView = (TextView) findViewById(R.id.nomeTextView);
        foneTextView = (TextView) findViewById(R.id.foneTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        fotoImageView = (ImageView) findViewById(R.id.fotoImageView);
        database = FirebaseDatabase.getInstance();
        usuarioReference = database.getReference("usuario");
        usuarioReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                if (usuario != null){
                    Usuario.getInstance().setNome(usuario.getNome());
                    Usuario.getInstance().setFone(usuario.getFone());
                    Usuario.getInstance().setEmail((usuario.getEmail()));
                    atualizaCamposVisuais();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        rootReference = FirebaseStorage.getInstance().getReference();
        //baixar foto
        StorageReference fotoUserReference = rootReference.child("img/foto.png");
        try{
            final File arquivo = File.createTempFile("img", "foto.png");
            fotoUserReference.getFile(arquivo).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(MainActivity.this, getString(R.string.download_ok), Toast.LENGTH_SHORT).show();
                    Bitmap foto = BitmapFactory.decodeFile(arquivo.getPath());
                    fotoImageView.setImageBitmap(foto);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, getString(R.string.download_falhou), Toast.LENGTH_SHORT).show();
                }
            });

        }
        catch (IOException e){
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.download_falhou), Toast.LENGTH_SHORT).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, EditarUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }

    private void atualizaCamposVisuais (){
        nomeTextView.setText(Usuario.getInstance().getNome());
        foneTextView.setText(Usuario.getInstance().getFone());
        emailTextView.setText(Usuario.getInstance().getEmail());
        if (Usuario.getInstance().getFoto() != null)
            fotoImageView.setImageBitmap(Usuario.getInstance().getFoto());
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaCamposVisuais ();
    }
}
