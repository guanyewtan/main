package moomoo;

import moomoo.command.Command;
import moomoo.feature.Budget;
import moomoo.feature.Cow;
import moomoo.feature.MooMooException;
import moomoo.feature.ScheduleList;
import moomoo.feature.Ui;
import moomoo.feature.category.CategoryList;
import moomoo.feature.parser.Parser;
import moomoo.feature.storage.CategoryStorage;
import moomoo.feature.storage.ExpenditureStorage;
import moomoo.feature.storage.Storage;
import moomoo.feature.Cow;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Runs MooMoo.
 */
public class MooMoo {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_PURPLE = "\u001B[35m";

    private Storage storage;
    private CategoryList categoryList;
    private Budget budget;
    public ScheduleList calendar;

    /**
     * Initializes different Category, Expenditures, Budget, Storage and Ui.
     */
    private MooMoo() {

        storage = new Storage("data/budget.txt","data/schedule.txt");

        try {
            storage.preloadData();
        } catch (MooMooException e) {
            Ui.printException(e);
            Ui.showResponse();
        }
        try {
            categoryList = CategoryStorage.loadFromFile();
            ExpenditureStorage.loadFromFile(categoryList);
        } catch (MooMooException e) {
            Ui.printException(e);
            Ui.showResponse();
            categoryList = new CategoryList();
        }

        HashMap<String, Double> loadedBudget = storage.loadBudget(categoryList.getCategoryList());
        if (loadedBudget == null) {
            Ui.showResponse();
            budget = new Budget();
        } else {
            budget = new Budget(loadedBudget);
        }

        HashMap<String, ArrayList<String>> scheduleList = storage.loadCalendar();
        if (scheduleList == null) {
            Ui.showResponse();
            calendar = new ScheduleList();
        } else {
            calendar = new ScheduleList(scheduleList);
        }
    }

    /**
     * Runs the command line interface, reads input from user and returns result accordingly.
     */
    private void run() {
        Ui.showWelcome();
        Cow moo = new Cow();
        Ui.setOutput(moo.getHappyCow());
        Ui.showResponse();
        
        String date = Ui.showDate();
        String todaySchedule = calendar.showSchedule(date);
        Ui.setOutput(todaySchedule);
        Ui.showResponse();

        String helpMessage = "Start managing your money! Or type <help> to get started.\n";
        Ui.setOutput(helpMessage);
        Ui.showResponse();

        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = Ui.readCommand();

                Ui.clearScreen();
                Ui.setOutput(ANSI_PURPLE + fullCommand + ANSI_RESET + "\n");
                Ui.showResponse();

                Command c = Parser.parse(fullCommand);
                c.execute(calendar, budget, categoryList, storage);

                if (!Ui.getOutput().equals("")) {
                    Ui.showResponse();
                }
              
                isExit = c.isExit;
            } catch (MooMooException e) {
                Ui.printException(e);
                Ui.showResponse();
            }
        }
    }

    /**
     * Returns the response to the GUI when given an input by a user.
     * @param input Input given by user in the GUI
     * @return String Response to display on GUI by the bot.
     */
    public String getResponse(String input) {
        boolean isExit;
        try {
            Command c = Parser.parse(input);
            c.execute(calendar, budget, categoryList, storage);

            isExit = c.isExit;
            if (isExit) {
                Ui.showGoodbye();
                return Ui.getOutput();
            }
            return Ui.getOutput();
        } catch (MooMooException e) {
            return Ui.printException(e);
        }
    }

    /**
     * Runs MooMoo.
     * @param args Argument values given when running the program
     */
    public static void main(String[] args) {
        new MooMoo().run();
    }
}
