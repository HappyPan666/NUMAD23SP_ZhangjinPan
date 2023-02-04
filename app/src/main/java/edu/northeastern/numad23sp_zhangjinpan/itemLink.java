package edu.northeastern.numad23sp_zhangjinpan;

public class itemLink implements itemClickListener{

    private final String webName;
    private final String webLink;

    public itemLink(String webName, String webLink) {
        this.webName = webName;
        this.webLink = webLink;
    }

    public String getWebName(){return webName;}
    public String getWebLink(){return webLink;}

    @Override
    public void onClick(int position) {

    }



}
