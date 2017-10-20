package br.com.bossini.cadastrousuariounicofatecipitarde;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class EditarUsuarioActivity extends AppCompatActivity {

    private static final int REQUISICAO_CAMERA = 743;
    private FirebaseDatabase database;
    private DatabaseReference userReference;
    private StorageReference rootReference;
    private EditText nomeEditText, foneEditText, emailEditText;
    private ImageView fotoEditarImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_usuario);
        nomeEditText = (EditText) findViewById(R.id.nomeEditText);
        foneEditText = (EditText) findViewById(R.id.foneEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        fotoEditarImageView = (ImageView) findViewById(R.id.fotoEditarImageView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = nomeEditText.getEditableText().toString();
                String fone = foneEditText.getEditableText().toString();
                String email = emailEditText.getEditableText().toString();
                Usuario.getInstance().setNome(nome);
                Usuario.getInstance().setFone(fone);
                Usuario.getInstance().setEmail(email);
                userReference.setValue(Usuario.getInstance());
                Toast.makeText(EditarUsuarioActivity.this, getString(R.string.dados_atualizados), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        database = FirebaseDatabase.getInstance();
        userReference = database.getReference("usuario");
        rootReference = FirebaseStorage.getInstance().getReference();

    }


    public void tirarFoto (View view){
        Intent intent = new Intent (MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUISICAO_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUISICAO_CAMERA){
            if (resultCode == Activity.RESULT_OK){
                Bundle b = intent.getExtras();
                Bitmap foto = (Bitmap) b.get("data");
                fotoEditarImageView.setImageBitmap(foto);
                Usuario.getInstance().setFoto(foto);
                //salvar no Firebase
                StorageReference fotoUserReference = rootReference.child("img/foto.png");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                foto.compress(Bitmap.CompressFormat.PNG, 0, baos);
                byte [] vetor =  baos.toByteArray();
                fotoUserReference.putBytes(vetor).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(EditarUsuarioActivity.this, getString(R.string.upload_ok), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void atualizaComponentesVisuais (){
        nomeEditText.setText(Usuario.getInstance().getNome());
        foneEditText.setText(Usuario.getInstance().getFone());
        emailEditText.setText(Usuario.getInstance().getEmail());
        if (Usuario.getInstance().getFoto() != null)
            fotoEditarImageView.setImageBitmap(Usuario.getInstance().getFoto());
    }
    @Override
    protected void onResume() {
        super.onResume();
        atualizaComponentesVisuais();
    }
}
