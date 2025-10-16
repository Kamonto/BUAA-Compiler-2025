package error;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

public class ErrorList {
    private ArrayList<Error> errors;

    public ErrorList() {
        errors = new ArrayList<Error>();
    }

    public void addError(Error error) {
        errors.add(error);
    }

    public boolean hasError() {
        return !errors.isEmpty();
    }

    public void printError() throws IOException {
        Collections.sort(errors);
        StringBuilder strb = new StringBuilder();
        for (Error error : errors) {
            strb.append(error.getLine()).append(" ").append(error.getType()).append("\n");
        }
        Path path = Paths.get("error.txt");
        Files.write(path, strb.toString().getBytes());
    }
}
