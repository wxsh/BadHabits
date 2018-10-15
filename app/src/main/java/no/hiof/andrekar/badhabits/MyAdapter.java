package no.hiof.andrekar.badhabits;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import model.Habit;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private Context mContext;

    public MyAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.habitName.setText(Habit.habits.get(position).getTitle().toString());
        holder.habitDescription.setText(Habit.habits.get(position).getDescription().toString());

        //TODO: reminder
        //holder.habitGoal.setText(Habit.habits.get(position).getGoal());


        if (Habit.habits.get(position).getIsFavourite()){
            holder.favoriteButton.setImageResource(R.drawable.star_on);
        }
        else {
            holder.favoriteButton.setImageResource(R.drawable.star_off);
        }


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: finish onClikListner
                Log.d(TAG, "onClick: clicked on: " + Habit.habits.get(position).getTitle());
                Toast.makeText(mContext, Habit.habits.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton imgB = view.findViewById(R.id.favoriteBtn);

                if (Habit.habits.get(position).getIsFavourite()){
                    Habit.habits.get(position).setFavourite(false);
                    imgB.setImageResource(R.drawable.star_off);
                }
                else {
                    Habit.habits.get(position).setFavourite(true);
                    imgB.setImageResource(R.drawable.star_on);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return Habit.habits.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView habitName;
        TextView habitGoal;
        TextView habitDescription;
        ImageButton favoriteButton;
        RelativeLayout parentLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            habitName = itemView.findViewById(R.id.habit_name);
            habitGoal = itemView.findViewById(R.id.habit_goal);
            habitDescription = itemView.findViewById(R.id.habit_description);
            favoriteButton = itemView.findViewById(R.id.favoriteBtn);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
