package de.sirvierl0ffel.viswiz.data;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.models.InputSave;

/**
 * Loads, saves and holds the list of inputs for each saved algorithm.
 */
public class InputSaveManager {

    private final Map<Algorithm, List<InputSave>> inputs = new HashMap<>();

    public void loadJSON(String data, Function<Long, Algorithm> getAlgorithm) {
        try {
            JsonObject jsonAlgorithms = new Gson().fromJson(data, JsonObject.class);
            for (Map.Entry<String, JsonElement> entry : jsonAlgorithms.entrySet()) {
                Algorithm algorithm = getAlgorithm.apply(Long.valueOf(entry.getKey()));
                List<InputSave> inputs = getInputs(algorithm);
                JsonArray jsonInputs = entry.getValue().getAsJsonArray();
                for (JsonElement jsonElementInput : jsonInputs) {
                    JsonObject jsonInput = jsonElementInput.getAsJsonObject();
                    inputs.add(new InputSave(jsonInput.get("input").getAsString()));
                }
            }
        } catch (Throwable t) {
            Log.e(getClass().getSimpleName(), "Failed to load inputs: " + t);
            t.printStackTrace();
        }
    }

    public String saveJSON() {
        JsonObject jsonAlgorithms = new JsonObject();
        for (Map.Entry<Algorithm, List<InputSave>> entry : inputs.entrySet()) {
            Algorithm algorithm = entry.getKey();
            List<InputSave> inputs = entry.getValue();
            JsonArray jsonInputs = new JsonArray(inputs.size()-1);
            for (int i = 1; i < inputs.size(); i++) {
                JsonObject jsonInput = new JsonObject();
                jsonInput.addProperty("input", inputs.get(i).input);
                jsonInputs.add(jsonInput);
            }
            jsonAlgorithms.add(String.valueOf(algorithm.id), jsonInputs);
        }
        return jsonAlgorithms.toString();
    }

    /**
     * @return A mutable list of inputs corresponding to this algorithm, including the algorithm's default input, this is why the first element of this list should not be removed.
     */
    public List<InputSave> getInputs(Algorithm algorithm) {
        return inputs.computeIfAbsent(algorithm, a -> new ArrayList<InputSave>() {
            {
                add(a.defaultInput);
            }
        });
    }

}
