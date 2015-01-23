package com.aaln.contactsexample.app;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.koushikdutta.async.http.server.AsyncHttpServer;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by aaron on 1/17/15.
 */
public class ModListener {

    public static Application application;

    private static AsyncHttpServer server;

    private static SparseArray<View> inspectedViews = new SparseArray<>();

    public static void addListeners(final Activity activity) {
        View view = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        ArrayList<View> arr = getAllChildren(view);

        for(View v : arr) {

            // Index views by their id for later reference.
            inspectedViews.append(System.identityHashCode(v), v);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<HashMap<?,?>> valuesArr = new ArrayList<>();
                    LayoutParams lp = v.getLayoutParams();
                    MarginLayoutParams lpMargin = (ViewGroup.MarginLayoutParams) lp;

                    if (v instanceof Button) {
                        Button instance = (Button) v;

                        /****** Get TEXT of BUTTON ******/
                        String text = instance.getText().toString();

                        valuesArr.add(prepareViewAttr("text", text));

                        /******* GET TEXTCOLOR of BUTTON *******/
                        int textColor = instance.getCurrentTextColor();
                        valuesArr.add(prepareViewAttr("textColor", textColor));

                        /****** GET HEIGHT/WIDTH of BUTTON ******/
                        // LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
                        // MATCH_PARENT = -1
                        // WRAP_CONTENT = -2
                        System.out.println(instance.getHeight());
                        System.out.println(instance.getWidth());
                        System.out.println(lp.height);
                        System.out.println(lp.width);

                        int pixelHeight = instance.getHeight();
                        int pixelWidth = instance.getWidth();
                        int layoutHeight = lp.height;
                        int layoutWidth = lp.width;

                        valuesArr.add(prepareViewAttr("pixelHeight", pixelHeight));
                        valuesArr.add(prepareViewAttr("pixelWidth", pixelWidth));
                        valuesArr.add(prepareViewAttr("layoutHeight", layoutHeight));
                        valuesArr.add(prepareViewAttr("layoutWidth", layoutWidth));


                        /******* GET ALIGNMENT of BUTTON *******/
                        int textAlignment = instance.getTextAlignment();
                        valuesArr.add(prepareViewAttr("textAlignment", textAlignment));


                        /****** GET ALPHA of BUTTON *******/
                        float alpha = instance.getAlpha();
                        System.out.println("ALPHA " + alpha);
                        valuesArr.add(prepareViewAttr("alpha", alpha));

                        /****** GET MARGIN of BUTTON ******/
                        int topMargin = lpMargin.topMargin;
                        int rightMargin = lpMargin.rightMargin;
                        int bottomMargin = lpMargin.bottomMargin;
                        int leftMargin = lpMargin.leftMargin;
                        valuesArr.add(prepareViewAttr("topMargin", topMargin));
                        valuesArr.add(prepareViewAttr("rightMargin", rightMargin));
                        valuesArr.add(prepareViewAttr("bottomMargin", bottomMargin));
                        valuesArr.add(prepareViewAttr("leftMargin", leftMargin));




                        /****** GET PADDING of BUTTON *******/
                        int topPadding = instance.getPaddingTop();
                        int rightPadding = instance.getPaddingRight();
                        int bottomPadding = instance.getPaddingBottom();
                        int leftPadding = instance.getPaddingLeft();

                        valuesArr.add(prepareViewAttr("topPadding", topPadding));
                        valuesArr.add(prepareViewAttr("rightPadding", rightPadding));
                        valuesArr.add(prepareViewAttr("bottomPadding", bottomPadding));
                        valuesArr.add(prepareViewAttr("leftPadding", leftPadding));


                        int paddingStart = instance.getPaddingStart();
                        int paddingEnd = instance.getPaddingEnd();
                        valuesArr.add(prepareViewAttr("paddingStart", paddingStart));
                        valuesArr.add(prepareViewAttr("paddingEnd", paddingEnd));


                        /****** Get BACKGROUND of BUTTON ******/

                        // If button background is BitmapDrawable
                        if(instance.getBackground() instanceof BitmapDrawable) {
                            System.out.println(instance.getBackground());
                            //String btnName = v.getResources().getString();
                        }

                        // If button background is a ColorDrawable
                        if (instance.getBackground() instanceof ColorDrawable) {
                            ColorDrawable buttonColor = (ColorDrawable) instance.getBackground();
                            int backgroundColor = buttonColor.getColor();
                        }
                        HashMap reqBody = new HashMap<>();
                        reqBody.put("data", valuesArr);
                        reqBody.put("instance", "button");
                        reqBody.put("id", System.identityHashCode(v));

                        JSONObject obj = new JSONObject(reqBody);
                        com.aaln.contactsexample.app.ModServer.sendMessage(obj.toString());


                        // sendRequest(reqBody);

                    }

                    if (v instanceof TextView) {
                        TextView instance = (TextView) v;

                        instance.setTextColor(Color.RED);
                    }
                    if (v instanceof EditText) {
                        EditText instance = (EditText) v;
                        instance.setTextColor(Color.RED);
                    }
                    if (v instanceof RelativeLayout) {
                        RelativeLayout instance = (RelativeLayout) v;
                        instance.setBackgroundColor(Color.RED);
                    }
                    if (v instanceof LinearLayout) {
                        LinearLayout instance = (LinearLayout) v;
                        instance.setBackgroundColor(Color.RED);
                    }

                    System.out.println(valuesArr);
                }
            });
        }

    }
    private static HashMap prepareViewAttr(String key, String value) {
        HashMap map = new HashMap<>();
        map.put("type", "string");
        map.put("key", key);
        map.put("value", value);

        return map;
    }
    private static HashMap prepareViewAttr(String key, Float value) {
        HashMap map = new HashMap<>();
        map.put("type", "float");
        map.put("key", key);
        map.put("value", value);

        return map;
    }

    private static HashMap prepareViewAttr(String key, int value) {
        HashMap map = new HashMap<>();
        map.put("type", "int");
        map.put("key", key);
        map.put("value", value);
        return map;
    }


    private static ArrayList<View> getAllChildren(View v) {
        System.out.println("v123");
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    public static void getViewDetails(View v) {
        System.out.println(v.getClass().getName());
    }


    public static void updateView(int id, String key, final String value) {
        final View view = inspectedViews.get(id);
        Handler handler = new Handler(application.getMainLooper());
//        Button button = new Button(null);
        if (key.equals("text")) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Method setText = view.getClass().getMethod("setText", CharSequence.class);
                        setText.invoke(view, value);
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            };
            handler.post(runnable);
        }
    }
}


/*
        ModListener.test1();

        try {
            Iterator it = ClassPool.getDefault().appendSystemPath(activity);
            while(it.hasNext()) {
                System.out.println(it.next());
            }

            CtClass clazz = ClassPool.getDefault().get("Activity");
//            CtMethod m = point.getMethod("test1", "()V");
            CtMethod newMethod = CtNewMethod.make(
                    "public static void test1() { System.out.println(\"test2\" } }",
                    clazz);
            clazz.addMethod(newMethod);
        } catch (CannotCompileException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        ModListener.test1();
        Activity act = (Activity) v.getContext();
        Class x = v.getClass();
        Constructor[] constructors = x.getDeclaredConstructors();
        Field[] fields = x.getDeclaredFields();
        Method[] methods = x.getDeclaredMethods();
        */
        /*
            Class<?> c = Class.forName("classpath.and.classname");
            Object dog = c.newInstance();
            Method m = c.getDeclaredMethod("bark", new Class<?>[0]);
            m.invoke(dog);

            */


