package com.gc.sam.banner;

/**
 * @author: geyuecang
 * @time: 2022/1/5 11:36 上午
 * @des: banner数据类
 **/
public class BannerDTO {
    private String img_url;
    private String jump_url;

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getJump_url() {
        return jump_url;
    }

    public void setJump_url(String jump_url) {
        this.jump_url = jump_url;
    }
}
