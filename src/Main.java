
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Main {


    // classes:
    /**

     VendingMachine
     Inventory
     Slot
     Product
     CashMachine
     ProductDispenser
     KeyBoardScanner
     Screen
     Wallet
     Label

     User
     Admin

     * */

    static class Slot {
        private int id;
        private int location;

        public int getLocation() {
            return location;
        }
    }

    static class Product {
        private int id;
        private String name;
        private String type;
        private int quantity;
        private double price;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getType() {
            return type;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getPrice() {
            return price;
        }

        public void reduceByOne() {
            quantity--;
        }
    }

    static class VendingMachineException extends Exception {
        VendingMachineException(String message) {
            super(message);
        }
    }

    static class Inventory {
        Map<Integer, Product> slotVsProductMap;
        public Product getProduct(int location) throws VendingMachineException {
            if(!slotVsProductMap.containsKey(location)) throw new VendingMachineException("INVALID_SLOT");
            Product product = slotVsProductMap.get(location);
            if(product.getQuantity()==0) throw new VendingMachineException("PRODUCT_EXHAUSTED");
            return product;
        }

        public void productDispensed(Product product) {
            product.reduceByOne();
        }
    }

    static class CashMachine {
        void acceptCash(double amount) {

        }

        void dispenseCash(double amount) {

        }
    }

    static class Transaction {
        private int id;
        double amount;
        Product product;
        String userInput;
        Date date;
        
        StateEnum state;

        public void updateState(StateEnum state) {
            this.state = state;
        }
    }
    
    static enum StateEnum {
        CREATED, 
        PENDING, 
        SUCCESS, 
        ABORTED
    }
    
    static class Ledger{
        List<Transaction> transactionList;
        
        public List<Transaction> getLedger() {
            return transactionList;
        }
        
        public void addTransaction(Transaction transaction) {
            transactionList.add(transaction);
        }
        
    }

    static class Wallet {
        private double amount=0;
        public double getAmount(){
            return amount;
        }
        
        public void loadWallet(double added) {
            amount += added;
        }
        public void withdraw(double withdrawn) {
            amount-= withdrawn;
        }
    }

    static class User {
        private Wallet wallet;

        public Wallet getWallet() {
            return wallet;
        }
    }

    static class KeyBoard {
        public String getInput(){
            return "";
        }
    }

    class Screen {
        public void show(String message){

        }
    }

    static class VendingMachine {
        private Inventory inventory;
        private CashMachine cashMachine;
        private KeyBoard keyBoard;
        private Screen screen;
        private Ledger ledger;


        public Product getProductInfo(Slot slot) throws VendingMachineException {
            return inventory.getProduct(slot.getLocation());
        }

        public double askUserForAmount(double pendingAmount) {
            double someUserCashInputGivenToMachcine = 999d;
            cashMachine.acceptCash(someUserCashInputGivenToMachcine);
            return someUserCashInputGivenToMachcine;
        }

        public void showMessage(String message) {
            screen.show(message);
        }

        public Lable getInputFromUser() {
            return new Lable();
        }

        public Transaction createTransaction(StateEnum state) {
            return new Transaction();
        }

        public Transaction updateTransactionState(Transaction transaction, StateEnum state) {
            transaction.updateState(state);
            return transaction;
        }

        public void dispenseProduct(Product product) {
        }
    }

    static class Lable {
        String value;

        public Slot getSlot() {
            return new Slot();
        }
    }

    private static VendingMachine init() {
        return new VendingMachine();
    }

    public static void main(String[] args) throws VendingMachineException {

        VendingMachine vendingMachine = init();
        Transaction transaction = vendingMachine.createTransaction(StateEnum.CREATED);
        try {
            
            User user = new User();
            Lable lable = vendingMachine.getInputFromUser();
            Slot slot = lable.getSlot();
            Product product = vendingMachine.getProductInfo(slot);
            Wallet wallet = user.getWallet();

            vendingMachine.showMessage("product dispensing is in progress");
            transaction = vendingMachine.updateTransactionState(transaction, StateEnum.PENDING);

            if(product.getPrice() > wallet.getAmount()) {
                double pendingAmount = product.getPrice() - wallet.getAmount();
                double acceptedCash = vendingMachine.askUserForAmount(pendingAmount);
                if(acceptedCash >= pendingAmount) {
                    double loadedAmount = acceptedCash - pendingAmount;
                    wallet.loadWallet(loadedAmount);
                    vendingMachine.showMessage(loadedAmount + " has been loaded in wallet ");
                } else {
                    wallet.loadWallet(acceptedCash);
                    throw new VendingMachineException(acceptedCash + " has been loaded in wallet, please retry whole process");
                }
            } else {
                wallet.withdraw(product.getPrice());
            }
            vendingMachine.dispenseProduct(product);
            vendingMachine.updateTransactionState(transaction, StateEnum.SUCCESS);
            vendingMachine.showMessage("Product Dispensed successfully");
        } catch (Exception e) {
            vendingMachine.updateTransactionState(transaction, StateEnum.ABORTED);
            throw e;
        }
        
    }


}