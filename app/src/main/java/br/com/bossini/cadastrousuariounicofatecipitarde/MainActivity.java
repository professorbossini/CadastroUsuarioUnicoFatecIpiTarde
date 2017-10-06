package br.com.bossini.cadastrousuariounicofatecipitarde;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private TextView nomeTextView, foneTextView, emailTextView;
    private FirebaseDatabase database;
    private DatabaseReference usuarioReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        nomeTextView = (TextView) findViewById(R.id.nomeTextView);
        foneTextView = (TextView) findViewById(R.id.foneTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void atualizaCamposVisuais (){
        nomeTextView.setText(Usuario.getInstance().getNome());
        foneTextView.setText(Usuario.getInstance().getFone());
        emailTextView.setText(Usuario.getInstance().getEmail());
    }


}
