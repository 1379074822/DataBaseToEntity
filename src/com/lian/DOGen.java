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

public class DOGen extends AnAction {

    @Override
    @SuppressWarnings("Duplicates")
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
        String[] split1 = textInClipboard.split("\n");
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<String> strings2 = new ArrayList<>();
        ArrayList<String> strings3 = new ArrayList<>();
        for (String s : split1) {
            String s1 = s.split("`")[1];
            String s2 = s.split("COMMENT")[1].replace("'", "").replace(",", "");
            String s3 = s.split("`")[2].split("\\(")[0];
            strings.add(s1);
            strings2.add(s2);
            strings3.add(s3);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strings.size(); i++) {
            String s = upperTable(strings.get(i).trim());
                stringBuilder.append("\t");
            if (strings.get(i).equals("id") ) {
                stringBuilder.append("/**\n" + "     * ").append(strings2.get(i)).append("\n").append("     */\n").append("\t@Column(name = \"").append(strings.get(i)).append("\")\n")
                        .append("\t@Id\n" +
                                "    @GeneratedValue(generator = \"JDBC\")\n").append("private ").append(MysqlEnum.getJavaByCode(strings3.get(i))).append("  ").append(s).append(";\n");
            } else {
                stringBuilder.append("/**\n" + "     * ").append(strings2.get(i)).append("\n").append("     */\n").append("\t@Column(name = \"").append(strings.get(i)).append("\")\n")
                        .append("\tprivate ").append(MysqlEnum.getJavaByCode(strings3.get(i))).append("  ").append(s).append(";\n");
            }

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

    public static void main(String[] args) {
        char[] chars = "avb".toCharArray();
        System.out.println(String.valueOf(chars));
    }
}
