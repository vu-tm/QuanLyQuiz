// xử lý ngày tháng
package helper;

import java.text.*;
import java.util.*;

public class DateHelper {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    
    public static String toString(Date date) {
        return date != null ? sdf.format(date) : "";
    }
    
    public static Date toDate(String dateStr) {
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static java.sql.Date toSqlDate(Date date) {
        return date != null ? new java.sql.Date(date.getTime()) : null;
    }
}