package de.sirvierl0ffel.viswiz.algorithm;

import java.util.Map;

public class ResultStep {

    public Map<String, String> variables;
    public int pseudoCodeIndex;
    public String message;

    public ResultStep() {
    }

    public ResultStep(Map<String, String> variables, int pseudoCodeIndex, String message) {
        this.variables = variables;
        this.pseudoCodeIndex = pseudoCodeIndex;
        this.message = message;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public int getPseudoCodeIndex() {
        return pseudoCodeIndex;
    }

    public void setPseudoCodeIndex(int pseudoCodeIndex) {
        this.pseudoCodeIndex = pseudoCodeIndex;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
