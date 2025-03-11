/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package distribuidos.documentconverter;

import distribuidos.documentconverter.Services.documentService;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class DocumentConverter {

   public static void main(String[] args) {
        try {
            
           
            Registry registry = LocateRegistry.createRegistry(8087); 
            documentService service = new documentService() ;
            Naming.rebind("rmi://192.168.1.6:8087/node1/documentService", service);

            System.out.println("Servidor RMI iniciado...");
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
