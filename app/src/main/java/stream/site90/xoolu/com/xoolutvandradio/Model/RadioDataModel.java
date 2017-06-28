package stream.site90.xoolu.com.xoolutvandradio.Model;


import android.os.Parcel;
import android.os.Parcelable;

public class RadioDataModel implements Parcelable {

    private String image;
    private String name;
    private String link;

    public RadioDataModel(String image, String name, String link) {
        this.image = image;
        this.name = name;
        this.link = link;
    }

    public RadioDataModel() {
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



    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(name);
        dest.writeString(link);

    }

    public static final Parcelable.Creator<RadioDataModel> CREATOR
            = new Parcelable.Creator<RadioDataModel>() {
        public RadioDataModel createFromParcel(Parcel in) {
            return new RadioDataModel(in);
        }

        public RadioDataModel[] newArray(int size) {
            return new RadioDataModel[size];
        }
    };

    private RadioDataModel(Parcel in) {
        image=in.readString();
        name=in.readString();
        link=in.readString();
    }


}
