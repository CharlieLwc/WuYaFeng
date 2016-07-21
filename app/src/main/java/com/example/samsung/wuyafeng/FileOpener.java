package com.example.samsung.wuyafeng;

import java.io.File; //导入相关包

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by matlab_user on 2016/7/20.
 */
public class FileOpener {

    String currPath;                                    // 当前路径字符串
    String rootPath = "/mnt/shell/emulated/0/360/";         // 根目录路径

    void initFileOpener(final AppCompatActivity appCompatActivity, Button bFolder, final View.OnClickListener returnListener)
    {

        bFolder.setOnClickListener								//为打开按钮添加监听器
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        changeInterface(appCompatActivity, returnListener);
                    }
                });



    }

    private void changeInterface(final AppCompatActivity appCompatActivity, View.OnClickListener returnListener)
    {

        appCompatActivity.setContentView(R.layout.folder_view);

        Button buttonReturn = (Button)appCompatActivity.findViewById(R.id.button_Return);	//获取打开Button

        buttonReturn.setOnClickListener(returnListener);

        Button buttonUp = (Button) appCompatActivity.findViewById(R.id.button_Up);
        final ListView listViewFloder = (ListView) appCompatActivity.findViewById(R.id.listView_Floder);
        final TextView textViewPath =  (TextView) appCompatActivity.findViewById(R.id.textView_Path); // 获取ListView

        final File[] files = getFiles(rootPath); // 调用getFiles方法获取根目录下文件列表
        currPath = rootPath;
        textViewPath.setText("当前路径：" + currPath);//设置当前路径
        initListView(appCompatActivity, files, listViewFloder, textViewPath); // 初始化显示列表
        buttonUp.setOnClickListener // 返回按钮监听器
                (new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!currPath.equals(rootPath)) {// 若当前路径不是根目录，返回到上一层目录
                            File f = new File(currPath); // 获取当前路径下的文件列表
                            f = f.getParentFile(); // 获取当前路径的上层路径
                            currPath = f.getPath(); // 更改当前路径
                            textViewPath.setText("当前路径：" + currPath);//设置当前路径
                            initListView(appCompatActivity, getFiles(currPath), listViewFloder, textViewPath); // 初始化显示列表
                        }
                    }
                });

    }






    // 获取当前目录下的文件列表的方法
    public File[] getFiles(String filePath) {
        File[] files = new File(filePath).listFiles();// 获取当前目录下的文件列表
        return files; // 返回文件列表
    }

    // 初始化ListView进行显示
    public void initListView(final AppCompatActivity appCompatActivity, final File[] files, final ListView lv, final TextView textViewPath) {
        // 当文件列表不为空时
        if (files != null) {
            if (files.length == 0) {// 当前目录为空
                File f = new File(currPath); // 获取当前路径对应文件列表
                f = f.getParentFile(); // 获取上层路径
                currPath = f.getPath(); // 记录当前路径
                textViewPath.setText("当前路径：" + currPath);//设置当前路径
                Toast.makeText(appCompatActivity, "该文件夹为空！！", Toast.LENGTH_SHORT).show();
            } else {
                BaseAdapter ba = new BaseAdapter()// 创建适配器
                {
                    @Override
                    public int getCount() {
                        return files.length;
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int arg0, View arg1, ViewGroup arg2) {
                        LinearLayout ll = new LinearLayout(
                                appCompatActivity); // 创建LinearLayout
                        ll.setOrientation(LinearLayout.VERTICAL); // 竖直排列
                        ll.setPadding(5, 5, 5, 5); // 设置留白
                        TextView tv = new TextView(appCompatActivity); // 创建TextView
                        tv.setTextColor(Color.BLACK); // 设置字体颜色
                        tv.setText(files[arg0].getName()); // 添加文字为文件名称
                        tv.setGravity(Gravity.LEFT); // 左对齐
                        tv.setTextSize(16); // 字体大小
                        ll.addView(tv); // 添加TextView
                        return ll; // 返回LinearLayout
                    }
                };
                lv.setAdapter(ba); // 为ListView设置适配器
                lv.setOnItemClickListener // 为ListView添加监听器
                        (new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1,
                                                    int arg2, long arg3) {
                                currPath = files[arg2].getPath(); // 获取点击的文件名称的当前路径
                                textViewPath.setText("当前路径：" + currPath);//设置当前路径
                                File[] fs = getFiles(currPath); // 获取当前路径的文件列表
                                initListView(appCompatActivity, fs, lv, textViewPath); // 初始化ListView
                            }
                        });
            }
        } else {
            File f = new File(currPath); // 获取当前文件列表的路径对应的文件
            f = f.getParentFile(); // 获取父目录文件
            currPath = f.getPath(); // 记录当前文件列表路径
            textViewPath.setText("当前路径：" + currPath);//设置当前路径
            Toast.makeText(appCompatActivity, "该目录不是文件夹或无权限访问！", Toast.LENGTH_SHORT).show();
        }
    }

}
