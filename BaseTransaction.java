import java.util.Calendar;
import java.util.UUID;

// QUESTION 1 - Extending Interface in Concrete Class (Base Class)

public class BaseTransaction implements TransactionInterface {
    protected double amount;
    protected Calendar date;
    protected String transactionID;
    protected boolean isApplied = false;

    public BaseTransaction(double amount) {
        this.amount = amount;
        this.date = Calendar.getInstance();
        this.transactionID = UUID.randomUUID().toString().substring(0, 8); // Generates a unique short ID
    }

    @Override
    public double getAmount() { return this.amount; } [cite: 25]

    @Override
    public Calendar getDate() { return this.date; } [cite: 26]

    @Override
    public String getTransactionID() { return this.transactionID; } [cite: 27]

    @Override
    public void printTransactionDetails() { [cite: 29]
        System.out.println("--- Transaction Details ---");
        System.out.println("ID: " + transactionID);
        System.out.println("Date: " + date.getTime());
        System.out.println("Amount: $" + amount);
        System.out.println("Type: Generic Base Transaction");
    }

    @Override
    public void apply(BankAccount ba) throws InsufficientFundsException { [cite: 30]
        // This base implementation differs substantially from the subclasses as required
        System.out.println("[BaseTransaction Log] Inspecting account. Current balance: $" + ba.getBalance()); [cite: 31]
        this.isApplied = true;
    }
}

// QUESTION 1 & 2 - DepositTransaction Subclass

class DepositTransaction extends BaseTransaction {

    public DepositTransaction(double amount) {
        super(amount);
    }

    // Question 1: Method Overriding
    @Override
    public void apply(BankAccount ba) { [cite: 33]
        ba.deposit(this.amount);
        this.isApplied = true;
        System.out.println("Successfully deposited $" + this.amount);
    }

    @Override
    public void printTransactionDetails() {
        super.printTransactionDetails();
        System.out.println("Type: Deposit (Irreversible)"); [cite: 37]
        System.out.println("---------------------------");
    }
}


// QUESTION 1, 2, & 3 - WithdrawalTransaction Subclass

class WithdrawalTransaction extends BaseTransaction {
    private BankAccount associatedAccount; // Used for tracking the reversal
    private double shortfallAmount = 0.0;  // Track unpaid deficit if partial withdrawal happens

    public WithdrawalTransaction(double amount) {
        super(amount);
    }

    // Question 1 & Question 3 (throws keyword requirement)
    @Override
    public void apply(BankAccount ba) throws InsufficientFundsException { [cite: 33, 44]
        this.associatedAccount = ba;
        if (ba.getBalance() < this.amount) {
            throw new InsufficientFundsException("Insufficient funds! Transaction amount: $" + this.amount + " exceeds Balance: $" + ba.getBalance()); [cite: 44]
        }
        ba.withdraw(this.amount);
        this.isApplied = true;
        System.out.println("Successfully withdrew $" + this.amount);
    }

    // Question 3: Overloaded apply() method with try...catch...finally block
    
    public void apply(BankAccount ba, boolean partialWithdrawalAllowed) { [cite: 45, 47]
        this.associatedAccount = ba;
        try { [cite: 47]
            if (ba.getBalance() <= 0) {
                throw new InsufficientFundsException("Account balance is zero or negative. Cannot withdraw.");
            } else if (ba.getBalance() < this.amount) {
                // If balance is greater than 0 but less than the withdrawal amount, withdraw everything left
                this.shortfallAmount = this.amount - ba.getBalance(); [cite: 46]
                this.amount = ba.getBalance(); // Update transaction amount to what was actually pulled [cite: 46]
                ba.withdraw(this.amount); [cite: 46]
                this.isApplied = true;
                System.out.println("Partial Withdrawal Executed! Withdrew available balance: $" + this.amount);
            } else {
                // Execute standard withdrawal logic
                apply(ba);
            }
        } catch (InsufficientFundsException e) { [cite: 47]
            System.out.println("[Caught Exception inside Overloaded apply]: " + e.getMessage());
        } finally { [cite: 47]
            System.out.println("[Finally Block Executed] Shortfall remaining to be paid: $" + shortfallAmount);
        }
    }

    // Question 2: Reversal logic for withdrawals
    public boolean reverse() { [cite: 38]
        if (this.isApplied && this.associatedAccount != null) {
            this.associatedAccount.deposit(this.amount); // Restores the balance to its original amount [cite: 39]
            this.isApplied = false;
            System.out.println("Withdrawal transaction " + this.transactionID + " reversed successfully.");
            return true;
        }
        System.out.println("Reversal failed. Transaction was not applied.");
        return false;
    }

    @Override
    public void printTransactionDetails() {
        super.printTransactionDetails();
        System.out.println("Type: Withdrawal (Reversible)"); [cite: 37]
        if (shortfallAmount > 0) {
            System.out.println("Shortfall Notice: Unpaid shortage of $" + shortfallAmount); [cite: 46]
        }
        System.out.println("---------------------------");
    }
}