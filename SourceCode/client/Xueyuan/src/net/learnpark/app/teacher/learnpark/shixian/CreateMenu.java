package net.learnpark.app.teacher.learnpark.shixian;

import net.learnpark.app.teacher.learnpark.R;
import android.support.v4.view.MenuItemCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

/**
 * MainActivity中菜单的创建
 * 
 * @author 张晓午
 * 
 */
public class CreateMenu {
	/**
	 * MainActivity中菜单的创建
	 * 
	 * @param menu
	 */
	public CreateMenu(Menu menu) {
		Menu mmenu = menu;

		SubMenu subMenu0 = mmenu.addSubMenu("添加");

		// 在子菜单里添加按钮
		subMenu0.add(0, 7, 7, "添加课程").setIcon(R.drawable.course);
		MenuItem subMenu0Item1 = subMenu0.getItem();
		subMenu0Item1
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu0Item1,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);

		subMenu0.add(0, 8, 8, "添加班级").setIcon(R.drawable.ic_addclasses);
		MenuItem subMenu0Item2 = subMenu0.getItem();
		subMenu0Item2
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu0Item2,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);

		subMenu0.add(0, 9, 9, "添加学生信息").setIcon(R.drawable.ic_addstudents);
		MenuItem subMenu0Item3 = subMenu0.getItem();
		subMenu0Item3
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu0Item3,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		
		subMenu0.add(0, 10, 10, "导入学生信息").setIcon(R.drawable.ic_loadcourses);
		MenuItem subMenu0Item4 = subMenu0.getItem();
		subMenu0Item4
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu0Item4,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);


		// 设置一个子菜单显示，这样可以显示图标和图片
		//SubMenu subMenu1 = mmenu.addSubMenu("更多");
		SubMenu subMenu1 = mmenu.addSubMenu(0, 11, 11, "更多");
		// 在子菜单里添加按钮
		subMenu1.add(0, 1, 1, "个人中心").setIcon(R.drawable.ic_user);
		MenuItem subMenu1Item1 = subMenu1.getItem();
		subMenu1Item1
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu1Item1,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);

		subMenu1.add(0, 2, 2, "上传签到信息").setIcon(R.drawable.ic_upload_qiandao);
		MenuItem subMenu1Item2 = subMenu1.getItem();
		subMenu1Item2
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu1Item2,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		
		subMenu1.add(0, 3, 3, "发送通知").setIcon(R.drawable.ic_messages);
		MenuItem subMenu1Item3 = subMenu1.getItem();
		subMenu1Item3
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu1Item3,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		
		subMenu1.add(0, 4, 4, "分享").setIcon(R.drawable.ic_share);
		MenuItem subMenu1Item4 = subMenu1.getItem();
		subMenu1Item4
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu1Item4,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);

		subMenu1.add(0, 5, 5, "设置").setIcon(R.drawable.ic_menu_setting);
		MenuItem subMenu1Item5 = subMenu1.getItem();
		subMenu1Item5
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu1Item5,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);

		subMenu1.add(0, 6, 6, "意见反馈").setIcon(R.drawable.ic_suggestion);
		MenuItem subMenu1Item6 = subMenu1.getItem();
		subMenu1Item6
				.setIcon(R.drawable.abc_ic_menu_moreoverflow_normal_holo_dark);
		MenuItemCompat.setShowAsAction(subMenu1Item6,
				MenuItemCompat.SHOW_AS_ACTION_ALWAYS
						| MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
		// 设置子菜单栏的添加按钮
		subMenu0.setIcon(R.drawable.ic_submenu_add);
		subMenu1.setIcon(R.drawable.ic_submenu_more);
	}

}
