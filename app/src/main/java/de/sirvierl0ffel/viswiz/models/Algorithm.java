package de.sirvierl0ffel.viswiz.models;

public class Algorithm {

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
