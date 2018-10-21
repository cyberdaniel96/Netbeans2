
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel
 */
public class Testing {

    public static void main(String[] args) {
        try {
            //Dates to compare
            String CurrentDate = "1/1/2018";
            String FinalDate = "10/21/2018";

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("MM/dd/yyyy");

            //Setting dates
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(FinalDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            int totalDays = (int) differenceDates + 1;
        //Convert long to String
            // System.out.println(totalDays);

            // System.out.println(getNumberOfMonthDay(2,"2018"));
            System.out.println(LocalDate.now().toString());
            Calendar cal = Calendar.getInstance();
            cal.set(2018,10,21);
            System.out.println(cal.get(cal.YEAR));
            
        } catch (Exception exception) {

        }
    }

    public static int getNumberOfMonthDay(int month, String year) throws Exception {
        int totalMonthDay = 0;
        for (int i = 1; i <= month; i++) {
            String date = "1/" + Integer.toString(i) + "/" + year;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date convertedDate = dateFormat.parse(date);
            Calendar c = Calendar.getInstance();
            c.setTime(convertedDate);
            totalMonthDay = totalMonthDay + c.getActualMaximum(Calendar.DAY_OF_MONTH);
        }

        return totalMonthDay;
    }
    
    public static boolean isSameDay(Calendar app, LocalDate current) {
       return false;
    }

     public static boolean isToday(Calendar appointmentCal) {
        return isSameDay(appointmentCal, LocalDate.now());
    }

    
    public static boolean isFirstDayofMonth(Calendar calendar) {
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar cannot be null.");
        }

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        return dayOfMonth == 1;
    }

  
}
