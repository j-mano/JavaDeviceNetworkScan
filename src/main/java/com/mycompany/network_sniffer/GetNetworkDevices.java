package com.mycompany.network_sniffer;

import com.mycompany.network_sniffer.BackendProcesses;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
/**
 *
 * @author joachim
 */
public class GetNetworkDevices {
    com.mycompany.network_sniffer.BackendProcesses backendprocessing = new com.mycompany.network_sniffer.BackendProcesses();
    
    //Api to get networkdevises on specifik subset
    static public ListModel getNetworkDevices(String subnet){
        DefaultListModel returnlsit = new DefaultListModel();
        
        Thread Thread = new Thread(() -> {
            DefaultListModel returnlsit1 = (DefaultListModel) BackendProcesses.NetworkScan(subnet);
            
            for (int i = 0; i < returnlsit1.size(); i++) {
                returnlsit.addElement(returnlsit1.get(i));
            }
        });
        
        Thread.start();
        
        try{
            Thread.join();
        }
        catch(Exception e){
            
        }
        
        return returnlsit;
    }
}
