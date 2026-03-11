package GUI.Component;

import java.awt.*;
import java.util.HashMap;
import javax.swing.*;

public final class MainFunction extends JToolBar {
    public HashMap<String, ButtonToolBar> btn = new HashMap<>();

    public MainFunction(String[] listBtn) {
        initComponent(listBtn);
    }

    private void initComponent(String[] listBtn) {
        this.setBackground(Color.WHITE);
        this.setRollover(true);
        this.setFloatable(false);
        
        btn.put("create", new ButtonToolBar("THÊM", "add.svg", "create"));
        btn.put("delete", new ButtonToolBar("XÓA", "delete.svg", "delete"));
        btn.put("update", new ButtonToolBar("SỬA", "edit.svg", "update"));
        btn.put("detail", new ButtonToolBar("CHI TIẾT", "detail.svg", "view"));
        btn.put("import", new ButtonToolBar("NHẬP EXCEL", "import_excel.svg", "create"));
        btn.put("export", new ButtonToolBar("XUẤT EXCEL", "export_excel.svg", "view"));

        for (String s : listBtn) {
            if (btn.containsKey(s)) {
                this.add(btn.get(s));
            }
        }
    }
}