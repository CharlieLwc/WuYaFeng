package com.example.samsung.wuyafeng;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by matlab_user on 2016/7/21.
 */
public class AssetOpener {



    public void initAssetOpener(final AppCompatActivity appCompatActivity, Button bAsset, final View.OnClickListener returnListener)
    {

        bAsset.setOnClickListener								//为打开按钮添加监听器
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeInterface(appCompatActivity, returnListener);
                    }
                });
    }

    private void changeInterface(final AppCompatActivity appCompatActivity, View.OnClickListener returnListener)
    {

        appCompatActivity.setContentView(R.layout.asset_view);

        Button buttonOpen = (Button)appCompatActivity.findViewById(R.id.button_Open);	//获取打开Button
        Button buttonReturn = (Button)appCompatActivity.findViewById(R.id.button_Return);	//获取打开Button
        final EditText editTextName = (EditText) appCompatActivity.findViewById(R.id.editText_Name);
        final EditText editTextAsset = (EditText) appCompatActivity.findViewById(R.id.editText_Asset);

        buttonReturn.setOnClickListener	(returnListener);

        buttonOpen.setOnClickListener								//为打开按钮添加监听器
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //调用loadText方法获取对应文件名的文件
                        String nr=loadText(appCompatActivity, editTextName.getText().toString().trim());
                        //设置显示框内容
                        editTextAsset.setText(nr);
                    }
                });

    }



    public String loadText(final AppCompatActivity appCompatActivity, String name)						//加载assets文件方法
    {
        String nr=null;    									//内容字符串
        try
        {	//打开对应名称文件的输入流
            InputStream is=appCompatActivity.getResources().getAssets().open(name);
            int ch=0;
            //创建字节数组输出流
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            while((ch=is.read())!=-1)
            {	baos.write(ch);		}						//读取文件
            byte[] buff=baos.toByteArray();					//转化为字节数组
            baos.close();									//关闭输入输出流
            is.close();										//关闭输入输出流
            nr=new String(buff,"utf-8");					//转码生产新字符串
            nr=nr.replaceAll("\\r\\n","\n");				//替换换行符等空白字符
        }
        catch (Exception e)
        {
            //没有找到对应文件，进行提示
            Toast.makeText(appCompatActivity.getBaseContext(), "对不起，没有找到指定文件。", Toast.LENGTH_LONG).show();
        }
        return nr;    										//返回内容字符串
    }

}


