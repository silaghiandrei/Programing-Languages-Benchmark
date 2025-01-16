module project_perf.performance_testing {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires com.almasb.fxgl.all;

    opens project_perf.performance_testing to javafx.fxml;
    exports project_perf.performance_testing;
}