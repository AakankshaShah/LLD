import java.util.*;


enum ExpenseSplitType {
    EQUAL,
    UNEQUAL,
    PERCENTAGE
}

interface ExpenseSplit {
    void split(Expense expense);
}
class User {
    int userId;
    String userName;
    List<Group> groups = new ArrayList<>();
    private UserExpenseBalanceSheet userExpenseBalanceSheet = new UserExpenseBalanceSheet();

    public UserExpenseBalanceSheet getUserExpenseBalanceSheet() {
        return userExpenseBalanceSheet;
    }
}

class Group {
    int groupId;
    String groupName;
    int amount;
    List<Expense> expenses = new ArrayList<>();
}

class Expense {
    int expenseId;
    String expenseName;
    User spender;
    List<User> spendees;
    ExpenseSplitType splitType;
    List<Split> splitDetails = new ArrayList<>();
    double totalAmount;
}

class Split {
    User user;
    double amountOwe;

    public Split(User user, double amountOwe) {
        this.user = user;
        this.amountOwe = amountOwe;
    }
}
class SplitFactory {
    ExpenseSplit getSplitObject(ExpenseSplitType splitType) {
        switch (splitType) {
            case EQUAL: return new EqualExpenseSplit();
            case UNEQUAL: return new UnequalExpenseSplit();
            case PERCENTAGE: return new PercentageExpenseSplit();
            default: return null;
        }
    }
}
class EqualExpenseSplit implements ExpenseSplit {
    public void split(Expense expense) {
        double perUserShare = expense.totalAmount / expense.spendees.size();
        for (User user : expense.spendees) {
            expense.splitDetails.add(new Split(user, perUserShare));
        }
    }
}
class UnequalExpenseSplit implements ExpenseSplit {
    public void split(Expense expense) {
        // Assume splitDetails are pre-filled with custom values
    }
}

class PercentageExpenseSplit implements ExpenseSplit {
    public void split(Expense expense) {
        // Assume splitDetails are pre-filled with % amounts
    }
}
class UserExpenseBalanceSheet {
    private double totalYourExpense;
    private double totalYouGetBack;
    private double totalYouOwe;
    private double totalPayment;
    private Map<String, Balance> userVsBalance = new HashMap<>();

    public double getTotalYourExpense() { return totalYourExpense; }
    public void setTotalYourExpense(double totalYourExpense) { this.totalYourExpense = totalYourExpense; }

    public double getTotalYouGetBack() { return totalYouGetBack; }
    public void setTotalYouGetBack(double totalYouGetBack) { this.totalYouGetBack = totalYouGetBack; }

    public double getTotalYouOwe() { return totalYouOwe; }
    public void setTotalYouOwe(double totalYouOwe) { this.totalYouOwe = totalYouOwe; }

    public double getTotalPayment() { return totalPayment; }
    public void setTotalPayment(double totalPayment) { this.totalPayment = totalPayment; }

    public Map<String, Balance> getUserVsBalance() { return userVsBalance; }
}
class Balance {
    private double amountGetBack;
    private double amountOwe;

    public double getAmountGetBack() { return amountGetBack; }
    public void setAmountGetBack(double amountGetBack) { this.amountGetBack = amountGetBack; }

    public double getAmountOwe() { return amountOwe; }
    public void setAmountOwe(double amountOwe) { this.amountOwe = amountOwe; }
}



 class BalanceSheetController {

    public void updateUserExpenseBalanceSheet(User expensePaidBy, List<Split> splits, double totalExpenseAmount){

        //update the total amount paid of the expense paid by user
        UserExpenseBalanceSheet paidByUserExpenseSheet = expensePaidBy.getUserExpenseBalanceSheet();
        paidByUserExpenseSheet.setTotalPayment(paidByUserExpenseSheet.getTotalPayment() + totalExpenseAmount);

        for(Split split : splits) {

            User userOwe = split.getUser();
            UserExpenseBalanceSheet oweUserExpenseSheet = userOwe.getUserExpenseBalanceSheet();
            double oweAmount = split.getAmountOwe();

            if(expensePaidBy.getUserId().equals(userOwe.getUserId())){
                paidByUserExpenseSheet.setTotalYourExpense(paidByUserExpenseSheet.getTotalYourExpense()+oweAmount);
            }
            else {

                //update the balance of paid user
                paidByUserExpenseSheet.setTotalYouGetBack(paidByUserExpenseSheet.getTotalYouGetBack() + oweAmount);

                Balance userOweBalance;
                if(paidByUserExpenseSheet.getUserVsBalance().containsKey(userOwe.getUserId())) {

                    userOweBalance = paidByUserExpenseSheet.getUserVsBalance().get(userOwe.getUserId());
                }
                else {
                    userOweBalance = new Balance();
                    paidByUserExpenseSheet.getUserVsBalance().put(userOwe.getUserId(), userOweBalance);
                }

                userOweBalance.setAmountGetBack(userOweBalance.getAmountGetBack() + oweAmount);


                //update the balance sheet of owe user
                oweUserExpenseSheet.setTotalYouOwe(oweUserExpenseSheet.getTotalYouOwe() + oweAmount);
                oweUserExpenseSheet.setTotalYourExpense(oweUserExpenseSheet.getTotalYourExpense() + oweAmount);

                Balance userPaidBalance;
                if(oweUserExpenseSheet.getUserVsBalance().containsKey(expensePaidBy.getUserId())){
                    userPaidBalance = oweUserExpenseSheet.getUserVsBalance().get(expensePaidBy.getUserId());
                }
                else{
                    userPaidBalance = new Balance();
                    oweUserExpenseSheet.getUserVsBalance().put(expensePaidBy.getUserId(), userPaidBalance);
                }
                userPaidBalance.setAmountOwe(userPaidBalance.getAmountOwe() + oweAmount);
            }
        }
    }

    public void showBalanceSheetOfUser(User user){

        System.out.println("---------------------------------------");

        System.out.println("Balance sheet of user : " + user.getUserId());

        UserExpenseBalanceSheet userExpenseBalanceSheet =  user.getUserExpenseBalanceSheet();

        System.out.println("TotalYourExpense: " + userExpenseBalanceSheet.getTotalYourExpense());
        System.out.println("TotalGetBack: " + userExpenseBalanceSheet.getTotalYouGetBack());
        System.out.println("TotalYourOwe: " + userExpenseBalanceSheet.getTotalYouOwe());
        System.out.println("TotalPaymnetMade: " + userExpenseBalanceSheet.getTotalPayment());
        for(Map.Entry<String, Balance> entry : userExpenseBalanceSheet.getUserVsBalance().entrySet()){

            String userID = entry.getKey();
            Balance balance = entry.getValue();

            System.out.println("userID:" + userID + " YouGetBack:" + balance.getAmountGetBack() + " YouOwe:" + balance.getAmountOwe());
        }

        System.out.println("---------------------------------------");

    }
}


public class Splitwise {
    public static void main(String[] args) {
        User u1 = new User(); u1.userId = 1; u1.userName = "Alice";
        User u2 = new User(); u2.userId = 2; u2.userName = "Bob";
        User u3 = new User(); u3.userId = 3; u3.userName = "Charlie";

        Expense expense = new Expense();
        expense.expenseId = 101;
        expense.expenseName = "Dinner";
        expense.spender = u1;
        expense.totalAmount = 300;
        expense.spendees = Arrays.asList(u1, u2, u3);
        expense.splitType = ExpenseSplitType.EQUAL;

        SplitFactory factory = new SplitFactory();
        ExpenseSplit splitter = factory.getSplitObject(expense.splitType);
        splitter.split(expense);

        for (Split s : expense.splitDetails) {
            System.out.println(s.user.userName + " owes: ₹" + s.amountOwe);
        }

        // 🔁 Balance Sheet Update
        BalanceSheetController controller = new BalanceSheetController();
        controller.updateUserExpenseBalanceSheet(u1, expense.splitDetails, expense.totalAmount);

        // ✅ Print Balance Sheets
        controller.showBalanceSheetOfUser(u1);
        controller.showBalanceSheetOfUser(u2);
        controller.showBalanceSheetOfUser(u3);
    }
}

