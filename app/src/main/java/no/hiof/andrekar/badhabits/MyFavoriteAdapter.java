package no.hiof.andrekar.badhabits;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.ViewHolder>{

        private static final String TAG = "RecyclerViewAdapter";

        private Context mContext;

        private boolean mHaveFavorite;

    public MyFavoriteAdapter(Context context) {
            mContext = context;
        }

        @Override
        public MyFavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favorite_listitem, parent, false);
            MyFavoriteAdapter.ViewHolder holder = new MyFavoriteAdapter.ViewHolder(view);

            return holder;
        }

        //TODO: fix some bugs, like clickable when empty

        @Override
        public void onBindViewHolder(final MyFavoriteAdapter.ViewHolder holder, final int position) {

        if (position == 0){
            mHaveFavorite = Habit.getHaveFavorite();
        }

            if (mHaveFavorite) {
                holder.parentLayout.setClickable(true);
                holder.emptyFav.setVisibility(View.GONE);
                holder.favoriteButton.setVisibility(View.VISIBLE);
                holder.habitDescription.setVisibility(View.VISIBLE);
                holder.habitGoal.setVisibility(View.VISIBLE);
                holder.habitName.setVisibility(View.VISIBLE);


                if (!Habit.habits.get(position).getIsFavourite()) {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                } else {
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, 400));


                    if (position % 2 == 1)
                        holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.faveHabitBG));
                    else
                        holder.itemView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.faveHabitBG2));


                    holder.habitName.setText(Habit.habits.get(position).getTitle().toString());
                    holder.habitDescription.setText(Habit.habits.get(position).getDescription().toString());

                    //TODO: DONE
                    if (Habit.habits.get(position).getClass() == EconomicHabit.class) {
                        holder.habitGoal.setText(((EconomicHabit) Habit.habits.get(position)).getProgress());
                    } else if (Habit.habits.get(position).getClass() == DateHabit.class) {
                        holder.habitGoal.setText(((DateHabit) Habit.habits.get(position)).getDateGoal());
                    }

                    if (Habit.habits.get(position).getIsFavourite()) {
                        holder.favoriteButton.setImageResource(R.drawable.star_on);
                    } else {
                        holder.favoriteButton.setImageResource(R.drawable.star_off);
                    }


                    holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Log.d(TAG, "onClick: clicked on: " + Habit.habits.get(position).getTitle());
                            ShowHabitActivity.setCurrentNumber(position);
                            Intent intent = new Intent(mContext, ShowHabitActivity.class);
                            mContext.startActivity(intent);
                        }
                    });

                    holder.favoriteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            if (Habit.habits.get(position).getIsFavourite()) {
                                Habit.habits.get(position).setFavourite(false);
                                holder.favoriteButton.setImageResource(R.drawable.star_off);
                            } else {
                                Habit.habits.get(position).setFavourite(true);
                                holder.favoriteButton.setImageResource(R.drawable.star_on);
                            }
                            SaveData saveData = new SaveData();
                            if (Habit.habits.get(position).getClass() == DateHabit.class) {
                                saveData.updateData(Habit.habits.get(position), 2);
                            } else if (Habit.habits.get(position).getClass() == EconomicHabit.class) {
                                saveData.updateData(Habit.habits.get(position), 1);
                            }

                            Collections.sort(Habit.habits, Habit.HabitComparator);
                            MainActivity.updateRecyclerView();

                        }
                    });
                }
            }
            else if (position == 0) {
                holder.parentLayout.setClickable(false);
                holder.emptyFav.setVisibility(View.VISIBLE);
                holder.favoriteButton.setVisibility(View.GONE);
                holder.habitDescription.setVisibility(View.GONE);
                holder.habitGoal.setVisibility(View.GONE);
                holder.habitName.setVisibility(View.GONE);
            }
            else {
                holder.emptyFav.setVisibility(View.GONE);
                holder.favoriteButton.setVisibility(View.GONE);
                holder.habitDescription.setVisibility(View.GONE);
                holder.habitGoal.setVisibility(View.GONE);
                holder.habitName.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));

            }
        }


        @Override
        public int getItemCount() {

            return Habit.habits.size();
        }


        public static class ViewHolder extends RecyclerView.ViewHolder{

            TextView habitName;
            TextView habitGoal;
            TextView habitDescription;
            ImageButton favoriteButton;
            RelativeLayout parentLayout;

            TextView emptyFav;

            public ViewHolder(View itemView) {
                super(itemView);
                habitName = itemView.findViewById(R.id.fav_habit_name);
                habitGoal = itemView.findViewById(R.id.fav_habit_goal);
                habitDescription = itemView.findViewById(R.id.fav_habit_description);
                favoriteButton = itemView.findViewById(R.id.fav_favoriteBtn);
                parentLayout = itemView.findViewById(R.id.fav_parent_layout);

                emptyFav = itemView.findViewById(R.id.empty_fav);
            }
        }


}
