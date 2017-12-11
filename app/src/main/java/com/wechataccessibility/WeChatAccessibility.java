package com.wechataccessibility;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.os.Parcelable;
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
            String layoutId = "com.tencent.mm:id/dcs";
            String buttonId = "com.tencent.mm:id/bps";
            AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
            AccessibilityNodeInfo layoutNode = AccessibilityHelper.findNodeInfosById(nodeInfo, layoutId);
            if (layoutNode != null) {
                AccessibilityNodeInfo targetNode = AccessibilityHelper.findNodeInfosById(layoutNode, buttonId);
                if (targetNode != null) {
                    AccessibilityHelper.performClick(targetNode);
                }
            }

            //openHongBao(event);
        } else if(eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {

        }
    }

    @Override
    public void onInterrupt() {

    }
}
