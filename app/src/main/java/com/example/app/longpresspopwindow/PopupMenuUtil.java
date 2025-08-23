package com.example.app.longpresspopwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

public class PopupMenuUtil {
    
    private static PopupWindow popupWindow;

    public static void hidePopupMenu() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    public static void showPopupMenu(View anchorView, View menuView) {
        if (anchorView == null || menuView == null) {
            return;
        }
        
        hidePopupMenu();

        Context context = anchorView.getContext();
        if (context == null) {
            return;
        }
        LinearLayout popupView = new LinearLayout(context);
        popupView.setOrientation(LinearLayout.VERTICAL);

        // 添加向上箭头
        ImageView arrowUp = new ImageView(context);
        arrowUp.setId(View.generateViewId());
        LinearLayout.LayoutParams arrowUpParams = new LinearLayout.LayoutParams(
                dpToPx(context, 16),
                dpToPx(context, 8)
        );
        arrowUpParams.gravity = Gravity.CENTER_HORIZONTAL;
        arrowUp.setLayoutParams(arrowUpParams);
        arrowUp.setImageResource(R.drawable.popup_arrow_down);
        arrowUp.setVisibility(View.INVISIBLE);
        arrowUp.setRotationX(180f);
        popupView.addView(arrowUp);

        LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        popupView.addView(menuView, menuParams);

        // 添加向下箭头
        ImageView arrowDown = new ImageView(context);
        arrowDown.setId(View.generateViewId());
        LinearLayout.LayoutParams arrowDownParams = new LinearLayout.LayoutParams(
                dpToPx(context, 16),
                dpToPx(context, 8)
        );
        arrowDownParams.gravity = Gravity.CENTER_HORIZONTAL;
        arrowDown.setLayoutParams(arrowDownParams);
        arrowDown.setImageResource(R.drawable.popup_arrow_down);
        arrowDown.setVisibility(View.INVISIBLE);
        popupView.addView(arrowDown);

        popupWindow = new PopupWindow(
                popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        // 设置背景和动画
        popupWindow.setFocusable(false); // true拦截外部事件，false外部事件会穿透
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);

        // 计算PopupWindow位置和箭头方向
        calculateAndShowPopupWithArrow(popupWindow, popupView, anchorView, arrowUp, arrowDown);
    }

    private static void calculateAndShowPopupWithArrow(PopupWindow popupWindow, View popupView, View anchorView, View arrowUp, View arrowDown) {
        if (popupWindow == null || popupView == null || anchorView == null) {
            return;
        }
        
        // 测量popupView以获取实际尺寸
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = popupView.getMeasuredWidth();
        int popupHeight = popupView.getMeasuredHeight();

        // 获取anchorView在屏幕中的位置
        int[] anchorLocation = new int[2];
        anchorView.getLocationOnScreen(anchorLocation);
        int anchorX = anchorLocation[0];
        int anchorY = anchorLocation[1];
        int anchorWidth = anchorView.getWidth();
        int anchorHeight = anchorView.getHeight();

        // 获取屏幕尺寸
        android.util.DisplayMetrics displayMetrics = anchorView.getContext().getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        // 获取状态栏高度
        int statusBarHeight = getStatusBarHeight(anchorView.getContext());
        int usableScreenHeight = screenHeight - statusBarHeight;

        // 安全边距
        int leftMargin = dpToPx(anchorView.getContext(), 5);
        int rightMargin = dpToPx(anchorView.getContext(), 5);
        int topMargin = dpToPx(anchorView.getContext(), 5);
        int bottomMargin = dpToPx(anchorView.getContext(), 5);
        
        // 计算水平位置 - 与聊天item相对居中
        int centerX = anchorX + anchorWidth / 2;
        int x = centerX - popupWidth / 2;
        
        // 检查左右边界
        if (x < leftMargin) {
            x = leftMargin; // 左边界限制
        } else if (x + popupWidth > screenWidth - rightMargin) {
            x = screenWidth - popupWidth - rightMargin; // 右边界限制
        }

        // 计算垂直位置和箭头方向
        int y;
        int verticalOffset = dpToPx(anchorView.getContext(), 3); // 紧贴消息显示
        boolean showAbove = false;
        
        // 尝试在上方显示
        int topY = anchorY - popupHeight - verticalOffset;
        
        // 检查上方是否有足够空间
        if (topY >= statusBarHeight + topMargin) {
            // 上方有足够空间，显示在上方
            y = topY;
            showAbove = true;
            arrowDown.setVisibility(View.VISIBLE);
            arrowUp.setVisibility(View.INVISIBLE);
        } else {
            // 上方空间不足，显示在下方
            y = anchorY + anchorHeight + verticalOffset;
            showAbove = false;
            arrowUp.setVisibility(View.VISIBLE);
            arrowDown.setVisibility(View.INVISIBLE);
            
            // 检查下方是否会超出屏幕
            if (y + popupHeight > usableScreenHeight - bottomMargin) {
                // 下方也会超出，调整到屏幕底部
                y = usableScreenHeight - popupHeight - bottomMargin;
            }
        }

        // 调整箭头位置到消息中心位置
        adjustArrowPosition(popupView, arrowUp, arrowDown, centerX, x, showAbove);

        // 显示PopupWindow
        popupWindow.showAtLocation(anchorView, android.view.Gravity.NO_GRAVITY, x, y);
    }

    private static void adjustArrowPosition(View popupView, View arrowUp, View arrowDown, int targetCenterX, int popupX, boolean showAbove) {
        // 计算箭头应该的水平偏移量 - 相对于popup的位置
        int arrowCenterX = targetCenterX - popupX;
        int popupWidth = popupView.getMeasuredWidth();
        int arrowWidth = dpToPx(popupView.getContext(), 16); // 箭头宽度
        
        // 箭头位置限制：确保箭头始终在popup范围内，但不会超出popup边界
        // 最小位置：箭头宽度的一半
        // 最大位置：popup宽度减去箭头宽度的一半
        int minArrowX = arrowWidth / 2;
        int maxArrowX = popupWidth - arrowWidth / 2;
        
        // 限制箭头位置在popup范围内
        arrowCenterX = Math.max(minArrowX, arrowCenterX);
        arrowCenterX = Math.min(maxArrowX, arrowCenterX);
        
        // 设置箭头的水平位置
        View activeArrow = showAbove ? arrowDown : arrowUp;
        if (activeArrow != null) {
            android.widget.LinearLayout.LayoutParams params = 
                (android.widget.LinearLayout.LayoutParams) activeArrow.getLayoutParams();
            
            // 设置左边距，使箭头居中在计算的位置
            params.leftMargin = arrowCenterX - arrowWidth / 2;
            params.rightMargin = 0; // 清除右边距
            params.gravity = android.view.Gravity.NO_GRAVITY; // 清除gravity设置
            
            activeArrow.setLayoutParams(params);
        }
    }

    private static int dpToPx(Context context, int dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private static int getStatusBarHeight(Context context) {
        int result = 20;
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception ignored) {

        }
        return result;
    }
}
