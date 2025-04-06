package com.osmosis.backend.test;

import com.osmosis.backend.entity.ClassHourRecord;
import java.time.LocalDate;

public class ClassHourRecordTest {
    public static void main(String[] args) {
        // Test with date
        ClassHourRecord record1 = new ClassHourRecord();
        LocalDate oldDate = LocalDate.of(2023, 9, 15);
        record1.setClassDate(oldDate);
        System.out.println("Record 1 date set to 2023-09-15");
        System.out.println("getClassDate() returns: " + record1.getClassDate());
        
        // Test with null date
        ClassHourRecord record2 = new ClassHourRecord();
        record2.setClassDate(null);
        System.out.println("Record 2 date set to null");
        System.out.println("getClassDate() returns: " + record2.getClassDate() + " (should be null now)");
        
        // Output current date for reference
        System.out.println("Current date: " + LocalDate.now());
    }
} 