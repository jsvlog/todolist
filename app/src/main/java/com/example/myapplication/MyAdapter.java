package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;

public class MyAdapter extends FirebaseRecyclerAdapter<MyModel,MyAdapter.myViewholder> {

    private String key = "";
    private String task;
    private String description;
    private Context context;
    private DatabaseReference reference;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String onlineUserID;

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyAdapter(@NonNull FirebaseRecyclerOptions<MyModel> options, Context context) {
        super(options);
        this.context = context;
    }



    @Override
    protected void onBindViewHolder(@NonNull myViewholder holder, @SuppressLint("RecyclerView") int position, @NonNull MyModel myModel) {
        holder.task.setText(myModel.getTask());
        holder.description.setText(myModel.getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = getRef(holder.getAdapterPosition()).getKey();
                task = myModel.getTask();
                description = myModel.getDescription();

                updateTask();
            }
        });
    }

    private void updateTask() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.update_data,null);
        myDialog.setView(view);

        AlertDialog dialog = myDialog.create();
        dialog.show();

        EditText mTask = view.findViewById(R.id.taskUpdate);
        EditText mDescription = view.findViewById(R.id.descriptionUpdate);
        Button deleteButton = view.findViewById(R.id.deleteUpdate);
        Button updateButton = view.findViewById(R.id.updateUpdate);

        mTask.setText(task);
        mTask.setSelection(task.length());

        mDescription.setText(description);
        mTask.setSelection(task.length());

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task = mTask.getText().toString().trim();
                description = mDescription.getText().toString().trim();

                String gdate = DateFormat.getInstance().format(new Date());

                MyModel myModel = new MyModel(task,description,key,gdate);

                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                onlineUserID = mUser.getUid();
                reference = FirebaseDatabase.getInstance().getReference().child("task").child(onlineUserID);

                reference.child(key).setValue(myModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Updated Sucessfuly", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else {
                            Toast.makeText(context, "failed to update", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                mAuth = FirebaseAuth.getInstance();
                mUser = mAuth.getCurrentUser();
                onlineUserID = mUser.getUid();
                reference = FirebaseDatabase.getInstance().getReference().child("task").child(onlineUserID);

                reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "Deleted Sucessfuly", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(context, "failed to delete", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    @NonNull
    @Override
    public myViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.retrieved_layout,parent,false);
        return new myViewholder(v);
    }

    public class myViewholder extends RecyclerView.ViewHolder {
        TextView task, description;
        public myViewholder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.taskCardview);
            description = itemView.findViewById(R.id.descriptionCardview);
        }
    }
}
