/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.JFrame;

/**
 *
 * @author salman
 */
import javax.swing.*;
import java.awt.*;              //for layout managers and more
import java.awt.event.*;        //for action events
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientGUI extends JPanel
        implements ActionListener {
    
    DFSClientOperations client;
    boolean connected;
    ArrayList<String> disconnectedWrites;
    HashMap<String,int[]> openFilesDescriptors;
    protected static String hostName = "host_name", fileName = "file_name", create = "create",
            open = "open", close = "close", read = "read", write = "write",
            connect = "connect", disconnect = "disconect";
    public JTextArea textArea, notificationArea;
    protected JLabel actionLabel;
    JTextField filenameTxtfld, hostNameTf;
    JTabbedPane tabbedPane;
    HashMap<String, JTextArea> textAreas;
    JRadioButton readRB, writeRB, blockingRB, nonBlockingRB, disconnectedRB;

    public ClientGUI() throws Exception {

        textAreas = new HashMap<>();
        connected = false;
        openFilesDescriptors = new HashMap<>();
        disconnectedWrites = new ArrayList<>();
        FlowLayout gridbagMain = new FlowLayout();
        setLayout(gridbagMain);
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setVisible(true);
        try {
            //setLayout(null);

            client = new DFSClientOperations( textAreas );
            
            // ... and bind it with the RMI Registry
            if( client != null )
                try {
                    Naming.bind (client.clientStubID, client);
                } catch (MalformedURLException | RemoteException ex) {
                    Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
                }

         } catch (RemoteException ex) {
            ex.printStackTrace();
            throw new Exception();
         }
        //Create a regular text field.
        filenameTxtfld = new JTextField(30);
        filenameTxtfld.setActionCommand(fileName);
        filenameTxtfld.addActionListener(this);

        //Create a password field.

        hostNameTf = new JTextField(20);
        hostNameTf.setActionCommand(hostName);
        hostNameTf.addActionListener(this);

        // Create Button
        //Create a password field.
        JButton createBt = new JButton(create);
        createBt.addActionListener(this);

        JButton readBt = new JButton(read);
        readBt.addActionListener(this);

        JButton writeBt = new JButton(write);
        writeBt.addActionListener(this);

        JButton openBt = new JButton(open);
        openBt.addActionListener(this);

        JButton closeBt = new JButton(close);
        closeBt.addActionListener(this);

        JButton connectBt = new JButton(connect);
        connectBt.addActionListener(this);

        JButton disconnectBt = new JButton(disconnect);
        disconnectBt.addActionListener(this);





        //Create some labels for the fields.

        JLabel hostLbl = new JLabel("HOST: ");
        JLabel fileNamelbl = new JLabel("");
        //ftfLabel.setLabelFor(ftf);

        //Create a label to put messages during an action event.
        actionLabel = new JLabel("Type text in a field and press Enter.");
        actionLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        //Lay out the text controls and the labels.
        JPanel textControlsPane = new JPanel();
        textControlsPane.setPreferredSize( new Dimension(1030, 130) );
        GridLayout gridbag = new GridLayout(4, 3);
        GridBagConstraints c = new GridBagConstraints();

        textControlsPane.setLayout(gridbag);

        //textControlsPane.add( hostLbl );
        textControlsPane.add(hostNameTf);
        JPanel tmpPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(1, 2);
        tmpPanel.setLayout(gridLayout);
        tmpPanel.add(connectBt);
        tmpPanel.add(disconnectBt);
        textControlsPane.add(tmpPanel);
        
        textControlsPane.add(new JLabel(""));
        textControlsPane.add(new JLabel(""));
        textControlsPane.add(fileNamelbl);

        textControlsPane.add(new JLabel(""));

        tmpPanel = new JPanel();
        tmpPanel.setLayout( new GridLayout(1,3));
        tmpPanel.add(openBt);
        //tmpPanel.add(closeBt);
        tmpPanel.add(createBt);
        tmpPanel.add(new JLabel("          File Name: "));
        textControlsPane.add(tmpPanel);
        textControlsPane.add(filenameTxtfld);
        
        JPanel read_writeRBs = new JPanel();
        readRB = new JRadioButton("read") ;
        readRB.setSelected(true);
        writeRB = new JRadioButton("write") ;
        ButtonGroup group = new ButtonGroup();
        group.add( readRB);
        group.add( writeRB );
        
        System.out.println(group.getSelection().getActionCommand());
        
        read_writeRBs.add( new JLabel( "Opening Mode  " ));
        read_writeRBs.add( readRB);
        read_writeRBs.add(writeRB);
        textControlsPane.add( read_writeRBs );
        
        
        //textControlsPane.add(readBt);

        //textControlsPane.add(writeBt);
       
        //textControlsPane.add(writeBt);
        
        JPanel write_modeRBs = new JPanel();
        blockingRB = new JRadioButton("B") ;
        blockingRB.setSelected(true);
        nonBlockingRB = new JRadioButton("NB");
        disconnectedRB = new JRadioButton("DC");
        ButtonGroup group_write_mode = new ButtonGroup();
        group_write_mode.add( blockingRB);
        group_write_mode.add( nonBlockingRB );
        group_write_mode.add( disconnectedRB );
        
       // write_modeRBs.add(new JLabel("Write  Mode") );
        write_modeRBs.add( blockingRB );
        write_modeRBs.add( nonBlockingRB );
        write_modeRBs.add( disconnectedRB );
        
        tmpPanel =  new JPanel();
        tmpPanel.setLayout(new GridLayout(1,3));
        
        tmpPanel.add( closeBt );
        tmpPanel.add( writeBt );
        tmpPanel.add( new JLabel("     Writing Mode: ") );
        textControlsPane.add(tmpPanel);
        
        tmpPanel = new JPanel( );
        tmpPanel.setLayout(new GridLayout(1,2));
        tmpPanel.add( write_modeRBs);
        tmpPanel.add(new JLabel(""));
        textControlsPane.add( tmpPanel );
        //textControlsPane.add( write_modeRBs );
         textControlsPane.add(new JLabel(""));




        //addLabelTextRows(labels, textFields, gridbag, textControlsPane);

        c.gridwidth = GridBagConstraints.REMAINDER; //last
        c.anchor = GridBagConstraints.WEST;
        c.weightx = 1.0;
        //textControlsPane.add(actionLabel, c);
        textControlsPane.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("HOST"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));



        //Create a text area.
        
        notificationArea = new JTextArea("welcome to the Distributed file system\n\n");
        notificationArea.setForeground(Color.RED);
        notificationArea.setFont(new Font("Serif", Font.ITALIC, 16));
        
        //notificationArea
        notificationArea.setLineWrap(true);
        notificationArea.setWrapStyleWord(true);
        notificationArea.setEditable(false);
       // notificationArea.setPreferredSize( new Dimension(750, 130) );
        JScrollPane areaScrollPane2 = new JScrollPane(notificationArea);
        areaScrollPane2.setVerticalScrollBarPolicy(
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        areaScrollPane2.setPreferredSize(new Dimension(1030, 150));
        areaScrollPane2.setBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Plain Text"),
        BorderFactory.createEmptyBorder(5,5,5,5)),
        areaScrollPane2.getBorder()));
        JPanel rightPane = new JPanel();
        //rightPane.setPreferredSize(new Dimension(750, 200));
        rightPane.add(areaScrollPane2, BorderLayout.CENTER);

        //Put everything together.
        JPanel leftPane = new JPanel(new BorderLayout());
       // leftPane.add(textControlsPane, BorderLayout.PAGE_START);
        leftPane.add(tabbedPane,
                BorderLayout.CENTER);
        
        add( textControlsPane );
        
        
        add(rightPane, BorderLayout.PAGE_END);
        add(leftPane, BorderLayout.LINE_START);
        rightPane.setVisible(true);
    }

    public void createJTxtArea( HashMap<String, JTextArea> textAreas, String name, 
                                                    String content, boolean editable) {
        JTextArea txtArea = new JTextArea( content );
        txtArea.setFont(new Font("Serif", Font.ITALIC, 16));
        txtArea.setLineWrap(true);
        txtArea.setWrapStyleWord(true);
        txtArea.setEditable(editable);
        
        JScrollPane areaScrollPane = new JScrollPane(txtArea);
        areaScrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_ALWAYS );
        areaScrollPane.setPreferredSize(new Dimension(1030, 400));
        areaScrollPane.setBorder( 
            BorderFactory.createCompoundBorder(
                BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder("Notification Panel"),
                    BorderFactory.createEmptyBorder(5, 5, 5, 5)
                ),
                    areaScrollPane.getBorder()
            )
        );
        textAreas.put(name, txtArea);
        JPanel panel = new JPanel(false);
        panel.setName(name);
        panel.setPreferredSize(new Dimension(1030, 400));
        panel.setLayout(new GridLayout(1, 1));
        panel.add(txtArea);
        tabbedPane.addTab(name, null, (JComponent)panel, "Does nothing");
        //The following line enables to use scrolling tabs.
        
        
    }

    public void actionPerformed(ActionEvent e) {

        String orignal_file_name = filenameTxtfld.getText(), temporary_file_name = null;
        int openFilsCountInfo[] = {-1, 0};
        
        if( !connected && !connect.equals(e.getActionCommand()) ){
            notificationArea.setText( notificationArea.getText() + 
                    "- You first need to connect to a legit DFS server\n" );
            return;
        }
        if (connect.equals(e.getActionCommand())) {
            if( connect() ){
                connected = true;
            }
       
        } else if (disconnect.equals(e.getActionCommand()) && connected ) {
            //invoke disconnection initiated by the client
        } else if (open.equals(e.getActionCommand())) {
            
            if( readRB.isSelected() ){
                openFile( orignal_file_name, 0, false );
            }else if( writeRB.isSelected() ){
                openFile( orignal_file_name, 1, true );
            }
            
        } else if (close.equals(e.getActionCommand())) {
            
            
            String tabTitle = tabbedPane.getSelectedComponent().getName();//filenameTxtfld.getText();
            int tabIndex = tabbedPane.indexOfTab(tabTitle);
            
            if( tabIndex < 0 ){
                return;
            }

            
            
            notificationArea.setText( notificationArea.getText() + 
                        "- closing file \""+tabTitle +"\" ...\n" );
            try {
                if(  disconnectedWrites.contains( tabTitle ) ){
                    notificationArea.setText( notificationArea.getText() + 
                        "- trying to commit disconnected write to file: \""+tabTitle +"\" ...\n" );
                    writeToFile(tabTitle, 2);
                    disconnectedWrites.remove( tabTitle );
                }
                client.closeFile( openFilesDescriptors.get( tabTitle )[0] );
                orignal_file_name = tabTitle.split("_")[0];
                System.out.println( orignal_file_name+"\n value: " +openFilesDescriptors.get( orignal_file_name ));
                
                
                
                if( tabTitle.contains("_") && openFilesDescriptors.get( orignal_file_name )[2] != 0 ){
                    System.out.println("removing "+tabTitle);
                    openFilesDescriptors.remove( tabTitle );
                }
                
                if( openFilesDescriptors.get( orignal_file_name )[2] == 0 ){
                    if( tabTitle.contains("_") ){
                        System.out.println("removing "+tabTitle);
                        openFilesDescriptors.remove( tabTitle );
                    }
                    System.out.println("removing "+orignal_file_name);
                    openFilesDescriptors.remove( orignal_file_name );
                }else{
                    openFilesDescriptors.get( orignal_file_name )[2] = 
                                                        openFilesDescriptors.get( orignal_file_name )[2] -1;
                
                }
                
                tabbedPane.removeTabAt(tabIndex);
                notificationArea.setText( notificationArea.getText() + 
                        "- \""+tabTitle +"\" successfully closed\n" );
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
        } else if (read.equals(e.getActionCommand())) {
        } else if (write.equals(e.getActionCommand())) {
            
            
            temporary_file_name = tabbedPane.getSelectedComponent().getName();
            notificationArea.setText( notificationArea.getText() +
                    "- writing to the file: "+temporary_file_name+"\n" );
            int mode;
            if( blockingRB.isSelected() ){
                mode =0;
            }else if( nonBlockingRB.isSelected() ){
                mode = 1;
            }else{
                mode = 2;
                
                if( !disconnectedWrites.contains( temporary_file_name ) ){
                    
                    disconnectedWrites.add( temporary_file_name );
                }
                notificationArea.setText( notificationArea.getText() +
                    "- disconnected write has started for file: "+temporary_file_name+"\n" );
                return;
            }
            writeToFile(temporary_file_name, mode);
            
        } else if (create.equals(e.getActionCommand())) {
            try {
                client.createFile( filenameTxtfld.getText() );
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }
            notificationArea.setText( notificationArea.getText() +
                    "file: "+filenameTxtfld.getText()+" created\n" );
        }
    }
    
    public void writeToFile(String temporary_file_name, int mode){
    
        try {
                int result = client.writeFile( openFilesDescriptors.get( temporary_file_name )[0],
                                    textAreas.get( temporary_file_name).getText(), mode );
                if( result == -2){
                    throw new Exception("opened in reading mode");
                }else if( result < 0 ){
                    throw new RemoteException();
                }
            } catch ( RemoteException ex) {
                notificationArea.setText( notificationArea.getText() +
                    "- an error occurred when writing to the file: "+temporary_file_name +"\n" );
                ex.printStackTrace();
                return;
            }catch( Exception ex ){
                notificationArea.setText( notificationArea.getText() +
                    "- file:"+temporary_file_name+"opened in reading mode, You are not allowed to write it.\n" );
                ex.printStackTrace();
                return;
            }
            notificationArea.setText( notificationArea.getText() +
                    "- writing to the file: "+temporary_file_name+" is done\n" );
    }
    public boolean connect(){
             notificationArea.setText( notificationArea.getText()+
                    "- connecting to host: "+hostNameTf.getText()+"\n" );
             connected = true;
            try {
                client.connect(hostNameTf.getText());
                notificationArea.setText( notificationArea.getText()+
                        "- connected to host: "+ hostNameTf.getText() +"\n");
                return true;
                
            } catch (RemoteException | NotBoundException | MalformedURLException ex) {
                ex.printStackTrace();
                notificationArea.setText( notificationArea.getText()+
                    "- an error occured when connecting to host: "+hostNameTf.getText()+"\n" );
                return false;
            }
    }
    public void openFile(String orignal_file_name, int mode, boolean editable){
        int openFilsCountInfo[] = new int[3];
        String temporary_file_name, content = null;
        int fd =-1;
         
            notificationArea.setText( notificationArea.getText()+
                    "- opening file: "+orignal_file_name+" ...\n" );
            
            try{
                fd = client.openFile(orignal_file_name, mode);                 //opening file for reading
                if( fd == -1 ){
                    notificationArea.setText( notificationArea.getText()+
                            "- \""+orignal_file_name+"\" does not exist\n" );
                    return;
                }else if( fd == -2 ){
                    notificationArea.setText( notificationArea.getText()+
                            "- \""+orignal_file_name+"\" already opened in writing mode by a user\n" );
                    return;
                }
                                    //reading the opened file
            } catch (RemoteException ex) {
                    ex.printStackTrace();
                     notificationArea.setText( notificationArea.getText()+
                            "- an error occured when opening file: "+orignal_file_name+"\n" );
                     return;
            }
            
            if( openFilesDescriptors.containsKey( orignal_file_name ) ){
                
                openFilesDescriptors.get(orignal_file_name)[1] = openFilesDescriptors.get(orignal_file_name)[1]+1;
                openFilesDescriptors.get(orignal_file_name)[2] = openFilesDescriptors.get(orignal_file_name)[2]+1;
                
                openFilsCountInfo[1] = openFilesDescriptors.get( orignal_file_name )[1];
                openFilsCountInfo[2] = openFilesDescriptors.get( orignal_file_name )[2];
                temporary_file_name = orignal_file_name +"_"+openFilsCountInfo[1];
                 
            }else{
                openFilsCountInfo[1] = 0;
                openFilsCountInfo[2] = 0;
                temporary_file_name = orignal_file_name;
                
                
            }
            
            openFilsCountInfo[0] = fd;
            openFilesDescriptors.put(temporary_file_name, openFilsCountInfo );
           
            if( mode == 1 ){
                try {
                    
                    int tmpFd = client.openFile(orignal_file_name, 0);
                    content = client.readFile( tmpFd );
                    client.closeFile(tmpFd);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                    notificationArea.setText( notificationArea.getText()+
                            "- an error occured when opening file: "+orignal_file_name +"\n");
                    return;
                }
                
            }else{
                try {
                    content = client.readFile( fd );
                } catch (RemoteException ex) {
                    notificationArea.setText( notificationArea.getText()+
                            "- an error occured when opening file: "+orignal_file_name +"\n");
                    ex.printStackTrace();
                }
            }
            createJTxtArea( textAreas, temporary_file_name, content, editable );
            
            notificationArea.setText( notificationArea.getText()+
                            "- successfully opened file: "+orignal_file_name +"\n" );
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Distributed File System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            //Add content to the window.
            frame.add(new ClientGUI());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }
}