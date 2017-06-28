package stream.site90.xoolu.com.xoolutvandradio.Model;



public class TvDataModel {

    private String image;
    private String name;
    private String link;
    private String type;

    public TvDataModel(String image, String name, String link, String type) {
        this.image = image;
        this.name = name;
        this.link = link;
        this.type = type;
    }


    public TvDataModel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
