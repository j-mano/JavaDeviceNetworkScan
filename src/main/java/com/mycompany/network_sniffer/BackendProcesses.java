package com.mycompany.network_sniffer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author joachim
 */
public class BackendProcesses {
    // Proccesing dataFile
    
    
    // MainFile to scan network for devices and get ip and name. Provide what subset of the network you wish to scan.
    // Return an list of ipadresses
    // ex subset = 192.168.0 return list of devices in 192.168.0.0 - 192.168.0.255
    static public ListModel NetworkScan(String subnet){
        // Timeout time before giving up scaning one device. Longer time / higher number makes the scan take longer time lower and it might not find all devices.
        int timeout = 100;
        DefaultListModel returnList = new DefaultListModel();
        
        
        // The network is scaned in parallel to improve performance
        Thread Thread1 = new Thread(() -> {
            DefaultListModel returnList2 = threadNetworkScan(subnet,0,64);
            for(int i=0; i < returnList2.getSize(); i++){
                returnList.addElement(returnList2.get(i));
            }
        });
        
        Thread Thread2 = new Thread(() -> {
            DefaultListModel returnList3 = threadNetworkScan(subnet,64,128);
            for(int i=0; i < returnList3.getSize(); i++){
                returnList.addElement(returnList3.get(i));
            }
        });
        
        Thread Thread3 = new Thread(() -> {
            DefaultListModel returnList4 = threadNetworkScan(subnet,128,192);
            for(int i=0; i < returnList4.getSize(); i++){
                returnList.addElement(returnList4.get(i));
            }
        });
        
        Thread Thread4 = new Thread(() -> {
            DefaultListModel returnList5 = threadNetworkScan(subnet,192,255);
            for(int i=0; i < returnList5.getSize(); i++){
                returnList.addElement(returnList5.get(i));
            }
        });
        
        Thread1.start();
        Thread2.start();
        Thread3.start();
        Thread4.start();
        
        // Waiting to all thread to exit before returning it
        try{
            Thread1.join();
            Thread2.join();
            Thread3.join();
            Thread4.join();
            
            System.out.println("Finishing Sniffing the network with subnet: " + subnet);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return returnList;
     }
    
    // Subversion to NetworkScan for treading
    static private DefaultListModel threadNetworkScan(String subnet, int startPos, int endPos){
        DefaultListModel returnList = new DefaultListModel();
        
        int timeout = 100;
        
        for (int i=startPos;i<endPos;i++){
                String host = subnet + "." + i;
                InetAddress ip = null;
                try {
                    ip = InetAddress.getByName(host);
                }
                catch (UnknownHostException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if (InetAddress.getByName(host).isReachable(timeout)){
                        System.out.println(host + " is reachable");
                        System.out.println("Hostname: " + ip.getHostName());

                        returnList.addElement(ip.getHostName());
                    }
                } catch (IOException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        
        return returnList;
    }
}