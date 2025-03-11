package distribuidos.documentconverter.Services;

import distribuidos.documentconverter.interfaces.Document;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import distribuidos.documentconverter.interfaces.IdocumentService;

public class documentService extends UnicastRemoteObject implements IdocumentService {

    private static final Object lock = new Object();
    private static final String OFFICE_RUTA = "/usr/bin/libreoffice";
    private static final String PDF_OUTPUT_DIR = "/home/johan/Descargas/";
    private final AtomicBoolean isBusy = new AtomicBoolean(false);

    public documentService() throws RemoteException {
        super();
    }
    
    
    @Override
public List<byte[]> convertToPDF(List<Document> documents) throws RemoteException {
    List<byte[]> pdfFiles = new ArrayList<>();

    for (Document doc : documents) {
        File tempDocFile = null;
        File outputPdfFile = null;

        try {
            
            String fileName = doc.getName();
            if (!fileName.toLowerCase().endsWith(".docx")) {
                fileName += ".docx"; 
            }

            
            tempDocFile = new File(PDF_OUTPUT_DIR, fileName);
            Files.write(tempDocFile.toPath(), doc.getContent()); 
            
            //aca ponemos el nombre del documento que viene desde el cliente
            String pdfFileName = fileName.replace(".docx", ".pdf");
            outputPdfFile = new File(PDF_OUTPUT_DIR, pdfFileName);

            
            String[] command = {
                OFFICE_RUTA, "--headless", "--convert-to", "pdf:writer_pdf_Export",
                "--outdir", PDF_OUTPUT_DIR, tempDocFile.getAbsolutePath()
            };

            synchronized (lock) {
                isBusy.set(true);
                ProcessBuilder pb = new ProcessBuilder(command);
                Process process = pb.start();
                int exitCode = process.waitFor();

                if (exitCode == 0 && outputPdfFile.exists()) {
                    System.out.println("✅ PDF generado: " + outputPdfFile.getAbsolutePath());
                    pdfFiles.add(Files.readAllBytes(outputPdfFile.toPath()));
                } else {
                    System.err.println("No se encontró el PDF generado para: " + fileName);
                }
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("Error procesando el archivo: " + e.getMessage());
            e.printStackTrace();
        } finally {
            isBusy.set(false);

            // Limpiar archivos temporales
            if (tempDocFile != null && tempDocFile.exists()) {
                tempDocFile.delete();
            }
        }
    }

    return pdfFiles;
}

    
    @Override
    public boolean isNodeAvailable() throws RemoteException {
        return !isBusy.get();
    }

    @Override
    public List<byte[]> distributeConversion(List<Document> documents) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
