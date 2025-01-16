package project_perf.performance_testing.processManagement;

import java.io.IOException;

public class ProcessManager {

    private void runCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.command(command);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        System.out.println("Running command: " + String.join(" ", command));
        Process process = processBuilder.start();
        int exitCode = process.waitFor();
        System.out.println("Process exited with code: " + exitCode);
    }

    public void createProcesses(String[] command1, String[] command2, String[] command3) {
        try {
            runCommand(command1);
            runCommand(command2);
            runCommand(command3);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
