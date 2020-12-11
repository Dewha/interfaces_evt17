package com.example.eswallet;

public class RecordsHelperClass {
    String sum, category, day, month, year, comment;

    public RecordsHelperClass(String sum, String category, String day, String month, String year, String comment) {
        this.sum = sum;
        this.category = category;
        this.day = day;
        this.month = month;
        this.year = year;
        this.comment = comment;
    }

    public RecordsHelperClass() {
    }

    public String getSum() {
        return sum;
    }

    public String getCategory() {
        return category;
    }

    public String getComment() {
        return comment;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }
}
