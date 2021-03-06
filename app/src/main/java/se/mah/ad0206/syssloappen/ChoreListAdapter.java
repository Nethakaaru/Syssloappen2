package se.mah.ad0206.syssloappen;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Sebastian Aspegren on 2015-03-05.
 *
 * A custom adapter for the list of the chores containing a textView for the points and one for the chore.
 */
public class ChoreListAdapter extends ArrayAdapter<String>{
    private ArrayList<String> chores = new ArrayList<>();
    private ArrayList<String> points = new ArrayList<>();
    private Context context;

    /**
     * Constructor
     * @param context
     *      The main activity.
     * @param chores
     *      An array with all the chores.
     * @param points
     *      An array with all the points for the chores.
     */
    public ChoreListAdapter(Activity context, ArrayList<String> chores, ArrayList<String> points) {
        super(context, R.layout.row, chores);
        this.chores = chores;
        this.points = points;
        this.context = context;
    }

    /**
     * A method that puts views into the list with the proper information.
     * @param position
     *      position the view has in the list.
     * @param view
     *      the view.
     * @param parent
     *      ViewGroup for inflating.
     * @return
     *      the view.
     */
    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder = new ViewHolder();
        if(view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.row, parent, false);
            holder.choreName = (TextView) view.findViewById(R.id.LVTextViewChore);
            holder.chorePoints = (TextView) view.findViewById(R.id.LVTextViewPoints);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.choreName.setText(chores.get(position));
        holder.chorePoints.setText(context.getResources().getString(R.string.points) + " " + points.get(position));

        return view;
    }

    /**
     * ViewHolder for increased performance.
     */
    private static class ViewHolder {
        TextView choreName;
        TextView chorePoints;
    }
}
