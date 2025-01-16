package project_perf.performance_testing;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import project_perf.performance_testing.dataManagement.ChartManager;
import project_perf.performance_testing.processManagement.ProcessManager;
import project_perf.performance_testing.validator.Validator;


public class PerformanceTestingController {

    private static final Validator validator = new Validator();
    private static final ChartManager chartManager = new ChartManager();
    private static final ProcessManager manager = new ProcessManager();

    private static final String[] languageName = {"C", "C#", "Java"};

    private static final Integer MAX = 100000;
    private static final Integer MIN = 1000;
    private static final Integer MAX_THREADS = 20;
    private static final Integer MIN_THREADS = 5;
    private static final Integer NO_OF_TESTS = 50;

    //Static allocation
    private static final String[] C_STATIC_ALLOCATION_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\c_programs\\c_allocation_static.exe", String.valueOf(NO_OF_TESTS)};
    private static final String[] CSHARP_STATIC_ALLOCATION_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\csharp_programs\\csharp_allocation_static\\csharp_allocation_static\\bin\\Release\\net8.0\\csharp_allocation_static", String.valueOf(NO_OF_TESTS)};
    private static final String[] JAVA_STATIC_ALLOCATION_COMMAND = {"java", "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\StaticAllocation.java", String.valueOf(NO_OF_TESTS)};

    //Dynamic allocation
    private static final String[] C_DYNAMIC_ALLOCATION_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\c_programs\\c_allocation_dynamic.exe", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] CSHARP_DYNAMIC_ALLOCATION_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\csharp_programs\\csharp_allocation_dynamic\\csharp_allocation_dynamic\\bin\\Release\\net8.0\\csharp_allocation_dynamic", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] JAVA_DYNAMIC_ALLOCATION_COMMAND = {"java", "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\DynamicAllocation.java", String.valueOf(NO_OF_TESTS), ""};

    //Static reading
    private static final String[] C_STATIC_READING_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\c_programs\\c_reading_static.exe", String.valueOf(NO_OF_TESTS)};
    private static final String[] CSHARP_STATIC_READING_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\csharp_programs\\csharp_reading_static\\csharp_reading_static\\bin\\Release\\net8.0\\csharp_reading_static", String.valueOf(NO_OF_TESTS)};
    private static final String[] JAVA_STATIC_READING_COMMAND = {"java", "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\StaticReading.java", String.valueOf(NO_OF_TESTS)};

    //Dynamic reading
    private static final String[] C_DYNAMIC_READING_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\c_programs\\c_reading_dynamic.exe", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] CSHARP_DYNAMIC_READING_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\csharp_programs\\csharp_dynamic_reading\\csharp_dynamic_reading\\bin\\Release\\net8.0\\csharp_dynamic_reading", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] JAVA_DYNAMIC_READING_COMMAND = {"java", "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\DynamicReading.java", String.valueOf(NO_OF_TESTS), ""};

    //Static writing
    private static final String[] C_STATIC_WRITING_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\c_programs\\c_writing_static.exe", String.valueOf(NO_OF_TESTS)};
    private static final String[] CSHARP_STATIC_WRITING_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\csharp_programs\\csharp_static_writing\\csharp_static_writing\\bin\\Release\\net8.0\\csharp_static_writing", String.valueOf(NO_OF_TESTS)};
    private static final String[] JAVA_STATIC_WRITING_COMMAND = {"java", "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\StaticWriting.java", String.valueOf(NO_OF_TESTS)};

    //Dynamic writing
    private static final String[] C_DYNAMIC_WRITING_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\c_programs\\c_writing_dynamic.exe", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] CSHARP_DYNAMIC_WRITING_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\csharp_programs\\csharp_dynamic_writing\\csharp_dynamic_writing\\bin\\Release\\net8.0\\csharp_dynamic_writing", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] JAVA_DYNAMIC_WRITING_COMMAND = {"java", "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\DynamicWriting.java", String.valueOf(NO_OF_TESTS), ""};

    //Thread creation
    private static final String[] C_THREAD_CREATION_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\c_programs\\c_thread_creation.exe", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] CSHARP_THREAD_CREATION_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\csharp_programs\\csharp_thread_creation\\csharp_thread_creation\\bin\\Release\\net8.0\\csharp_thread_creation", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] JAVA_THREAD_CREATION_COMMAND = {"java", "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\ThreadCreation.java", String.valueOf(NO_OF_TESTS), ""};

    //Thread migration
    private static final String[] C_THREAD_MIGRATION_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\c_programs\\c_thread_migration.exe", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] CSHARP_THREAD_MIGRATION_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\csharp_programs\\csharp_thread_migration\\csharp_thread_migration\\bin\\Release\\net8.0\\csharp_thread_migration", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] JAVA_THREAD_MIGRATION_COMMAND = {"java", "-cp", ".;E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\jna-3.0.9.jar", "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\ThreadMigration.java", String.valueOf(NO_OF_TESTS), ""};

    //Context switch
    private static final String[] C_CONTEXT_SWITCH_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\c_programs\\c_context_switch.exe", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] CSHARP_CONTEXT_SWITCH_COMMAND = {"E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\csharp_programs\\csharp_context_switch\\csharp_context_switch\\bin\\Release\\net8.0\\csharp_context_switch", String.valueOf(NO_OF_TESTS), ""};
    private static final String[] JAVA_CONTEXT_SWITCH_COMMAND = {"java", "-cp", ".;E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\jna-3.0.9.jar", "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\java_tests\\src\\main\\java\\tests\\ContextSwitch.java", String.valueOf(NO_OF_TESTS), ""};

    //Result files names
    private static final String STATIC_ALLOCATION_RESULTS = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\static_allocation_results.txt";
    private static final String DYNAMIC_ALLOCATION_RESULTS = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\dynamic_allocation_results.txt";
    private static final String STATIC_READING_RESULTS = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\static_reading_results.txt";
    private static final String DYNAMIC_READING_RESULTS = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\dynamic_reading_results.txt";
    private static final String STATIC_WRITING_RESULTS = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\static_writing_results.txt";
    private static final String DYNAMIC_WRITING_RESULTS = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\dynamic_writing_results.txt";
    private static final String THREAD_CREATION_RESULTS = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\thread_creation_results.txt";
    private static final String THREAD_MIGRATION_RESULTS = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\thread_migration_results.txt";
    private static final String CONTEXT_SWITCH_RESULTS = "E:\\Facultate\\3-sem1\\SSC\\proiect\\programs\\results\\context_switch_results.txt";

    private static final Double[] staticAllocationAvg = new Double[3];
    private static final Double[] dynamicAllocationAvg = new Double[3];
    private static final Double[] staticReadingAvg = new Double[3];
    private static final Double[] dynamicReadingAvg = new Double[3];
    private static final Double[] staticWritingAvg = new Double[3];
    private static final Double[] dynamicWritingAvg = new Double[3];
    private static final Double[] threadCreationAvg = new Double[3];
    private static final Double[] threadMigrationAvg = new Double[3];
    private static final Double[] contextSwitchAvg = new Double[3];

    @FXML
    private Text cSharpText;

    @FXML
    private Text cText;

    @FXML
    private ScrollPane dataPane;

    @FXML
    private VBox chartPane;

    @FXML
    private Text javaText;

    @FXML
    private Pane langBar;

    @FXML
    private Pane mainPanel;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Button startButton;

    @FXML
    private Pane test1;

    @FXML
    private CheckBox test1Check;

    @FXML
    private Pane test2;

    @FXML
    private CheckBox test2Check;

    @FXML
    private Pane test3;

    @FXML
    private CheckBox test3Check;

    @FXML
    private Pane test4;

    @FXML
    private CheckBox test4Check;

    @FXML
    private Pane test5;

    @FXML
    private CheckBox test5Check;

    @FXML
    private Pane test6;

    @FXML
    private CheckBox test6Check;

    @FXML
    private Pane test7;

    @FXML
    private CheckBox test7Check;

    @FXML
    private Pane test8;

    @FXML
    private CheckBox test8Check;

    @FXML
    private Pane test9;

    @FXML
    private CheckBox test9Check;

    @FXML
    private VBox tests;

    @FXML
    private TextField textField1;
    @FXML
    private TextField textField2;
    @FXML
    private TextField textField3;
    @FXML
    private TextField textField4;
    @FXML
    private TextField textField5;
    @FXML
    private TextField textField6;
    @FXML
    private TextField textField7;
    @FXML
    private TextField textField8;
    @FXML
    private TextField textField9;

    @FXML
    private Pane topBar;

    @FXML
    private Text text1;
    @FXML
    private Text text2;
    @FXML
    private Text text3;
    @FXML
    private Text text4;
    @FXML
    private Text text5;
    @FXML
    private Text text6;
    @FXML
    private Text text7;
    @FXML
    private Text text8;
    @FXML
    private Text text9;

    @FXML
    private void clearText(MouseEvent event) {
        TextField textField = (TextField) event.getSource();
        textField.setText(null);
    }

    @FXML
    public void toggleCheckBoxInPane(MouseEvent event) {
        Pane pane = (Pane) event.getSource();
        for (javafx.scene.Node node : pane.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                checkBox.setSelected(!checkBox.isSelected());
            }
        }
    }

    @FXML
    public void startProcesses() {
        chartPane.getChildren().clear();
        if (test1Check.isSelected()) {
            manager.createProcesses(C_STATIC_ALLOCATION_COMMAND, CSHARP_STATIC_ALLOCATION_COMMAND, JAVA_STATIC_ALLOCATION_COMMAND);
            chartManager.addLineChartToPane(chartPane, chartManager.returnLineChart(NO_OF_TESTS, "Static allocation", languageName, STATIC_ALLOCATION_RESULTS, staticAllocationAvg));
        }
        if (test2Check.isSelected() && validator.areInputsValid(textField2.getText(), MAX, MIN)) {
            String input = textField2.getText();
            C_DYNAMIC_ALLOCATION_COMMAND[2] = input;
            CSHARP_DYNAMIC_ALLOCATION_COMMAND[2] = input;
            JAVA_DYNAMIC_ALLOCATION_COMMAND[3] = input;
            manager.createProcesses(C_DYNAMIC_ALLOCATION_COMMAND, CSHARP_DYNAMIC_ALLOCATION_COMMAND, JAVA_DYNAMIC_ALLOCATION_COMMAND);
            chartManager.addLineChartToPane(chartPane, chartManager.returnLineChart(NO_OF_TESTS, "Dynamic allocation", languageName, DYNAMIC_ALLOCATION_RESULTS, dynamicAllocationAvg));
        }
        if (test3Check.isSelected()) {
            manager.createProcesses(C_STATIC_READING_COMMAND, CSHARP_STATIC_READING_COMMAND, JAVA_STATIC_READING_COMMAND);
            chartManager.addLineChartToPane(chartPane, chartManager.returnLineChart(NO_OF_TESTS, "Static reading", languageName, STATIC_READING_RESULTS, staticReadingAvg));
        }
        if (test4Check.isSelected() && validator.areInputsValid(textField4.getText(), MAX, MIN)) {
            String input = textField4.getText();
            C_DYNAMIC_READING_COMMAND[2] = input;
            CSHARP_DYNAMIC_READING_COMMAND[2] = input;
            JAVA_DYNAMIC_READING_COMMAND[3] = input;
            manager.createProcesses(C_DYNAMIC_READING_COMMAND, CSHARP_DYNAMIC_READING_COMMAND, JAVA_DYNAMIC_READING_COMMAND);
            chartManager.addLineChartToPane(chartPane, chartManager.returnLineChart(NO_OF_TESTS, "Dynamic reading", languageName, DYNAMIC_READING_RESULTS, dynamicReadingAvg));
        }
        if (test5Check.isSelected()) {
            manager.createProcesses(C_STATIC_WRITING_COMMAND, CSHARP_STATIC_WRITING_COMMAND, JAVA_STATIC_WRITING_COMMAND);
            chartManager.addLineChartToPane(chartPane, chartManager.returnLineChart(NO_OF_TESTS, "Static writing", languageName, STATIC_WRITING_RESULTS, staticWritingAvg));
        }
        if (test6Check.isSelected() && validator.areInputsValid(textField6.getText(), MAX, MIN)) {
            String input = textField6.getText();
            C_DYNAMIC_WRITING_COMMAND[2] = input;
            CSHARP_DYNAMIC_WRITING_COMMAND[2] = input;
            JAVA_DYNAMIC_WRITING_COMMAND[3] = input;
            manager.createProcesses(C_DYNAMIC_WRITING_COMMAND, CSHARP_DYNAMIC_WRITING_COMMAND, JAVA_DYNAMIC_WRITING_COMMAND);
            chartManager.addLineChartToPane(chartPane, chartManager.returnLineChart(NO_OF_TESTS, "Dynamic writing", languageName, DYNAMIC_WRITING_RESULTS, dynamicWritingAvg));
        }
        if (test7Check.isSelected() && validator.areInputsValid(textField7.getText(), MAX_THREADS, MIN_THREADS)) {
            String input = textField7.getText();
            C_THREAD_CREATION_COMMAND[2] = input;
            CSHARP_THREAD_CREATION_COMMAND[2] = input;
            JAVA_THREAD_CREATION_COMMAND[3] = input;
            manager.createProcesses(C_THREAD_CREATION_COMMAND, CSHARP_THREAD_CREATION_COMMAND, JAVA_THREAD_CREATION_COMMAND);
            chartManager.addLineChartToPane(chartPane, chartManager.returnLineChart(NO_OF_TESTS, "Thread creation", languageName, THREAD_CREATION_RESULTS, threadCreationAvg));
        }
        if (test8Check.isSelected() && validator.areInputsValid(textField8.getText(), MAX_THREADS, MIN_THREADS)) {
            String input = textField8.getText();
            C_THREAD_MIGRATION_COMMAND[2] = input;
            CSHARP_THREAD_MIGRATION_COMMAND[2] = input;
            JAVA_THREAD_MIGRATION_COMMAND[5] = input;
            manager.createProcesses(C_THREAD_MIGRATION_COMMAND, CSHARP_THREAD_MIGRATION_COMMAND, JAVA_THREAD_MIGRATION_COMMAND);
            chartManager.addLineChartToPane(chartPane, chartManager.returnLineChart(NO_OF_TESTS, "Thread migration", languageName, THREAD_MIGRATION_RESULTS, threadMigrationAvg));
        }
        if (test9Check.isSelected() && validator.areInputsValid(textField9.getText(), MAX_THREADS, MIN_THREADS)) {
            String input = textField9.getText();
            C_CONTEXT_SWITCH_COMMAND[2] = input;
            CSHARP_CONTEXT_SWITCH_COMMAND[2] = input;
            JAVA_CONTEXT_SWITCH_COMMAND[5] = input;
            manager.createProcesses(C_CONTEXT_SWITCH_COMMAND, CSHARP_CONTEXT_SWITCH_COMMAND, JAVA_CONTEXT_SWITCH_COMMAND);
            chartManager.addLineChartToPane(chartPane, chartManager.returnLineChart(NO_OF_TESTS, "Context switch", languageName, CONTEXT_SWITCH_RESULTS, contextSwitchAvg));
        }
    }
}