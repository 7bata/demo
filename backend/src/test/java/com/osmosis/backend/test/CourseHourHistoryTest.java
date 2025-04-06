package com.osmosis.backend.test;

import com.osmosis.backend.entity.ClassHourRecord;
import com.osmosis.backend.entity.CourseHourHistory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CourseHourHistoryTest {
    public static void main(String[] args) {
        System.out.println("Testing CourseHourHistory class date handling...");
        
        // Test with a valid date from database
        ClassHourRecord record1 = new ClassHourRecord();
        LocalDate databaseDate = LocalDate.of(2023, 9, 15);
        record1.setClassDate(databaseDate);
        
        CourseHourHistory history1 = new CourseHourHistory();
        LocalDate recordDate1 = record1.getClassDate();
        if (recordDate1 != null) {
            history1.setDate(recordDate1);
            history1.setFormattedDate(recordDate1.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            System.out.println("Record 1: Database date = " + databaseDate);
            System.out.println("Record 1: History date = " + history1.getDate());
            System.out.println("Record 1: Formatted date = " + history1.getFormattedDate());
        }
        
        // Test with null date from database, but with default date setup
        ClassHourRecord record2 = new ClassHourRecord();
        record2.setClassDate(null);
        
        CourseHourHistory history2 = new CourseHourHistory();
        LocalDate recordDate2 = record2.getClassDate();
        if (recordDate2 != null) {
            history2.setDate(recordDate2);
            history2.setFormattedDate(recordDate2.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        } else {
            // Use default date (2023-09-15)
            LocalDate defaultDate = LocalDate.of(2023, 9, 15);
            history2.setDate(defaultDate);
            history2.setFormattedDate(defaultDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            System.out.println("Record 2: Database date = null");
            System.out.println("Record 2: Using default date = " + defaultDate);
            System.out.println("Record 2: History date = " + history2.getDate());
            System.out.println("Record 2: Formatted date = " + history2.getFormattedDate());
        }
    }
} 