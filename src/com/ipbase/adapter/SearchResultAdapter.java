package com.ipbase.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ipbase.R;
import com.ipbase.bean.Article;

/**
 * 搜索结果
 * @author 李先华
 *2015年11月4日下午5:11:57
 */
public class SearchResultAdapter extends BaseAdapter {

	private Context mContext;
	private ArrayList<Article> mArticles;
	private LayoutInflater mlayoutInflater;


	public SearchResultAdapter(Context context, ArrayList<Article> mArticles) {
		this.mContext = context;
		this.mArticles = mArticles;
		mlayoutInflater = (LayoutInflater) mContext
				.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
	}

	@Override
	public int getCount() {
		return mArticles.size();
	}

	public  Object getItem(int position) {
		return mArticles.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) 
		{
			convertView = mlayoutInflater.inflate(R.layout.fragment_search_result_item, null);
			viewHolder = new ViewHolder();
			viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_search_result_title);
			viewHolder.tvAuthor = (TextView) convertView.findViewById(R.id.tv_search_result_article_author);
			viewHolder.tvPublishTime = (TextView) convertView.findViewById(R.id.tv_search_result_article_publish_time);
			viewHolder.tvDigest = (TextView) convertView.findViewById(R.id.tv_search_result_digest);
			convertView.setTag(viewHolder);
		} 
		else 
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		//赋值
		viewHolder.tvTitle.setText(mArticles.get(position).getTitle());
		viewHolder.tvAuthor.setText(mArticles.get(position).getAuthor());
		viewHolder.tvPublishTime.setText(mArticles.get(position).getCreatetime());
		viewHolder.tvDigest.setText(Html.fromHtml(mArticles.get(position).getContent()));
		
		return convertView;
	}

	public static class ViewHolder
	{
		public TextView tvTitle;
		public TextView tvAuthor;
		public TextView tvPublishTime;
		public TextView tvDigest;
	}



}
