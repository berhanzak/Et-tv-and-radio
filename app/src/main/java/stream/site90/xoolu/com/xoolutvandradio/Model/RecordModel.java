package stream.site90.xoolu.com.xoolutvandradio.Model;


public class RecordModel {

    private String type;
    private String name;
    private String duration;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public RecordModel(String type, String name, String duration) {
        this.type = type;
        this.name = name;
        this.duration = duration;



    }
}
