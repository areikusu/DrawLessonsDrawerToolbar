package app.draw_lessons.com.drawlessonsdrawertoolbar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

/**
 * Created by Aleix Casanova on 1/2/15.
 */
public class AdapterDrawer extends RecyclerView.Adapter<adapter_curso.MyViewHolder> {

	private ArrayList<itemDrawer> itemsDataset;
	private webservice wb = new webservice();
	private Context context;

	public AdapterDrawer(ArrayList<itemDrawer> element, Context context) {
		this.itemsDataset = element;
		this.context = context;
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {

		TextView tv_item_drawer;
		ImageView icon;

		public MyViewHolder(View itemView) {
			super(itemView);

			this.tv_item_drawer = (TextView) itemView.findViewById(R.id.tv_nombre_curso);
			this.icon = (ImageView) itemView.findViewById(R.id.img_curso);
		}
	}


	@Override
	public adapter_curso.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
		View v = LayoutInflater.from(viewGroup.getContext())
				.inflate(R.layout.item_drawer, viewGroup, false);

		adapter_curso.MyViewHolder vh = new adapter_curso.MyViewHolder(v);
		return vh;
	}

	@Override
	public void onBindViewHolder(adapter_curso.MyViewHolder holder, int listPosition) {
		TextView tv_nombre_curso = holder.tv_nombre_curso;
		ImageView img_curso = holder.img_curso;

		tv_nombre_curso.setText(itemsDataset.get(listPosition).getTittle());
		int icDrawer = itemsDataset.get(listPosition).getIcon();

		img_curso.setImageResource(icDrawer);
	}


	@Override
	public int getItemCount() {
		return itemsDataset.size();
	}
}
