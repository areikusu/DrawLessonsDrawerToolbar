package app.draw_lessons.com.drawlessonsdrawertoolbar;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class DrawLessonsLauncher extends ActionBarActivity {
	DrawerLayout mDrawerLayout;
	RecyclerView mDrawerList;
	ActionBarDrawerToggle mDrawerToggle;
	String[] mDrawerListItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_draw_lessons_launcher);
		Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
		mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
		mDrawerList = (RecyclerView)findViewById(android.R.id.list);
		mDrawerListItems = getResources().getStringArray(R.array.drawer_list);
		mDrawerList.setAdapter(new RecyclerView.Adapter<String>(this, android.R.layout.simple_list_item_1, mDrawerListItems));
		mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int editedPosition = position+1;
				Toast.makeText(DrawLessonsLauncher.this, "You selected item " + editedPosition, Toast.LENGTH_SHORT).show();
				mDrawerLayout.closeDrawer(mDrawerList);
			}
		});mDrawerToggle = new ActionBarDrawerToggle(this,
				mDrawerLayout,
				toolbar,
				R.string.drawer_open,
				R.string.drawer_close){
			public void onDrawerClosed(View v){
				super.onDrawerClosed(v);
				invalidateOptionsMenu();
				syncState();
			}
			public void onDrawerOpened(View v){
				super.onDrawerOpened(v);
				invalidateOptionsMenu();
				syncState();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		mDrawerToggle.syncState();
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState){
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig){
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_draw_lessons_launcher, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()){
			case android.R.id.home: {
				if (mDrawerLayout.isDrawerOpen(mDrawerList)){
					mDrawerLayout.closeDrawer(mDrawerList);
				} else {
					mDrawerLayout.openDrawer(mDrawerList);
				}
				return true;
			}

			default: return super.onOptionsItemSelected(item);
	}
}
}
