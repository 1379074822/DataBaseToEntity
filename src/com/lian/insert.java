package com.lian;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.application.ex.ClipboardUtil;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;

import java.util.ArrayList;

public class insert extends AnAction {
    @Override
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        String textInClipboard = ClipboardUtil.getTextInClipboard();
        if (null == textInClipboard || "".equals(textInClipboard)) {
            return;
        }
        SelectionModel model = mEditor.getSelectionModel();
        final String selectedText = model.getSelectedText();

        final Document document = mEditor.getDocument();
        // Work off of the primary caret to get the selection info
        Caret primaryCaret = mEditor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        String[] split1 = textInClipboard.split("TABLE")[1].split("\\(\n");
        String tableName = split1[0];
        String[] split2 = split1[1].split("PRIMARY")[0].trim().split("\n");

        ArrayList<String> strings = new ArrayList<>();
        for (String s : split2) {
            String s1 = s.split("`")[1];
            strings.add(s1);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\tinsert into  ").append(tableName).append("\n\t(");
        for (int i = 0; i < strings.size(); i++) {
            stringBuilder.append("\t").append("`").append(strings.get(i).trim()).append("`");
            if(i!=strings.size()-1){
                stringBuilder.append(",");
            }
            if(i%4==0&&i!=0){
                stringBuilder.append("\n");
            }
        }
        stringBuilder.append("\t)\n").append("\tvalues(\n");
        for (int i = 0; i < strings.size(); i++) {
            String s = upperTable(strings.get(i).trim());
            stringBuilder.append("\t").append("#{").append(s).append("}");
            if(i!=strings.size()-1){
                stringBuilder.append(",");
            }
            if(i%4==0&&i!=0){
                stringBuilder.append("\n");
            }
        }
        stringBuilder.append("\t)");
        WriteCommandAction.runWriteCommandAction(project, () ->
                document.replaceString(start, end, stringBuilder)
        );


    }

    @SuppressWarnings("Duplicates")
    public static String upperTable(String str) {
        // 字符串缓冲区
        StringBuffer sbf = new StringBuffer();
        // 按下划线来切割字符串为数组
        String[] split = str.split("_");
        if (split.length == 1) {
            sbf.append(split[0]);
            return sbf.toString();
        }
        // 循环数组操作其中的字符串
        for (int i = 0, index = split.length; i < index; i++) {
            if (i == 0) {
                sbf.append(split[i]);
                continue;
            }
            // 递归调用本方法
            String upperTable = toBig(split[i]);
            // 添加到字符串缓冲区
            sbf.append(upperTable);
        }
        // 返回
        return sbf.toString();
    }

    public static String toBig(String str) {
        // 转换成字符数组
        char[] ch = str.toCharArray();
        // 判断首字母是否是字母
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            // 利用ASCII码实现大写
            ch[0] = (char) (ch[0] - 32);
        }
        return String.valueOf(ch);
    }
}
