package com.example.user.changeskin_master;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;


public class MainActivity extends AppCompatActivity {

    /**
     * 需要替换主题的控件
     * 这里就列举三个：TextView,ImageView,LinearLayout
     */
    private ImageView imgV;
    ClassLoader classLoader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgV = (ImageView)findViewById(R.id.imageview);

        findViewById(R.id.btn1).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        moveFile();
                    }
                }).start();
            }});

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String filesDir = getCacheDir().getAbsolutePath();
                String filePath = filesDir + File.separator +"apk1.apk";
                String deoxPath=filePath+File.separator+"opt_dex";
                File fopt = new File(deoxPath);
                if(!fopt.exists()){
                    fopt.mkdirs();
                }
                classLoader=new DexClassLoader(filePath,deoxPath,null,getClassLoader());
                loadResources(filePath);
                setContent();
            }
        });
    }
    private void moveFile() {
            //目录：/data/data/packageName/odex
            String fileDir = getCacheDir().getAbsolutePath();
            //往该目录下面放置我们修复好的dex文件。
            String name = "apk1.apk";
            String filePath = fileDir+File.separator+name;
            File file= new File(filePath);
            if(file.exists()){
                file.delete();
            }
            //搬家：把下载好的在SD卡里面的修复了的classes2.dex搬到应用目录filePath
            InputStream is = null;
            FileOutputStream os = null;
            try{
                is = new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+name);
                os = new FileOutputStream(filePath);
                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len=is.read(buffer))!=-1){
                    os.write(buffer,0,len);
                }
                File f = new File(filePath);
                if(f.exists()){
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * 动态加载主题包中的资源，然后替换每个控件
     */
    @SuppressLint("NewApi")
    private void setContent(){
        try{
            Class clazz = classLoader.loadClass("com.example.user.skinresource_master.UIUtil");
            Method method = clazz.getMethod("getImageDrawable",Context.class);
            Drawable drawable = (Drawable)method.invoke(null, this);
            imgV.setBackground(drawable);
//            Class clazz=mResources.getClass();
//            Method method= clazz.getDeclaredMethod("getDrawable",int.class);
//            method.setAccessible(true);
//            clazz.
//           Drawable drawable= (Drawable) method.invoke(mResources,);
//          imgV.setBackground(drawable);
        }catch(Exception e){
            Log.i("Loader", "error:"+Log.getStackTraceString(e));
        }
    }
    /**
     * 另外的一种方式获取
     */
    private void setContent1(){
        int stringId = getTextStringId();
        int drawableId = getImgDrawableId();
        int layoutId = getLayoutId();
        Log.i("Loader", "stringId:"+stringId+",drawableId:"+drawableId+",layoutId:"+layoutId);
    }

    @SuppressLint("NewApi")
    private int getTextStringId(){
        try{
            Class clazz = classLoader.loadClass("com.example.resourceloaderapk1.R$string");
            Field field = clazz.getField("app_name");
            int resId = (int)field.get(null);
            return resId;
        }catch(Exception e){
            Log.i("Loader", "error:"+Log.getStackTraceString(e));
        }
        return 0;
    }

    @SuppressLint("NewApi")
    private int getImgDrawableId(){
        try{
            Class clazz = classLoader.loadClass("com.example.resourceloaderapk1.R$drawable");
            Field field = clazz.getField("ic_launcher");
            int resId = (int)field.get(null);
            return resId;
        }catch(Exception e){
            Log.i("Loader", "error:"+Log.getStackTraceString(e));
        }
        return 0;
    }

    @SuppressLint("NewApi")
    private int getLayoutId(){
        try{
            Class clazz = classLoader.loadClass("com.example.resourceloaderapk1.R$layout");
            Field field = clazz.getField("activity_main");
            int resId = (int)field.get(null);
            return resId;
        }catch(Exception e){
            Log.i("Loader", "error:"+Log.getStackTraceString(e));
        }
        return 0;
    }

    protected AssetManager mAssetManager;//资源管理器
    protected Resources mResources;//资源
    protected Resources.Theme mTheme;//主题

    protected void loadResources(String dexPath) {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager,dexPath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(),superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
      return mResources == null ? super.getResources() : mResources;
    }
    @Override
    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }

    @SuppressLint("NewApi")
    private void printResourceId(){
        try{
            Class clazz = classLoader.loadClass("com.example.resourceloaderapk.UIUtil");
            Method method = clazz.getMethod("getTextStringId", null);
            Object obj = method.invoke(null, null);
            Log.i("Loader", "stringId:"+obj);
            Log.i("Loader", "newId:"+R.string.app_name);
            method = clazz.getMethod("getImageDrawableId", null);
            obj = method.invoke(null, null);
            Log.i("Loader", "drawableId:"+obj);
            Log.i("Loader", "newId:"+R.drawable.ic_launcher);
            method = clazz.getMethod("getLayoutId", null);
            obj = method.invoke(null, null);
            Log.i("Loader", "layoutId:"+obj);
            Log.i("Loader", "newId:"+R.layout.activity_main);
        }catch(Exception e){
            Log.i("Loader", "error:"+Log.getStackTraceString(e));
        }
    }

    private void printRField(){
        Class clazz = R.id.class;
        Field[] fields = clazz.getFields();
        for(Field field : fields){
            Log.i("Loader", "fields:"+field);
        }
        Class clazzs = R.layout.class;
        Field[] fieldss = clazzs.getFields();
        for(Field field : fieldss){
            Log.i("Loader", "fieldss:"+field);
        }
    }

}