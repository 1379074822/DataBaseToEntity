package com.lian;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import com.intellij.openapi.project.Project;
import org.apache.http.util.TextUtils;

public class GenWhereTrans extends AnAction {

    @Override
    @SuppressWarnings("Duplicates")
    public void actionPerformed(AnActionEvent e) {
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (null == mEditor) {
            return;
        }
        SelectionModel model = mEditor.getSelectionModel();
        final String selectedText = model.getSelectedText();
        if (TextUtils.isEmpty(selectedText)) {
            return;
        }
        final Document document = mEditor.getDocument();
        Caret primaryCaret = mEditor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        String[] split = selectedText.split("\n");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\twhere\n\t1=1\n");
        for (int i = 0; i < split.length; i++) {
            String s = upperTable(split[i].trim());
            String ss = split[i];

                stringBuilder.append("\t");

            stringBuilder.append("<if test=\"").append(s).append(" != null");
            if(!s.toLowerCase().contains("time")&&!s.toLowerCase().contains("date")){
                stringBuilder.append(" and ").append(s).append("!= ''");
            }
            stringBuilder.append("\">\n\t\tand ").append(ss.trim()).append(" = #{").append(s).append("}\n");
            stringBuilder.append("\t</if>\n");
        }
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
