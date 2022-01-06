package com.gc.sam;

import com.gc.sam.banner.BannerDTO;
import com.google.gson.Gson;

import java.util.List;

/**
 * @author: geyuecang
 * @time: 2022/1/5 4:59 下午
 * @des:
 **/
public class DataManager {
    private static String bannerJson = "{[{\n" +
            "\t\t\t\"scene\": 1,\n" +
            "\t\t\t\"img_url\": \"https:\\/\\/static0.xesimg.com\\/puffty\\/zhongshiping\\/auth_banner.png\",\n" +
            "\t\t\t\"jump_url\": \"xeswxvideo:\\/\\/xrsApp?m=webbrowser&d={\\\"p\\\":{\\\"url\\\":\\\"https%3A%2F%2Flogin.xueersi.com%3Fwarrant%3D11%26gu_ml%3D1%26encryptedUserId%3DFvz3wzgt3YnoSGx92xC7ow%26redirect_url%3D%26bl_id%3D50\\\"}}\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"scene\": 0,\n" +
            "\t\t\t\"img_url\": \"https:\\/\\/aliallrecord.wangxiao.eaydu.com\\/sourcetest\\/zhuanyong\\/2021\\/11\\/19\\/17\\/16\\/45\\/b33fa2041f2989f58a25dca2a6a35025.png\",\n" +
            "\t\t\t\"jump_url\": \"https:\\/\\/login.xueersi.com?warrant=11&gu_ml=1&encryptedUserId=${encryptedUserId}&redirect_url=${_redirectUrl}&bl_id=50\"\n" +
            "\t\t}],\n" +
            "\t\t\"head_img\": {\n" +
            "\t\t\t\"title\": \"初中重难点\",\n" +
            "\t\t\t\"img_url\": \"\",\n" +
            "\t\t\t\"jump_url\": \"\"\n" +
            "\t\t}}";

    public static List<BannerDTO> getBanner() {
        return new Gson().fromJson(bannerJson, List.class);
    }
}
