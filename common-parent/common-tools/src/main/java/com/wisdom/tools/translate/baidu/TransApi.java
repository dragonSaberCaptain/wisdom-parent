package com.wisdom.tools.translate.baidu;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransApi {
    // 在平台申请的APP_ID 详见 http://api.fanyi.baidu.com/api/trans/product/desktop?req=developer
    private static final String TRANS_API_HOST = "http://api.fanyi.baidu.com/api/trans/vip/translate";

    private static final String APP_ID = "20171225000108426";
    private static final String SECURITY_KEY = "UUH1pZ0pl1ih43ZzS_MC";

    public static String getTransResult(String query, String from, String to) {
        Map<String, String> params = buildParams(query, from, to);
        return HttpGet.get(TRANS_API_HOST, params);
    }

    private static Map<String, String> buildParams(String query, String from, String to) {
        Map<String, String> params = new HashMap<>();
        params.put("q", query);
        params.put("from", from);
        params.put("to", to);

        params.put("appid", APP_ID);

        // 随机数
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("salt", salt);

        // 签名
        String src = APP_ID + query + salt + SECURITY_KEY; // 加密前的原文
        params.put("sign", MD5.md5(src));

        return params;
    }

    public static String transDealZhTOEn(String str) throws Exception {
        return transDealBase(str, "zh", "en");
    }

    public static String transDealAutoTOEn(String str) throws Exception {
        return transDealBase(str, "auto", "en");
    }

    public static String transDealEnTOPt(String str) throws Exception {
        return transDealBase(str, "en", "pt");
    }

    public static String transDealAutoTOPt(String str) throws Exception {
        return transDealBase(str, "auto", "pt");
    }


    public static String transDealBase(String str, String from, String to) throws Exception {
        if (StrUtil.isNotBlank(str)) {
            String transResult1 = TransApi.getTransResult(str, from, to);
            TransResult transResult = JSONUtil.toBean(transResult1, TransResult.class);
            List<TranslateData> data = transResult.getTrans_result();
            if (data.size() > 0) {
                str = transResult.getTrans_result().get(0).getDst();
            }
            Thread.sleep(1500);
        }
        return str;
    }
}
