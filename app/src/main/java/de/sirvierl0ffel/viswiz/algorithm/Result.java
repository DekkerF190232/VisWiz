package de.sirvierl0ffel.viswiz.algorithm;

import java.util.List;

public class Result {

    public List<ResultStep> stepList;

    public Result() {
    }

    public Result(List<ResultStep> stepList) {
        this.stepList = stepList;
    }

    public List<ResultStep> getStepList() {
        return stepList;
    }

    public void setStepList(List<ResultStep> stepList) {
        this.stepList = stepList;
    }
}
