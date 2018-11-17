package no.hiof.andrekar.badhabits;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import model.DateHabit;
import model.EconomicHabit;
import model.Habit;
import model.SaveData;

public class MyFavoriteAdapter extends RecyclerView.Adapter<MyFavoriteAdapter.ViewHolder>{

    private static final String TAG = "RecyclerViewAdapter";

    private String failedAmount = "";

    public int fav = 0;


    private Context mContext;

        private boolean mHaveFavorite;
        private Point size;
        private ViewHolder holder;

    private SharedPreferences sharedPref;

    public MyFavoriteAdapter(Context context) {
            mContext = context;
            sharedPref =
                PreferenceManager.getDefaultSharedPreferences(mContext);
        }


        @Override
        public MyFavoriteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_favorite_listitem, parent, false);
            MyFavoriteAdapter.ViewHolder holder = new MyFavoriteAdapter.ViewHolder(view);

            String userTheme = sharedPref.getString("key_theme", "");

            if (userTheme.equals("Light")){
                holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(ThemeColors.COLOR_PRIMARY_DARK));

            }
            else if (userTheme.equals("Dark")){
                holder.cardView.setCardBackgroundColor(mContext.getResources().getColor(ThemeColors.COLOR_ACENT));
                holder.habitName.setBackgroundResource(ThemeColors.COLOR_ACENT);
                holder.habitName.setTextColor(mContext.getResources().getColor(ThemeColors.EDIT_TEXT_COLOR));

                holder.habitGoal.setBackgroundResource(ThemeColors.COLOR_ACENT);
                holder.habitGoal.setTextColor(mContext.getResources().getColor(ThemeColors.EDIT_TEXT_COLOR));

                holder.habitDescription.setBackgroundResource(ThemeColors.COLOR_ACENT);
                holder.habitDescription.setTextColor(mContext.getResources().getColor(ThemeColors.EDIT_TEXT_COLOR));
                //holder.habitGoal.setTextColor(R.attr.colorPrimary);

                //holder.cardView.setBackgroundColor(mContext.getResources().getColor(R.color.primaryColorDark));
                //holder.parentLayout.setBackgroundResource(R.color.colorPrimaryDark);
            }

            return holder;
        }

        //TODO: fix some bugs, like clickable when empty
        //DONE: Empty item view does not show if you remove the last favourite.

    @Override
        public void onBindViewHolder(final MyFavoriteAdapter.ViewHolder holder, final int position) {

        if(position == 0) {
            mHaveFavorite = Habit.getHaveFavorite();
        }

        fav = Habit.getNumFavourites();


        String currency = sharedPref.getString
                (SettingsActivity.KEY_PREF_CURRENCY, "");



            if (mHaveFavorite) {
                holder.parentLayout.setClickable(true);
                holder.emptyFav.setVisibility(View.GONE);
                holder.favoriteButton.setVisibility(View.VISIBLE);
                holder.habitDescription.setVisibility(View.VISIBLE);
                holder.habitGoal.setVisibility(View.VISIBLE);
                holder.habitName.setVisibility(View.VISIBLE);
                holder.failedButton.setVisibility(View.VISIBLE);

                WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);

                if (!Habit.habits.get(position).getIsFavourite()) {
                    holder.itemView.setVisibility(View.GONE);
                    holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                } else {
                    holder.itemView.setVisibility(View.VISIBLE);
                    holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    holder.habitName.setWidth((int)(size.x/1.3) );


                    if (fav == 1) {
                        holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        holder.habitName.setWidth((int)(size.x/1.3) );
                    } else if (fav == 2) {
                        holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams((int)(size.x /1.5), ViewGroup.LayoutParams.MATCH_PARENT));
                        holder.habitName.setWidth((int)(holder.itemView.getLayoutParams().width/1.5) );
                    } else if (fav >= 3) {
                        holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams((int)(size.x /2.2), ViewGroup.LayoutParams.MATCH_PARENT));
                        holder.habitName.setWidth((int)(holder.itemView.getLayoutParams().width/1.7) );
                    }
                    // */
                    holder.habitName.setText(Habit.habits.get(position).getTitle().toString());

                    holder.habitDescription.setText(Habit.habits.get(position).getDescription().toString());

                    //>DONE: DONE
                    if (Habit.habits.get(position).getClass() == EconomicHabit.class) {
                        holder.habitGoal.setText(Float.toString(((EconomicHabit) Habit.habits.get(position)).getProgress()) + " " + currency);
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
                                saveData.saveData(Habit.habits.get(position), 2);
                            } else if (Habit.habits.get(position).getClass() == EconomicHabit.class) {
                                saveData.saveData(Habit.habits.get(position), 1);
                            }

                            //Collections.sort(Habit.habits, Habit.HabitComparator);
                            MainActivity.adapter.notifyItemChanged(position);
                            //Collections.sort(Habit.habits, Habit.FavComparator_Help);
                            MainActivity.favAdapter.updateFavs();
                            MainActivity.favAdapter.notifyDataSetChanged();
                        }
                    });

                    holder.failedButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final Habit habit = Habit.habits.get(position);

                            if (habit instanceof EconomicHabit) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("Don't worry, even if you fail, you can still do this! How much did you spend?");
                                final EditText input = new EditText(view.getContext());
                                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_CLASS_NUMBER);
                                builder.setView(input);

                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SaveData saveData = new SaveData();
                                        failedAmount = input.getText().toString();
                                        ((EconomicHabit) habit).increaseFailedTotal(Integer.parseInt(failedAmount));
                                        habit.setFailDate(new Date().getTime());
                                        saveData.saveData(Habit.habits.get(position), 1);
                                        notifyDataSetChanged();
                                        MainActivity.updateBottomSheet();
                                    }
                                });
                                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                AlertDialog ecoAlert = builder.create();
                                ecoAlert.show();

                            } else if (habit instanceof DateHabit) {


                                //DONE: editButton onclick
                                final AlertDialog.Builder failedBuilder = new AlertDialog.Builder(view.getContext());
                                failedBuilder.setMessage("Don't worry, even if you fail, you can still do this! Do you want to reset the days since last fail?").setTitle("Failed habit?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        Date currentTime = Calendar.getInstance().getTime();
                                        habit.setFailDate(currentTime.getTime());
                                        SaveData saveData = new SaveData();
                                        saveData.saveData(Habit.habits.get(position), 2);

                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // CANCEL AND DO NOTHING
                                    }
                                });
                                // Create the AlertDialog object and return it
                                AlertDialog dialog = failedBuilder.create();
                                //ecoAlert.show();
                                dialog.show();
                                //dialog.show();
                            }
                        }
                    });
                }
            }
            else if (position == 0) {
                holder.parentLayout.setClickable(false);
                holder.emptyFav.setVisibility(View.VISIBLE);
                holder.itemView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                holder.favoriteButton.setVisibility(View.GONE);
                holder.habitDescription.setVisibility(View.GONE);
                holder.habitGoal.setVisibility(View.GONE);
                holder.habitName.setVisibility(View.GONE);
                holder.failedButton.setVisibility(View.GONE);
            }
            else {
                holder.emptyFav.setVisibility(View.GONE);
                holder.favoriteButton.setVisibility(View.GONE);
                holder.habitDescription.setVisibility(View.GONE);
                holder.habitGoal.setVisibility(View.GONE);
                holder.habitName.setVisibility(View.GONE);
                holder.failedButton.setVisibility(View.GONE);
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
            ImageButton failedButton;
            RelativeLayout parentLayout;
            CardView cardView;

            TextView emptyFav;

            public ViewHolder(View itemView) {
                super(itemView);
                habitName = itemView.findViewById(R.id.fav_habit_name);
                habitGoal = itemView.findViewById(R.id.fav_habit_goal);
                habitDescription = itemView.findViewById(R.id.fav_habit_description);
                favoriteButton = itemView.findViewById(R.id.fav_favoriteBtn);
                parentLayout = itemView.findViewById(R.id.fav_parent_layout);
                failedButton = itemView.findViewById(R.id.fav_btn_habitFailed);
                cardView = itemView.findViewById(R.id.card_view);

                emptyFav = itemView.findViewById(R.id.empty_fav);


            }
        }

        public void add(int pos) {
            notifyItemInserted(pos);
        }

    public void updateFavs(){
        fav = Habit.getNumFavourites();
        mHaveFavorite = Habit.getHaveFavorite();
    }
}
