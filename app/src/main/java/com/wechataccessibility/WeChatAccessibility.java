package com.wechataccessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.wechataccessibility.util.AccessibilityHelper;

import java.util.List;

/**
 * @author
 * @Description:
 * @date 2017/12/10 22:34
 * @copyright
 */
public class WeChatAccessibility extends AccessibilityService {

    public static final String TAG = WeChatAccessibility.class.getSimpleName();

    public static final int PAGE1 = 1;
    public static final int PAGE2 = 2;
    public static final int PAGE3 = 3;
    public static final int PREPAGE4 = 4;
    public static final int PAGE4 = 40;
    public static final int PREPAGE5 = 5;
    public static final int PAGE5 = 50;
    public static final int PAGE6 = 6;

    private int lastPage = -1;
    private Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        //通知栏事件
        if(eventType == AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED) {
            Parcelable data = event.getParcelableData();
            if(data == null || !(data instanceof Notification)) {
                return;
            }

            List<CharSequence> texts = event.getText();
            if(!texts.isEmpty()) {
                String text = String.valueOf(texts.get(0));
                //notificationEvent(text, (Notification) data);
            }
        } else if(eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String itemNameTvId = "com.tencent.mm:id/aoj";//微信首页聊天记录
//            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            AccessibilityNodeInfo nodeInfo = event.getSource();
            if (nodeInfo == null) {
                return;
            }
            Log.d(TAG,"TYPE_WINDOW_STATE_CHANGED"+lastPage);

            //接听页面
            String layoutId = "com.tencent.mm:id/dcs";
            String buttonId = "com.tencent.mm:id/bps";
            AccessibilityNodeInfo layoutNode = AccessibilityHelper.findNodeInfosById(nodeInfo, layoutId);
            if (layoutNode != null) {
                AccessibilityNodeInfo targetNode = AccessibilityHelper.findNodeInfosById(layoutNode, buttonId);
                if (targetNode != null) {
                    AccessibilityHelper.performClick(targetNode);
                    Log.d(TAG,"5");
                    lastPage = PREPAGE5;
                    nodeInfo.recycle();
                    return;
                }
            }

            if (lastPage == PREPAGE5) {
                lastPage = PAGE5;
                if (nodeInfo != null) {
                    nodeInfo.recycle();
                }
                return;
            }

            if (lastPage == PAGE5) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lastPage = -1;
                    }
                },10*1000);
                if (nodeInfo != null) {
                    nodeInfo.recycle();
                }
                return;
            }

            if (lastPage == PREPAGE4) {
                lastPage = PAGE4;
                if (nodeInfo != null) {
                    nodeInfo.recycle();
                }
                return;
            }

            if (lastPage == PAGE4) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lastPage = -1;
                    }
                },10*1000);
                if (nodeInfo != null) {
                    nodeInfo.recycle();
                }
                return;
            }

            List<AccessibilityNodeInfo> datas = nodeInfo.findAccessibilityNodeInfosByViewId(itemNameTvId);
            if (datas != null) {
                SharedPreferences sp = getSharedPreferences("app", Context.MODE_PRIVATE);
                String chatName = sp.getString("chatname","");
                for(AccessibilityNodeInfo data:datas) {
                    if (chatName.equals(data.getText().toString())) {
                        AccessibilityHelper.performClick(data);
                        Log.d(TAG,"1");
                    }
                }
            }

            String plusId = "com.tencent.mm:id/aa6";//聊天页面的加号
            AccessibilityNodeInfo plusNode = AccessibilityHelper.findNodeInfosById(nodeInfo,plusId);
            if (plusNode != null) {
                AccessibilityHelper.performClick(plusNode);
                Log.d(TAG,"2");
            }

            String o4 = "com.tencent.mm:id/o4";//聊天页面点击加号后，各功能项的图标
            List<AccessibilityNodeInfo> icons = nodeInfo.findAccessibilityNodeInfosByViewId(o4);
            if (icons != null) {
                if (icons.size() > 2 ) {
                    AccessibilityHelper.performClick(icons.get(2));
                    Log.d(TAG,"3");
                }
            }

            String fy = "com.tencent.mm:id/fy";//对话框上的"视频聊天""语音聊天"
            List<AccessibilityNodeInfo> fys = nodeInfo.findAccessibilityNodeInfosByViewId(fy);
            if (fys != null) {
                if (fys.size() > 0 ) {
                    AccessibilityHelper.performClick(fys.get(0));
                    Log.d(TAG,"4");
                    lastPage = PREPAGE4;
                }
            }

            nodeInfo.recycle();
        }

    }

    @Override
    public void onInterrupt() {

    }
}
