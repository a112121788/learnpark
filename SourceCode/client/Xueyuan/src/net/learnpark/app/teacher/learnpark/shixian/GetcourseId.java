package net.learnpark.app.teacher.learnpark.shixian;

public class GetcourseId {

	public int GetmycourseId(int day,String timebegin, String timeend) {

		// 开始时间
		String[] mytime_begin = timebegin.split(":");
		int myhour_begin = Integer.valueOf(mytime_begin[0]);
		//int myminute_begin = Integer.valueOf(mytime_begin[1]);
		// 结束时间
		//String[] mytime_end = timeend.split(":");
		//int myhour_end = Integer.valueOf(mytime_end[0]);
		//int myminute_end = Integer.valueOf(mytime_end[1]);
		
		int moiningstart=8;
		int afternonestart=14;
		//int summinute = (myhour_end * 60 + myminute_end)- (myhour_begin * 60 + myminute_begin);
		
		
		if (myhour_begin < (moiningstart+1)) {
			return (day-1)*8+1;
		}else if (myhour_begin>=(moiningstart+1)&&myhour_begin<(moiningstart+2)) {
			return (day-1)*8+2;
		}else if (myhour_begin>=(moiningstart+2)&&myhour_begin<(moiningstart+3)) {
			return (day-1)*8+3;
		}else if (myhour_begin>=(moiningstart+3)&&myhour_begin<(moiningstart+4)) {
			return (day-1)*8+4;
		}else if (myhour_begin<(afternonestart+1)) {
			return (day-1)*8+5;
		}else if (myhour_begin>=(afternonestart+1)&&myhour_begin<(afternonestart+2)) {
			return (day-1)*8+6;
		}else if (myhour_begin>=(afternonestart+2)&&myhour_begin<(afternonestart+3)) {
			return (day-1)*8+7;
		}else if (myhour_begin>=(afternonestart+3)&&myhour_begin<(afternonestart+4)) {
			return (day-1)*8+8;
		}
		return 0;
	}
}
