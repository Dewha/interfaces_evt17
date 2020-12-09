package com.example.eswallet;

public class RecordsHelperClass {
    String sum, category, date, comment;

    public RecordsHelperClass(String sum, String category, String date, String comment) {
        this.sum = sum;
        this.category = category;
        this.date = date;
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

    public String getDate() {
        return date;
    }

    public String getComment() {
        return comment;
    }
}
