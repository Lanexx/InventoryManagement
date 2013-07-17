package com.fabcoders.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fabcoders.config.ConfigManager;
import com.fabcoders.domain.Product;
import com.fabcoders.domain.Stock;
import com.fabcoders.domain.StockHistoryEvent;
import com.fabcoders.exception.InventoryManagementException;
import com.fabcoders.persistence.ProductRDFOperations;
import com.fabcoders.persistence.StockHistoryUtil;
import com.fabcoders.persistence.StockRDFOperations;
import com.fabcoders.reader.gsit.GSITReaderImpl;
import com.fabcoders.task.ClearHistoryTask;

/**
 *
 * @author Administrator
 */
@SuppressWarnings({"serial","unchecked"})
public class GSITInventoryTracker extends javax.swing.JFrame{

    private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
   
    private static final String APP_NAME = "Inventory Management";

    private static final int FETCH_INTERVAL = 1000;
    
    private BufferedImage addImage = null;

    /**
     * Logger for LLRPInventoryTracker
     */
    private Log log = LogFactory.getLog(GSITInventoryTracker.class);

    /**
     *  Set of items present in stock at any point of time
     */
    private static Set<String> itemsInStock = new TreeSet<String>();
    
    /**
     *  Set of items present in sales stock at any point of time
     */
    private static Map<String,Long[]> itemMonitoredMap = new HashMap<String, Long[]>();
    
    /**
     * 
     */
    private static List<String> itemsToMonitorList = new ArrayList<String>();
    
    /**
     * instance of llrp reader
     */
    private GSITReaderImpl gsitReaderImpl = new GSITReaderImpl();
    
    /**
     *  Antenna id set for adding items to stock
     */
    private int inAntenna = 1;
    
    /**
     *  Antenna id set for removing items to stock
     */
    private int outAntenna = 2;
    
    /**
     *  Antenna id set for item monitoring
     */
    private int monitorAntenna = 3;
    
    /**
     *  Antenna id set for items at point of sales
     */
    private int salesAntenna = 4;
    
    
    /**
     * Show message after n Sec
     */
    private static int NSec = 3;
    
    private static boolean monitorFlag = false;

    // UI related Variables declaration
    private DefaultListModel listModel1;
    private DefaultListModel listModel2;
    private DefaultListModel listModel3;

    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox10;
    private javax.swing.JComboBox jComboBox12;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JComboBox jComboBox3;
    private javax.swing.JComboBox jComboBox4;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JComboBox jComboBox6;
    private javax.swing.JComboBox jComboBox7;
    private javax.swing.JComboBox jComboBox8;
    private javax.swing.JComboBox jComboBox9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable6;
    private javax.swing.JTable jTable7;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.Timer timer2;
    // End of UI variables declaration
    
    /**
     * Creates new form LLRPInventoryTracker
     */
    public GSITInventoryTracker() {
        try {
            new ConfigManager();
            initComponents();
            page1Int();
            loadPreviousProducts();
            populatePage2();
            populatePage3();
        } catch (Exception e) {
            log.error("Exception Occured on strart up", e);
            System.exit(1);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     */
    private void initComponents() {

        listModel1 = new DefaultListModel();
        listModel2 = new DefaultListModel();
        listModel3 = new DefaultListModel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable6 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jComboBox7 = new javax.swing.JComboBox();
        jComboBox8 = new javax.swing.JComboBox();
        jLabel15 = new javax.swing.JLabel();
        jComboBox9 = new javax.swing.JComboBox();
        jLabel45 = new javax.swing.JLabel();
        jComboBox10 = new javax.swing.JComboBox();
        jLabel46 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton5 = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        jScrollPane8 = new javax.swing.JScrollPane();
        jLabel12 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox();
        jComboBox3 = new javax.swing.JComboBox();
        jComboBox4 = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jComboBox6 = new javax.swing.JComboBox();
        jTextField5 = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        jComboBox12 = new javax.swing.JComboBox();
        jLabel49 = new javax.swing.JLabel();
        jButton15 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel18 = new javax.swing.JLabel();
        jScrollPane12 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList(listModel3);
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList(listModel1);
        jScrollPane9 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList(listModel2);
        jButton9 = new javax.swing.JButton();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jTextField6 = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel22 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle(APP_NAME);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jButton5.setText("Clear");
        jButton5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton5MouseClicked(evt);
            }
        });
        jButton5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton5KeyPressed(evt);
            }
        });
        
        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setBorder(null);
        jScrollPane5.setViewportView(jTextArea1);

        jScrollPane13.setInheritsPopupMenu(true);

        jTable6.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EPC", "Item No", "Item Name", "ItemGroup", "Collection", "Color", "Size", "Sex"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable6.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if(!event.getValueIsAdjusting()){
                    loadProductImage();  
                }
            }
        });
        jTable6.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jScrollPane13.setViewportView(jTable6);

        jLabel13.setVisible(false);
        
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setText("Com Port :");

        jTextField1.setText("9600");
        
        jButton6.setText("Start Reader");
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton6MouseClicked(evt);
            }
        });
        jButton6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton6KeyPressed(evt);
            }
        });
        
        jButton8.setText("Stop Reader");
        jButton8.setEnabled(false);
        jButton8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton8MouseClicked(evt);
            }
        });
        jButton8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton8KeyPressed(evt);
            }
        });
        
        jLabel2.setText("Baud Rate :");

        jLabel15.setText("In/Out Antenna");

        jComboBox7.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        jComboBox7.setSelectedIndex(0);

        jComboBox8.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        jComboBox8.setSelectedIndex(1);
        
        jLabel46.setText("Sales Antenna");
        
        jComboBox9.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        jComboBox9.setSelectedIndex(3);
        
        jLabel45.setText("Monitor Antenna");

        jComboBox10.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4" }));
        jComboBox10.setSelectedIndex(2);
        
        
        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addGap(0, 7, Short.MAX_VALUE)
                            .addComponent(jButton6)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton8))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel45)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel15)
                                    .addGap(18, 18, 18)
                                    .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                    .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addComponent(jLabel46)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel3Layout.createSequentialGroup()
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jTextField1)
                                        .addComponent(jComboBox1, 0, 102, Short.MAX_VALUE))))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addGap(13, 13, 13)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel15)
                        .addComponent(jComboBox8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel46)
                        .addComponent(jComboBox10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComboBox9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 11, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane13, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
                    .addComponent(jScrollPane5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 136, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 9, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jScrollPane13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jScrollPane6.setViewportView(jPanel2);

        jTabbedPane2.addTab("Stock Available", jScrollPane6);

        jButton3.setText("Add Product");
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton3MouseClicked(evt);
            }
        });
        jButton3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton3KeyPressed(evt);
            }
        });
        
        jButton4.setText("Reset");
        jButton4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton4MouseClicked(evt);
            }
        });
        jButton4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton4KeyPressed(evt);
            }
        });
        jLabel11.setText("Description :");

        jButton7.setText("Upload Picture");
        jButton7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton7MouseClicked(evt);
            }
        });
        jButton7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton7KeyPressed(evt);
            }
        });
        
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("No Picture Uploaded");
        jScrollPane8.setViewportView(jLabel12);

        jLabel3.setText("Item No :");

        jLabel4.setText("Item Name :");

        jLabel6.setText("ItemGroup :");

        jLabel7.setText("Collection :");

        jLabel8.setText("Color :");

        jLabel9.setText("Size :");

        jLabel10.setText("Sex :");

        jLabel14.setText("Photo :");

        jLabel48.setText("Select Product :");

        jLabel49.setText("EPC :");

        jButton15.setText("Associate Inventory");
        jButton15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton15MouseClicked(evt);
            }
        });
        
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel6)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel8)
                                    .addComponent(jLabel11))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jComboBox2, 0, 162, Short.MAX_VALUE)
                                    .addComponent(jComboBox4, 0, 162, Short.MAX_VALUE)
                                    .addComponent(jTextField5)
                                    .addComponent(jTextField2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addContainerGap(192, Short.MAX_VALUE)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(41, 41, 41)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel10)
                                        .addComponent(jLabel9))
                                    .addGap(51, 61, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jComboBox5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jComboBox6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel7))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jComboBox3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jTextField3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 120, Short.MAX_VALUE)
                        .addComponent(jLabel14))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(73, 73, 73)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel48)
                            .addComponent(jLabel49))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jComboBox12, 0, 203, Short.MAX_VALUE)
                            .addComponent(jTextField4))
                        .addGap(42, 42, 42)
                        .addComponent(jButton15)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 296, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel3))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComboBox4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComboBox6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11)
                            .addComponent(jLabel10))
                        .addGap(30, 30, 30)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel48)
                            .addComponent(jComboBox12, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel49)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addComponent(jButton15, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(85, 85, 85))
        );

        jScrollPane7.setViewportView(jPanel1);

        jTabbedPane2.addTab("Associate Inventory", jScrollPane7);

        jLabel17.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel17.setText("Home Antenna :");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EPC", "No Of Movememnt"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable1);

        jLabel18.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel18.setText("Antenna"+monitorAntenna);

        jScrollPane12.setViewportView(jList3);

        jLabel43.setText("Items Available in Stock");

        jLabel44.setText("Items Belong to Home Antenna");

        jButton13.setText(">");
        jButton13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton13MouseClicked(evt);
            }
        });

        jButton14.setText("<");
        jButton14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton14MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel17)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel18))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton13, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40, 40, 40)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addGap(209, 209, 209)
                        .addComponent(jLabel44)))
                .addContainerGap(300, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel18))
                .addGap(33, 33, 33)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(jLabel44))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane12, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 376, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(128, 128, 128)
                        .addComponent(jButton13)
                        .addGap(34, 34, 34)
                        .addComponent(jButton14)))
                .addContainerGap(119, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel4);

        jTabbedPane2.addTab("Inventory Movement", jScrollPane1);

        jLabel19.setText("Customer Name: ");

        jList1.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if(!event.getValueIsAdjusting()){
                    loadProductInformation(jList1);  
                }
            }
        });
        jList2.getSelectionModel().addListSelectionListener(new ListSelectionListener(){
            public void valueChanged(ListSelectionEvent event) {
                // do some actions here, for example
                // print first column value from selected row
                if(!event.getValueIsAdjusting()){
                    loadProductInformation(jList2);  
                }
            }
        });
        
        jScrollPane4.setViewportView(jList1);

        jScrollPane9.setViewportView(jList2);

        jButton9.setText(">");
        jButton9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton9MouseClicked(evt);
            }
        });

        jButton10.setText("<");
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton10MouseClicked(evt);
            }
        });

        jButton11.setText("<<");
        jButton11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton11MouseClicked(evt);
            }
        });

        jButton12.setText("Sell");
        jButton12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton12MouseClicked(evt);
            }
        });

        jLabel20.setText("Items Available");

        jLabel21.setText("Items Selected");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Item Details"));

        jLabel36.setText("EPC :");

        jLabel35.setText("ItemGroup :");

        jLabel34.setText("Collection :");

        jLabel39.setText("Description :");

        jLabel24.setText("---------");

        jLabel37.setText("Item Name :");

        jLabel38.setText("Item No :");

        jLabel27.setText("---------");

        jLabel28.setText("---------");

        jLabel25.setText("---------");

        jLabel26.setText("---------");

        jLabel41.setText("---------");

        jLabel40.setText("---------");

        jLabel31.setText("Sex :");

        jLabel29.setText("---------");

        jLabel32.setText("Size :");

        jLabel30.setText("---------");

        jLabel33.setText("Color :");

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("No Picture Associated");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel38)
                    .addComponent(jLabel37)
                    .addComponent(jLabel36)
                    .addComponent(jLabel35)
                    .addComponent(jLabel33)
                    .addComponent(jLabel34)
                    .addComponent(jLabel32)
                    .addComponent(jLabel31)
                    .addComponent(jLabel39))
                .addGap(28, 28, 28)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel41)
                    .addComponent(jLabel24)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(jLabel30)
                    .addComponent(jLabel40))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel38)
                                .addComponent(jLabel24))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel37)
                                    .addComponent(jLabel25))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel36)
                            .addComponent(jLabel26))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel35)
                            .addComponent(jLabel27))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel28))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel29))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel30))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel31)
                            .addComponent(jLabel40))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel39)
                            .addComponent(jLabel41)))
                    .addComponent(jLabel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EPC", "Item No", "Item Name", "Customer Name", "Item Group", "Collection", "Color", "Size", "Sex", "Sold On"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(jTable2);
        
        jTextField6.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                jTextField6ActionPerformed();
            }
            public void removeUpdate(DocumentEvent e) {
                jTextField6ActionPerformed();
            }
            public void insertUpdate(DocumentEvent e) {
                jTextField6ActionPerformed();
            }
        });
        
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EPC", "Item No", "Item Name", "Customer Name", "Item Group", "Collection", "Color", "Size", "Sex", "Sold On"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane11.setViewportView(jTable3);

        jLabel22.setText("Prevously Sold Items Present");

        jLabel42.setText("Items bought by this customer earlier");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(295, 295, 295)
                        .addComponent(jLabel19)
                        .addGap(43, 43, 43)
                        .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(jLabel20)
                                .addGap(98, 98, 98))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(33, 33, 33)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel21)
                            .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel22)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 1100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel42))))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jLabel22)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(10, 10, 10)
                .addComponent(jLabel42)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addComponent(jButton9)
                                .addGap(18, 18, 18)
                                .addComponent(jButton10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton11)
                                .addGap(42, 42, 42))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel21)
                                    .addComponent(jLabel20))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(11, 11, 11)))
                        .addComponent(jButton12, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jScrollPane3.setViewportView(jPanel5);

        jTabbedPane2.addTab("Inventory Sales", jScrollPane3);

        jScrollPane14.setInheritsPopupMenu(true);

        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EPC", "Item No", "Item Name", "ItemGroup", "Collection", "Color", "Size", "Sex", "Operation", "Operation Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.sql.Timestamp.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable7.setAutoCreateRowSorter(true);
        jTable7.getColumnModel().getColumn(9).setCellRenderer(new DateRenderer());
        jTable7.getRowSorter().toggleSortOrder(9); 
        jTable7.getRowSorter().toggleSortOrder(9); 
        
        jScrollPane14.setViewportView(jTable7);

        jTabbedPane2.addTab("Inventory History", jScrollPane14);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 618, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        timer2 = new javax.swing.Timer(FETCH_INTERVAL, new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timer2ActionPerformed(evt);
            }
        });
        pack();
    }
    private void page1Int() {
        log.debug("Loading Serial Port");
        jComboBox1.insertItemAt("COM1", 0);
        jComboBox1.insertItemAt("COM2", 1);
        jComboBox1.insertItemAt("COM3", 2);
        jComboBox1.insertItemAt("COM4", 3);
        jComboBox1.insertItemAt("COM5", 4);
        jComboBox1.insertItemAt("COM6", 5);
        jComboBox1.insertItemAt("COM7", 6);
        jComboBox1.insertItemAt("COM8", 7);  
        jComboBox1.insertItemAt("COM9", 8);
        jComboBox1.insertItemAt("COM10", 9);
        jComboBox1.insertItemAt("COM11", 10);
        jComboBox1.insertItemAt("COM12", 11);
        jComboBox1.insertItemAt("COM13", 12);
        jComboBox1.insertItemAt("COM14", 13);
        jComboBox1.insertItemAt("COM15", 14);
        jComboBox1.insertItemAt("COM16", 15);
        jComboBox1.insertItemAt("COM17", 16);
        jComboBox1.insertItemAt("COM18", 17);
        jComboBox1.insertItemAt("COM19", 18);
        jComboBox1.setSelectedIndex(0);
    }
    
    /**
     * method to load all previous product available in stock
     */
    private void loadPreviousProducts() {
        log.debug("Loading previously available stock");
        try {
            Set<String> taglist = StockRDFOperations.getTagsInStock(); 
            for (String epc : taglist) {
                Product item = ProductRDFOperations.getItemForEPC(epc);
                
                if(null != item){
                    itemsInStock.add(epc);
                    addItemToJtable6(item);
                    ((DefaultListModel)jList3.getModel()).addElement(item.getEpc());
                }
            }
        } catch (InventoryManagementException e) {
           logMessage("Failed to log Stock Available");
        }
    }
    
    /**
     * Loads elements in page2. function of populating drop down is done here 
     */
    private void populatePage2() {
        log.debug("Loading Item Groups...");
        String [] itemGroupArray = ConfigManager.ITEMGROUPS;
        
        if(null != itemGroupArray){
            int i=0;
            for (String item : itemGroupArray) {
                jComboBox2.insertItemAt(item, i++);
            }
        }
        
        log.debug("Loading Collections...");
        String [] collectionArray = ConfigManager.COLLECTIONS;
        
        if(null != collectionArray){
            int i=0;
            for (String item : collectionArray) {
                jComboBox3.insertItemAt(item, i++);
            }
        }
        
        log.debug("Loading Colors...");
        String [] colorArray = ConfigManager.COLORS;
        
        if(null != colorArray){
            int i=0;
            for (String item : colorArray) {
                jComboBox4.insertItemAt(item, i++);
            }
        }
        
        log.debug("Loading Size...");
        String [] sizeArray = ConfigManager.SIZE;
        
        if(null != sizeArray){
            int i=0;
            for (String item : sizeArray) {
                jComboBox5.insertItemAt(item, i++);
            }
        }
        
        log.debug("Loading Gender...");
        String [] genderArray = ConfigManager.GENDER;
        
        if(null != genderArray){
            int i=0;
            for (String item : genderArray) {
                jComboBox6.insertItemAt(item, i++);
            }
        }
        loadProductIds();
    }

    /**
     * 
     */
    private void loadProductIds() {
        try {
            List<Product> items = ProductRDFOperations.getAllProducts();
            jComboBox12.removeAllItems();
            
            if(null != items){
                int i=0;
                for (Product item : items) {
                    jComboBox12.insertItemAt(item.getProductCode(), i++);
                }
            }
        } catch (InventoryManagementException e) {
            logMessage(e.getMessage());
        }
    }
    
    /**
     * Loads elements in page3. this method loads previous transaction history
     */
    private void populatePage3() {
        
       List<StockHistoryEvent> eventList = null;
    try {
        eventList = StockHistoryUtil.getAllRecord();
        for (StockHistoryEvent event : eventList) {
            addItemToJtable7(event);
        }
    } catch (InventoryManagementException e) {
        logMessage(e.getMessage());
    }
    }
    
    /**
     * This methods print the msg in message textbox provided
     * @param msg message to print
     */
    private void logMessage(String msg) {
        String oldmsg =jTextArea1.getText()  + sdf.format(new Date()) + " " + msg + "\n";
        jTextArea1.setText(oldmsg);
    }
    
    /**
     * adding to item to history table
     * @param event
     */
    private void addItemToJtable7(StockHistoryEvent event) {
        if(null != event){
            DefaultTableModel model = (DefaultTableModel)jTable7.getModel();
            Product item = event.getProduct();
            if(null != item)
                model.addRow(new Object[] { item.getEpc(), Integer.parseInt(item.getProductCode()),
                    item.getProductName(), item.getProductGroup(), item.getCollection(),
                    item.getColor(),item.getSize(), item.getSex(),
                    event.getOperation(), event.getOperationOn()});    
        }
    }
    
    /**
     * adding item to item table
     * @param item
     */
    private void addItemToJtable6(Product item){
        log.debug("Adding "+item+" to stock");

        DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
        model.addRow(new Object[] { item.getEpc(), item.getProductCode(),
                item.getProductName(), item.getProductGroup(), item.getCollection(),
                item.getColor(), item.getSize(), item.getSex()});
    }
    
    /**
     * removing item from table 6
     * @param epc
     */
    private void removeFromJtable6(String epc){
        log.debug("Removing item with tagid : "+epc+" from stock");
        DefaultTableModel model = (DefaultTableModel) jTable6.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals(epc)) {
                model.removeRow(i);
                break;
            }
        }
    }
    
    /**
     * This function is called on closing window and checks if reader is running 
     * @param evt
     */
    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        int i = JOptionPane.showConfirmDialog(this,
                "Are you sure to close Application", APP_NAME,
                JOptionPane.YES_NO_OPTION);
        if (i == 0) {
            if (jButton8.isEnabled()) {
                JOptionPane.showMessageDialog(this,"Cannot close without Stopping Reader",
                                APP_NAME, JOptionPane.INFORMATION_MESSAGE);
            } else {
                System.exit(0);
            }
        }
    }

    /**
     * This method upload images to images path and display it on jLabel12
     * @param evt
     */
    private void jButton7MouseClicked(java.awt.event.MouseEvent evt) {
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            log.debug("File selected for Image upload : "+file.getPath());
            // to filter out images

            String ext = null;
            String s = file.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }

            if ((ext.contentEquals("jpg")) || (ext.contentEquals("jpeg"))
                    || (ext.contentEquals("tiff"))
                    || (ext.contentEquals("gif"))
                    || (ext.contentEquals("tif"))
                    || (ext.contentEquals("png"))) {
                // to get the height and width of an image
                
                    ImageIcon image = new ImageIcon(file.getPath());
                    jLabel12.setIcon(image);
                    jLabel12.setText("");
                    jLabel12.revalidate();

                    addImage  = new BufferedImage(image.getIconWidth(),image.getIconHeight(),BufferedImage.TYPE_INT_RGB);
                    image.paintIcon(null, addImage.getGraphics(), 0, 0);
            } else {
                    jLabel12.setText("Please Correct Image File");
            }
        }
    }

    /**
     * This method moves items from detected list to selected item list
     * @param evt
     */
    private void jButton13MouseClicked(MouseEvent evt) {
        
        Object[] objects = jList3.getSelectedValues();
        for (Object object : objects) {
            ((DefaultTableModel)jTable1.getModel()).addRow(new Object[]{(String)object, Integer.valueOf(0)});
            itemsToMonitorList.add((String)object);
            ((DefaultListModel)jList3.getModel()).removeElement(object);   
        }
    }
    
    /**
     *  This method moves items from selected list to detected item list
     * @param evt
     */
    private void jButton14MouseClicked(MouseEvent evt) {
        int index = jTable1.getSelectedRow();
        if(index != -1)
        {
            Object objects =((DefaultTableModel)jTable1.getModel()).getValueAt(jTable1.getSelectedRow(), 0) ;
            ((DefaultTableModel)jTable1.getModel()).removeRow(jTable1.getSelectedRow());
            itemsToMonitorList.remove((String)objects);
            itemMonitoredMap.remove((String)objects);
            ((DefaultListModel)jList3.getModel()).addElement(objects);
        }
    }

    /**
     * @param evt
     */
    private void jButton15MouseClicked(MouseEvent evt) {

        String epc = (String) jTextField4.getText();
        String productid = (String) jComboBox12.getSelectedItem();
        if (null == productid || "".equalsIgnoreCase(productid)) {
            JOptionPane.showMessageDialog(this, "Please select the product");
            jTextField2.requestFocus();
            return;
        }else if (null == epc || "".equalsIgnoreCase(epc)) {
            JOptionPane.showMessageDialog(this,
            "Please Enter Electronic Product Code (EPC) to associate");
            jTextField4.requestFocus();
            return;
        } else if (!"".equals(epc) && !epc.matches("^[0-9A-Fa-f]{24}$")) {
            JOptionPane.showMessageDialog(this,
            "EPC is a HexaDecimal Value Of 24 Length");
            jTextField4.requestFocus();
            return;
        }else{
            try {
                StockRDFOperations.addStockDetails(productid, epc);
                JOptionPane.showMessageDialog(this,
                "Item Added to stock successfully");
                jComboBox12.setSelectedIndex(-1);
                jTextField4.setText("");
            } catch (InventoryManagementException e) {
                logMessage(e.getMessage());
            }
        }
    }
    /**
     * This method resets the associate form
     * @param evt
     */
    private void jButton4MouseClicked(java.awt.event.MouseEvent evt) {
        
        jTextField2.setText("");
        jTextField3.setText("");
        jTextField5.setText("");
        jComboBox2.setSelectedIndex(-1);
        jComboBox3.setSelectedIndex(-1);
        jComboBox4.setSelectedIndex(-1);
        jComboBox5.setSelectedIndex(-1);
        jComboBox6.setSelectedIndex(-1);
        jLabel12.setText("No Picture Uploaded");
        jLabel12.setIcon(null);
        addImage = null;
    
    }
    
    /**
     * This method is called when user presses Associate button while performing associate operation
     * 
     * @param evt
     */
    private void jButton3MouseClicked(java.awt.event.MouseEvent evt) {
        
        if (jButton3.isEnabled()) {
           
            String itemNo = jTextField2.getText();
            String itemName = jTextField3.getText();
            String itemGroup = (String) jComboBox2.getSelectedItem();
            String collection = (String) jComboBox3.getSelectedItem();
            String color = (String) jComboBox4.getSelectedItem();
            String size = (String) jComboBox5.getSelectedItem();
            String sex = (String) jComboBox6.getSelectedItem();
            String description = jTextField5.getText();

            if (null == itemNo || "".equalsIgnoreCase(itemNo)) {
                JOptionPane.showMessageDialog(this, "Please Enter Item No");
                jTextField2.requestFocus();
                return;
            }else if (!itemNo.matches("^[0-9]*$")) {
                JOptionPane.showMessageDialog(this, "Item No Can Only Contain Numbers.");
                jTextField2.requestFocus();
                return;
            }else if (null == itemName || "".equalsIgnoreCase(itemName)) {
                JOptionPane.showMessageDialog(this, "Please Enter Item Name");
                jTextField3.requestFocus();
                return;
            }else if (null == itemGroup || "".equalsIgnoreCase(itemGroup)) {
                JOptionPane.showMessageDialog(this,
                "Please Select The ItemGroup");
                jComboBox2.requestFocus();
                return;
            } else if (null == collection || "".equalsIgnoreCase(collection)) {
                JOptionPane.showMessageDialog(this,
                "Please Select The Collection");
                jComboBox3.requestFocus();
                return;
            } else if (null == color || "".equalsIgnoreCase(color)) {
                JOptionPane.showMessageDialog(this,
                        "Please Select Color");
                jComboBox4.requestFocus();
                return;
            } else if (null == size || "".equalsIgnoreCase(size)) {
                JOptionPane.showMessageDialog(this,
                        "Please Select Size");
                jComboBox5.requestFocus();
                return;
            } else if (null == sex || "".equalsIgnoreCase(sex)) {
                JOptionPane.showMessageDialog(this,
                        "Please Select Sex");
                jComboBox6.requestFocus();
                return;
            }  else if (null == addImage) {
                JOptionPane.showMessageDialog(this,
                        "Please Upload the Product Image");
                return;
            } 
            jButton3.setEnabled(false);

            try {
                File outputFile = new File(ConfigManager.IMAGE_LOCATION
                        + System.getProperty("file.separator") + itemNo + ".jpg");
                log.debug("Uploading Image to : " + outputFile.getPath());

                ImageIO.write(addImage, "JPG", outputFile);
                Product product = new Product();
                product.setCollection(collection);
                product.setColor(color);
                product.setProductGroup(itemGroup);
                product.setProductName(itemName);
                product.setProductCode(itemNo);
                product.setSex(sex);
                product.setSize(size);
                product.setDescription(description);
                product.setImage(outputFile.getName());
                ProductRDFOperations.create(product);
                JOptionPane.showMessageDialog(this, "Added Product Successfully");
                jButton4MouseClicked(null);
                loadProductIds();
            } catch (InventoryManagementException e) {
                JOptionPane.showMessageDialog(this, "Failed to add Item: " + e.getMessage());
                log.warn("Exception Occured while Associating product to Tagid", e);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "Failed to upload image: " + e.getMessage());
                log.warn("Exception Occured while Associating product to Tagid", e);
            }

            jButton3.setEnabled(true);
        }
    
    }

    /**
     * This method clears log message box
     * @param evt
     */
    private void jButton5MouseClicked(java.awt.event.MouseEvent evt) {
        jTextArea1.setText("");  
    }

    /**
     * This method stops reader from reading tags and disconnects reader
     * @param evt
     */
    private void jButton8MouseClicked(java.awt.event.MouseEvent evt) {
        log.debug("Stoping the reader...");
        if(jButton8.isEnabled()){
            try {
                gsitReaderImpl.stopTagRead();
                if(gsitReaderImpl.disconnect())
                {
                    timer2.stop();
                    monitorFlag = false;
                    jTextField1.setEnabled(true);
                    jButton8.setEnabled(false);
                    jButton6.setEnabled(true);
                    jComboBox1.setEnabled(true);
                    jComboBox7.setEnabled(true);
                    jComboBox8.setEnabled(true);
                    jComboBox9.setEnabled(true);
                    jComboBox10.setEnabled(true);
                    logMessage("Reader Stoped successfully");
                }
            } catch (Exception e) {
                logMessage(e.getMessage());
            }
        }
    }
    
    /**
     * This method Connects reader selected from jComboBox1 and sends read tag messages
     * @param evt
     */
    private void jButton6MouseClicked(java.awt.event.MouseEvent evt) {
        log.debug("Starting the reader...");
        if(jButton6.isEnabled()){
            String port = (String)jComboBox1.getSelectedItem();
            String baudRate =jTextField1.getText();
            if (null == baudRate || "".equalsIgnoreCase(baudRate)) {
                JOptionPane.showMessageDialog(this, "Please Enter Baud Rate");
                jTextField1.requestFocus();
                return;
            }else if (!baudRate.matches("^[0-9]*$")) {
                JOptionPane.showMessageDialog(this, "Baud Rate Can Only Contain Numbers.");
                jTextField1.requestFocus();
                return;
            }
            inAntenna = Integer.parseInt((String)jComboBox7.getSelectedItem());
            outAntenna = Integer.parseInt((String)jComboBox8.getSelectedItem());
            salesAntenna = Integer.parseInt((String)jComboBox9.getSelectedItem());
            monitorAntenna = Integer.parseInt((String)jComboBox10.getSelectedItem());
           if(inAntenna == outAntenna || inAntenna == salesAntenna || 
              inAntenna == monitorAntenna || outAntenna == salesAntenna || 
              outAntenna == monitorAntenna || salesAntenna == monitorAntenna  ){
               JOptionPane.showMessageDialog(this,"Same Antenna Cannot Assign to Two or More Operations.");
               jComboBox7.requestFocus();
               return;
           }
            try {
                boolean isConnected = gsitReaderImpl.serialConnect(port, Integer.parseInt(baudRate));
                if(isConnected)
                    isConnected = gsitReaderImpl.startTagRead();
                if(isConnected)
                {
                    jTextField1.setEnabled(false);
                    jButton6.setEnabled(false);
                    jButton8.setEnabled(true);
                    jComboBox1.setEnabled(false);
                    jComboBox7.setEnabled(false);
                    jComboBox8.setEnabled(false);
                    jComboBox9.setEnabled(false);
                    jComboBox10.setEnabled(false);
					timer2.start();
                    monitorFlag = true;
                    logMessage("Reader Started successfully");
                }else
                {
                    logMessage("Not able to connect Reader");
                }
            } catch (Exception e) {
                logMessage(e.getMessage());
            }
        }
    }

    private void addToTable2(Stock stock) {

        boolean shouldAdd= true;
        int count = jTable2.getModel().getRowCount();
        DefaultTableModel model = (DefaultTableModel) jTable2.getModel();
        if(count > 0){
            for (int i = 0; i < count; i++) {
                
                if(stock.getEpc().equals(jTable2.getValueAt(i, 0))){

                    shouldAdd = false;
                }
            }
        }
        if(shouldAdd){

            try {
                Product item = ProductRDFOperations.getItemForEPC(stock.getEpc());

                model.addRow(new Object[] { item.getEpc(), item.getProductCode(),
                        item.getProductName(), stock.getSoldTo(), item.getProductGroup(), item.getCollection(),
                        item.getColor(), item.getSize(), item.getSex(), stock.getSoldOn()});
            } catch (InventoryManagementException e) {
                logMessage(e.getMessage());
            }
        
        }
    }

    /**
     * This methods loads all the items purchased by same customer
     */
    private void loadProductForSameCustomer(String customerName) {
        DefaultTableModel model = (DefaultTableModel)jTable3.getModel();
       
        model.getDataVector().removeAllElements();
        model.fireTableDataChanged();
        Set<String> taglist = StockRDFOperations.getStockSoldTo(customerName);
        Product item;
        Stock stock;
        try {
            for (String tagId : taglist) {
                item = ProductRDFOperations.getItemForEPC(tagId);
                if(null != item){
                    stock = StockRDFOperations.getStockDetailsForEpc(tagId);
                    if(null != stock){
                        model.addRow(new Object[] { item.getEpc(), item.getProductCode(),
                                item.getProductName(), stock.getSoldTo(), item.getProductGroup(), item.getCollection(),
                                item.getColor(), item.getSize(), item.getSex(), stock.getSoldOn()});    
                    }
                }
            }
        } catch (InventoryManagementException e) {
            logMessage(e.getMessage());
        } 
    }
    
    /**
     * This method loads product image on jLabel13 and is called when any row in
     * jTable6 is selected
     */
    private void loadProductImage() {
        int rowNo = jTable6.getSelectedRow();
        if(rowNo != -1){
            String itemNo = jTable6.getValueAt(rowNo,1).toString();
            try {
                BufferedImage image = ProductRDFOperations.getPictureforItemNo(itemNo);
                int height = 230; 
                int width = jPanel3.getWidth();
                BufferedImage resizedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(image, 0, 0, width,height, null);
                g.dispose();
                jLabel13.setIcon(new ImageIcon(resizedImage));
                jLabel13.setVisible(true);
                jLabel13.revalidate();
            } catch (InventoryManagementException e) {
                logMessage("Failed to get Product Image");
                log.warn("Failed to get Product Image",e);
            }
        }else{
            jLabel13.setVisible(false);
            jLabel13.setIcon(null);
            jLabel13.revalidate();
        }
    }
    
    private boolean getStockForSale(String itemNo) {
        Set<String> salesItems = new HashSet<String>();
        ListModel model = jList1.getModel();
        int count = model.getSize();
        for (int i = 0; i < count; i++) {
            salesItems.add((String)model.getElementAt(i));
        }
        model = jList2.getModel();
        count = model.getSize();
        for (int i = 0; i < count; i++) {
            salesItems.add((String)model.getElementAt(i));
        }
        return salesItems.contains(itemNo);
    }

    private void jTextField6ActionPerformed() {  
        String customerName = jTextField6.getText();
        loadProductForSameCustomer(customerName);
    }                                           

    /**
     * This method moves items from detected list to selected item list
     * @param evt
     */
    private void jButton9MouseClicked(MouseEvent evt) {
        
        Object[] objects = jList1.getSelectedValues();
        for (Object object : objects) {
            listModel2.addElement(object);
            listModel1.removeElement(object);   
        }
    }
    
    /**
     *  This method moves items from selected list to detected item list
     * @param evt
     */
    private void jButton10MouseClicked(MouseEvent evt) {
        Object[] objects = jList2.getSelectedValues();
        for (Object object : objects) {
            listModel1.addElement(object);
            listModel2.removeElement(object);   
        }
    }
    
    /**
     *  This method moves all items from selected list to detected item list
     * @param evt
     */
    private void jButton11MouseClicked(MouseEvent evt) {
        int count = listModel2.getSize();
        for (int i = 0; i < count; i++) {
            listModel1.addElement(listModel2.getElementAt(i));
        }
        listModel2.removeAllElements();
    }
    
    /**
     * loads product information when user selects item in detected and selected item list
     * @param list
     */
    private void loadProductInformation(JList list){
        String selectedValue = (String) list.getSelectedValue();
        if(null != selectedValue){
            Product item;
            try {
                item = ProductRDFOperations.getItemForEPC(selectedValue);
                BufferedImage image = ProductRDFOperations.getPictureforItemNo(item.getProductCode());
                BufferedImage resizedImage = new BufferedImage(jLabel23.getWidth(),jLabel23.getHeight(), BufferedImage.TYPE_INT_RGB);
                Graphics2D g = resizedImage.createGraphics();
                g.drawImage(image, 0, 0, jLabel23.getWidth(),jLabel23.getHeight(), null);
                g.dispose();
                jLabel23.setIcon(new ImageIcon(resizedImage));
                jLabel23.setVisible(true);
                jLabel23.revalidate();
                jLabel24.setText(item.getProductCode());
                jLabel25.setText(item.getProductName());
                jLabel26.setText(item.getEpc());
                jLabel27.setText(item.getProductGroup());
                jLabel28.setText(item.getCollection());
                jLabel29.setText(item.getColor());
                jLabel30.setText(item.getSize());
                jLabel40.setText(item.getSex());
                jLabel41.setText(item.getDescription());
            } catch (InventoryManagementException e) {
                logMessage(e.getMessage());
            }
        }else{
            jLabel23.setIcon(null);
            jLabel24.setText("---------");
            jLabel25.setText("---------");
            jLabel26.setText("---------");
            jLabel27.setText("---------");
            jLabel28.setText("---------");
            jLabel29.setText("---------");
            jLabel30.setText("---------");
            jLabel40.setText("---------");
            jLabel41.setText("---------");
        }
    }
    
    /**
     * This method set flag for item as sold. this method is called when user click on sell
     * @param evt
     */
    private void jButton12MouseClicked(MouseEvent evt) {
        String customerName = jTextField6.getText();
        if("".equals(customerName)){
            JOptionPane.showMessageDialog(this, "Please Enter the Customer Name.");
            jTextField6.requestFocus();
            return;
        }
        int count = listModel2.getSize();
        if(count <= 0){
            JOptionPane.showMessageDialog(this, "There is no Item present in Selected Items");
            jTextField6.requestFocus();
            return;
        }
        jButton12.setEnabled(false);
        String itemName="";
        Set<String> epcSet = new HashSet<String>();
        for (int i = 0; i < count; i++) {
            epcSet.add((String) listModel2.getElementAt(i));
        }
        for (String epc :epcSet)
        {
            Product item;
            try {
                item = ProductRDFOperations.getItemForEPC(epc);
                StockRDFOperations.setStockToSold(customerName, epc);
                
                // remove item from stock
                itemsInStock.remove(epc);
                removeFromJtable6(epc);

                // remove items from other places
                removeFromOthrPlaces(epc);
                
                // adding entry to history
                StockHistoryEvent event = new StockHistoryEvent();
                event.setEpc(epc);
                event.setProduct(item);
                event.setOperation("SOLD");
                event.setOperationOn(new Date());
                StockHistoryUtil.addToHistoryLog(event);
                addItemToJtable7(event);
                itemName += item.getProductName()+ ", ";
                
            } catch (InventoryManagementException e) {
                logMessage(e.getMessage());
            }
        }
        
        listModel2.removeAllElements();
        jTextField6.setText("");
        JOptionPane.showMessageDialog(this, "Item "+itemName+" sold to "+customerName);
        jButton12.setEnabled(true);
    }
    
    /**
     * 
     * @param item
     */
    private void addToMovemement(Product item) {
       DefaultTableModel model = (DefaultTableModel)jTable1.getModel();
      
       boolean isexist = false;
       int i = 0;
       for (; i < model.getRowCount(); i++) {
           if (model.getValueAt(i, 0).equals(item.getEpc())) {
               isexist = true;     
               break;
           }
       }
       if(isexist){
           Integer countValue = (Integer) model.getValueAt(i, 1);
           int count = countValue.intValue();
           count = count +1;
           model.setValueAt(count,i ,1) ;
       }
    }
    
    // below method are for enter press detetion
    
    private void jButton3KeyPressed(java.awt.event.KeyEvent evt) {                                    
        if(KeyEvent.VK_ENTER == evt.getKeyCode())
        {
            jButton3MouseClicked(null);
        }
    } 

    private void jButton4KeyPressed(java.awt.event.KeyEvent evt) {                                    
        if(KeyEvent.VK_ENTER == evt.getKeyCode())
        {
            jButton4MouseClicked(null);
        }
    } 

    private void jButton5KeyPressed(java.awt.event.KeyEvent evt) {                                    
        if(KeyEvent.VK_ENTER == evt.getKeyCode())
        {
            jButton5MouseClicked(null);
        }
    } 

    private void jButton6KeyPressed(java.awt.event.KeyEvent evt) {                                    
        if(KeyEvent.VK_ENTER == evt.getKeyCode())
        {
            jButton6MouseClicked(null);
        }
    } 
    
    private void jButton7KeyPressed(java.awt.event.KeyEvent evt) {                                    
        if(KeyEvent.VK_ENTER == evt.getKeyCode())
        {
            jButton7MouseClicked(null);
        }
    } 
   
    private void jButton8KeyPressed(java.awt.event.KeyEvent evt) {                                    
        if(KeyEvent.VK_ENTER == evt.getKeyCode())
        {
            jButton8MouseClicked(null);
        }
    } 
    
    /**
     * Slash screen on start up
     * @param window
     */
    public static void showSplash(JWindow window) {
        JPanel content = (JPanel) window.getContentPane();
        content.setBackground(Color.white);
        // Set the window's bounds, centering the window
        int width = 450;
        int height = 115;
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screen.width - width) / 2;
        int y = (screen.height - height) / 2;
        window.setBounds(x, y, width, height);

        // Build the splash screen
        JLabel label = new JLabel(new ImageIcon("logo.gif"));
        JLabel copyrt = new JLabel("today:" + new Date(),
            JLabel.RIGHT);
        copyrt.setFont(new Font("Sans-Serif", Font.BOLD, 12));
        content.add(label, BorderLayout.CENTER);
        content.add(copyrt, BorderLayout.SOUTH);
        content.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 10));

        // Display it
        window.setVisible(true);

      }
    
    /**
     * exiting slash screen
     * @param window
     */
    public static void exitSplash(JWindow window){
        // Wait a little while, maybe while loading resources
           window.setVisible(false);
       }
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        final JWindow window = new JWindow();
        showSplash(window);
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GSITInventoryTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GSITInventoryTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GSITInventoryTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GSITInventoryTracker.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new GSITInventoryTracker().setVisible(true);
                exitSplash(window);
            }
        });
        
        // Schedule to run once in day
        final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new ClearHistoryTask(), 1, 1, TimeUnit.DAYS);
        
        Thread t = new Thread(new Runnable() {
            public void run() {
                monitorTagPresence();
            }
        });
        t.setDaemon(true);
        t.start();
    }
        
    

    /**
     * This is Important method which handles all logic when tag is detected at readers antenna.
     * 
     * Here in case of inAntenna we add inventory to stock,
     *  in case of outAntenna we remove inventory from stock
     * and in case of salesAntenna we add inventory to salesStock
     * 
     * @param adaptor is adaptor to whom we have connected
     * @param reader reader sending message
     * @param message llrp message received from reader
     */
    private void timer2ActionPerformed(ActionEvent evt) {
        Map<String, String> tagAntennaMap =GSITReaderImpl.getTags();
        if(tagAntennaMap.size() <= 0){
            return;
        }
        // initializing variables
        String tagId; 
        int antennaID;
        Product item;
        boolean isPresentInStock;
        
        // Loop through the list and get the EPC of each tag.
        for (Map.Entry<String, String> entry : tagAntennaMap.entrySet()) {
            
            try {
                // getting and setting antenna id from tag read 
                antennaID = Integer.parseInt(entry.getValue());
                // getting and setting tagId, it contains HexaDecimal value
                tagId = entry.getKey();

                item = ProductRDFOperations.getItemForEPC(tagId);

                // in case of item not belong to our repository return
                if(null == item){
                    continue;
                }
                //add item to Inventory stock
                if(antennaID == inAntenna){
                    isPresentInStock = itemsInStock.contains(tagId);
                    if(!isPresentInStock)
                    {
                        addItemToJtable6(item);
                        itemsInStock.add(tagId);
                        StockRDFOperations.addToStock(tagId);
                        ((DefaultListModel)jList3.getModel()).addElement(tagId);
                        
                        // adding entry to history
                        StockHistoryEvent event = new StockHistoryEvent();
                        event.setEpc(tagId);
                        event.setProduct(item);
                        event.setOperation("ADDED");
                        event.setOperationOn(new Date());
                        StockHistoryUtil.addToHistoryLog(event);
                        addItemToJtable7(event);
                    }
                    
                }
                // remove item from inventory stock
                else if(antennaID == outAntenna){
                    isPresentInStock = itemsInStock.contains(tagId);
                    if(isPresentInStock)
                    {
                        // remove from stock available
                        removeFromJtable6(tagId);
                        StockRDFOperations.removeFromStock(tagId);
                        itemsInStock.remove(tagId);
                        
                        // remove from items all the other places
                        removeFromOthrPlaces(tagId);
                        
                        
                        // adding entry to history
                        StockHistoryEvent event = new StockHistoryEvent();
                        event.setEpc(tagId);
                        event.setProduct(item);
                        event.setOperation("REMOVED");
                        event.setOperationOn(new Date());
                        StockHistoryUtil.addToHistoryLog(event);
                        addItemToJtable7(event);
                    }
                   
                }
                //  monitor inventory
                else if(antennaID == monitorAntenna){

                    
                    //if(itemMonitoredMap.containsKey(tagId))
                    if(itemsToMonitorList.contains(tagId)){
                        if(itemMonitoredMap.containsKey(tagId)&& itemMonitoredMap.get(tagId)[1]==1)
                        {
                            itemMonitoredMap.remove(tagId);
                            addToMovemement(item);
                        }
                        itemMonitoredMap.put(tagId, new Long []{new Date().getTime(),Long.valueOf(0)});   
                    }
                }
                // sale of inventory
                else if(antennaID == salesAntenna){
                    Stock stock = StockRDFOperations.getStockDetailsForEpc(tagId);
                    if(stock.isSold()){
                        addToTable2(stock);
                    }else{
                        isPresentInStock = itemsInStock.contains(tagId);
                        if(isPresentInStock){
                            boolean isPresentInStockForSale = getStockForSale(tagId);
                            if(!isPresentInStockForSale)
                            {
                                listModel1.addElement(tagId);
                            }
                        }
                    }
                }
            } catch (InventoryManagementException e) {
                logMessage(e.getMessage());
            }
        }
    }
    
    private void removeFromOthrPlaces(String epc) {
        
        // removing from list of items available for sales
        int count = listModel1.getSize();
        for (int i = 0; i < count; i++) {
        if(epc.equals(listModel1.getElementAt(i))){
            listModel1.remove(i);
            break;
        }    
        }
        
        // removing from list of items selected for sales
        count = listModel2.getSize();
        for (int i = 0; i < count; i++) {
            if(epc.equals(listModel2.getElementAt(i))){
                listModel2.remove(i);
                break;
            }    
        }
        
        // removing from list of items available for monitoring
        count = listModel3.getSize();
        for (int i = 0; i < count; i++) {
            if(epc.equals(listModel3.getElementAt(i))){
                listModel3.remove(i);
                break;
            }    
        }
        itemsToMonitorList.remove(epc);
        
        // removing from list of items being monitored
        DefaultTableModel model =(DefaultTableModel)jTable1.getModel();
        int rowCount =  model.getRowCount();
        for (int i = 0; i < rowCount; i++) {
            String itemNoData = (String)model.getValueAt(i, 0) ;  
            if(epc.equals(itemNoData)){
                model.removeRow(i);
                break;
            }
        }
        itemMonitoredMap.remove(epc);
        
    }

    /**
     * 
     */
    private static void monitorTagPresence() {
        boolean showMessage = false;
        long diffSeconds;
        long currentTime;
        while(true){
            if(monitorFlag){
                currentTime = new Date().getTime();
                showMessage = false;
                 
                String message = "Below items are moved from stack \n\n";
                for (String itemToMonitor : itemsToMonitorList) {
                    
                    if(itemMonitoredMap.containsKey(itemToMonitor)){
                        
                        Long[]attributes = itemMonitoredMap.get(itemToMonitor);
                        
                        diffSeconds = (currentTime - attributes[0] ) / 1000;
                        if( attributes[1]==0 && diffSeconds > NSec){
                            message += "    "+itemToMonitor+ "\n";
                            itemMonitoredMap.put(itemToMonitor, new Long []{new Date().getTime(),Long.valueOf(1)});
                            showMessage= true;
                        }
                    }
                }
               if(showMessage){
                   JOptionPane.showMessageDialog(null, message);
               }
            }
            try {
                Thread.sleep(1000*NSec);
            } catch (InterruptedException e) { }
            
        }
    }
    /**
     * this class renders date in specified format
     * @author Administrator
     *
     */
    class DateRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = 1L;
        private SimpleDateFormat sdfNewValue = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        private String valueToString = "";

        @Override
        public void setValue(Object value) {
            if ((value != null)) {
                Date date = (Date)value;
                
                valueToString = sdfNewValue.format(date);
                value = valueToString;
            }
            super.setValue(value);
        }
    }
}
