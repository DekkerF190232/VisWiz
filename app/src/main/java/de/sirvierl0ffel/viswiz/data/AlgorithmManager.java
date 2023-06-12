package de.sirvierl0ffel.viswiz.data;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import de.sirvierl0ffel.viswiz.models.Algorithm;

public class AlgorithmManager {

    private final Map<Long, Algorithm> data = new LinkedHashMap<>();

    public void load() {

    }

    public Map<Long, Algorithm> getData() {
        return data;
    }
}
