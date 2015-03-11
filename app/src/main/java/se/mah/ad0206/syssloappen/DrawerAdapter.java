package se.mah.ad0206.syssloappen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by Sebastian Aspegren, Jonas Dahlström on 2015-03-09.
 * A drawer for a wicked sick menu with animations.
 */
public class DrawerAdapter extends ArrayAdapter<String> {
    private String[] pages;

    /**
     * Constructor. require the activity and a string with the titles of the fragments.
     * @param context
     *              the main activity.
     * @param pages
     *              the names of the fragments in a way the user would understand them.
     */
    public DrawerAdapter(Context context, String[] pages) {
        super(context, R.layout.draweritem,pages);
        this.pages=pages;
    }

    /**
     * The getView method.
     * @param position
     *                  The position of the view.
     * @param convertView
     *                  The view..
     * @param parent
     *          The ViewGroup.
     * @return
     *          the view.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        //init viewHolder.
        ViewHolder holder = new ViewHolder();
        if(convertView==null) {
            //inflate the layout.
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.draweritem, parent, false);
            holder.item = (TextView) convertView.findViewById(R.id.DrawerItem);
            convertView.setTag(holder);

        }else{
            holder=(ViewHolder) convertView.getTag();
        }

        holder.item.setText(pages[position]);
        return convertView;
    }


    /**
     * A ViewHolder class containing a textView.
     */
    private static class ViewHolder {
            TextView item;
    }
    }
