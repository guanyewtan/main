package moomoo.command.category;

import moomoo.command.Command;

import moomoo.feature.Budget;
import moomoo.feature.category.CategoryList;
import moomoo.feature.MooMooException;
import moomoo.feature.ScheduleList;
import moomoo.feature.category.Expenditure;
import moomoo.feature.storage.Storage;
import moomoo.feature.Ui;
import moomoo.feature.MainDisplay;
import moomoo.feature.category.Category;

public class SortCategoryCommand extends Command {

    public SortCategoryCommand(String sortBy) {
        super(false, sortBy);
    }

    @Override
    public void execute(ScheduleList calendar, Budget budget, CategoryList categoryList,
                        Storage storage) throws MooMooException {
        if (input.startsWith("name")) {
            sort(categoryList, "name");
        } else if (input.startsWith("cost")) {
            sort(categoryList, "cost");
        } else if (input.startsWith("date")) {
            sort(categoryList, "date");
        } else {
            throw new MooMooException("Oops you can only sort by <name> or <cost> or <date>");
        }

        MainDisplay newMainDisplay = new MainDisplay();
        int cols = newMainDisplay.getCatListSize(categoryList);
        int rows = newMainDisplay.getMaxCatSize(categoryList);
        String output = newMainDisplay.newToPrint(0,0,rows,cols,categoryList,budget,1);
        Ui.printMainDisplay(output);
        setTestOutput(categoryList);
    }

    /**
     * Sets the test output for testing purposes, shows the expenditures in the first category.
     * @param categoryList category list
     */
    private void setTestOutput(CategoryList categoryList) throws MooMooException {
        Category category = categoryList.get(0);
        String output = "";
        for (Expenditure entry : category.getCategory()) {
            output = output.concat(entry.toString() + "\n");
        }
        Ui.setTestOutput(output);
    }

    /**
     * Sorts the category list in specified order.
     * @param categoryList list to be sorted
     * @param order specified ordering
     */
    private void sort(CategoryList categoryList, String order) {
        for (Category category : categoryList.getCategoryList()) {
            category.sort(order);
        }
    }
}
