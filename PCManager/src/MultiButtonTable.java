import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import element.RoundedButton;
import element.TableBtnCellEditor;
import element.TableBtnPane;
import element.TableBtnCellRenderer;

public class MultiButtonTable {

    public MultiButtonTable() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }

                MyTableModel model = new MyTableModel();
                model.add(new Data(1, "AD", "Blah 1"));
                model.add(new Data(2, "SD", "Blah 2"));
                model.add(new Data(3, "PD", "Blah 3"));
                model.add(new Data(4, "DD", "Blah 4"));
                model.add(new Data(5, "MD", "Blah 5"));

                JTable table = new JTable(model);
                TableBtnCellRenderer renderer = new TableBtnCellRenderer();
                table.getColumnModel().getColumn(3).setCellRenderer(renderer);
                table.getColumnModel().getColumn(3).setCellEditor(new TableBtnCellEditor());
                table.setRowHeight(renderer.getTableCellRendererComponent(table, null, true, true, 0, 0).getPreferredSize().height);

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new JScrollPane(table));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class Data {

        private int id;
        private String name;
        private String application;

        public Data(int id, String name, String application) {
            this.id = id;
            this.name = name;
            this.application = application;
        }

        public int getID() {
            return id;
        }

        public void setID(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getApplication() {
            return application;
        }

        public void setApplication(String application) {
            this.application = application;
        }
    }

    public class MyTableModel extends AbstractTableModel {

        private List<Data> data;

        public MyTableModel() {
            data = new ArrayList<>(25);
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Data obj = data.get(rowIndex);
            Object value = null;
            switch (columnIndex) {
                case 0:
                    value = obj.getID();
                    break;
                case 1:
                    value = obj.getName();
                    break;
                case 2:
                    value = obj.getApplication();
                    break;
                case 3:
                    break;
            }
            return value;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            if (columnIndex == 3) {

                System.out.println(aValue);

                Data value = data.get(rowIndex);
                if ("accept".equals(aValue)) {
                    System.out.println("Accepted");
                } else {
                    System.out.println("Rejected");
                }
                fireTableCellUpdated(rowIndex, columnIndex);
                remove(value);

            }
        }

        public void add(Data value) {
            int startIndex = getRowCount();
            data.add(value);
            fireTableRowsInserted(startIndex, getRowCount() - 1);
        }

        public void remove(Data value) {
            int startIndex = data.indexOf(value);
            System.out.println("startIndex = " + startIndex);
            data.remove(value);
            fireTableRowsInserted(startIndex, startIndex);
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return columnIndex == 3;
        }
    }


    
}