package Service;

import java.util.Calendar;
import java.util.Date;

public class DateService {

    public int betweenDates(Date earlyDate, Date laterDate) {
        long diff = laterDate.getTime() - earlyDate.getTime();
        long days = diff / (24 * 60 * 60 * 1000);
        return (int)(days);
    }

    public Date setDate(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        //Since january is 0
        calendar.set(Calendar.MONTH, month-1 );
        calendar.set(Calendar.DATE, day);
        return calendar.getTime();
    }
}
