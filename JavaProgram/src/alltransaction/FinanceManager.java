package alltransaction;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.time.YearMonth;
import java.util.stream.Collectors;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class FinanceManager {

	static Scanner sc = new Scanner(System.in);
	static List<FinanceRecord> records = new ArrayList<>();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
     boolean exit = false;
     
     while(!exit)
     {
    	 System.out.println("\n==== Expense Tracker Menu ====");
    	 System.out.println("1. Add Income");
    	 System.out.println("2. Add Expense");
    	 System.out.println("3. View All Records");
    	 System.out.println("4. Show Monthly Summary");
    	 System.out.println("5. Save to File");
    	 System.out.println("6. Load from File");
    	 System.out.println("7. Exit");
    	 System.out.print("Choose an option: ");
    	 
    	 int choice = sc.nextInt();
    	 sc.nextLine();
    	 switch (choice) {
         case 1:
             addRecord("Income");
             break;
         case 2:
             addRecord("Expense");
             break;
         case 3:
             viewRecords();
             break;
         case 4:
        	 showMonthlySummary();
         case 5:
             System.out.print("Enter file name to save: ");
             String saveFile = sc.next();
             saveToFile(saveFile);
             break;
         case 6:
             System.out.print("Enter file name to load: ");
             String loadFile = sc.next();
             loadFromFile(loadFile);
             break;
         case 7:
             exit = true;
             System.out.println("Exiting... Thank you!");
             break;
         default:
             System.out.println("Invalid option. Try again.");
     }
     }
	}
	static void addRecord(String type) {
        System.out.print("Enter category (" + (type.equals("Income") ? "Salary/Business" : "Food/Rent/Travel") + "): ");
        String category = sc.nextLine();

        System.out.print("Enter amount: ₹");
        double amount = sc.nextDouble();
        sc.nextLine();

        LocalDate today = LocalDate.now();

        FinanceRecord record = new FinanceRecord(today, type, category, amount);
        records.add(record);

        System.out.println("✅ " + type + " record added successfully!");
	}
	static void viewRecords() {
        System.out.println("\n==== All Transactions ====");
        if (records.isEmpty()) {
            System.out.println("No records yet.");
        } else {
            for (FinanceRecord r : records) {
                System.out.println(r);
            }
        }
    }
	static void showMonthlySummary()
	{
		if(records.isEmpty())
		{
			System.out.println("No records to summarize. ");
			return;
		}
		Map<YearMonth, List<FinanceRecord>> recordsByMonth = records.stream()
				.collect(Collectors.groupingBy(r -> YearMonth.from(r.getDate())));
		
		for(YearMonth month : recordsByMonth.keySet())
		{
			List<FinanceRecord> monthlyRecords = recordsByMonth.get(month);
			
			double totalIncome = monthlyRecords.stream()
					.filter(r -> r.getType().equalsIgnoreCase("Income"))
			        .mapToDouble(FinanceRecord::getamount)
			        .sum();
			double totalExpense = monthlyRecords.stream()
			                .filter(r -> r.getType().equalsIgnoreCase("Expense"))
			                .mapToDouble(FinanceRecord::getamount)
			                .sum();

			        System.out.println("\nSummary for " + month);
			        System.out.println("Total Income: ₹" + totalIncome);
			        System.out.println("Total Expense: ₹" + totalExpense);

			        // Breakdown by category
			        System.out.println("Category-wise Expense:");
			        Map<String, Double> expenseByCategory = monthlyRecords.stream()
			                .filter(r -> r.getType().equalsIgnoreCase("Expense"))
			                .collect(Collectors.groupingBy(FinanceRecord::getcategory,
			                        Collectors.summingDouble(FinanceRecord::getamount)));

			        for (String category : expenseByCategory.keySet()) {
			            System.out.println("  " + category + ": ₹" + expenseByCategory.get(category));
			        }
			    }        
	}
	static void saveToFile(String filename) {
	    try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
	        for (FinanceRecord record : records) {
	            writer.println(record.getDate() + "," + record.getType() + "," +
	                    record.getcategory() + "," + record.getamount());
	        }
	        System.out.println("✅ Data saved to file: " + filename);
	    } catch (IOException e) {
	        System.out.println("❌ Error saving to file: " + e.getMessage());
	    }
	}
	static void loadFromFile(String filename) {
	    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
	        String line;
	        while ((line = reader.readLine()) != null) {
	            String[] parts = line.split(",");
	            if (parts.length == 4) {
	                LocalDate date = LocalDate.parse(parts[0]);
	                String type = parts[1];
	                String category = parts[2];
	                double amount = Double.parseDouble(parts[3]);

	                records.add(new FinanceRecord(date, type, category, amount));
	            }
	        }
	        System.out.println("✅ Data loaded from file: " + filename);
	    } catch (IOException e) {
	        System.out.println("❌ Error loading from file: " + e.getMessage());
	    }
	}

}
