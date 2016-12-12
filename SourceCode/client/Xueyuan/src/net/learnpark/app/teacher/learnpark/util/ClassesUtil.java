package net.learnpark.app.teacher.learnpark.util;

import java.util.List;

import android.content.Context;

import net.learnpark.app.teacher.learnpark.shixian.Classes;
import net.learnpark.app.teacher.learnpark.shixian.Students;
import net.tsz.afinal.FinalDb;

public class ClassesUtil {

	public static List<Classes> getClassesList(Context context) {
		List<Classes> classes_all;
		FinalDb classesDb = FinalDb.create(context);
		classes_all = classesDb.findAll(Classes.class);

		return classes_all;
	}

	public static Boolean insertClasses(Context context, Classes classes) {
		List<Classes> classes_all;
		FinalDb classesDb = FinalDb.create(context);
		classes_all = classesDb.findAll(Classes.class);
		if (classes_all != null && classes_all.size() > 0) {
			for (int i = 0; i < classes_all.size(); i++) {
				if (classes_all.get(i).getClassname()
						.equals(classes.getClassname())
						&& classes_all.get(i).getCoursename()
								.equals(classes.getCoursename())) {
					return true;
				} else if (i == classes_all.size() - 1) {
					classesDb.save(classes);
					return true;
				}
			}
		}else {
			classesDb.save(classes);
			return true;
		}
		return false;
	}
	
	public static void deleteClasses(Context context,Classes classes){
		FinalDb classesDb = FinalDb.create(context);
		classesDb.deleteByWhere(Classes.class, "classname='"+classes.getClassname()+"' and coursename='"+classes.getCoursename()+"'");
	}

}
