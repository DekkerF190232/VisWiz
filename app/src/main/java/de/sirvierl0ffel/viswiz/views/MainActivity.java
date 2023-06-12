package de.sirvierl0ffel.viswiz.views;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import de.sirvierl0ffel.viswiz.R;
import de.sirvierl0ffel.viswiz.databinding.ActivityMainBinding;
import de.sirvierl0ffel.viswiz.models.Algorithm;
import de.sirvierl0ffel.viswiz.models.InputSave;
import de.sirvierl0ffel.viswiz.util.FileUtil;
import de.sirvierl0ffel.viswiz.util.GsonRequest;
import de.sirvierl0ffel.viswiz.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        load();

        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
        model._currentFragment.observe(this, clazz -> getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, clazz, null)
                .addToBackStack(null)
                .commit());

        ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, AlgorithmListFragment.newInstance(1))
                .commit();
        setContentView(binding.getRoot());
    }

    private void load() {

        RequestQueue volley = Volley.newRequestQueue(this);
        volley.add(new GsonRequest<>(Request.Method.GET, "http://10.0.2.2:8080/algorithm/all",
                new TypeToken<List<Algorithm>>() {
                },
                Collections.emptyMap(),
                success -> {
                    MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
                    model.managerAlgorithms.getData().clear();
                    model.managerAlgorithms.getData().putAll(success.stream()
                            .collect(Collectors.toMap(e -> e.id, e -> e)));
                    String data = FileUtil.readFromFile(this, "inputs.json", "{}");
                    model.managerInputs.loadJSON(data, model.managerAlgorithms.getData()::get);
                    model.algorithmUpdate.setValue(Boolean.FALSE.equals(model.algorithmUpdate.getValue()));
                },
                error -> {
                    MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
                    onResponseError();
                    System.err.println("Error requesting algorithms! :(");
                    error.printStackTrace();
                    model.algorithmUpdate.setValue(Boolean.FALSE.equals(model.algorithmUpdate.getValue()));
                }));
    }

    private void onResponseError() {
        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);

        Algorithm offlineExample = new Algorithm(0, "(Offline) Bubble Sort",
                "\\h{Input}\n" +
                        "\t\\a{A}: Array of unsorted numbers.\n" +
                        "\n" +
                        "\\h{Output}\n" +
                        "\t\\a{A}: Array of sorted numbers. The order is ascending (smallest to highest).\n" +
                        "\n" +
                        "\\h{Description}\n" +
                        "\tGoes over every number, except the last one. Switches the number with the following number, if the current number is bigger. Repeat this until \\a{A} was traversed without encountering an unordered pair. In each subsequent pass, bubble sort focuses on a smaller window of the array, excluding the sorted elements at the end of the array from the previous passes.",
                "https://i.imgur.com/o2HHnsl.png",
                "(function() {\n" +
                        "\tlet steps = [];\n" +
                        "\tlet arr = A; // [5, 3, 8, 4, 2];\n" +
                        "\tsteps.push({ v: { A: [...arr] }, i: 0 });\n" +
                        "\tlet len = arr.length;\n" +
                        "\tlet swapped = true;\n" +
                        "\tsteps.push({ v: { A: [...arr], n: len }, i: 1 });\n" +
                        "\tsteps.push({ v: { A: [...arr], n: len, swapped }, i: 2 });\n" +
                        "\tsteps.push({\n" +
                        "\t\tv: { A: [...arr], n: len, swapped },\n" +
                        "\t\ti: 3\n" +
                        "\t});\n" +
                        "\tlet ran = false;\n" +
                        "\twhile (true) {\n" +
                        "\t\tif (ran) {\n" +
                        "\t\t\tsteps.push({ v: { A: [...arr], n: len, swapped}, i: 12 });\n" +
                        "\t\t}\n" +
                        "\t\tif (!swapped) break;\n" +
                        "\t\tran = true;\n" +
                        "\t\tswapped = false;\n" +
                        "\t\tsteps.push({ v: { A: [...arr], n: len, swapped }, i: 4 });\n" +
                        "\t\tfor (let i = 0; ; i++) {\n" +
                        "\t\t\tsteps.push({ v: { A: [...arr], n: len, swapped, i }, i: 5 });\n" +
                        "\t\t\tif (i == len - 1) break;\n" +
                        "\t\t\tsteps.push({ v: { A: [...arr], n: len, swapped, i }, i: 6 });\n" +
                        "\t\t\tif (arr[i] > arr[i + 1]) {\n" +
                        "\t\t\t\tlet temp = arr[i];\n" +
                        "\t\t\t\tsteps.push({\n" +
                        "\t\t\t\t\tv: { A: [...arr], n: len, swapped, i, temp },\n" +
                        "\t\t\t\t\ti: 7\n" +
                        "\t\t\t\t});\n" +
                        "\t\t\t\tarr[i] = arr[i + 1];\n" +
                        "\t\t\t\tsteps.push({\n" +
                        "\t\t\t\t\tv: { A: [...arr], n: len, swapped, i, temp },\n" +
                        "\t\t\t\t\ti: 8\n" +
                        "\t\t\t\t});\n" +
                        "\t\t\t\tarr[i + 1] = temp;\n" +
                        "\t\t\t\tsteps.push({ v: { A: [...arr], n: len, swapped, i, temp }, i: 9 });\n" +
                        "\t\t\t\tswapped = true;\n" +
                        "\t\t\t\tsteps.push({ v: { A: [...arr], n: len, swapped, i, temp }, i: 10 });\n" +
                        "\t\t\t}\n" +
                        "\t\t}\n" +
                        "\t\tupdateProgress((arr.length - Math.max(1, len)) / arr.length);\n" +
                        "\t\tlen--;\n" +
                        "\t\tsteps.push({ v: { A: [...arr], n: len, swapped}, i: 11});\n" +
                        "\t}\n" +
                        "\tsteps.push({ v: { A: [...arr] }, i: -1 });\n" +
                        "\treturn JSON.stringify( { steps } );\n" +
                        "})();",
                new String[]{
                        "\\k{function} bubbleSort(\\a{A})",
                        "	\\k{let} \\a{n} = \\a{A}.\\a{length}",
                        "	\\k{let} \\a{swapped} = \\v{true}",
                        "	\\k{do}",
                        "		\\a{swapped} = \\v{false}",
                        "		\\k{for} \\a{i} = \\v{0} \\k{to} \\a{n} - \\v{1}",
                        "			\\k{if} \\a{A}[\\a{i}] > \\a{A}[\\a{i} + \\v{1}]",
                        "				\\k{let} \\a{temp} = \\a{A}[\\a{i}]",
                        "				\\a{A}[\\a{i}] = \\a{arr}[\\a{i} + \\v{1}]",
                        "				\\a{A}[\\a{i} + \\v{1}] = \\a{temp}",
                        "				\\a{swapped} = \\v{true}",
                        "		\\a{n} = \\a{n} - \\v{1}",
                        "	\\k{while} \\a{swapped}"
                },
                new InputSave("let A = [3,5,8,2,6]"));

        model.managerAlgorithms.getData().put(0L, offlineExample);

        String data = FileUtil.readFromFile(this, "inputs.json", "{}");
        model.managerInputs.loadJSON(data, model.managerAlgorithms.getData()::get);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MainViewModel model = new ViewModelProvider(this).get(MainViewModel.class);
        String data = model.managerInputs.saveJSON();
        FileUtil.writeToFile(this, "inputs.json", data);
    }
}