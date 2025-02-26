package moomoo.feature;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Holds a map for the budget for each category.
 */
public class Budget {
    private HashMap<String, Double> categoryBudgets;
    private DecimalFormat df;

    /**
     * Initializes values to default values if no previous budget information is found.
     * Initializes DecimalFormat to force doubles to display with 2 decimal places.
     */
    public Budget() {
        this.categoryBudgets = new HashMap<String, Double>();
        df = new DecimalFormat("#.00");
    }

    /**
     * Takes in budget set by user and set budget variable.
     * Initializes DecimalFormat to force doubles to display with 2 decimal places.
     */
    public Budget(HashMap<String, Double> newBudget) {
        this.categoryBudgets = newBudget;
        df = new DecimalFormat("#.00");
    }

    @Override
    public String toString() {
        String returnVal = "";
        Iterator budgetIterator = categoryBudgets.entrySet().iterator();
        while (budgetIterator.hasNext()) {
            Map.Entry mapElement = (Map.Entry) budgetIterator.next();
            returnVal += "Your budget for " + mapElement.getKey() + " is: $" + df.format(mapElement.getValue()) + "\n";
        }
        return returnVal;
    }

    /**
     * Returns budget from corresponding category.
     *
     * @param category Category to get budget from.
     * @return The budget of the given category.
     */
    public double getBudgetFromCategory(String category) {
        if (this.categoryBudgets.containsKey(category)) {
            return this.categoryBudgets.get(category);
        } else {
            return 0;
        }
    }

    /**
     * Adds budget to the HashMap and re-adds to total budget if budget is changed or added.
     * @param category Category to which the budget was set
     * @param budget Budget to set for the corresponding category
     */
    public void addNewBudget(String category, double budget) {
        this.categoryBudgets.put(category, budget);
    }

    public HashMap<String, Double> getBudget() {
        return this.categoryBudgets;
    }

}