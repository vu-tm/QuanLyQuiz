/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.Component;

import java.math.BigDecimal;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.Comparator;
import java.util.Date;

/**
 *
 * @author DELL
 */
public class TableSorter {
    public static boolean isValueEmpty(Object o) {
        if (o == null) return true;
        String s = o.toString().trim();
        return s.isEmpty() || s.equals("---");
    }

    private static int nullCheck(Object o1, Object o2) {
        if (o1 == o2) return 0;

        boolean empty1 = isValueEmpty(o1);
        boolean empty2 = isValueEmpty(o2);

        if (empty1 && empty2) return 0;
        if (empty1) return 1;
        if (empty2) return -1;

        return 2;
    }

    public static final Comparator<Object> STRING_COMPARATOR = (o1, o2) -> {
        int res = nullCheck(o1, o2);
        return (res != 2) ? res : o1.toString().compareToIgnoreCase(o2.toString());
    };

    public static final Comparator<Object> DATE_COMPARATOR = (o1, o2) -> {
        int res = nullCheck(o1, o2);
        return (res != 2) ? res : ((Date) o1).compareTo((Date) o2);
    };

    public static final Comparator<Object> INTEGER_COMPARATOR = (o1, o2) -> {
        int res = nullCheck(o1, o2);
        if (res != 2) {
            return res;
        }
        try {
            return Integer.valueOf(o1.toString()).compareTo(Integer.valueOf(o2.toString()));
        } catch (Exception e) {
            return 0;
        }
    };

    public static final Comparator<Object> DOUBLE_COMPARATOR = (o1, o2) -> {
        int res = nullCheck(o1, o2);
        return (res != 2) ? res : ((Double) o1).compareTo((Double) o2);
    };

    public static final Comparator<Object> BIG_DECIMAL_COMPARATOR = (o1, o2) -> {
        String s1 = (o1 == null) ? "" : o1.toString().trim();
        String s2 = (o2 == null) ? "" : o2.toString().trim();

        try {
            return new BigDecimal(s1).compareTo(new BigDecimal(s2));
        } catch (Exception e) {
            return 0;
        }
    };

    public static void configureTableColumnSorter(JTable table, int columnIndex, Comparator<Object> comparator) {
        DefaultTableModel tblModel = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = (TableRowSorter<DefaultTableModel>) table.getRowSorter();
        if (sorter == null) {
            sorter = new TableRowSorter<>(tblModel);
            table.setRowSorter(sorter);
        }
        sorter.setComparator(columnIndex, comparator);
    }
}
