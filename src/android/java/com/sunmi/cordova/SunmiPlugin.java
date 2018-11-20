package com.sunmi.cordova;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sunmi.cordova.utils.AidlUtil;
import com.sunmi.cordova.utils.ESCUtil;
import com.sunmi.cordova.utils.Json;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONException;

public class SunmiPlugin extends CordovaPlugin {

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        AidlUtil.getInstance().connectPrinterService(webView.getContext());
    }

    @Override
    public boolean execute(String action, String args, CallbackContext callbackContext) throws JSONException {
        JSONArray ja = Json.toJA(args);
        if (ja.isEmpty()) {
            return false;
        }

        for (Object o : ja) {
            JSONObject jo = Json.toJO(o);

            if (jo.isEmpty()) {
                callbackContext.error("content is empty.");
                continue;
            }
            String text = jo.getString("text");
            int size = jo.getIntValue("size");
            boolean isBold = jo.getBooleanValue("isBold");
            boolean isUnderLine = jo.getBooleanValue("isUnderLine");
            String type = jo.getString("type");
            int height = jo.getIntValue("height");
            int width = jo.getIntValue("width");

            if (text == null) {
                callbackContext.error("text is nulls.");
                continue;
            }

            if ("printText".equals(type)) {
                AidlUtil.getInstance().sendRawData(ESCUtil.alignLeft());
                AidlUtil.getInstance().initPrinter();
                AidlUtil.getInstance().printText(text, size, isBold, isUnderLine);
            } else if ("print3Line".equals(type)) {
                AidlUtil.getInstance().print3Line();
            } else if ("printBarCode".equals(type)) {
                AidlUtil.getInstance().sendRawData(ESCUtil.alignCenter());
                AidlUtil.getInstance().initPrinter();
                AidlUtil.getInstance().printBarCode(text, 8, height, width, 2);
            } else if ("printQr".equals(type)) {
                AidlUtil.getInstance().sendRawData(ESCUtil.alignCenter());
                AidlUtil.getInstance().initPrinter();
                AidlUtil.getInstance().printQr(text, size, 3);
            } else {
                callbackContext.error("type is empty");
                continue;
            }

        }
        return true;
    }
}
