package de.sirvierl0ffel.viswiz.models;

import java.util.Collections;
import java.util.List;

public class Algorithm {

    public static final List<Algorithm> DUMMY = Collections.singletonList(
            new Algorithm(0, "Bubble Sort",
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
//                          "\t\tlogi((arr.length - Math.max(1, len)) / arr.length);\n" +
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
                    new InputSave("let A = [3,5,8,2,6]"))
    );

    public static Algorithm dummyGetAlgorithm(long id) {
        return DUMMY.get((int) id);
    }

    public long id;
    public String name;
    public String description;
    public String imageLocation;
    public String code;
    public String[] pseudoCode;
    public InputSave defaultInput;

    private Post post;

    public Algorithm(long id, String name, String description, String imageLocation, String code, String[] pseudoCode, InputSave defaultInput) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imageLocation = imageLocation;
        this.code = code;
        this.pseudoCode = pseudoCode;
        this.defaultInput = defaultInput;
    }

    public Algorithm() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String[] getPseudoCode() {
        return pseudoCode;
    }

    public void setPseudoCode(String[] pseudoCode) {
        this.pseudoCode = pseudoCode;
    }

    public InputSave getDefaultInput() {
        return defaultInput;
    }

    public void setDefaultInput(InputSave defaultInput) {
        this.defaultInput = defaultInput;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
