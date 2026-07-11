package com.wmp.countdown.CTComponent.CTPanel.setsPanel;

import com.wmp.publicTools.appFileControl.CTInfoControl;
import com.wmp.publicTools.appFileControl.IconControl;
import com.wmp.publicTools.printLog.Log;
import com.wmp.countdown.CTComponent.CTButton.CTTextButton;
import com.wmp.countdown.CTComponent.CTTable;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public abstract class CTTableSetsPanel<T> extends CTBasicSetsPanel<T>{

    private final CTTable table = new CTTable();
    private final String[] titleArray;

    public CTTableSetsPanel(String[] titleArray, String[][] array, CTInfoControl<T> infoControl) {
        super(infoControl);
        setName("表格设置页");
        this.titleArray = titleArray;

        initTable(titleArray, array);
    }

    private void initTable(String[] titleArray, String[][] array){
        this.removeAll();

        this.setLayout(new BorderLayout());

        DefaultTableModel model = null;
        model = new DefaultTableModel(Objects.requireNonNullElseGet(array, () -> new String[][]{}),
                titleArray);
        table.setModel(model);
        //禁用编辑
        table.setCellEditor( null);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        this.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        //新建
        {

            CTTextButton newBtn = new CTTextButton("添加");
            newBtn.setIcon("通用.添加", IconControl.COLOR_COLORFUL, 30, 30);
            DefaultTableModel finalModel = model;
            newBtn.addActionListener(e -> {
                try {
                    String[] strings = addToTable();

                    if (strings != null) {
                        finalModel.addRow(strings);
                    }
                } catch (Exception ex) {
                    Log.err.print(this.getClass(), "添加数据失败", ex);
                }
            });
            buttonPanel.add(newBtn);
        }

        // 删除
        {
            CTTextButton deleteBtn = new CTTextButton("删除");
            deleteBtn.setIcon("通用.删除", IconControl.COLOR_COLORFUL, 30, 30);
            DefaultTableModel finalModel1 = model;
            deleteBtn.addActionListener(_ -> deleteToTable(finalModel1));


            buttonPanel.add(deleteBtn);
        }

        // 修改
        {
            CTTextButton removeBtn = new CTTextButton("修改");
            removeBtn.setIcon("通用.刷新", IconControl.COLOR_COLORFUL, 30, 30);
            DefaultTableModel finalModel1 = model;
            removeBtn.addActionListener(_ -> {
                try {
                    int selectedRow = table.getSelectedRow();
                    String[] strings = null;
                    if (selectedRow != -1) {
                        strings = new String[finalModel1.getColumnCount()];
                        for (int i = 0; i < finalModel1.getColumnCount(); i++) {
                            strings[i] = finalModel1.getValueAt(selectedRow, i).toString();
                        }
                        String[] newArray = modifyToTable(strings);
                        if (newArray != null) {
                            for (int i = 0; i < finalModel1.getColumnCount(); i++) {
                                finalModel1.setValueAt(newArray[i], selectedRow, i);
                            }
                        }
                    }
                } catch (Exception e) {
                    Log.err.print(this.getClass(), "修改数据失败", e);
                }

            });


            buttonPanel.add(removeBtn);
        }

        JScrollPane scrollPane1 = new JScrollPane(buttonPanel);
        scrollPane1.setOpaque(false);
        scrollPane1.getViewport().setOpaque(false);
        this.add(scrollPane1, BorderLayout.SOUTH);
    }

    public String[] addToTable(){
        ArrayList<String> arrays = new ArrayList<>();

        for (String string : titleArray) {

            String s = Log.info.showInputDialog(this, "CTTableSetsPanel-新建", "请输入" + string);
            if (s == null || s.trim().isEmpty()) {
                return null;
            }

            arrays.add(s);

        }
        return arrays.toArray(new String[0]);
    }

    public void deleteToTable(DefaultTableModel model){
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            model.removeRow(selectedRow);
        }
    }

    public String[] modifyToTable(String[] oldArray){
        ArrayList<String> arrays = new ArrayList<>();

        for (int i = 0; i < titleArray.length; i++) {
            String s = Log.info.showInputDialog(this, "CTTableSetsPanel-修改",
                    String.format("原数据:%s\n请输入%s\n注:若不修改不用输入内容", oldArray[i], titleArray[i])
                    );
            if (s == null || s.trim().isEmpty()) {
                arrays.add(oldArray[i]);
            }else arrays.add(s);
        }
        return arrays.toArray(new String[0]);
    }

    /**
     * 获取表格数据
     * @return 数据第一行为标题
     */
    public String[][] getArray() {
        Object[][] data = table.getData();

        String[][] result = new String[data.length + 1][data[0].length];
        result[0] = titleArray;
        for (int i = 0; i < data.length; i++) {
            ArrayList<String> arrays = new ArrayList<>();
            for (int j = 0; j < data[i].length; j++) {
                arrays.add(data[i][j].toString());
            }
            result[i + 1] = arrays.toArray(new String[0]);
        }
        return result;
    }

    public void setArray(String[][] array){
        this.removeAll();
        initTable(titleArray, array);

        this.revalidate();
        this.repaint();
    }
    @Override
    public void refresh() {
        if (getInfoControl() != null)
            getInfoControl().refresh();

        this.removeAll();
        String[][] data = resetData();
        //初始化
        initTable(titleArray, data == null?table.getStrData(): data);

        this.revalidate();
        this.repaint();
    }

    /**
     * 重置数据 在调用刷新时需要调用<br>
     * 若生成的数据异常,请返回null
     */
    abstract public String[][] resetData();
}
