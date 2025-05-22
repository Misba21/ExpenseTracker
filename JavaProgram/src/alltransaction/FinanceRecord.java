package alltransaction;

import java.time.LocalDate;

public class FinanceRecord {
    private LocalDate date;
    private String type;
    private String category;
    private double amount;
    
    public FinanceRecord(LocalDate date, String type, String category, double amount)
    {
    	this.date=date;
    	this.type=type;
    	this.category=category;
    	this.amount=amount;
    }
    public LocalDate getDate()
    {
    	return date;
    }
    public String getType()
    {
    	return type;
    }
    public String getcategory()
    {
    	return category;
    }
    public double getamount()
    {
    	return amount;
    }
    public String toString() {
        return date + " | " + type + " | " + category + " | ₹" + amount;
    }
}
