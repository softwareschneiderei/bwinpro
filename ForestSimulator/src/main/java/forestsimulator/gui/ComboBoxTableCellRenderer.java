package forestsimulator.gui;

import java.awt.Component;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ComboBoxTableCellRenderer implements TableCellRenderer {
    private final JComboBox comboBox;

    public ComboBoxTableCellRenderer() {
        comboBox = new JComboBox();
        comboBox.setBorder(BorderFactory.createEmptyBorder());
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        comboBox.removeAllItems();
        comboBox.addItem(value);
        return comboBox;
    }
}
