package com.example.android.githubsearch;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class FastAdapter extends RecyclerView.Adapter<FastAdapter.FastAdapterViewHolder> {



    final private ListItemClickListener onClickListener;

    private String[] data;

    public interface ListItemClickListener {
        void onListItemClick(String url);
    }

    public FastAdapter(ListItemClickListener listener) {
        onClickListener = listener;
    }

    public class FastAdapterViewHolder extends RecyclerView.ViewHolder  implements OnClickListener {

        // Return the list items #
        TextView listItemNumberView;


        public final TextView itemTextView;

        public FastAdapterViewHolder(View view) {
            super(view);
            itemTextView = (TextView) view.findViewById(R.id.item_number);
            listItemNumberView = (TextView) itemView.findViewById(R.id.item_number);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                int clickedPosition = getAdapterPosition();
                String temp = data[clickedPosition];
                //String lines[] = temp.split("\\r?\\n");

                String lines[] = temp.split("URL: ");
                String url = lines[1];
                onClickListener.onListItemClick(url);
        }

    }

    @Override
    public FastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.display_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new FastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FastAdapterViewHolder fastAdapterViewHolder, int position) {
        String singleRespository = data[position];
        fastAdapterViewHolder.itemTextView.setText(singleRespository);
    }

    @Override
    public int getItemCount() {
        if (null == data) return 0;
        return data.length;
    }

    public void setRespository(String[] newData) {
        data = newData;
        notifyDataSetChanged();
    }

}