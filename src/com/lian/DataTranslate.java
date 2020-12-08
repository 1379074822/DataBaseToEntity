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

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class DataTranslate extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        //获取项目和编辑器
        final Project project = e.getRequiredData(CommonDataKeys.PROJECT);
        final Editor mEditor = e.getData(PlatformDataKeys.EDITOR);
        if (null == mEditor) {
            return;
        }
        //获取编辑器内容
        SelectionModel model = mEditor.getSelectionModel();
         String selectedText = model.getSelectedText();
        if (selectedText == null || selectedText.isEmpty()) {
            mEditor.getSelectionModel().selectWordAtCaret(true);
//            boolean moveLeft = true;
//            boolean moveRight = true;
//            int start = mEditor.getSelectionModel().getSelectionStart();
//            int end = mEditor.getSelectionModel().getSelectionEnd();
//            Pattern p = Pattern.compile("[^A-z0-9.\\-]");
//
//            // move caret left
//            while (moveLeft && start != 0) {
//                start--;
//                EditorActionUtil.moveCaretToLineStart(mEditor, true);
//                Matcher m = p.matcher(Objects.requireNonNull(mEditor.getSelectionModel().getSelectedText()));
//                if (m.find()) {
//                    start++;
//                    moveLeft = false;
//                }
//            }
//            mEditor.getSelectionModel().setSelection(end - 1, end);
//            // move caret right
//            while (moveRight && end != mEditor.getDocument().getTextLength()) {
//                end++;
//                EditorActionUtil.moveCaretToLineEnd(mEditor, true);
//                Matcher m = p.matcher(Objects.requireNonNull(mEditor.getSelectionModel().getSelectedText()));
//                if (m.find()) {
//                    end--;
//                    moveRight = false;
//                }
//            }
//            mEditor.getSelectionModel().setSelection(start, end);
            selectedText = mEditor.getSelectionModel().getSelectedText();
        }
        if (TextUtils.isEmpty(selectedText)) {
            return;
        }
        final String text = selectedText;
        //获取选中文档
        final Document document = mEditor.getDocument();
        //获取选中起始结束角标
        Caret primaryCaret = mEditor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        ArrayList<String> strings = new ArrayList<>();
        for (String string : strings) {

        }
        int end = primaryCaret.getSelectionEnd();
        //将选中内容按_分离
        String[] split = text.split("_");
        if (split.length == 1) {
            //大写字母开头转_+小写字母
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.replaceString(start, end, camel4underline(text))
            );

        } else {
            //_+小写字母转大写字母
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.replaceString(start, end, upperTable(text))
            );
        }


    }

    @SuppressWarnings("Duplicates")
    public String upperTable(String str) {
        // 字符串缓冲区
        StringBuffer sbf = new StringBuffer();
        // 如果字符串包含 下划线
            // 按下划线来切割字符串为数组
            String[] split = str.split("_");
            if(split.length==1){
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


    public String toBig(String str) {
        // 转换成字符数组
        char[] ch = str.toCharArray();
        // 判断首字母是否是字母
        if (ch[0] >= 'a' && ch[0] <= 'z') {
            // 利用ASCII码实现大写
            ch[0] = (char) (ch[0] - 32);
        }
        return String.valueOf(ch);
    }

    public String toUnderLine(String str) {
        // 字符串缓冲区
        StringBuffer sbf = new StringBuffer();
        String[] split = str.split("[A-Z]");
        return null;
    }


    public static String camel4underline(String param) {
        Pattern p = compile("[A-Z]");
        if (param == null || param.equals("")) {
            return "";
        }
        StringBuilder builder = new StringBuilder(param);
        Matcher mc = p.matcher(param);
        int i = 0;
        while (mc.find()) {
            builder.replace(mc.start() + i, mc.end() + i, "_" + mc.group().toLowerCase());
            i++;
        }

        if ('_' == builder.charAt(0)) {
            builder.deleteCharAt(0);
        }
        return builder.toString();
    }

}
