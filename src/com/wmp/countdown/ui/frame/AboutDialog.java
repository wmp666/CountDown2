package com.wmp.countdown.ui.frame;

import com.wmp.WCompanent.WIconButton;
import com.wmp.WCompanent.WTextButton;
import com.wmp.WCompanent.tools.GetIcon;
import com.wmp.countdown.tools.CDInfo;
import com.wmp.countdown.tools.OpenInExp;
import com.wmp.countdown.tools.io.GetPath;
import com.wmp.countdown.tools.printLog.Log;
import com.wmp.countdown.tools.uiTools.CDFont;
import com.wmp.countdown.tools.uiTools.CDFontSizeStyle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutDialog extends JDialog{


    public AboutDialog() throws MalformedURLException {

        this.setTitle("关于");
        this.setSize(300, 400);
        this.setLayout(null);
        this.setLocationRelativeTo(null);
        this.setModal(true);
        this.setResizable(false);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        this.getContentPane().setBackground( CDInfo.COLOR.getBgColor());


        JLabel icon = new JLabel(GetIcon.getIcon(CDInfo.class.getResource(CDInfo.ICON_PATH), 100, 100));
        icon.setBounds(10, 10, 100, 100);

        JLabel title = new JLabel("程序名: " + CDInfo.NAME);
        title.setBounds(120, 10, 200, 20);
        title.setFont(CDFont.getCDFont(Font.BOLD, CDFontSizeStyle.SMALL));
        title.setForeground( CDInfo.COLOR.getTextColor());

        //将图标显示在文字上方
        JLabel version = new JLabel("版本: " + CDInfo.VERSION);
        version.setBounds(120, 40, 200, 20);
        version.setFont(CDFont.getCDFont(Font.BOLD, CDFontSizeStyle.SMALL));
        version.setForeground( CDInfo.COLOR.getTextColor());

        JLabel author = new JLabel("作者: " + CDInfo.AUTHOR);
        author.setBounds(120, 70, 200, 20);
        author.setFont(CDFont.getCDFont(Font.BOLD, CDFontSizeStyle.SMALL));
        author.setForeground( CDInfo.COLOR.getTextColor());

        //view = new JPanel();

        this.add(icon);
        this.add(title);
        this.add(version);
        this.add(author);

        initMenuBar();



    }

    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        this.setJMenuBar(menuBar);

        JMenu menu = new JMenu("转到");

        JMenu chat = new JMenu("社交");

        JMenuItem weChat = new JMenuItem("微信");
        weChat.setIcon(GetIcon.getIcon(getClass().getResource("/image/wechat.png"), 20, 20));
        weChat.addActionListener(e ->
                Log.info.message(this, "关于-个人信息", "微信: w13607088913")
        );

        JMenuItem qq = new JMenuItem("QQ");
        qq.setIcon(GetIcon.getIcon(getClass().getResource("/image/qq.png"), 20, 20));
        qq.addActionListener(e ->
                Log.info.message(this, "关于-个人信息", "QQ: 2134868121"));

        JMenuItem bilibili = new JMenuItem("哔哩哔哩");
        bilibili.setIcon(GetIcon.getIcon(getClass().getResource("/image/bilibili.png"), 20, 20));
        bilibili.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://space.bilibili.com/1075810224/"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        chat.add(weChat);
        chat.add(qq);
        chat.add(bilibili);

        JMenu github = new JMenu("Github");
        github.setIcon(GetIcon.getIcon(getClass().getResource("/image/github.png"), 20, 20));

        JMenuItem authorGithub = new JMenuItem("作者");
        authorGithub.setIcon(GetIcon.getIcon(getClass().getResource("/image/github.png"), 20, 20));
        authorGithub.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/wmp666"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        JMenuItem repo = new JMenuItem("仓库");
        repo.setIcon(GetIcon.getIcon(getClass().getResource("/image/github.png"), 20, 20));
        repo.addActionListener(e -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/wmp666/CountDown2"));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        github.add(authorGithub);
        github.add(repo);

        JMenuItem appPath = new JMenuItem("程序路径");
        appPath.setIcon(GetIcon.getIcon(getClass().getResource("/image/openExp.png"), 20, 20));
        appPath.addActionListener(e -> {
            OpenInExp.open(GetPath.getAppPath(GetPath.APPLICATION_PATH));

        });

        menu.add(chat);
        menu.add(github);
        menu.add(appPath);

        menuBar.add(menu);

        // 在现有菜单中添加

        JMenu downloadMenu = new JMenu("下载");

        //获取源代码
        JMenuItem getSource = new JMenuItem("获取源代码");
        getSource.addActionListener(e -> Log.info.message(this, "获取源代码", "请前往仓库寻找"));

        JMenuItem checkUpdate = new JMenuItem("检查更新");
        checkUpdate.setIcon(GetIcon.getIcon(getClass().getResource("/image/light/update_0.png"), 20, 20));
        checkUpdate.addActionListener(e -> Log.info.message(this, "检查更新", "请前往仓库寻找"));

        downloadMenu.add(getSource);
        downloadMenu.add(checkUpdate);

        menuBar.add(downloadMenu);

        JMenu helpMenu = new JMenu("帮助");

        JMenuItem helpDoc = new JMenuItem("帮助文档");
        helpDoc.setIcon(GetIcon.getIcon(getClass().getResource("/image/doc.png"), 20, 20));
        helpDoc.addActionListener(e -> {
            Log.info.message(null, "IOForInfo-帮助文档", "前面的区域,以后再来探索吧");

        });


        helpMenu.add(helpDoc);


        menuBar.add(helpMenu);
    }
}
