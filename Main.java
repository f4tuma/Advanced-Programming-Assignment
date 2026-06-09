// QUESTION 4 - Client Testing Code

public class Main {
    public static void main(String[] args) {
        System.out.println("=== STARTING BANK TRANSACTION SYSTEM TESTS ===\n");

        // Set up test account
        BankAccount myAccount = new BankAccount(500.0);
        System.out.println("Initial Bank Account Balance: $" + myAccount.getBalance());
        System.out.println("------------------------------------------------");

        // 1. Test Deposit Transaction
        System.out.println("\n--- Testing Deposit Transaction ---");
        BaseTransaction deposit = new DepositTransaction(200.0); [cite: 52]
        try {
            deposit.apply(myAccount); [cite: 52]
            deposit.printTransactionDetails();
            System.out.println("New Account Balance: $" + myAccount.getBalance());
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }

        // 2. Test Successful Withdrawal Transaction
        System.out.println("\n--- Testing Successful Withdrawal Transaction ---");
        WithdrawalTransaction withdrawal1 = new WithdrawalTransaction(150.0); [cite: 52]
        try {
            withdrawal1.apply(myAccount); [cite: 52]
            withdrawal1.printTransactionDetails();
            System.out.println("New Account Balance: $" + myAccount.getBalance());
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }

        // 3. Test Question 2: Withdrawal Reversal
        System.out.println("\n--- Testing Question 2: Reversal of Withdrawal ---");
        withdrawal1.reverse();
        System.out.println("Account Balance after reversal: $" + myAccount.getBalance());

        // 4. Test Question 3: Exception Handling (Standard apply throwing Exception)
        System.out.println("\n--- Testing Question 3: Exception Throwing (Insufficient Funds) ---");
        WithdrawalTransaction expensiveWithdrawal = new WithdrawalTransaction(2000.0);
        try {
            expensiveWithdrawal.apply(myAccount); // This will fail and throw the exception
        } catch (InsufficientFundsException e) {
            System.out.println("Successfully caught expected exception: " + e.getMessage());
        }

        // 5. Test Question 3: Overloaded apply() with Partial Withdrawal (Try/Catch/Finally)
        System.out.println("\n--- Testing Question 3: Overloaded Partial Withdrawal Method ---");
        // Balance is currently $700. Attempting to withdraw $1000.
        WithdrawalTransaction partialWithdrawal = new WithdrawalTransaction(1000.0);
        partialWithdrawal.apply(myAccount, true); // True activates the partial withdrawal catch block logic
        partialWithdrawal.printTransactionDetails();
        System.out.println("Final Account Balance: $" + myAccount.getBalance());

        // 6. Test Hint requirement: Polymorphism & Type Casting
        System.out.println("\n--- Testing Question 4 Hint: Type Casting to Base Object ---");
        WithdrawalTransaction basicW = new WithdrawalTransaction(50.0); [cite: 52]
        
        // Polymorphically mapping subtype object to base type object via type casting
        BaseTransaction baseRef = (BaseTransaction) basicW; [cite: 53]
        
        try {
            // This runs the overriding apply method due to late binding polymorphism
            baseRef.apply(myAccount); [cite: 54]
            baseRef.printTransactionDetails();
        } catch (InsufficientFundsException e) {
            System.out.println(e.getMessage());
        }
    }
}