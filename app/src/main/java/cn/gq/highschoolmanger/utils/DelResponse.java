package cn.gq.highschoolmanger.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.gq.highschoolmanger.config.MessageType;
import cn.gq.highschoolmanger.entity.Content_Icon;
import cn.gq.highschoolmanger.entity.MessageEntity;
import cn.gq.highschoolmanger.handler.MY_SYNHandler;

/***
 * 处理服务端传过来的Icon数据
 * TODO ：只在ManngerActivity和
 */
public class DelResponse {
 public static<T>  void delIconJsonObject (ArrayList <Content_Icon> icons, JSONObject data , MY_SYNHandler my_synHandler) throws JSONException {
     JSONArray data1 = data.getJSONArray("data");
     for (int i =0 ; i<data1.length() ; i++) {
         Content_Icon contentIcon = new Content_Icon();
         JSONObject json = (JSONObject) data1.get(i);
         contentIcon.iconName = (String)json.get("iconName");
         contentIcon.base64 = (String) json.get("image");
         icons.add(contentIcon);
     }
     my_synHandler.sendMessage(MessageEntity.getMessage(MessageType.ICON_MESSAGE,1) );
 }
}
