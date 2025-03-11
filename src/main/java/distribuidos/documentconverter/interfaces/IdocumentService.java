/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package distribuidos.documentconverter.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author johan
 */
public interface IdocumentService extends Remote {
   List<byte[]> convertToPDF(List<Document> documents) throws RemoteException;
    public List<byte[]> distributeConversion(List<Document> documents) throws RemoteException;
    boolean isNodeAvailable() throws RemoteException;
     
}
