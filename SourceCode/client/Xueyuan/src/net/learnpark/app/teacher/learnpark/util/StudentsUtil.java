package net.learnpark.app.teacher.learnpark.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import net.learnpark.app.teacher.learnpark.shixian.Students;
import net.tsz.afinal.FinalDb;

public class StudentsUtil {

	public static List<Students> getStudentsList(Context context,
			String classname) {
		List<Students> students_all;
		List<Students> students_class = new ArrayList<Students>();
		FinalDb studentdDb = FinalDb.create(context);
		students_all = studentdDb.findAll(Students.class);
		if (students_all != null && students_all.size() > 0) {
			for (int i = 0; i < students_all.size(); i++) {
				if (students_all.get(i).getClassname().equals(classname)) {
					students_class.add(students_all.get(i));
				}
			}
		}
		return students_class;
	}

	//添加一组学生
	public static Boolean insertStudents(Context context,
			List<Students> students_list) {
		List<Students> students_all;
		FinalDb studentsDb = FinalDb.create(context);
		students_all = studentsDb.findAll(Students.class);
		for (int i = 0; i < students_list.size(); i++) {
			if (students_all != null && students_all.size() > 0) {
				for (int j = 0; j < students_all.size(); j++) {
					if (students_list.get(i).getStudentnum()
							.equals(students_all.get(j).getStudentnum())) {
						break;
					} else if (j == students_all.size() - 1) {
						Students students = new Students();
						students.setStudentname(students_list.get(i).getStudentname());
						students.setStudentnum(students_list.get(i).getStudentnum());
						students.setSex(students_list.get(i).getSex());
						students.setClassname(students_list.get(i)
								.getClassname());
						studentsDb.save(students);
						break;
					}

				}
			} else {
				Students students = new Students();
				students.setStudentname(students_list.get(i).getStudentname());
				students.setStudentnum(students_list.get(i).getStudentnum());
				students.setSex(students_list.get(i).getSex());
				students.setClassname(students_list.get(i)
						.getClassname());
				studentsDb.save(students);
			}
			if (i==students_list.size()-1) {
				return true;
			}
		}
		return false;
	}

	//添加单个学生
	public static int insertStudents(Context context,Students students){
		List<Students> students_all;
		FinalDb studentsDb = FinalDb.create(context);
		students_all = studentsDb.findAll(Students.class);
		if (students_all!=null&&students_all.size()>0) {
			for (int i = 0; i < students_all.size(); i++) {
				if (students.getStudentnum().equals(students_all.get(i).getStudentnum())) {
					return 2;
				}else if (i==students_all.size()-1) {
					studentsDb.save(students);
					return 1;
				}
			}
		}else {
			studentsDb.save(students);
			return 1;
		}
		return 0;
	}
	
	public static int getStudentsSum(Context context, String classname) {
		int stusum = 0;
		List<Students> students_all;
		FinalDb studentdDb = FinalDb.create(context);
		students_all = studentdDb.findAll(Students.class);
		if (students_all != null && students_all.size() > 0) {
			for (int i = 0; i < students_all.size(); i++) {
				if (students_all.get(i).getClassname().equals(classname)) {
					stusum = stusum + 1;
				}
			}
		}
		return stusum;

	}
	
	public static void modifyStudentMessages(Context context,Students students){
		FinalDb studentdDb = FinalDb.create(context);
		//这里只是根据学号修改，所以有局限性
//        studentdDb.deleteByWhere(Students.class, "studentnum'000002'");
//		studentdDb.save(students);
		studentdDb.update(students, "studentnum='"+students.getStudentnum()+"'");
	}

	public static void deleteStudent(Context context,Students students){
		FinalDb studentdDb = FinalDb.create(context);
		//这里只是根据学号删除，所以有局限性
		studentdDb.deleteByWhere(Students.class, "studentnum='"+students.getStudentnum()+"'");
	}
	
}
